package org.vaadin.crudui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.vaadin.crudui.CrudBrowserlessTest.Person;
import org.vaadin.crudui.form.CrudForm;
import org.vaadin.crudui.form.CrudFormFactory;
import org.vaadin.crudui.layout.CrudLayout;
import org.vaadin.crudui.list.CrudList;
import org.vaadin.crudui.list.impl.GridList;

import com.vaadin.browserless.BrowserlessApplicationContext;
import com.vaadin.browserless.BrowserlessUIContext;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;

class CrudContractsTest {

	@Test
	void customContractsReceiveOperationsAndSuccessfulCallbacksRefreshTheList() {
		Person existing = new Person("Ada", 37);
		Person created = new Person("Grace", 85);
		RecordingList list = new RecordingList();
		RecordingLayout layout = new RecordingLayout();
		RecordingFormFactory forms = new RecordingFormFactory();
		List<Person> creates = new ArrayList<>();
		List<Person> updates = new ArrayList<>();
		List<Person> deletes = new ArrayList<>();
		List<Person> saves = new ArrayList<>();
		List<Person> deleted = new ArrayList<>();
		TextField filter = new TextField();
		DataProvider<Person, ?> provider = DataProvider.ofCollection(List.of(existing));
		Crud<Person> crud = Crud.of(Person.class)
				.addFilterComponent(filter)
				.onRead(provider)
				.list(() -> list)
				.layout(() -> layout)
				.form(forms)
				.form().newInstanceSupplier(() -> created)
				.viewBeforeEdit(true)
				.onCreate(creates::add)
				.onUpdate(updates::add)
				.onDelete(deletes::add)
				.onSaveSuccess(saves::add)
				.onDeleteSuccess(deleted::add)
				.build();

		try (BrowserlessApplicationContext app = BrowserlessApplicationContext.forComponent(() -> crud)) {
			BrowserlessUIContext window = app.newUser().newWindow();
			assertSame(list, layout.list);
			assertSame(provider, list.provider);
			assertEquals(List.of(filter), layout.filters);

			window.test(layout.crudActions.get(0)).click();
			assertEquals(List.of(CrudOperation.CREATE), forms.operations);
			assertSame(created, forms.current.getValue());
			assertEquals("Create custom", layout.caption);
			forms.current.valid = false;
			window.test(layout.formActions.get(0)).click();
			assertTrue(creates.isEmpty());
			assertEquals(0, list.refreshes);
			forms.current.valid = true;
			window.test(layout.formActions.get(0)).click();
			assertEquals(List.of(created), creates);
			assertEquals(List.of(created), saves);
			assertEquals(1, list.refreshes);
			assertNull(layout.form);

			list.select(existing);
			assertEquals(CrudOperation.READ, forms.operations.get(1));
			assertSame(existing, forms.current.getValue());
			assertTrue(forms.current.isReadOnly());
			assertNull(layout.caption);
			window.test(layout.crudActions.get(1)).click();
			assertEquals(CrudOperation.UPDATE, forms.operations.get(2));
			assertFalse(forms.current.isReadOnly());
			assertEquals("Update custom", layout.caption);
			window.test(layout.formActions.get(0)).click();
			assertEquals(List.of(existing), updates);
			assertEquals(List.of(created, existing), saves);
			assertEquals(2, list.refreshes);

			list.select(existing);
			window.test(layout.crudActions.get(2)).click();
			window.test(window.find(ConfirmDialog.class).single()).confirm();
			assertEquals(List.of(existing), deletes);
			assertEquals(List.of(existing), deleted);
			assertEquals(3, list.refreshes);
			assertNull(list.selected);
			assertNull(layout.form);
		}
	}

	@Test
	void failedOperationsReportOriginalErrorWithoutRefreshOrSuccess() {
		for (CrudOperation operation : List.of(CrudOperation.CREATE, CrudOperation.UPDATE, CrudOperation.DELETE)) {
			Person person = new Person("Ada", 37);
			RecordingList list = new RecordingList();
			RecordingLayout layout = new RecordingLayout();
			RuntimeException failure = new IllegalArgumentException(operation.name());
			List<Throwable> errors = new ArrayList<>();
			List<Person> successes = new ArrayList<>();
			Crud<Person> crud = Crud.of(Person.class)
					.list(() -> list).layout(() -> layout)
					.form(new RecordingFormFactory())
					.onCreate(bean -> { throw failure; })
					.onUpdate(bean -> { throw failure; })
					.onDelete(bean -> { throw failure; })
					.onSaveSuccess(successes::add)
					.onDeleteSuccess(successes::add)
					.onOperationError(errors::add)
					.build();
			try (BrowserlessApplicationContext app = BrowserlessApplicationContext.forComponent(() -> crud)) {
				BrowserlessUIContext window = app.newUser().newWindow();
				invokeOperation(window, layout, list, person, operation);
				assertEquals(List.of(failure), errors);
				assertTrue(successes.isEmpty());
				assertEquals(0, list.refreshes);
				assertNotNull(layout.form);
				if (operation != CrudOperation.CREATE) {
					assertSame(person, list.selected);
				}
			}
		}
	}

	@Test
	void missingHandlersAreReportedOrPropagated() {
		for (CrudOperation operation : List.of(CrudOperation.CREATE, CrudOperation.UPDATE, CrudOperation.DELETE)) {
			for (boolean handleError : List.of(false, true)) {
				RecordingList list = new RecordingList();
				RecordingLayout layout = new RecordingLayout();
				List<Throwable> errors = new ArrayList<>();
				Crud<Person> crud = Crud.of(Person.class)
						.list(() -> list).layout(() -> layout)
						.form(new RecordingFormFactory());
				if (handleError) {
					crud.onOperationError(errors::add);
				}
				crud.build();
				try (BrowserlessApplicationContext app = BrowserlessApplicationContext.forComponent(() -> crud)) {
					BrowserlessUIContext window = app.newUser().newWindow();
					Person person = new Person("Ada", 37);
					if (handleError) {
						invokeOperation(window, layout, list, person, operation);
						assertEquals(1, errors.size());
						assertInstanceOf(IllegalStateException.class, errors.get(0));
						assertTrue(errors.get(0).getMessage().contains(operation.name().substring(0, 1)
								+ operation.name().substring(1).toLowerCase()));
					} else {
						assertThrows(IllegalStateException.class,
								() -> invokeOperation(window, layout, list, person, operation));
					}
					assertEquals(0, list.refreshes);
				}
			}
		}
	}

	@Test
	void cancelDeleteLeavesSelectionAndDoesNotCallAnyOperationHooks() {
		Person person = new Person("Ada", 37);
		RecordingList list = new RecordingList();
		RecordingLayout layout = new RecordingLayout();
		List<Person> calls = new ArrayList<>();
		List<Throwable> errors = new ArrayList<>();
		Crud<Person> crud = Crud.of(Person.class)
				.list(() -> list).layout(() -> layout)
				.form(new RecordingFormFactory())
				.onDelete(calls::add).onDeleteSuccess(calls::add)
				.onOperationError(errors::add).build();
		try (BrowserlessApplicationContext app = BrowserlessApplicationContext.forComponent(() -> crud)) {
			BrowserlessUIContext window = app.newUser().newWindow();
			list.select(person);
			window.test(layout.crudActions.get(2)).click();
			window.test(window.find(ConfirmDialog.class).single()).cancel();
			assertTrue(calls.isEmpty());
			assertTrue(errors.isEmpty());
			assertEquals(0, list.refreshes);
			assertSame(person, list.selected);
			assertSame(person, layout.form.getValue());
		}
	}

	@Test
	void hiddenUpdateShowsReadOnlyFormAndHiddenActionsAreNotShown() {
		RecordingList list = new RecordingList();
		RecordingLayout layout = new RecordingLayout();
		RecordingFormFactory forms = new RecordingFormFactory();
		Crud<Person> crud = Crud.of(Person.class).list(() -> list).layout(() -> layout).form(forms)
				.createOperationVisible(false).updateOperationVisible(false).deleteOperationVisible(false).build();
		try (BrowserlessApplicationContext app = BrowserlessApplicationContext.forComponent(() -> crud)) {
			app.newUser().newWindow();
			list.select(new Person("Ada", 37));
			assertEquals(List.of(CrudOperation.READ), forms.operations);
			assertTrue(forms.current.isReadOnly());
			assertEquals(1, layout.formActions.size());
			assertTrue(layout.crudActions.stream().noneMatch(Component::isVisible));
		}
	}

	@Test
	void supplierIsUsedForEveryCreateAndNullIsRejected() {
		RecordingList list = new RecordingList();
		RecordingLayout layout = new RecordingLayout();
		RecordingFormFactory forms = new RecordingFormFactory();
		AtomicInteger calls = new AtomicInteger();
		Crud<Person> crud = Crud.of(Person.class).list(() -> list).layout(() -> layout).form(forms)
				.form().newInstanceSupplier(() -> new Person("Person " + calls.incrementAndGet(), 1))
				.build();
		try (BrowserlessApplicationContext app = BrowserlessApplicationContext.forComponent(() -> crud)) {
			BrowserlessUIContext window = app.newUser().newWindow();
			window.test(layout.crudActions.get(0)).click();
			assertEquals("Person 1", forms.current.getValue().getName());
			window.test(layout.formActions.get(1)).click();
			window.test(layout.crudActions.get(0)).click();
			assertEquals("Person 2", forms.current.getValue().getName());
			assertEquals(2, calls.get());
			crud.form().newInstanceSupplier(() -> null);
			IllegalStateException error = assertThrows(IllegalStateException.class,
					() -> window.test(layout.crudActions.get(0)).click());
			assertTrue(error.getMessage().contains("returned null"));
		}
	}

	@Test
	void gridListProviderAndCrudReadWiringWorkInEitherBuilderOrder() {
		DataProvider<Person, ?> first = DataProvider.ofCollection(List.of(new Person("First", 1)));
		DataProvider<Person, ?> second = DataProvider.ofCollection(List.of(new Person("Second", 2)));
		var grid = new com.vaadin.flow.component.grid.Grid<Person>();
		GridList<Person> standalone = GridList.of(Person.class).grid(grid).dataProvider(first).build();
		assertSame(first, grid.getDataProvider());
		Crud<Person> crud = Crud.of(Person.class).list(() -> standalone).onRead(second);
		assertSame(second, grid.getDataProvider());
		var replacementGrid = new com.vaadin.flow.component.grid.Grid<Person>();
		crud.list(GridList.of(Person.class).grid(replacementGrid));
		assertSame(second, replacementGrid.getDataProvider());
	}

	@Test
	void collectionSupplierReadsCurrentDataAfterCrudRefresh() {
		List<Person> people = new ArrayList<>();
		RecordingList list = new RecordingList();
		RecordingLayout layout = new RecordingLayout();
		Crud<Person> crud = Crud.of(Person.class).onRead(() -> people)
				.list(() -> list).layout(() -> layout).form(new RecordingFormFactory())
				.onCreate(people::add).build();
		try (BrowserlessApplicationContext app = BrowserlessApplicationContext.forComponent(() -> crud)) {
			BrowserlessUIContext window = app.newUser().newWindow();
			assertEquals(0, list.provider.size(new Query<>()));
			window.test(layout.crudActions.get(0)).click();
			window.test(layout.formActions.get(0)).click();
			assertEquals(1, list.refreshes);
			assertEquals(1, list.provider.size(new Query<>()));
			assertSame(people.get(0), list.provider.fetch(new Query<>()).findFirst().orElseThrow());
		}
	}

	@Test
	void successfulOperationsRefreshConfiguredDataProvider() {
		Person person = new Person("Ada", 37);
		List<Person> people = new ArrayList<>(List.of(person));
		DataProvider<Person, Void> provider = DataProvider.fromCallbacks(
				query -> people.stream().skip(query.getOffset()).limit(query.getLimit()),
				query -> people.size());
		Grid<Person> grid = new Grid<>();
		grid.addColumn(Person::getName);
		RecordingLayout layout = new RecordingLayout();
		Crud<Person> crud = Crud.of(Person.class)
				.list(GridList.of(Person.class).grid(grid))
				.layout(() -> layout).form(new RecordingFormFactory())
				.onRead(provider).onCreate(people::add).onUpdate(ignored -> { })
				.onDelete(people::remove).build();
		AtomicInteger refreshes = new AtomicInteger();
		provider.addDataProviderListener(event -> refreshes.incrementAndGet());
		try (BrowserlessApplicationContext app = BrowserlessApplicationContext.forComponent(() -> crud)) {
			BrowserlessUIContext window = app.newUser().newWindow();
			window.test(layout.crudActions.get(0)).click();
			window.test(layout.formActions.get(0)).click();
			assertEquals(1, refreshes.get());
			window.test(grid).select(0);
			window.test(layout.formActions.get(0)).click();
			assertEquals(2, refreshes.get());
			window.test(grid).select(0);
			window.test(layout.crudActions.get(2)).click();
			window.test(window.find(ConfirmDialog.class).single()).confirm();
			assertEquals(3, refreshes.get());
			assertFalse(people.contains(person));
		}
	}

	@Test
	void buttonConfigurationIsAppliedToTheCorrespondingFormActions() {
		RecordingList list = new RecordingList();
		RecordingLayout layout = new RecordingLayout();
		Crud<Person> crud = Crud.of(Person.class).list(() -> list).layout(() -> layout)
				.form(new RecordingFormFactory()).viewBeforeEdit(true)
				.button(CrudOperation.CREATE).label("Create item", VaadinIcon.PLUS::create)
				.button(CrudOperation.UPDATE).label("Update item", VaadinIcon.EDIT::create)
				.button(CrudOperation.UPDATE).variants(ButtonVariant.LUMO_ERROR)
				.button(CrudOperation.READ).label("Discard")
				.build();
		try (BrowserlessApplicationContext app = BrowserlessApplicationContext.forComponent(() -> crud)) {
			BrowserlessUIContext window = app.newUser().newWindow();
			window.test(layout.crudActions.get(0)).click();
			assertEquals("Create item", layout.formActions.get(0).getText());
			assertNotNull(layout.formActions.get(0).getIcon());
			assertEquals("Discard", layout.formActions.get(1).getText());
			window.test(layout.formActions.get(1)).click();
			list.select(new Person("Ada", 37));
			assertEquals("Discard", layout.formActions.get(0).getText());
			window.test(layout.crudActions.get(1)).click();
			Button updateSave = layout.formActions.get(0);
			assertEquals("Update item", updateSave.getText());
			assertNotNull(updateSave.getIcon());
			assertTrue(updateSave.getThemeNames().contains(ButtonVariant.LUMO_ERROR.getVariantName()));
		}
	}

	@Test
	void listAndLayoutFactoriesWorkWhenLayoutIsConfiguredFirst() {
		RecordingList list = new RecordingList();
		RecordingLayout layout = new RecordingLayout();
		TextField filter = new TextField();
		DataProvider<Person, ?> provider = DataProvider.ofCollection(List.of());
		Crud<Person> crud = Crud.of(Person.class).layout(() -> layout)
				.addFilterComponent(filter).list(() -> list).onRead(provider)
				.form(new RecordingFormFactory()).build();
		try (BrowserlessApplicationContext app = BrowserlessApplicationContext.forComponent(() -> crud)) {
			app.newUser().newWindow();
			assertSame(list, layout.list);
			assertSame(provider, list.provider);
			assertEquals(List.of(filter), layout.filters);
			Person selected = new Person("Ada", 37);
			list.select(selected);
			assertSame(selected, layout.form.getValue());
		}
	}

	private static void invokeOperation(BrowserlessUIContext window, RecordingLayout layout,
			RecordingList list, Person person, CrudOperation operation) {
		if (operation == CrudOperation.CREATE) {
			window.test(layout.crudActions.get(0)).click();
			window.test(layout.formActions.get(0)).click();
		} else {
			list.select(person);
			if (operation == CrudOperation.UPDATE) {
				window.test(layout.formActions.get(0)).click();
			} else {
				window.test(layout.crudActions.get(2)).click();
				window.test(window.find(ConfirmDialog.class).single()).confirm();
			}
		}
	}

	private static class RecordingList extends VerticalLayout implements CrudList<Person> {
		private DataProvider<Person, ?> provider;
		private ItemSelectedListener<Person> listener;
		private Person selected;
		private int refreshes;

		@Override
		public void setDataProvider(DataProvider<Person, ?> dataProvider) {
			provider = dataProvider;
		}

		@Override
		public void addItemSelectedListener(ItemSelectedListener<Person> listener) {
			this.listener = listener;
		}

		void select(Person person) {
			selected = person;
			listener.onItemSelected(person);
		}

		@Override
		public void refreshItem(Person item) {
		}

		@Override
		public void refreshAllItems() {
			refreshes++;
		}

		@Override
		public void clearSelection() {
			selected = null;
			if (listener != null) {
				listener.onItemSelected(null);
			}
		}
	}

	private static class RecordingLayout extends VerticalLayout implements CrudLayout<Person> {
		private CrudList<Person> list;
		private CrudForm<Person> form;
		private String caption;
		private final List<Button> crudActions = new ArrayList<>();
		private final List<Button> formActions = new ArrayList<>();
		private final List<Component> filters = new ArrayList<>();

		@Override
		public void setCrudList(CrudList<Person> list) {
			if (this.list != null) {
				remove((Component) this.list);
			}
			this.list = list;
			add((Component) list);
		}

		@Override
		public void showCrudForm(CrudForm<Person> form) {
			this.form = form;
			add((Component) form);
		}

		@Override
		public void hideForm() {
			if (form != null) {
				remove((Component) form);
				form = null;
			}
			formActions.forEach(this::remove);
			formActions.clear();
		}

		@Override
		public void addCrudActionComponent(Component component) {
			crudActions.add((Button) component);
			add(component);
		}

		@Override
		public void addFormActionComponent(Component component) {
			formActions.add((Button) component);
			add(component);
		}

		@Override
		public void setFormActionCaption(String caption) {
			this.caption = caption;
		}

		@Override
		public void addFilterComponent(Component component) {
			filters.add(component);
			add(component);
		}
	}

	private static class RecordingFormFactory implements CrudFormFactory<Person> {
		private final List<CrudOperation> operations = new ArrayList<>();
		private RecordingForm current;

		@Override
		public CrudForm<Person> build(Person bean) {
			return new RecordingForm(bean);
		}

		@Override
		public CrudForm<Person> build(Person bean, CrudOperation operation) {
			operations.add(operation);
			current = new RecordingForm(bean);
			return current;
		}

		@Override
		public String getCreateCaption() {
			return "Create custom";
		}

		@Override
		public String getUpdateCaption() {
			return "Update custom";
		}
	}

	private static class RecordingForm extends VerticalLayout implements CrudForm<Person> {
		private Person value;
		private boolean readOnly;
		private boolean valid = true;

		RecordingForm(Person value) {
			this.value = value;
		}

		@Override
		public Person getValue() {
			return value;
		}

		@Override
		public void setValue(Person value) {
			this.value = value;
		}

		@Override
		public boolean isReadOnly() {
			return readOnly;
		}

		@Override
		public void setReadOnly(boolean readOnly) {
			this.readOnly = readOnly;
		}

		@Override
		public boolean isValid() {
			return valid;
		}
	}
}

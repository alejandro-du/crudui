# CRUD UI add-on for Vaadin

[Crud UI Add-on](https://vaadin.com/directory#!addon/crud-ui-add-on) provides an API to automatically generate CRUD-like UIs for any Java Bean at runtime.


## API

The API is defined through 4 interfaces:

* `CrudComponent`: A Vaadin `Component` that can be added into any `ComponentContainer`. This is the actual CRUD final users will see on the browser.

* `CrudListener`: Encapsulates the CRUD operations. You can implement this interface to delegate CRUD operations to your back-end.

* `CrudLayout`: Encapsulates layout-related behaviour.

* `CrudFormFactory`: Builds the forms required by the CRUD UI.

Crud UI Add-on includes atnleast one implementation for each of these interfaces (except `CrudListener` which is an optional "connection point" to your application.


## Basic sage

Create a new CrudComponent and add it to a layout (currently, Crud UI Add-on provides only one implementation of the `CrudComponent` interface: `GridBasedCrudComponent`):

```
GridBasedCrudComponent<User> crud = new GridBasedCrudComponent<>(User.class);
layout.addComponent(crud);
```
Use lambda expressions or method references to delegate CRUD operations to your backend:

```
crud.setFindAllOperation(() -> backend.findAll());
crud.setAddOperation(backend::add);
crud.setUpdateOperation(backend::update);
crud.setDeleteOperation(backend::delete);
```

## Advanced usage

As an alternative to lambada expressions you can implement the `CrudListener` interface to delegate CRUD operations to your backend:

```
crud.setCrudListener(new CrudListener<User>() {
    @Override
    public Collection<User> findAll() {
        return backend.findAllUsers();
    }
    @Override
    public User add(User user) {
        return backend.add(user);
    }

    @Override
    public User update(User user) {
        return backend.update(user);
    }

    @Override
    public void delete(User user) {
        backend.remove(user);
    }
});
```

Use a different `CrudLayout` implementation:

```
GridBasedCrudComponent<User> crud = new GridBasedCrudComponent<>(User.class, new HorizontalSplitCrudLayout());
```

Set a different `CrudFormFactory` implentation:

```
GridLayoutCrudFormFactory<User> formFactory = new GridLayoutCrudFormFactory<>(User.class, 2, 2);
crud.setCrudFormFactory(formFactory);
```

Configure the visibility of the fields in the forms:

```
formFactory.setVisiblePropertyIds(CrudOperation.READ, "name", "birthDate", "email", "groups", "mainGroup", "active");
formFactory.setVisiblePropertyIds(CrudOperation.ADD, "name", "birthDate", "email", "password", "groups", "mainGroup", "active");
formFactory.setVisiblePropertyIds(CrudOperation.UPDATE, "name", "birthDate", "email", "groups", "mainGroup", "active");
formFactory.setVisiblePropertyIds(CrudOperation.DELETE, "name", "email");
```

Use nested properties:

```
crud.getGridContainer().addNestedContainerBean("mainGroup");
crud.getGrid().setColumns("name", "birthDate", "email", "mainGroup.name", "active");
crud.getGrid().getColumn("mainGroup.name").setHeaderCaption("Main group");
```

Configure `Grid` renderers:

```
crud.getGrid().getColumn("birthDate").setRenderer(new DateRenderer("%1$tY-%1$tm-%1$te"));
```

Configure the type of a field:

```
formFactory.setFieldType("password", PasswordField.class);
```

Customize fields after their creation:

```
formFactory.setFieldCreationListener("birthDate", field -> ((DateField) field).setDateFormat("yyyy-MM-dd"));
```

Define a provider to manually create a field:

```
formFactory.setFieldProvider("groups", () -> {
    OptionGroup optionGroup = new OptionGroup();
    optionGroup.setMultiSelect(true);
    optionGroup.setContainerDataSource(new BeanItemContainer<>(Group.class, groups));
    optionGroup.setItemCaptionPropertyId("name");
    return optionGroup;
});
```

Customize captions:

```
formFactory.setButtonCaption(CrudOperation.ADD, "Add new user");
crud.setRowCountCaption("%d user(s) found");
```


## License

Add-on is distributed under Apache License 2.0. For license terms, see LICENSE.txt.

[![Published on Vaadin  Directory](https://img.shields.io/badge/Vaadin%20Directory-published-00b4f0.svg)](https://vaadin.com/directory/component/crud-ui-add-on)
[![Stars on Vaadin Directory](https://img.shields.io/vaadin-directory/star/crud-ui-add-on.svg)](https://vaadin.com/directory/component/crud-ui-add-on)
[![Latest version on vaadin.com/directory](https://img.shields.io/vaadin-directory/v/crud-ui-add-on.svg)](https://img.shields.io/vaadin-directory/v/crud-ui-add-on.svg)

Crud UI Add-on provides an API to automatically generate CRUD-like UIs for any Java Bean at runtime.

The API is defined through 4 interfaces:

**`CrudComponent`**: A Vaadin `Component` that can be added into any layout.

**`CrudListener`**: Interface to delegate CRUD operations to a Java back-end service.

**`CrudLayout`**: Interface to define layouts (how different parts of the `CrudComponent` are arranged.

**`CrudFormFactory`**: Builds forms for showing and editing data.

The add-on includes several implementations of the previous interfaces.

# Basic usage

Having the following Java Bean class:

```java
public class User {

    @NotNull // Validation API is required! Add it as a dependency on your project
    private Long id;

    @NotNull
    private String name;

    private Date birthDate;

    @Email
    private String email;

    @NotNull
    private String password;

    ... getters & setters ...
}
```

The following creates a `CrudComponent` adds it into a Vaadin layout:
```java
GridCrud<User> crud = new GridCrud<>(User.class);
layout.addComponent(crud);
```

Enable _Java Bean Validation_ as follows:
```java
crud.getCrudFormFactory().setUseBeanValidation(true);
```

Use lambda expressions or method references to delegate CRUD operations a Java backend:
```java
crud.setFindAllOperation(() -> backend.findAll());
crud.setAddOperation(backend::add);
crud.setUpdateOperation(backend::update);
crud.setDeleteOperation(backend::delete);
```

# Advanced usage

As an alternative to method references and lambda expressions, implement `CrudListener` to delegate CRUD operations to a Java backend:

```java
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

Set an alternative `CrudLayout` implementation:

```java
GridCrud<User> crud = new GridCrud<>(User.class, new HorizontalSplitCrudLayout());
````

Set a custom `CrudFormFactory` (implementation not shown):

```java

MyCustomCrudFormLayout<User> formFactory = new MyCustomCrudFormLayout<>(User.class);
crud.setCrudFormFactory(formFactory);
```

Configure form fields visibility:

```java
        crud.getCrudFormFactory().setProperties("name", "birthDate", "email", "salary", "phoneNumber", "maritalStatus",
                "groups", "active", "mainGroup");
````

Configure the `Grid` when using the `GridCrud` implementation:

```java
crud.getGrid().setColumns("name", "birthDate", "email", "mainGroup", "active");
crud.getGrid().getColumn("mainGroup").setRenderer(group -> group == null ? "" : ((Group) group).getName(), new TextRenderer());
crud.getGrid().getColumn("mainGroup.name").setHeaderCaption("Main group");
```

Configure `Grid` renderers:

```java
((Grid.Column<User, Date>) crud.getGrid().getColumn("birthDate")).setRenderer(new DateRenderer("%1$tY-%1$tm-%1$te"));
```

Configure the field types:

```java
crud.getCrudFormFactory().getProperty(CrudOperation.ADD, "password").setFieldType(PasswordField.class);
```

Customize fields after their creation:
```java
crud.getCrudFormFactory().getProperties("mainGroup").stream().forEach(
        property -> property.setFieldCreationListener(field -> ((ComboBox) field).setPlaceholder("...select..."))
);
```

Define a `FieldProvider` to manually create a field:

```java
crud.getCrudFormFactory().getProperties("groups").stream().forEach(
        property -> property.setFieldProvider(
                () -> {
                    CheckboxGroup<Group> checkboxes = new CheckboxGroup<>();
                    checkboxes.setItems(groupService.findAll());
                    checkboxes.setItemLabelGenerator(Group::getName);
                    return checkboxes;
                }                        
        )
);
```

Use `FieldProvider` implementations included in the addon:

```
crud.getCrudFormFactory().getProperties("mainGroup").stream().forEach(
        property -> property.setFieldProvider(
                new ComboBoxProvider<>("Main Group", groupService.findAll(), new TextRenderer<>(Group::getName),
                        Group::getName))
);
```

Set a `Converter`:

````
crud.getCrudFormFactory().getProperty(CrudOperation.UPDATE, "salary").setConverter(
        new Converter<String, BigDecimal>() {
            @Override
            public Result<BigDecimal> convertToModel(String value, ValueContext valueContext) {
                return Result.ok(new BigDecimal(value)); // error handling omitted
            }

            @Override
            public String convertToPresentation(BigDecimal value, ValueContext valueContext) {
                return value.toPlainString();
            }
        }
);
````

Customize captions:

```
DefaultCrudFormFactory<User> formFactory = new DefaultCrudFormFactory<>(User.class);
formFactory.getProperty(CrudOperation.ADD, "password").setFieldType(PasswordField.class);
formFactory.setButtonCaption(CrudOperation.ADD, "Add new user");

crud.setRowCountCaption("%d user(s) found");
```

Set an error listener:

```
crud.setErrorConsumer(e -> Notification.show("Error!", Notification.Type.ERROR_MESSAGE));
```

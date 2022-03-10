[![Published on Vaadin  Directory](https://img.shields.io/badge/Vaadin%20Directory-published-00b4f0.svg)](https://vaadin.com/directory/component/crud-ui-add-on)
[![Stars on Vaadin Directory](https://img.shields.io/vaadin-directory/star/crud-ui-add-on.svg)](https://vaadin.com/directory/component/crud-ui-add-on)
[![Latest version on vaadin.com/directory](https://img.shields.io/vaadin-directory/v/crud-ui-add-on.svg)](https://img.shields.io/vaadin-directory/v/crud-ui-add-on.svg)

Crud UI Add-on provides an API to automatically generate CRUD-like UIs for any Java Bean at runtime.

The API is defined through 4 interfaces:

**`CrudComponent`**: A Vaadin `Component` that can be added to any `ComponentContainer`. This is the actual CRUD final users will see in the browser.

**`CrudListener`**: Encapsulates the CRUD operations. You can implement this interface to delegate CRUD operations to your back-end.

**`CrudLayout`**: Encapsulates layout-related behavior.

**`CrudFormFactory`**: Builds the forms required by the CRUD UI.

The add-on includes several implementations of these interfaces.

# Basic usage

Say, you have the following domain/entity/Java Bean class:
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
&nbsp;

You can create a new CRUD component and add it into any Vaadin layout as follows:
```java
GridCrud<User> crud = new GridCrud<>(User.class);
layout.addComponent(crud);
```
&nbsp;

You can enable _Java Bean Validation_ as follows:
```java
crud.getCrudFormFactory().setUseBeanValidation(true);
```
&nbsp;

Use lambda expressions or method references to delegate CRUD operations to your backend:
```java
crud.setFindAllOperation(() -> backend.findAll());
crud.setAddOperation(backend::add);
crud.setUpdateOperation(backend::update);
crud.setDeleteOperation(backend::delete);
```
&nbsp;

# Advanced usage

As an alternative to method references and lambda expressions, you can implement a `CrudListener` to delegate CRUD operations to your backend:
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
&nbsp;

Use a different `CrudLayout` implementation:
```java
GridCrud<User> crud = new GridCrud<>(User.class, new HorizontalSplitCrudLayout());
````
&nbsp;

Set a custom `CrudFormFactory`:
```java
CustomCrudFormFactory<User> formFactory = new CustomCrudFormFactory<>(User.class);
crud.setCrudFormFactory(formFactory);
```
&nbsp;

Configure form fields visibility:
```java
formFactory.setVisibleProperties(CrudOperation.READ, "name", "birthDate", "email", "groups", "mainGroup", "active");
formFactory.setVisibleProperties(CrudOperation.ADD, "name", "birthDate", "email", "password", "groups", "mainGroup", "active");
formFactory.setVisibleProperties(CrudOperation.UPDATE, "name", "birthDate", "email", "groups", "mainGroup", "active");
formFactory.setVisibleProperties(CrudOperation.DELETE, "name", "email");
````
&nbsp;

Use nested properties in `GridCrud` instances:
```java
crud.getGrid().addColumn(user -> user.getMainGroup().getName()).setHeader("Main group").setKey("key");
```
&nbsp;

Configure the type of an input field:
```java
formFactory.setFieldType("password", PasswordField.class);
```
&nbsp;

Customize fields after their creation:
```java
formFactory.setFieldCreationListener("birthDate", field -> ... your own logic here ...);
```
&nbsp;

Define a `FieldProvider` to manually create a field:
```java
formFactory.setFieldProvider("groups", user -> {
    CheckboxGroup<Group> checkboxes = new CheckboxGroup<>();
    checkboxes.setItems(groups);
    checkboxes.setItemLabelGenerator(Group::getName);
    return checkboxes;
});
```
&nbsp;

Or use one of the included `FieldProvider` implementations:
```
formFactory.setFieldProvider("groups",
        new CheckBoxGroupProvider<>("Groups", GroupRepository.findAll(), Group::getName));
```
&nbsp;

Set a `Converter`:

````
formFactory.setConverter("salary", new Converter<String, BigDecimal>() {
    @Override
    public Result<BigDecimal> convertToModel(String value, ValueContext valueContext) {
        return Result.ok(new BigDecimal(value)); // error handling omitted
    }

    @Override
    public String convertToPresentation(BigDecimal value, ValueContext valueContext) {
        return value.toPlainString();
    }
});
````

Customize captions:
```
formFactory.setButtonCaption(CrudOperation.ADD, "Add new user");
crud.setRowCountCaption("%d user(s) found");
```
&nbsp;


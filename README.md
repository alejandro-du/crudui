[![Published on Vaadin  Directory](https://img.shields.io/badge/Vaadin%20Directory-published-00b4f0.svg)](https://vaadin.com/directory/component/crud-ui-add-on)
[![Stars on Vaadin Directory](https://img.shields.io/vaadin-directory/star/crud-ui-add-on.svg)](https://vaadin.com/directory/component/crud-ui-add-on)
[![Latest version on vaadin.com/directory](https://img.shields.io/vaadin-directory/v/crud-ui-add-on.svg)](https://img.shields.io/vaadin-directory/v/crud-ui-add-on.svg)

Crud UI Add-on provides an API to automatically generate CRUD-like UIs for any Java Bean at runtime.

The API is defined through 4 interfaces and their implementations:

* **`CrudComponent`**: A Vaadin `Component` that can be added to any `ComponentContainer`. This is the actual CRUD final users will see in the browser. Implementations:

  * `GridCrud`: A CRUD UI based on Vaadin's standard `Grid` component.
  * `TreeGridCrud`: A CRUD UI based on Vaadin's standard `TreeGrid` component.

* **`CrudLayout`**: Defines CRUD layouts and related behaviors. Implementations:

  * `WindowBasedCrudLayout`: Shows forms in pop-up windows.
  * `HorizontalSplitCrudLayout`: Grid on the left, form on the right in a split layout.
  * `VerticalSplitCrudLayout`: Grid on the top, form on the bottom in a draggable split layout.
  * `VerticalCrudLayout`: Grid on the top, form on the bottom in a vertical layout (no draggable split).

* **`CrudFormFactory`**: Builds required UI forms for new, update, delete operations. Implementations:

  * `DefaultCrudFormFactory`: Java Reflection-based autogenerated UI forms customizable through `FieldProvider` implementations.

* **`CrudListener`**: Connects the CRUD to your backend operations. You must implement this interface or call the equivalent methods defined in the `Crud` interface.

# Basic usage

Let's say, you have the following domain/entity/Java Bean class:

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

You can create a new CRUD component and add it into any Vaadin layout as follows:
```java
GridCrud<User> crud = new GridCrud<>(User.class);
someLayout.addComponent(crud);
```

You can enable _Java Bean Validation_ as follows (don't forget to add the corresponding Java Validation API dependency to your project):

```java
crud.getCrudFormFactory().setUseBeanValidation(true);
```

Then use lambda expressions or method references to delegate CRUD operations to your backend implemented for example with JPA/Hibernate, Spring Data, or MyBatis):

```java
crud.setFindAllOperation(() -> backend.findAll());
crud.setAddOperation(backend::add);
crud.setUpdateOperation(backend::update);
crud.setDeleteOperation(backend::delete);
```

# Advanced usage

As an alternative to method references and lambda expressions, you can implement the `CrudListener` interface to connect the CRUD UI to your backend:

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

To change the general layout, use an alternative `CrudLayout` implementation (defaults to `WindowBasedCrudLayout`):

```java
GridCrud<User> crud = new GridCrud<>(User.class, new HorizontalSplitCrudLayout());
````

To configure form behavior or override related functionality, define a `CrudFormFactory`:

```java
CustomCrudFormFactory<User> formFactory = new CustomCrudFormFactory<>(User.class);
crud.setCrudFormFactory(formFactory);
```

To configure form fields visibility:

```java
formFactory.setVisibleProperties(CrudOperation.READ, "name", "birthDate", "email", "groups", "mainGroup", "active");
formFactory.setVisibleProperties(CrudOperation.ADD, "name", "birthDate", "email", "password", "groups", "mainGroup", "active");
formFactory.setVisibleProperties(CrudOperation.UPDATE, "name", "birthDate", "email", "groups", "mainGroup", "active");
formFactory.setVisibleProperties(CrudOperation.DELETE, "name", "email");
````

To configure field captions in the same order as you defined the set of visible properties:

```java
formFactory.setFieldCaptions("The name", "The birthdate", "The e-mail");
```

To add columns as nested properties in the  `Crud` component of `GridCrud` instances:

```java
crud.getGrid().addColumn(user -> user.getMainGroup().getName()).setHeader("Main group").setKey("key");
```

To configure the type of UI component to use for a specific field:

```java
formFactory.setFieldType("password", PasswordField.class);
```

To further customize fields after their creation:

```java
formFactory.setFieldCreationListener("birthDate", field -> ... your own logic here ...);
```

To manually create input fields, define a `FieldProvider`:

```java
formFactory.setFieldProvider("groups", user -> {
    CheckboxGroup<Group> checkboxes = new CheckboxGroup<>();
    checkboxes.setItems(groups);
    checkboxes.setItemLabelGenerator(Group::getName);
    return checkboxes;
});
```

Or use an included or custom `FieldProvider` implementation:

```java
formFactory.setFieldProvider("groups",
        new CheckBoxGroupProvider<>("Groups", GroupRepository.findAll(), Group::getName));
```

To set a `Converter`:

```java
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
```

To customize button captions:

```java
formFactory.setButtonCaption(CrudOperation.ADD, "Add new user");
crud.setRowCountCaption("%d user(s) found");
```

To customize form titles:

```java
crud.getCrudFormFactory().setCaption(CrudOperation.ADD, "Create new User");
crud.getCrudFormFactory().setCaption(CrudOperation.UPDATE, "Modify User");
```

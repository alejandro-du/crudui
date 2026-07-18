# Vision: Crud UI Add-On

## Problem

Vaadin business applications often require many CRUD views. Creating CRUDs for Java beans becomes repetitive (often leading to copy/paste of code) and error-prone. Developers spend time configuring grids, forms, and implementing glue code between these components instead of focusing on business logic.

## Solution

Provide a Vaadin add-on that generates CRUD Vaadin components that can be added to any Vaadin layout at runtime from Java beans via a fluent, type-safe API. The library offers customizable layouts and forms as well as validation and customization hooks so UIs can be generated and customized with minimal boilerplate.

## Target Users

Java developers and teams using Vaadin to build admin or data-entry UIs who want fast, configurable CRUD screens.

## Goals

- Offer a fluid, type-safe API for quick CRUD Vaadin UI component implementation.
- Support multiple pluggable list and layout implementations.
- Provide customizable form generation with sensible and useful defaults.
- Make backend wiring simple through an interface or lambda operations.
- Support bean validation, converters, and field customization.
- Provide a fluent API that's easy to use.

## Non Goals

- Not intended as a full application framework or opinionated admin theme.
- Not to replace hand-crafted UIs when complete custom contract is required.

## The API

The add-on provides a "fluid" API through the `Crud` class. This class chains methods for easy add-on functionality discoverability. In its simplest form, a developer must be able to create a CRUD component for **any Java Bean** as follows:

```Java
// myCrud is a Vaadin component that can be added to a VerticalLayout, HorizontalLayout, or any other layout component
Crud myCrud = Crud.of(User.class).build();
```

The previous example is only the minimal CRUD component with no connection to backend services or customizations. It should run without problems and use sensitive defaults so that the application developer has a starting point to start customizing and connecting to their backend services.

### Core interfaces

A CRUD is formed by a set of individual components that work together:

- a *list* component for showing a list of Java beans
- a *form* component for editing a Java bean
- a *layout* component to arranged lists and forms in the UI

Each individual component is specified via a factory/builder method:

```Java
Crud myCrud = Crud.of(User.class)
        .layout(SomeLayoutFactory.of(User.class))
        .list(SomeListFactory.of(User.class))
        .form(SomeFormFactory.of(User.class))
        .build();
```

The API places methods where they belong "under" one of the individual components methods:

```Java
Crud myCrud = Crud.of(User.class)
        .layout(SomeLayoutFactory.of(User.class)
                .layoutRelatedMethod1()
                .layoutRelatedMethod2()
                .layoutRelatedMethod3())
        .list(SomeListFactory.of(User.class)
                .listRelatedMethod1()
                .listRelatedMethod1()
                .listRelatedMethod3())
        .form(SomeFormFactory.of(User.class)
                .formRelatedMethod1()
                .formRelatedMethod2()
                .formRelatedMethod3())
        .build();
```

There can be multiple implementations of each of the previous individual components. Each implementation should implement a contract. This add-on's API defines core Java interfaces to define the contract for these individual components:

- **`CrudLayout`:** Defines the contract that every CRUD *layout* (the way different individual components are arranged) must implement.

- **`CrudList`:** Defines the contract that every CRUD *list* must implement.

- **`CrudForm`:** Defines the contract that every CRUD *form* must implement.

There are also some additional support Java interfaces that help gluing things together:

- **`CrudFormFactory`:** Defines the contract required for building CRUD forms. Needed because forms are created multiple times per CRUD instance as the user interacts with it.

- **`VaadinFieldProvider`:** Defines the contract required for building new Vaadin input fields when auto-generating CRUD forms. Needed because forms with input fields are created multiple times per CRUD instance as the user interacts with it.

There is a `CrudOperation` enum with values that correspond to each CRUD operation. This enum can be used when convenient. Never add, remove or modify the values in this enum.

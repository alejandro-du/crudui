# Vision: CrudUI Add-On for Vaadin

## Problem

Building CRUD interfaces for Java beans in Vaadin apps is repetitive, boilerplate-heavy, and requires manual form and grid wiring for each entity.

## Solution

Provide a reusable Vaadin add-on that generates CRUD UIs at runtime from Java beans, with pluggable layouts, form factories, and backend operation hooks.

## Target Users

- Vaadin developers who need fast admin or data-management UIs.
- Library maintainers building reusable CRUD components.
- Teams who want a demo app showing add-on integration with Spring Boot and JPA.

## Goals

- Implement a new API (version 2.0.0) in the package "org.vaadin.crudui2" without modifying the current API in the package "org.vaadin.crudui".
- The new API must be a type-safe and fluent API. There is an implementation in progress already. Use it as a starting point.
- Offer everything that is possible in the current version of the CrudUI add-on, including what is described in the README.md file plus everything else from the current API.

## Non Goals

- Not a full low-code platform or business-process workflow engine.
- Not intended to replace custom Vaadin UI design for highly specialized interfaces.
- Not meant to provide a complete backend framework, database ORM, or enterprise security solution.

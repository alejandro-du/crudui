# AI Agent Instructions for `crudui`

## What this project is

This is a Java/Maven multi-module Vaadin project for the CRUD UI Add-on for Vaadin (https://vaadin.com/directory/component/crud-ui-add-on):

- `add-on`: the main Vaadin add-on producing the `crudui` library jar that gets published in the Vaadin Directory.
- `crud-ui-demo`: a Spring Boot + Vaadin demo application that uses the add-on.

The library implements runtime CRUD UIs for Java beans using Vaadin components like `Grid`, `TreeGrid`, and forms. You'll be working on a new API located in the package `org.vaadin.crudui2` of the `add-on` Maven module and you will be modifying only that package and nothing else. This new API is an evolution of the previous API (in `org.vaadin.crudui`) and focuses on offering a type-safe fluent variant.

## Important conventions

- Java version: 21
- Vaadin version: 24.6.6
- Build system: Maven
- The demo app uses Spring Boot 3.3.1
- Do not edit generated build output under `target/`

## Key source locations

- Library source: `add-on/src/main/java/org/vaadin/crudui/...`
- Demo source: `crud-ui-demo/src/main/java/...`

## Build and run commands

Do not stop or run the demo application. It is already running and accessible at http://localhost:8080. Changes in `.java` files are automatically picked up, compiled, and deployed so you don't need to compile, deploy, stop/start anything. Changes take effect after 4 seconds approximately. All you need to do is modify the source code, wait ~4 seconds, and then use the Playwright MCP server to check that your code modifications are working correctly.

## What to focus on when editing

- For library behavior or new features, change code under `add-on/src/main/java/org/vaadin/crudui2`.

## Notes for AI coding agents

- Do not touch anything else than what is in `add-on/src/main/java/org/vaadin/crudui2`.
- Do not use Git to modify any file in any way, including checking out files, reverting, committing changes (do not do anything that writes to disk with Git).

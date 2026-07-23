# AI Agent Instructions for Crud UI Add-On

## What this project is

This is a Java/Maven multi-module Vaadin project for the CRUD UI Add-on for Vaadin (https://vaadin.com/directory/component/crud-ui-add-on):

- `add-on`: the main Vaadin add-on producing the **Crud UI Add-On** library jar that gets published in the Vaadin Directory.
- `crud-ui-demo`: a Spring Boot + Vaadin demo application that uses the add-on.

Now read the [vision.md](/docs/vision.md) file and learn a more detailed general description of the project.

## Important conventions

- Java version: 21
- Vaadin version: 25.2.3
- Build system: Maven
- The demo app uses Spring Boot 4.0.0
- Do not edit generated build output under `target/`

## Key source locations

- Library source: `add-on/src/main/java/org/vaadin/crudui/...`
- Demo source: `crud-ui-demo/src/main/java/...`

## Your role

You'll be working as a Senior Software Engineer on the API for this Vaadin add-on. The API is located in the package `org.vaadin.crudui` of the `add-on` Maven module and it's work in progress. You will be modifying only that package and nothing else in the `add-on` Maven module. The API is type-safe and fluent as described in the [vision.md](/docs/vision.md) file (read it!). You are allowed create and modify files in `crud-ui-demo/src/main/java/org/vaadin/crudui/demo/ui/view/test/` which are used to test and show case that implemented features work (see the [test-view-template.md](/docs/requirements/test-view-template.md) file).

## Build and run commands

Do not stop or run the demo application. It is already running and accessible at http://localhost:8080. Changes in `.java` files are automatically picked up, compiled, and deployed so you don't need to compile, deploy, stop/start anything. Changes take effect after 4 seconds approximately. All you need to do is modify the source code, wait ~4 seconds, and then use the Playwright MCP server to check that your code modifications are working correctly. You can build the project if necessary using Maven.

## What to focus on when editing

- For library behavior or new features, change code under `add-on/src/main/java/org/vaadin/crudui/`.

## Additional notes

- Do not touch anything else than what is in `add-on/src/main/java/org/vaadin/crudui/` and `-ui-demo/src/main/java/org/vaadin/crudui/demo/ui/view/test/`, unless instructed to do so.
- Do not use Git to modify any file in any way, including checking out files, reverting, committing changes (do not do anything that writes to disk with Git).
- When available, prefer java classes and interfaces that already exist in the Vaadin library. Use the Vaadin MCP Server that you have access to.

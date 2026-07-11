# AI Agent Instructions for `crudui`

## What this project is

This is a Java/Maven multi-module Vaadin project for the CRUD UI Add-on for Vaadin (https://vaadin.com/directory/component/crud-ui-add-on):

- `add-on`: the main Vaadin add-on producing the `crudui` library jar that gets published in the Vaadin Directory.
- `crud-ui-demo`: a Spring Boot + Vaadin demo application that uses the add-on.

The library implements runtime CRUD UIs for Java beans using Vaadin components like `Grid`, `TreeGrid`, and forms.

## Important conventions

- Java version: 21
- Vaadin version: 24.6.6
- Build system: Maven
- The demo app uses Spring Boot 3.3.1
- Do not edit generated build output under `target/`

## Key source locations

- Library source: `add-on/src/main/java/org/vaadin/crudui/...`
- Demo source: `crud-ui-demo/src/main/java/...`
- Main documentation: `README.md`

## Build and run commands

- Build entire repository:
  - `mvn clean install`
- Build only the add-on:
  - `mvn -pl add-on clean install`
- Run the demo app from the repo root:
  - `cd crud-ui-demo && mvn spring-boot:run`
- When preparing production/demo packaging, use the `production` profile in `crud-ui-demo`:
  - `cd crud-ui-demo && mvn -Pproduction clean package`

## What to focus on when editing

- For library behavior or new features, change code under `add-on/src/main/java`.
- For example/demo changes, update `crud-ui-demo/src/main/java`.
- Keep the API stable where possible: this repo is an add-on library with public component classes.
- Use the root `README.md` as the main reference for user-facing usage and API examples and update when new features are added or the API changes.

## Notes for AI coding agents

- There is no existing `.github/copilot-instructions.md` or `AGENTS.md` in this repo.
- Prefer Maven lifecycle commands over npm or Gradle.
- Avoid touching `target/` folders or generated frontend files under `crud-ui-demo/target/`.
- If asked about styling or demo integration, the demo app is the best place to verify actual Vaadin usage.

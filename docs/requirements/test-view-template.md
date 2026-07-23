# Test view template

Every time you implement a new requirement, you must create a new test view using the number of the requirement. Place the Java file in `/crud-ui-demo/src/main/java/org/vaadin/crudui/demo/ui/view/test/`. Here's an example for requirement `requirement-9999999`:

```Java
package org.vaadin.crudui.demo.ui.view.test;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import org.vaadin.crudui.demo.service.GroupService;
import org.vaadin.crudui.demo.service.UserService;

@Route(value = "requirement-9999999")
public class Requirement9999999 extends VerticalLayout {

    public Requirement9999999(UserService userService, GroupService groupService) {
    }

}
```

Replace `9999999` accordingly using the actual requirement number from the requirements file.

Any CRUD created must use the `userService` and/or `groupService` beans to connect to the backend when needed. Do not code dummy-data logic, use the services instead.

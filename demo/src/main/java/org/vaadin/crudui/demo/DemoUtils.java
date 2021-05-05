package org.vaadin.crudui.demo;

import org.apache.commons.lang3.StringUtils;

public class DemoUtils {

    public static String getViewName(Class clazz) {
        String lowerCase = StringUtils.join(
                StringUtils.splitByCharacterTypeCamelCase(clazz.getSimpleName()),
                " "
        ).toLowerCase().replace("view", "").replace("crud", "CRUD");

        return lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
    }

    public static String getGitHubLink(Class clazz) {
        return "https://github.com/alejandro-du/crudui/tree/master/demo/src/main/java/"
                + clazz.getName().replace(".", "/")
                + ".java";
    }

}

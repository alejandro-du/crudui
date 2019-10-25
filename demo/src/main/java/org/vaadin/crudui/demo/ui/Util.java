package org.vaadin.crudui.demo.ui;

public class Util {

    public static String getGitHubLink(Class clazz) {
        return "https://github.com/alejandro-du/crudui/tree/master/demo/src/main/java/"
                + clazz.getName().replace(".", "/")
                + ".java";
    }

}

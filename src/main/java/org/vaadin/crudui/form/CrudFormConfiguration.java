package org.vaadin.crudui.form;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import org.vaadin.crudui.form.CrudFieldConfiguration;

import java.util.List;

/**
 * @author Alejandro Duarte.
 */
public class CrudFormConfiguration {

    private List<CrudFieldConfiguration> crudFieldConfigurations;

    private String buttonCaption;

    private String errorMessage;

    private Resource buttonIcon;

    private String buttonStyle;

    private Button.ClickListener buttonClickListener;

    public CrudFormConfiguration(
            List<CrudFieldConfiguration> crudFieldConfigurations,
            String buttonCaption,
            String errorMessage,
            Resource buttonIcon,
            String buttonStyle,
            Button.ClickListener buttonClickListener
    ) {
        this.crudFieldConfigurations = crudFieldConfigurations;
        this.buttonCaption = buttonCaption;
        this.errorMessage = errorMessage;
        this.buttonIcon = buttonIcon;
        this.buttonStyle = buttonStyle;
        this.buttonClickListener = buttonClickListener;
    }

    public List<CrudFieldConfiguration> getCrudFieldConfigurations() {
        return crudFieldConfigurations;
    }

    public void setCrudFieldConfigurations(List<CrudFieldConfiguration> crudFieldConfigurations) {
        this.crudFieldConfigurations = crudFieldConfigurations;
    }

    public String getButtonCaption() {
        return buttonCaption;
    }

    public void setButtonCaption(String buttonCaption) {
        this.buttonCaption = buttonCaption;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Resource getButtonIcon() {
        return buttonIcon;
    }

    public void setButtonIcon(Resource buttonIcon) {
        this.buttonIcon = buttonIcon;
    }

    public String getButtonStyle() {
        return buttonStyle;
    }

    public void setButtonStyle(String buttonStyle) {
        this.buttonStyle = buttonStyle;
    }

    public Button.ClickListener getButtonClickListener() {
        return buttonClickListener;
    }

    public void setButtonClickListener(Button.ClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
    }

}

package org.vaadin.data;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.SingularAttribute;
import javax.validation.Configuration;
import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import javax.validation.constraints.Size;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.HasValue;
import com.vaadin.data.PropertyDefinition;
import com.vaadin.data.RequiredFieldConfigurator;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.UI;

public class JpaValidationBinder<BEAN> extends BeanValidationBinder<BEAN> {

    private final Class<BEAN> beanType;
    private ManagedType<BEAN> managedType;

	public JpaValidationBinder(Class<BEAN> beanType) {
		super(beanType);
		this.beanType = beanType;
	}
	
	public JpaValidationBinder(ManagedType<BEAN> managedType, Class<BEAN> beanType) {
		super(beanType);
		this.beanType = beanType;
		this.managedType=managedType;
	}

	@Override
	public <FIELDVALUE> BindingBuilder<BEAN, FIELDVALUE> forField(HasValue<FIELDVALUE> field) {
		return super.forField(field);
	}
	
    @Override
    protected BindingBuilder<BEAN, ?> configureBinding(BindingBuilder<BEAN, ?> binding,
            PropertyDefinition<BEAN, ?> definition) {
    	
    	BindingBuilder<BEAN, ?> bindingBuilder = super.configureBinding(binding, definition);

    	configureRequired(bindingBuilder, definition);
    	
        return bindingBuilder;
    }

    private void configureRequired(BindingBuilder<BEAN, ?> binding, PropertyDefinition<BEAN, ?> definition) {
    	Attribute<? super BEAN, ?> attribute = managedType.getAttribute(definition.getName());
    	
    	Member javaMember = attribute.getJavaMember();
    	
    	if (attribute.getJavaMember() instanceof AnnotatedElement) {
    		AnnotatedElement annotated = (AnnotatedElement)attribute.getJavaMember();
    		
    		
    		Column column = annotated.getAnnotation(Column.class);
    		if (column!=null && column.length()>0) {
        		if (javaMember instanceof Field && ((Field) javaMember).getType()==String.class 
        				|| javaMember instanceof Method && ((Method) javaMember).getReturnType()==String.class ) {
        				
    				MessageInterpolator dd = Validation.byDefaultProvider().configure().getDefaultMessageInterpolator();
    				Map<String, Object> attributes = new HashMap<>();
    				attributes.put("min", 0);
    				attributes.put("max", column.length());
    				MessageInterpolatorContext ctx = new MessageInterpolatorContext(
    						new ConstraintDescriptorImpl(column, null, null,null,null,null,attributes,null, true, null)
    						, null, null, Collections.EMPTY_MAP, Collections.EMPTY_MAP);
    				String msg = dd.interpolate("{javax.validation.constraints.Size.message}", ctx, UI.getCurrent().getLocale());
       				binding.withValidator((Validator) new StringLengthValidator(msg,null,column.length()));
        		}

    			
    		}
    	}
    	
		if (attribute instanceof SingularAttribute) {
			SingularAttribute<?, ?> singAtt = (SingularAttribute<?, ?>)attribute;
			
			if (!singAtt.isOptional()) {
				MessageInterpolator dd = Validation.byDefaultProvider().configure().getDefaultMessageInterpolator();
				binding.withValidator((v)-> v!=null, dd.interpolate("{javax.validation.constraints.NotNull.message}", null, UI.getCurrent().getLocale()));
				
				binding.getField().setRequiredIndicatorVisible(true);
			}
		}

    }

    

	public ManagedType<BEAN> getManagedType() {
		return managedType;
	}


	public void setManagedType(ManagedType<BEAN> managedType) {
		this.managedType = managedType;
	}
}

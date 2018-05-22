package org.vaadin.data;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintTarget;
import javax.validation.ConstraintValidator;
import javax.validation.Payload;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ValidateUnwrappedValue;

public class ConstraintDescriptorImpl<T extends Annotation> implements ConstraintDescriptor<T>, Serializable {

	private T annotation;
	private String messageTemplate;
	private Set<Class<?>> groups;
	private Set<Class<? extends Payload>> payload;
	private ConstraintTarget validationAppliesTo;
	private List<Class<? extends ConstraintValidator<T, ?>>> constraintValidatorClasses;
	private Map<String, Object> attributes;
	private Set<ConstraintDescriptor<?>> composingConstraints;
	private boolean reportAsSingleViolation;
	private ValidateUnwrappedValue valueUnwrapping;
	
	public ConstraintDescriptorImpl(T annotation, String messageTemplate, Set<Class<?>> groups,
			Set<Class<? extends Payload>> payload, ConstraintTarget validationAppliesTo,
			List<Class<? extends ConstraintValidator<T, ?>>> constraintValidatorClasses, Map<String, Object> attributes,
			Set<ConstraintDescriptor<?>> composingConstraints, boolean reportAsSingleViolation,
			ValidateUnwrappedValue valueUnwrapping) {

		this.annotation = annotation;
		this.messageTemplate = messageTemplate;
		this.groups = groups;
		this.payload = payload;
		this.validationAppliesTo = validationAppliesTo;
		this.constraintValidatorClasses = constraintValidatorClasses;
		this.attributes = attributes;
		this.composingConstraints = composingConstraints;
		this.reportAsSingleViolation = reportAsSingleViolation;
		this.valueUnwrapping = valueUnwrapping;
	}

	@Override
	public T getAnnotation() {
		return annotation;
	}

	@Override
	public String getMessageTemplate() {
		return messageTemplate;
	}

	@Override
	public Set<Class<?>> getGroups() {
		return groups;
	}

	@Override
	public Set<Class<? extends Payload>> getPayload() {
		return payload;
	}

	@Override
	public ConstraintTarget getValidationAppliesTo() {
		return validationAppliesTo;
	}

	@Override
	public List<Class<? extends ConstraintValidator<T, ?>>> getConstraintValidatorClasses() {
		return constraintValidatorClasses;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public Set<ConstraintDescriptor<?>> getComposingConstraints() {
		return composingConstraints;
	}

	@Override
	public boolean isReportAsSingleViolation() {
		return reportAsSingleViolation;
	}

	@Override
	public ValidateUnwrappedValue getValueUnwrapping() {
		return valueUnwrapping;
	}

	@Override
	public <U> U unwrap(Class<U> type) {
		// TODO Auto-generated method stub
		return null;
	}
}

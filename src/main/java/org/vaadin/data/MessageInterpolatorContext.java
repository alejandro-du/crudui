package org.vaadin.data;

import static org.hibernate.validator.internal.util.CollectionHelper.toImmutableMap;

import java.util.Map;

import javax.validation.MessageInterpolator;
import javax.validation.ValidationException;
import javax.validation.metadata.ConstraintDescriptor;

public class MessageInterpolatorContext implements MessageInterpolator.Context {

	private final ConstraintDescriptor<?> constraintDescriptor;
	private final Object validatedValue;
	private final Class<?> rootBeanType;
	private final Map<String, Object> messageParameters;
	private final Map<String, Object> expressionVariables;
	
	public MessageInterpolatorContext(ConstraintDescriptor<?> constraintDescriptor,
			Object validatedValue,
			Class<?> rootBeanType,
			Map<String, Object> messageParameters,
			Map<String, Object> expressionVariables) {
		this.constraintDescriptor = constraintDescriptor;
		this.validatedValue = validatedValue;
		this.rootBeanType = rootBeanType;
		this.messageParameters = toImmutableMap( messageParameters );
		this.expressionVariables = toImmutableMap( expressionVariables );
	}

	@Override
	public ConstraintDescriptor<?> getConstraintDescriptor() {
		return constraintDescriptor;
	}

	@Override
	public Object getValidatedValue() {
		return validatedValue;
	}

	@Override
	public <T> T unwrap(Class<T> type) {
		//allow unwrapping into public super types
		if ( type.isAssignableFrom( MessageInterpolatorContext.class ) ) {
			return type.cast( this );
		}
		throw new ValidationException("type not supported: " + type.toString());
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		MessageInterpolatorContext that = (MessageInterpolatorContext) o;

		if ( constraintDescriptor != null ? !constraintDescriptor.equals( that.constraintDescriptor ) : that.constraintDescriptor != null ) {
			return false;
		}
		if ( rootBeanType != null ? !rootBeanType.equals( that.rootBeanType ) : that.rootBeanType != null ) {
			return false;
		}
		if ( validatedValue != null ? !validatedValue.equals( that.validatedValue ) : that.validatedValue != null ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = constraintDescriptor != null ? constraintDescriptor.hashCode() : 0;
		result = 31 * result + ( validatedValue != null ? validatedValue.hashCode() : 0 );
		result = 31 * result + ( rootBeanType != null ? rootBeanType.hashCode() : 0 );
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append( "MessageInterpolatorContext" );
		sb.append( "{constraintDescriptor=" ).append( constraintDescriptor );
		sb.append( ", validatedValue=" ).append( validatedValue );
		sb.append( ", messageParameters=" ).append( messageParameters );
		sb.append( ", expressionVariables=" ).append( expressionVariables );
		sb.append( '}' );
		return sb.toString();
	}
}

package org.vaadin.data.converter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class StringToCharacterConverter implements Converter<String, Character> {

	@Override
	public Result<Character> convertToModel(String value, ValueContext context) {
		if (value == null) {
			return Result.ok(null);
		}

		if (value.length() > 1) {
			return Result.error("Could not convert '" + value);
		}

		return Result.ok(value.charAt(0));
	}

	@Override
	public String convertToPresentation(Character value, ValueContext context) {
		if (value == null) {
			return null;
		}

		return value.toString();
	}

}

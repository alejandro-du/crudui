package org.vaadin.data.converter;

import java.text.*;
import java.util.*;

import com.vaadin.data.*;

public class StringToCharacterConverter implements Converter<String, Character> {

    /**
     * Returns the format used by {@link #convertToPresentation(Date, ValueContext)}
     * and {@link #convertToModel(String, ValueContext)}.
     *
     * @param locale
     *            The locale to use
     * @return A DateFormat instance
     */
    protected DateFormat getFormat(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }

        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
                DateFormat.MEDIUM, locale);
        format.setLenient(false);
        return format;
    }

    @Override
    public Result<Character> convertToModel(String value, ValueContext context) {
        if (value == null) {
            return Result.ok(null);
        }

        if (value.length()>1) {
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

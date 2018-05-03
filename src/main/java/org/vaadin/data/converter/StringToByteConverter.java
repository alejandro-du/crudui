package org.vaadin.data.converter;

import java.text.*;
import java.util.*;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.AbstractStringToNumberConverter;

public class StringToByteConverter extends AbstractStringToNumberConverter<Byte> {

    /**
     * Creates a new converter instance with the given error message. Empty
     * strings are converted to <code>null</code>.
     *
     * @param errorMessage
     *            the error message to use if conversion fails
     */
    public StringToByteConverter(String errorMessage) {
        this(null, errorMessage);
    }

    /**
     * Creates a new converter instance with the given empty string value and
     * error message.
     *
     * @param emptyValue
     *            the presentation value to return when converting an empty
     *            string, may be <code>null</code>
     * @param errorMessage
     *            the error message to use if conversion fails
     */
    public StringToByteConverter(Byte emptyValue, String errorMessage) {
        super(emptyValue, errorMessage);
    }

    /**
     * Returns the format used by
     * {@link #convertToPresentation(Object, ValueContext)} and
     * {@link #convertToModel(String, ValueContext)}.
     *
     * @param locale
     *            The locale to use
     * @return A NumberFormat instance
     */
    @Override
    protected NumberFormat getFormat(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return NumberFormat.getIntegerInstance(locale);
    }

    @Override
    public Result<Byte> convertToModel(String value, ValueContext context) {
        Result<Number> n = convertToNumber(value, context.getLocale().orElse(null));
        return n.flatMap(number -> {
            if (number == null) {
                return Result.ok(null);
            } else {
                byte intValue = number.byteValue();
                if (intValue == number.longValue()) {
                    // If the value of n is outside the range of long, the
                    // return value of longValue() is either Long.MIN_VALUE or
                    // Long.MAX_VALUE. The/ above comparison promotes int to
                    // long and thus does not need to consider wrap-around.
                    return Result.ok(intValue);
                } else {
                    return Result.error(getErrorMessage());
                }
            }
        });
    }

}

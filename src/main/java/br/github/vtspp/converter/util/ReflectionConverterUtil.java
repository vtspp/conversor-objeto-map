package br.github.vtspp.converter.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

public final class ReflectionConverterUtil {

    private static final String BOOLEAN_PRIMITIVE_NAME = "boolean";
    private static final String PREFIX_BOOLEAN_PRIMITIVO = "is";
    private static final String PREFIX_ACCESS = "get";

    private ReflectionConverterUtil() {
    }

    public static Method accessMethod(Method method) {
        if (Objects.nonNull(method))
            method.setAccessible(true);
        return method;
    }

    public static String toFieldClassName(Field field) {
        var prefix = isBooleanPrimitivo(field) ? PREFIX_BOOLEAN_PRIMITIVO : PREFIX_ACCESS;

        return field.getName().transform(s -> {
            var stringArray = s.split("");
            stringArray[0] = stringArray[0].toUpperCase();

            final var builder = new StringBuilder(prefix);

            for (var string : stringArray)
                builder.append(string);

            return builder.toString();
        });
    }

     private static boolean isBooleanPrimitivo(Field field) {
        return field.getType().isPrimitive() && BOOLEAN_PRIMITIVE_NAME.equals(field.getType().getName());
    }
}

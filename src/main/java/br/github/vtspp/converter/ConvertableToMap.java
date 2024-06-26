package br.github.vtspp.converter;

import br.github.vtspp.converter.exception.ConvertableToMapException;
import br.github.vtspp.converter.exception.FailExtractMethodException;
import br.github.vtspp.converter.util.ReflectionConverterUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

abstract class ConvertableToMap<T> {

    public Map<String, Object> toMap(T obj, boolean skipNullValues) {
        Objects.requireNonNull(obj, "O objeto a ser convertido não pode ser nulo");

        final var objToMap = new LinkedHashMap<String, Object>();
        final var fields = obj.getClass().getDeclaredFields();

        Arrays.stream(fields).forEach(field -> {
            final var value = extractValue(obj, fieldNameByStrategy(field));

            if (Objects.isNull(value) && skipNullValues)
                return;

            objToMap.put(field.getName(), value);
        });

        return objToMap;
    }

    protected abstract String fieldNameByStrategy(Field field);

    protected Method extractMethod(T obj, final String fieldName) {
        try {
            var method = obj.getClass().getMethod(fieldName);
            return ReflectionConverterUtil.accessMethod(method);
        } catch (NoSuchMethodException e) {
            throw new FailExtractMethodException(String.format("Falha ao extrair o método do objeto %s devido não existir a declaração do método %s", obj.getClass().getName(),e.getMessage()));
        }
    }

    protected Object extractValue(T obj, final String fieldName) {
        var callback = new AtomicReference<Method>();
        try {
            callback.set(extractMethod(obj, fieldName));
            return callback.get().invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            final var messages = Map.of(
                    IllegalAccessException.class, "Falha ao acessar o método \"%s()\" do objeto %s. Causa do erro: %s",
                    InvocationTargetException.class, "Falha ao invocar o método \"%s()\" do objeto %s. Causa do erro: %s"
            );

            throw new ConvertableToMapException(String.format(messages.get(e.getClass()), callback.get().getName(), obj.getClass().getSimpleName(), e.getMessage()));
        }
    }
}

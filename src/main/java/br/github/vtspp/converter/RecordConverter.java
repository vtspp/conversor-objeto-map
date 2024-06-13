package br.github.vtspp.converter;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class RecordConverter<T> extends ConvertableToMap<T> {

    @Override
    public Map<String, Object> toMap(T obj, boolean skipNullValues) {
        Objects.requireNonNull(obj, "O objeto a ser convertido não pode ser nulo");

        final var objToMap = new LinkedHashMap<String, Object>();
        final var fields = obj.getClass().getDeclaredFields();

        Arrays.stream(fields).forEach(field -> {
            final var value = extractValue(obj, field.getName());

            if (Objects.isNull(value) && skipNullValues)
                return;

            objToMap.put(field.getName(), value);
        });

        return objToMap;
    }
}

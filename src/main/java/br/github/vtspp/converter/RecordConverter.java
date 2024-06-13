package br.github.vtspp.converter;

import java.lang.reflect.Field;

public class RecordConverter<T> extends ConvertableToMap<T> {

    @Override
    protected String fieldNameByStrategy(Field field) {
        return field.getName();
    }
}

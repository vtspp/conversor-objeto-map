package br.github.vtspp.converter;

import br.github.vtspp.converter.util.ReflectionConverterUtil;

import java.lang.reflect.Field;

public class ClassConverter<T> extends ConvertableToMap<T> {

    @Override
    protected String fieldNameByStrategy(Field field) {
        return ReflectionConverterUtil.toFieldClassName(field);
    }
}

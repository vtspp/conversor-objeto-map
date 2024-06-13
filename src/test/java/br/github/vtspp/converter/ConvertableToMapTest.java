package br.github.vtspp.converter;

import br.github.vtspp.converter.exception.FailExtractMethodException;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class ConvertableToMapTest {

    public static void main(String[] args) {
        System.out.printf("shouldToConvertRecordObjetToMap: %s \n", shouldToConvertRecordObjetToMap() ? "passed" : "failed");
        System.out.printf("shouldToConvertClassObjetToMap: %s \n", shouldToConvertClassObjetToMap() ? "passed" : "failed");
        System.out.printf("shouldExcludeNullValuesWhenSkipNullValuesIsTrue: %s \n", shouldExcludeNullValuesWhenSkipNullValuesIsTrue() ? "passed" : "failed");
        System.out.printf("shouldNotExcludeNullValuesWhenSkipNullValuesIsFalse: %s \n", shouldNotExcludeNullValuesWhenSkipNullValuesIsFalse() ? "passed" : "failed");
        System.out.printf("shouldThrowFailExtractMethodExceptionWhenMethodGetNotDeclared: %s \n", shouldThrowFailExtractMethodExceptionWhenMethodGetNotDeclared() ? "passed" : "failed");
    }

    static boolean shouldToConvertRecordObjetToMap() {
        record Person(long id, String name, LocalDate birthdate) {}

        var person = new Person(1L, "Victor", LocalDate.now());
        var expectedMap = Map.of(
                "id", person.id,
                "name", person.name,
                "birthdate", person.birthdate);

        var converter = new RecordConverter<Person>();

        return expectedMap.equals(converter.toMap(person, true));
    }

    static boolean shouldToConvertClassObjetToMap() {
        class Person {
            private long id;
            private String name;
            private LocalDate birthdate;

            public Person(long id, String name, LocalDate birthdate) {
                this.id = id;
                this.name = name;
                this.birthdate = birthdate;
            }

            public long getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public LocalDate getBirthdate() {
                return birthdate;
            }
        }

        var person = new Person(1L, "Victor", LocalDate.now());
        var expectedMap = Map.of(
                "id", person.id,
                "name", person.name,
                "birthdate", person.birthdate);

        var converter = new ClassConverter<Person>();

        return expectedMap.equals(converter.toMap(person, true));
    }

    static boolean shouldExcludeNullValuesWhenSkipNullValuesIsTrue() {
        record Person(long id, String name, LocalDate birthdate) {}

        var person = new Person(1L, "Victor", null);
        var expectedMap = new LinkedHashMap<>();
        expectedMap.put("id", person.id);
        expectedMap.put("name", person.name);
        expectedMap.put("birthdate", null);

        var converter = new RecordConverter<Person>();
        var skipNullValues = true;
        var result = converter.toMap(person, skipNullValues);

        return expectedMap.equals(result) && !result.containsKey("birthdate");
    }

    static boolean shouldNotExcludeNullValuesWhenSkipNullValuesIsFalse() {
        record Person(long id, String name, LocalDate birthdate) {}

        var person = new Person(1L, "Victor", null);
        var expectedMap = new LinkedHashMap<>();
        expectedMap.put("id", person.id);
        expectedMap.put("name", person.name);
        expectedMap.put("birthdate", null);

        var converter = new RecordConverter<Person>();
        var skipNullValues = false;
        var result = converter.toMap(person, skipNullValues);

        return expectedMap.equals(result) && result.containsKey("birthdate") && Objects.isNull(result.get("birthdate"));
    }

    static boolean shouldThrowFailExtractMethodExceptionWhenMethodGetNotDeclared() {
        class Person {
            private long id;
            private String name;
            private LocalDate birthdate;

            public Person(long id, String name, LocalDate birthdate) {
                this.id = id;
                this.name = name;
                this.birthdate = birthdate;
            }
        }

        var person = new Person(1L, "Victor", LocalDate.now());

        var converter = new ClassConverter<Person>();

        try {
            return converter.toMap(person, true) instanceof FailExtractMethodException;
        } catch (RuntimeException e) {
            return e instanceof FailExtractMethodException;
        }
    }
}

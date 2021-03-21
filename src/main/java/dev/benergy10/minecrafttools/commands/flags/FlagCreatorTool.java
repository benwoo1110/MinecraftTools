package dev.benergy10.minecrafttools.commands.flags;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FlagCreatorTool {

    public static class BooleanCreator {

        private final String name;
        private final String identifier;
        private boolean presentValue = true;


        public BooleanCreator(String name, String identifier) {
            this.name = name;
            this.identifier = identifier;
        }

        public BooleanCreator presentValue(boolean presentValue) {
            this.presentValue = presentValue;
            return this;
        }

        public Flag<Boolean> create() {
            return new NoValueFlag<Boolean>(name, identifier, Boolean.class) {
                @Override
                public Boolean getValue() {
                    return presentValue;
                }

                @Override
                public Boolean getDefaultValue() {
                    return !presentValue;
                }
            };
        }
    }

    public static class NumberCreator<T extends Number & Comparable<T>> {

        private final String name;
        private final String identifier;
        private final Class<T> numberClass;
        private Function<String, T> parser;
        private T defaultValue;
        private T min;
        private T max;

        public NumberCreator(String name, String identifier, Class<T> numberClass) {
            this.name = name;
            this.identifier = identifier;
            this.numberClass = numberClass;
        }

        public NumberCreator<T> parser(Function<String, T> parser) {
            this.parser = parser;
            return this;
        }

        public NumberCreator<T> defaultValue(T defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public NumberCreator<T> range(T min, T max) {
            this.min = min;
            this.max = max;
            return this;
        }

        public Flag<T> create() {
            Objects.requireNonNull(parser);
            return new RequiredValueFlag<T>(name, identifier, numberClass) {
                @Override
                public Collection<String> suggestValue() {
                    return IntStream.range(1, 21).boxed().map(Object::toString).collect(Collectors.toList());
                }

                @Override
                public T getValue(@NotNull String input) {
                    T num;
                    try {
                        num = parser.apply(input);
                    } catch (NumberFormatException e) {
                        throw new FlagParseFailedException("%s is not a number!", input);
                    }
                    if (!isInRange(num)) {
                        throw new FlagParseFailedException("%s is not in valid range!", input);
                    }
                    return num;
                }

                @Override
                public T getDefaultValue() {
                    return defaultValue;
                }
            };
        }

        private boolean isInRange(T num) {
            if (min != null && num.compareTo(min) < 0) {
                return false;
            }
            if (max != null && num.compareTo(max) > 0) {
                return false;
            }
            return true;
        }

    }
}

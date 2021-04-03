package dev.benergy10.minecrafttools.configs;

class SimpleConfigOptionHandler<T> implements ConfigOptionHandler<T, Object> {

    SimpleConfigOptionHandler() { }

    @Override
    public Object serialize(T t) {
        return t;
    }

    @Override
    public T deserialize(Object obj) {
        return (T) obj;
    }
}

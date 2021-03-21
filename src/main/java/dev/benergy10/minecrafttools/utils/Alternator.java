package dev.benergy10.minecrafttools.utils;

import org.jetbrains.annotations.NotNull;

public class Alternator<T> {

    public static @NotNull Alternator<Boolean> ofBoolean() {
        return ofBoolean(true);
    }

    public static @NotNull Alternator<Boolean> ofBoolean(boolean startWith) {
        return new Alternator<>(startWith, !startWith);
    }

    public static <T> @NotNull Alternator<T> of(T thisT, T thatT) {
        return new Alternator<>(thisT, thatT);
    }

    private T thisT;
    private T thatT;
    boolean state = false;

    public Alternator(T thisT, T thatT) {
        this.thisT = thisT;
        this.thatT = thatT;
    }

    public T get() {
        return (state ^= true) ? thisT : thatT;
    }

    public T getThis() {
        return thisT;
    }

    public void setThis(T thisT) {
        this.thisT = thisT;
    }

    public T getThat() {
        return thatT;
    }

    public void setThat(T thatT) {
        this.thatT = thatT;
    }

    public boolean isState() {
        return state;
    }
}

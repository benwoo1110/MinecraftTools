package dev.benergy10.minecrafttools.utils;

import com.google.common.base.Function;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Utility class used to help in doing various reflection actions.
 */
public final class ReflectHelper {

    private static final HashMap<String, Class<?>> CACHED_CLASSES = new HashMap<>();

    /**
     * Try to get the {@link Class} based on its classpath.
     *
     * @param classPath     The target classpath.
     * @param recalculate   Whether to invalidate cache and get new result.
     * @return A {@link Class} if found, else null.
     */
    @Nullable
    public static Class<?> getClass(String classPath, boolean recalculate) {
        if (recalculate) {
            CACHED_CLASSES.remove(classPath);
        }
        return CACHED_CLASSES.computeIfAbsent(classPath, (Function<String, Class<?>>) input -> {
            try {
                return Class.forName(input);
            } catch (ClassNotFoundException e) {
                return null;
            }
        });
    }

    /**
     * Try to get the {@link Class} based on its classpath. Results are cached.
     *
     * @param classPath The target classpath.
     * @return A {@link Class} if found, else null.
     */
    public static Class<?> getClass(String classPath) {
        return getClass(classPath, false);
    }

    /**
     * Check if the {@link Class} for a give classpath is present/valid.
     *
     * @param classPath Target classpath.
     * @return True if class path is a valid class, else false.
     */
    public static boolean hasClass(String classPath) {
        return getClass(classPath) != null;
    }

    /**
     * Try to get a {@link Method} from a given class.
     *
     * @param clazz             The class to search the method on.
     * @param methodName        Name of the method to get.
     * @param parameterTypes    Parameters present for that method.
     * @param <C>               The class type.
     * @return A {@link Method} if found, else null.
     */
    @Nullable
    public static <C> Method getMethod(Class<C> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * Try to get a {@link Method} from a given class.
     *
     * @param classInstance     Instance of the class to search the method on.
     * @param methodName        Name of the method to get.
     * @param parameterTypes    Parameters present for that method.
     * @param <C>               The class type.
     * @return A {@link Method} if found, else null.
     */
    @Nullable
    public static <C> Method getMethod(C classInstance, String methodName, Class<?>... parameterTypes) {
        return getMethod(classInstance.getClass(), methodName, parameterTypes);
    }

    /**
     * Calls a {@link Method}.
     *
     * @param classInstance Instance of the class responsible for the method.
     * @param method        The method to call.
     * @param parameters    Parameters needed when calling the method.
     * @param <C>           The class type.
     * @param <R>           The return type.
     * @return Return value of the method call if any, else null.
     */
    @Nullable
    public static <C, R> R invokeMethod(C classInstance, Method method, Object...parameters) {
        try {
            return (R) method.invoke(classInstance, parameters);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Try to get a {@link Field} from a given class.
     *
     * @param clazz     The class to search the field on.
     * @param fieldName Name of the field to get.
     * @param <C>       The class type.
     * @return A {@link Field} if found, else null.
     */
    @Nullable
    public static <C> Field getField(Class<C> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    /**
     * Try to get a {@link Field} from a given class.
     *
     * @param classInstance Instance of the class to search the field on.
     * @param fieldName     Name of the field to get.
     * @param <C>           The class type.
     * @return A {@link Field} if found, else null.
     */
    @Nullable
    public static <C> Field getField(C classInstance, String fieldName) {
        return getField(classInstance.getClass(), fieldName);
    }

    /**
     * Gets the value of an {@link Field} from an instance of the class responsible.
     *
     * @param classInstance Instance of the class to get the field value from.
     * @param field         The field to get value from.
     * @param <C>           The class type.
     * @param <V>           The field value type.
     * @return The field value if any, else null.
     */
    @Nullable
    public static <C, V> V getFieldValue(C classInstance, Field field)  {
        try {
            return (V) field.get(classInstance);
        } catch (IllegalAccessException e) {
            return null;
        }
    }
}

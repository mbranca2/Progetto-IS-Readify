package testUtil;

import java.lang.reflect.Field;

public final class ReflectionTestUtils {
    private ReflectionTestUtils() {}

    public static void setField(Object target, String fieldName, Object value) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Impossibile settare field '" + fieldName + "' su " + target.getClass(), e);
        }
    }
}
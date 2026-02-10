package testUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class ReflectionTestUtils {
    private ReflectionTestUtils() {}

    public static void setField(Object target, String fieldName, Object value) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            if (Modifier.isFinal(f.getModifiers())) {
                try {
                    Field modifiersField = Field.class.getDeclaredField("modifiers");
                    modifiersField.setAccessible(true);
                    modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
                } catch (NoSuchFieldException ignored) {
                    // JDK 12+ removes the modifiers field; rely on setAccessible only.
                }
            }
            f.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Impossibile settare field '" + fieldName + "' su " + target.getClass(), e);
        }
    }
}
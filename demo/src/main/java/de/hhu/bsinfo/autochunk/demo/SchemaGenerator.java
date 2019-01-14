package de.hhu.bsinfo.autochunk.demo;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SchemaGenerator {

    private static final sun.misc.Unsafe unsafe = getUnsafe();

    private static sun.misc.Unsafe getUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe) f.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static int generate(Class<?> clazz) {

        List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
        fields.sort(Comparator.comparing(Field::getName));

        for (Field field : fields) {
            System.out.printf("%s %s - %d\n", field.getType().getCanonicalName(), field.getName(), unsafe.objectFieldOffset(field));
        }

        return 0;
    }

    public static void main(String args[]) {
        generate(TestChunk.class);
    }
}

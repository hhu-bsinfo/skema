package de.hhu.bsinfo.skema.random;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import de.hhu.bsinfo.skema.schema.Schema;
import de.hhu.bsinfo.skema.schema.SchemaRegistry;
import de.hhu.bsinfo.skema.util.ClassUtil;
import de.hhu.bsinfo.skema.util.Constants;
import de.hhu.bsinfo.skema.util.FieldUtil;
import de.hhu.bsinfo.skema.util.UnsafeProvider;

@SuppressWarnings("sunapi")
public class ObjectGenerator {

    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();

    private static final Config DEFAULT_CONFIG = new Config(4, 32);

    public static <T> T newInstance(final Class<T> clazz) {
        return newInstance(clazz, DEFAULT_CONFIG, ThreadLocalRandom.current());
    }

    public static <T> T newInstance(final Class<T> clazz, final Config config) {
        return newInstance(clazz, config, ThreadLocalRandom.current());
    }

    public static <T> T newInstance(final Class<T> clazz, final Random random) {
        return newInstance(clazz, DEFAULT_CONFIG, random);
    }

    public static <T> T newInstance(final Class<T> clazz, final Config config, final Random random) {

        if (clazz.isEnum()) {
            return (T) FieldUtil.randomEnum(clazz, random);
        }

        T object = ClassUtil.allocateInstance(clazz);
        if (object == null) {
            return null;
        }

        randomize(object, config, random);
        return object;
    }

    static void randomize(final Object instance, final Config config, final Random random) {
        Schema schema = SchemaRegistry.getSchema(instance.getClass());
        int arrayLength = 0;
        int i;
        int j;
        Object object;
        Object[] array;
        Schema.FieldSpec fieldSpec;
        Schema.FieldSpec[] fields = schema.getFields();
        for (i = 0; i < fields.length; i++) {
            fieldSpec = fields[i];
            switch (fieldSpec.getFieldType()) {

                case BYTE:
                    UNSAFE.putByte(instance, fieldSpec.getOffset(), (byte) (random.nextInt(Byte.MAX_VALUE) * (random.nextBoolean() ? 1 : -1)));
                    break;

                case CHAR:
                    UNSAFE.putChar(instance, fieldSpec.getOffset(), (char) (random.nextInt(Character.MAX_VALUE) * (random.nextBoolean() ? 1 : -1)));
                    break;

                case SHORT:
                    UNSAFE.putShort(instance, fieldSpec.getOffset(), (short) (random.nextInt(Short.MAX_VALUE) * (random.nextBoolean() ? 1 : -1)));
                    break;

                case INT:
                    UNSAFE.putInt(instance, fieldSpec.getOffset(), random.nextInt());
                    break;

                case LONG:
                    UNSAFE.putLong(instance, fieldSpec.getOffset(), random.nextLong());
                    break;

                case FLOAT:
                    UNSAFE.putFloat(instance, fieldSpec.getOffset(), random.nextFloat());
                    break;

                case DOUBLE:
                    UNSAFE.putDouble(instance, fieldSpec.getOffset(), random.nextDouble());
                    break;

                case BOOLEAN:
                    UNSAFE.putBoolean(instance, fieldSpec.getOffset(), random.nextBoolean());
                    break;

                case BYTE_ARRAY:
                case CHAR_ARRAY:
                case SHORT_ARRAY:
                case INT_ARRAY:
                case LONG_ARRAY:
                case FLOAT_ARRAY:
                case DOUBLE_ARRAY:
                case BOOLEAN_ARRAY:
                    arrayLength = random.nextInt(config.getMaxArrayLength() - config.getMinArrayLength() + 1) + config.getMinArrayLength();
                    object = FieldUtil.randomArray(fieldSpec, arrayLength, random);
                    UNSAFE.putObject(instance, fieldSpec.getOffset(), object);
                    break;

                case ENUM:
                    object = FieldUtil.randomEnum(fieldSpec, random);
                    UNSAFE.putObject(instance, fieldSpec.getOffset(), object);
                    break;

                case OBJECT:
                    object = FieldUtil.allocateInstance(fieldSpec);
                    randomize(object, config, random);
                    UNSAFE.putObject(instance, fieldSpec.getOffset(), object);
                    break;

                case OBJECT_ARRAY:
                    arrayLength = random.nextInt(config.getMaxArrayLength() - config.getMinArrayLength() + 1) + config.getMinArrayLength();
                    array = FieldUtil.allocateArray(fieldSpec, arrayLength);
                    for (j = 0; j < arrayLength; j++) {
                        object = FieldUtil.allocateComponent(fieldSpec);
                        randomize(object, config, random);
                        UNSAFE.putObject(array, Constants.OBJECT_ARRAY_OFFSET + j * Constants.REFERENCE_SIZE, object);
                    }
                    UNSAFE.putObject(instance, fieldSpec.getOffset(), array);
                    break;

                default:
                    break;
            }
        }
    }

    public static final class Config {
        private final int minArrayLength;
        private final int maxArrayLength;

        public Config(int p_minArrayLength, int p_maxArrayLength) {
            minArrayLength = p_minArrayLength;
            maxArrayLength = p_maxArrayLength;
        }

        public int getMinArrayLength() {
            return minArrayLength;
        }

        public int getMaxArrayLength() {
            return maxArrayLength;
        }
    }
}

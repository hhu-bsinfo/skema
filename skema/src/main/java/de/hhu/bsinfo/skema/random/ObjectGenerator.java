package de.hhu.bsinfo.skema.random;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import de.hhu.bsinfo.skema.Skema;
import de.hhu.bsinfo.skema.schema.Schema;
import de.hhu.bsinfo.skema.schema.SchemaRegistry;
import de.hhu.bsinfo.skema.util.ClassUtil;
import de.hhu.bsinfo.skema.util.Constants;
import de.hhu.bsinfo.skema.util.FieldUtil;
import de.hhu.bsinfo.skema.util.UnsafeProvider;

public class ObjectGenerator {

    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();

    private static final Config DEFAULT_CONFIG = new Config(4, 32);

    public static <T> T newInstance(final Class<T> p_class) {
        return newInstance(p_class, DEFAULT_CONFIG, ThreadLocalRandom.current());
    }

    public static <T> T newInstance(final Class<T> p_class, final Config p_config) {
        return newInstance(p_class, p_config, ThreadLocalRandom.current());
    }

    public static <T> T newInstance(final Class<T> p_class, final Random p_random) {
        return newInstance(p_class, DEFAULT_CONFIG, p_random);
    }

    public static <T> T newInstance(final Class<T> p_class, final Config p_config, final Random p_random) {

        if (p_class.isEnum()) {
            return (T) FieldUtil.randomEnum(p_class, p_random);
        }

        T object = ClassUtil.allocateInstance(p_class);
        if (object == null) {
            return null;
        }

        randomize(object, p_config, p_random);
        return object;
    }

    static void randomize(final Object p_object, final Config p_config, final Random p_random) {
        Schema schema = SchemaRegistry.getSchema(p_object.getClass());
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
                    UNSAFE.putByte(p_object, fieldSpec.getOffset(), (byte) (p_random.nextInt(Byte.MAX_VALUE) * (p_random.nextBoolean() ? 1 : -1)));
                    break;

                case CHAR:
                    UNSAFE.putChar(p_object, fieldSpec.getOffset(), (char) (p_random.nextInt(Character.MAX_VALUE) * (p_random.nextBoolean() ? 1 : -1)));
                    break;

                case SHORT:
                    UNSAFE.putShort(p_object, fieldSpec.getOffset(), (short) (p_random.nextInt(Short.MAX_VALUE) * (p_random.nextBoolean() ? 1 : -1)));
                    break;

                case INT:
                    UNSAFE.putInt(p_object, fieldSpec.getOffset(), p_random.nextInt());
                    break;

                case LONG:
                    UNSAFE.putLong(p_object, fieldSpec.getOffset(), p_random.nextLong());
                    break;

                case FLOAT:
                    UNSAFE.putFloat(p_object, fieldSpec.getOffset(), p_random.nextFloat());
                    break;

                case DOUBLE:
                    UNSAFE.putDouble(p_object, fieldSpec.getOffset(), p_random.nextDouble());
                    break;

                case BOOLEAN:
                    UNSAFE.putBoolean(p_object, fieldSpec.getOffset(), p_random.nextBoolean());
                    break;

                case BYTE_ARRAY:
                case CHAR_ARRAY:
                case SHORT_ARRAY:
                case INT_ARRAY:
                case LONG_ARRAY:
                case FLOAT_ARRAY:
                case DOUBLE_ARRAY:
                case BOOLEAN_ARRAY:
                    arrayLength = p_random.nextInt(p_config.getMaxArrayLength() - p_config.getMinArrayLength() + 1) + p_config.getMinArrayLength();
                    object = FieldUtil.randomArray(fieldSpec, arrayLength, p_random);
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), object);
                    break;

                case ENUM:
                    object = FieldUtil.randomEnum(fieldSpec, p_random);
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), object);
                    break;

                case OBJECT:
                    object = FieldUtil.allocateInstance(fieldSpec);
                    randomize(object, p_config, p_random);
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), object);
                    break;

                case OBJECT_ARRAY:
                    arrayLength = p_random.nextInt(p_config.getMaxArrayLength() - p_config.getMinArrayLength() + 1) + p_config.getMinArrayLength();
                    array = FieldUtil.allocateArray(fieldSpec, arrayLength);
                    for (j = 0; j < arrayLength; j++) {
                        object = FieldUtil.allocateComponent(fieldSpec);
                        randomize(object, p_config, p_random);
                        UNSAFE.putObject(array, Constants.OBJECT_ARRAY_OFFSET + j * Constants.REFERENCE_SIZE, object);
                    }
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), array);
                    break;

                default:
                    break;
            }
        }
    }

    public static final class Config {
        private final int m_minArrayLength;
        private final int m_maxArrayLength;

        public Config(int p_minArrayLength, int p_maxArrayLength) {
            m_minArrayLength = p_minArrayLength;
            m_maxArrayLength = p_maxArrayLength;
        }

        public int getMinArrayLength() {
            return m_minArrayLength;
        }

        public int getMaxArrayLength() {
            return m_maxArrayLength;
        }
    }
}

package de.hhu.bsinfo.skema;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hhu.bsinfo.skema.random.ObjectGenerator;
import de.hhu.bsinfo.skema.schema.Schema;
import de.hhu.bsinfo.skema.schema.SchemaRegistry;
import de.hhu.bsinfo.skema.util.ClassUtil;
import de.hhu.bsinfo.skema.util.Operation;
import de.hhu.bsinfo.skema.util.UnsafeProvider;

@SuppressWarnings({"WeakerAccess", "sunapi"})
public final class Skema {

    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();

    private Skema() {}

    public static byte[] serialize(final Object p_object) {
        Schema schema = SchemaRegistry.getSchema(p_object.getClass());
        byte[] buffer = new byte[schema.getSize(p_object)];
        serialize(p_object, buffer);
        return buffer;
    }

    public static int serialize(final Object p_object, final long p_address) {
        return FullSerializer.serialize(p_object, p_address);
    }

    public static int serialize(final Object p_object, final byte[] p_buffer) {
        return FullSerializer.serialize(p_object, p_buffer, 0);
    }

    public static void deserialize(final Object p_object, final byte[] p_buffer) {
        FullDeserializer.deserialize(p_object, p_buffer, 0);
    }

    public static <T> T deserialize(final Class<T> p_class, final byte[] p_buffer) {
        return deserialize(p_class, p_buffer, 0);
    }

    public static <T> T deserialize(final Class<T> p_class, final long p_address) {
        if (p_class.isEnum()) {
            return FullDeserializer.deserializeEnum(p_class, p_address);
        }

        try {
            Object object = UNSAFE.allocateInstance(p_class);
            FullDeserializer.deserialize(object, p_address);
            return p_class.cast(object);
        } catch (InstantiationException e) {
            return null;
        }
    }

    public static <T> T deserialize(final Class<T> p_class, final byte[] p_buffer, final int p_offset) {
        if (p_class.isEnum()) {
            return FullDeserializer.deserializeEnum(p_class, p_buffer, p_offset);
        }

        try {
            Object object = UNSAFE.allocateInstance(p_class);
            FullDeserializer.deserialize(object, p_buffer, p_offset);
            return p_class.cast(object);
        } catch (InstantiationException e) {
            return null;
        }
    }

    public static int serialize(final Operation p_operation, final byte[] p_buffer, final int p_offset, final int p_length) {
        if (p_length == 0) {
            return 0;
        }

        // Try to serialize an interrupted field first
        int totalBytes = 0;
        if (p_operation.getStatus() == Operation.Status.INTERRUPTED) {
            totalBytes += PartialSerializer.serializeInterrupted(p_operation, p_buffer, p_offset, p_length);
        }

        // Perform normal serialization if the interruption status has been cleared
        if (p_operation.getStatus() == Operation.Status.NONE && p_length - totalBytes > 0) {
            totalBytes += PartialSerializer.serializeNormal(p_operation, p_buffer, p_offset + totalBytes, p_length - totalBytes);
        }

        // Write interrupted field if normal serialization did not work
        if (p_operation.getStatus() == Operation.Status.INTERRUPTED && p_length - totalBytes > 0) {
            totalBytes += PartialSerializer.serializeInterrupted(p_operation, p_buffer, p_offset + totalBytes, p_length - totalBytes);
        }

        return totalBytes;
    }

    public static int serialize(final Operation p_operation, final long p_address, final int p_length) {
        if (p_length == 0) {
            return 0;
        }

        // Try to serialize an interrupted field first
        int totalBytes = 0;
        if (p_operation.getStatus() == Operation.Status.INTERRUPTED) {
            totalBytes += PartialSerializer.serializeInterrupted(p_operation, p_address, p_length);
        }

        // Perform normal serialization if the interruption status has been cleared
        if (p_operation.getStatus() == Operation.Status.NONE && p_length - totalBytes > 0) {
            totalBytes += PartialSerializer.serializeNormal(p_operation, p_address + totalBytes, p_length - totalBytes);
        }

        // Write interrupted field if normal serialization did not work
        if (p_operation.getStatus() == Operation.Status.INTERRUPTED && p_length - totalBytes > 0) {
            totalBytes += PartialSerializer.serializeInterrupted(p_operation, p_address + totalBytes, p_length - totalBytes);
        }

        return totalBytes;
    }

    public static int deserialize(final Operation p_operation, final byte[] p_buffer, final int p_offset, final int p_length) {
        if (p_length == 0) {
            return 0;
        }

        // Try to serialize an interrupted field first
        int totalBytes = 0;
        if (p_operation.getStatus() == Operation.Status.INTERRUPTED) {
            totalBytes += PartialDeserializer.deserializeInterrupted(p_operation, p_buffer, p_offset, p_length);
        }

        // Perform normal serialization if the interruption status has been cleared
        if (p_operation.getStatus() == Operation.Status.NONE && p_length - totalBytes > 0) {
            totalBytes += PartialDeserializer.deserializeNormal(p_operation, p_buffer, p_offset + totalBytes, p_length - totalBytes);
        }

        // Write interrupted field if normal serialization did not work
        if (p_operation.getStatus() == Operation.Status.INTERRUPTED && p_length - totalBytes > 0) {
            totalBytes += PartialDeserializer.deserializeInterrupted(p_operation, p_buffer, p_offset + totalBytes, p_length - totalBytes);
        }

        return totalBytes;
    }

    public static int deserialize(final Operation p_operation, final long p_address, final int p_length) {
        if (p_length == 0) {
            return 0;
        }

        // Try to serialize an interrupted field first
        int totalBytes = 0;
        if (p_operation.getStatus() == Operation.Status.INTERRUPTED) {
            totalBytes += PartialDeserializer.deserializeInterrupted(p_operation, p_address, p_length);
        }

        // Perform normal serialization if the interruption status has been cleared
        if (p_operation.getStatus() == Operation.Status.NONE && p_length - totalBytes > 0) {
            totalBytes += PartialDeserializer.deserializeNormal(p_operation, p_address + totalBytes, p_length - totalBytes);
        }

        // Write interrupted field if normal serialization did not work
        if (p_operation.getStatus() == Operation.Status.INTERRUPTED && p_length - totalBytes > 0) {
            totalBytes += PartialDeserializer.deserializeInterrupted(p_operation, p_address + totalBytes, p_length - totalBytes);
        }

        return totalBytes;
    }


    public static void enableAutoRegistration() {
        SchemaRegistry.enableAutoRegistration();
    }

    public static void disableAutoRegistration() {
        SchemaRegistry.disableAutoRegistration();
    }

    public static <T> T newInstance(Class<T> p_class) {
        return ClassUtil.allocateInstance(p_class);
    }

    public static <T> T newInstance(String p_class) {
        return ClassUtil.allocateInstance(p_class);
    }

    public static <T> T newInstance(final short p_identifier) { return ClassUtil.allocateInstance(SchemaRegistry.resolveClass(p_identifier)); }

    public static short resolveIdentifier(final Class<?> p_class) { return SchemaRegistry.resolveIdentifier(p_class); }

    public static Schema register(Class<?> p_class) {
        return SchemaRegistry.register(p_class);
    }

    public static int sizeOf(Object p_object) {
        return SchemaRegistry.getSchema(p_object.getClass()).getSize(p_object);
    }

    public static long allocate(long p_size) {
        return UNSAFE.allocateMemory(p_size);
    }

    public static void free(long p_address) {
        UNSAFE.freeMemory(p_address);
    }

    public static Schema getSchema(Class<?> p_class) {
        return SchemaRegistry.getSchema(p_class);
    }

    public static <T> T newRandomInstance(final Class<T> p_class) {
        return ObjectGenerator.newInstance(p_class);
    }

    public static <T> T newRandomInstance(final Class<T> p_class, final Random p_random) {
        return ObjectGenerator.newInstance(p_class, p_random);
    }

    public static <T> T newRandomInstance(final Class<T> p_class, final ObjectGenerator.Config p_config) {
        return ObjectGenerator.newInstance(p_class, p_config);
    }

    public static <T> T newRandomInstance(final Class<T> p_class, final ObjectGenerator.Config p_config, final Random p_random) {
        return ObjectGenerator.newInstance(p_class, p_config, p_random);
    }
}

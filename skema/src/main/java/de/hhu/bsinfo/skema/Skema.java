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

    public static byte[] serialize(final Object instance) {
        Schema schema = SchemaRegistry.getSchema(instance.getClass());
        byte[] buffer = new byte[schema.getSize(instance)];
        serialize(instance, buffer);
        return buffer;
    }

    public static int serialize(final Object instance, final long address) {
        return FullSerializer.serialize(instance, address);
    }

    public static int serialize(final Object instance, final byte[] buffer) {
        return FullSerializer.serialize(instance, buffer, 0);
    }

    public static void deserialize(final Object instance, final byte[] buffer) {
        FullDeserializer.deserialize(instance, buffer, 0);
    }

    public static <T> T deserialize(final Class<T> clazz, final byte[] buffer) {
        return deserialize(clazz, buffer, 0);
    }

    public static <T> T deserialize(final Class<T> clazz, final long address) {
        if (clazz.isEnum()) {
            return FullDeserializer.deserializeEnum(clazz, address);
        }

        try {
            Object object = UNSAFE.allocateInstance(clazz);
            FullDeserializer.deserialize(object, address);
            return clazz.cast(object);
        } catch (InstantiationException e) {
            return null;
        }
    }

    public static <T> T deserialize(final Class<T> clazz, final byte[] buffer, final int offset) {
        if (clazz.isEnum()) {
            return FullDeserializer.deserializeEnum(clazz, buffer, offset);
        }

        try {
            Object object = UNSAFE.allocateInstance(clazz);
            FullDeserializer.deserialize(object, buffer, offset);
            return clazz.cast(object);
        } catch (InstantiationException e) {
            return null;
        }
    }

    public static int serialize(final Operation operation, final byte[] buffer, final int offset, final int length) {
        if (length == 0) {
            return 0;
        }

        // Try to serialize an interrupted field first
        int totalBytes = 0;
        if (operation.getStatus() == Operation.Status.INTERRUPTED) {
            totalBytes += PartialSerializer.serializeInterrupted(operation, buffer, offset, length);
        }

        // Perform normal serialization if the interruption status has been cleared
        if (operation.getStatus() == Operation.Status.NONE && length - totalBytes > 0) {
            totalBytes += PartialSerializer.serializeNormal(operation, buffer, offset + totalBytes, length - totalBytes);
        }

        // Write interrupted field if normal serialization did not work
        if (operation.getStatus() == Operation.Status.INTERRUPTED && length - totalBytes > 0) {
            totalBytes += PartialSerializer.serializeInterrupted(operation, buffer, offset + totalBytes, length - totalBytes);
        }

        return totalBytes;
    }

    public static int serialize(final Operation operation, final long address, final int length) {
        if (length == 0) {
            return 0;
        }

        // Try to serialize an interrupted field first
        int totalBytes = 0;
        if (operation.getStatus() == Operation.Status.INTERRUPTED) {
            totalBytes += PartialSerializer.serializeInterrupted(operation, address, length);
        }

        // Perform normal serialization if the interruption status has been cleared
        if (operation.getStatus() == Operation.Status.NONE && length - totalBytes > 0) {
            totalBytes += PartialSerializer.serializeNormal(operation, address + totalBytes, length - totalBytes);
        }

        // Write interrupted field if normal serialization did not work
        if (operation.getStatus() == Operation.Status.INTERRUPTED && length - totalBytes > 0) {
            totalBytes += PartialSerializer.serializeInterrupted(operation, address + totalBytes, length - totalBytes);
        }

        return totalBytes;
    }

    public static int deserialize(final Operation operation, final byte[] buffer, final int offset, final int length) {
        if (length == 0) {
            return 0;
        }

        // Try to serialize an interrupted field first
        int totalBytes = 0;
        if (operation.getStatus() == Operation.Status.INTERRUPTED) {
            totalBytes += PartialDeserializer.deserializeInterrupted(operation, buffer, offset, length);
        }

        // Perform normal serialization if the interruption status has been cleared
        if (operation.getStatus() == Operation.Status.NONE && length - totalBytes > 0) {
            totalBytes += PartialDeserializer.deserializeNormal(operation, buffer, offset + totalBytes, length - totalBytes);
        }

        // Write interrupted field if normal serialization did not work
        if (operation.getStatus() == Operation.Status.INTERRUPTED && length - totalBytes > 0) {
            totalBytes += PartialDeserializer.deserializeInterrupted(operation, buffer, offset + totalBytes, length - totalBytes);
        }

        return totalBytes;
    }

    public static int deserialize(final Operation operation, final long address, final int length) {
        if (length == 0) {
            return 0;
        }

        // Try to serialize an interrupted field first
        int totalBytes = 0;
        if (operation.getStatus() == Operation.Status.INTERRUPTED) {
            totalBytes += PartialDeserializer.deserializeInterrupted(operation, address, length);
        }

        // Perform normal serialization if the interruption status has been cleared
        if (operation.getStatus() == Operation.Status.NONE && length - totalBytes > 0) {
            totalBytes += PartialDeserializer.deserializeNormal(operation, address + totalBytes, length - totalBytes);
        }

        // Write interrupted field if normal serialization did not work
        if (operation.getStatus() == Operation.Status.INTERRUPTED && length - totalBytes > 0) {
            totalBytes += PartialDeserializer.deserializeInterrupted(operation, address + totalBytes, length - totalBytes);
        }

        return totalBytes;
    }


    public static void enableAutoRegistration() {
        SchemaRegistry.enableAutoRegistration();
    }

    public static void disableAutoRegistration() {
        SchemaRegistry.disableAutoRegistration();
    }

    public static <T> T newInstance(Class<T> clazz) {
        return ClassUtil.allocateInstance(clazz);
    }

    public static <T> T newInstance(String clazz) {
        return ClassUtil.allocateInstance(clazz);
    }

    public static <T> T newInstance(final short identifier) { return ClassUtil.allocateInstance(SchemaRegistry.resolveClass(identifier)); }

    public static short resolveIdentifier(final Class<?> clazz) { return SchemaRegistry.resolveIdentifier(clazz); }

    public static Schema register(Class<?> clazz) {
        return SchemaRegistry.register(clazz);
    }

    public static int sizeOf(Object instance) {
        return SchemaRegistry.getSchema(instance.getClass()).getSize(instance);
    }

    public static long allocate(long size) {
        return UNSAFE.allocateMemory(size);
    }

    public static void free(long address) {
        UNSAFE.freeMemory(address);
    }

    public static Schema getSchema(Class<?> clazz) {
        return SchemaRegistry.getSchema(clazz);
    }

    public static <T> T newRandomInstance(final Class<T> clazz) {
        return ObjectGenerator.newInstance(clazz);
    }

    public static <T> T newRandomInstance(final Class<T> clazz, final Random random) {
        return ObjectGenerator.newInstance(clazz, random);
    }

    public static <T> T newRandomInstance(final Class<T> clazz, final ObjectGenerator.Config config) {
        return ObjectGenerator.newInstance(clazz, config);
    }

    public static <T> T newRandomInstance(final Class<T> clazz, final ObjectGenerator.Config config, final Random random) {
        return ObjectGenerator.newInstance(clazz, config, random);
    }
}

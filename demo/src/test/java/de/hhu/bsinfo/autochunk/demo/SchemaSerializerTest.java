package de.hhu.bsinfo.autochunk.demo;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class SchemaSerializerTest {

    @BeforeClass
    public static void setup() {
        SchemaSerializer.register(Timestamp.class);
    }

    @Test
    public void testSerialize() throws InstantiationException {

        sun.misc.Unsafe unsafe = UnsafeProvider.getUnsafe();

        Timestamp first = new Timestamp(10, 42L, new int[]{1, 2, 3}, new long[]{0L, 20L, 40L});
        Timestamp second = new Timestamp(-1, -1, null, null);
        Timestamp third = (Timestamp) unsafe.allocateInstance(Timestamp.class);

        Schema schema = SchemaSerializer.getSchema(Timestamp.class);
        int bufferSize = schema.getSize(first);

        System.out.printf("buffersize is %d bytes\n", bufferSize);

        byte[] buffer = new byte[bufferSize];
        SchemaSerializer.serialize(first, buffer);
        SchemaSerializer.deserialize(second, buffer);
        SchemaSerializer.deserialize(third, buffer);

        System.out.printf("A : {id: %d, value: %d, ints: %s, longs: %s}\n",
                first.getId(), first.getValue(), Arrays.toString(first.getInts()), Arrays.toString(first.getLongs()));

        System.out.printf("B : {id: %d, value: %d, ints: %s, longs: %s}\n",
                second.getId(), second.getValue(), Arrays.toString(second.getInts()), Arrays.toString(second.getLongs()));

        System.out.printf("C : {id: %d, value: %d, ints: %s, longs: %s}\n",
                third.getId(), third.getValue(), Arrays.toString(third.getInts()), Arrays.toString(third.getLongs()));

        assertEquals(first, second);
        assertEquals(first, third);
        assertEquals(second, third);
    }

}
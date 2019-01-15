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
    public void testSerialize() {
        Timestamp first = new Timestamp(10, 42L, new int[]{1, 2, 3});
        Timestamp second = new Timestamp(20, 41L, null);

        Schema schema = SchemaSerializer.getSchema(Timestamp.class);
        int bufferSize = schema.getSize(first);

        System.out.printf("buffersize is %d bytes\n", bufferSize);

        byte[] buffer = new byte[bufferSize];
        SchemaSerializer.serialize(first, buffer);
        SchemaSerializer.deserialize(second, buffer);

        System.out.printf("A : {id: %d, value: %d, ints: %s}\n",
                first.getId(), first.getValue(), Arrays.toString(first.getInts()));

        System.out.printf("B : {id: %d, value: %d, ints: %s}\n",
                second.getId(), second.getValue(), Arrays.toString(second.getInts()));

        assertEquals(first, second);
    }

}
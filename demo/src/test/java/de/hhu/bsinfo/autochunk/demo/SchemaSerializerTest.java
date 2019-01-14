package de.hhu.bsinfo.autochunk.demo;

import java.nio.ByteBuffer;

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
        Timestamp first = new Timestamp(10, 10000L);
        Timestamp second = new Timestamp(5010, 123213L);

        int bufferSize = SchemaSerializer.getSize(Timestamp.class);

        ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);
        SchemaSerializer.serialize(first, buffer);

        buffer.position(0);

        SchemaSerializer.deserialize(second, buffer);

        assertEquals(first, second);
    }

}
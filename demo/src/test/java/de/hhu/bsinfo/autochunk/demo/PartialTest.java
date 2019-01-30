package de.hhu.bsinfo.autochunk.demo;

import org.junit.BeforeClass;
import org.junit.Test;

import de.hhu.bsinfo.autochunk.demo.data.Measurement;
import de.hhu.bsinfo.autochunk.demo.util.Operation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PartialTest {

    @BeforeClass
    public static void setup() {
        SchemaSerializer.register(Measurement.class);
    }

    @Test
    public void testSerialize() {
        Measurement object = new Measurement(42, 832.8, 0);

        byte[] expected = SchemaSerializer.serialize(object);
        byte[] actual = new byte[expected.length];

        Operation operation = new Operation(object);

        SchemaSerializer.serialize(operation, actual, 0, expected.length - 9);
        SchemaSerializer.serialize(operation, actual, expected.length - 9, 6);
        SchemaSerializer.serialize(operation, actual, expected.length - 3, 3);

        assertArrayEquals(expected, actual);
    }
}

package de.hhu.bsinfo.autochunk.demo;

import org.junit.BeforeClass;
import org.junit.Test;

import de.hhu.bsinfo.autochunk.demo.data.Measurement;
import de.hhu.bsinfo.autochunk.demo.data.Numbers;
import de.hhu.bsinfo.autochunk.demo.data.PrimitiveCollection;
import de.hhu.bsinfo.autochunk.demo.util.Operation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PartialTest {

    @BeforeClass
    public static void setup() {
        SchemaSerializer.register(PrimitiveCollection.class);
    }

    @Test
    public void testSerialize() {
        PrimitiveCollection object = new PrimitiveCollection();

        byte[] expected = SchemaSerializer.serialize(object);
        byte[] actual = new byte[expected.length];

        Operation operation = new Operation(object);

        SchemaSerializer.serialize(operation, actual, 0, expected.length - 51);
        SchemaSerializer.serialize(operation, actual, expected.length - 51, 36);
        SchemaSerializer.serialize(operation, actual, expected.length - 15, 5);
        SchemaSerializer.serialize(operation, actual, expected.length - 10, 7);
        SchemaSerializer.serialize(operation, actual, expected.length - 3, 3);

        assertArrayEquals(expected, actual);
    }
}

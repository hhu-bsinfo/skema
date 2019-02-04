package de.hhu.bsinfo.autochunk.demo;

import org.junit.BeforeClass;
import org.junit.Test;

import de.hhu.bsinfo.autochunk.demo.data.BoxedCollection;
import de.hhu.bsinfo.autochunk.demo.data.NestedObject;
import de.hhu.bsinfo.autochunk.demo.data.PrimitiveCollection;
import de.hhu.bsinfo.autochunk.demo.util.Operation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PartialTest {

    @BeforeClass
    public static void setup() {
        SchemaSerializer.register(PrimitiveCollection.class);
        SchemaSerializer.register(BoxedCollection.class);
        SchemaSerializer.register(NestedObject.class);
    }

    @Test
    public void testNonNestedSerialize() {
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

    @Test
    public void testNestedSerialize() {
        NestedObject object = new NestedObject();

        byte[] expected = SchemaSerializer.serialize(object);
        byte[] actual = new byte[expected.length];

        Operation operation = new Operation(object);

        SchemaSerializer.serialize(operation, actual, 0, expected.length - 51);
        assertPartialArrayEquals(expected, actual, expected.length - 51);
        SchemaSerializer.serialize(operation, actual, expected.length - 51, 36);
        assertPartialArrayEquals(expected, actual, expected.length - 15);
        SchemaSerializer.serialize(operation, actual, expected.length - 15, 5);
        assertPartialArrayEquals(expected, actual, expected.length - 10);
        SchemaSerializer.serialize(operation, actual, expected.length - 10, 7);
        assertPartialArrayEquals(expected, actual, expected.length - 3);
        SchemaSerializer.serialize(operation, actual, expected.length - 3, 3);
        assertPartialArrayEquals(expected, actual, expected.length);
    }

    @Test
    public void testNestedSerializeBytes() {
        NestedObject object = new NestedObject();

        byte[] expected = SchemaSerializer.serialize(object);
        byte[] actual = new byte[expected.length];

        Operation operation = new Operation(object);

        for (int i = 0; i < expected.length; i++) {
            SchemaSerializer.serialize(operation, actual, i, 1);
        }

        assertArrayEquals(expected, actual);
    }

    private static void assertPartialArrayEquals(final byte[] p_expected, final byte[] p_actual, final int p_length) {
        for (int i = 0; i < p_length; i++) {
            assertEquals(String.format("first differed at element %d", i), p_expected[i], p_actual[i]);
        }
    }
}

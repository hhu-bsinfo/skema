package de.hhu.bsinfo.skema;

import org.junit.BeforeClass;
import org.junit.Test;

import de.hhu.bsinfo.skema.data.BoxedCollection;
import de.hhu.bsinfo.skema.data.PrimitiveCollection;
import de.hhu.bsinfo.skema.schema.SchemaRegistry;
import de.hhu.bsinfo.skema.util.ClassUtil;
import de.hhu.bsinfo.skema.util.Operation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PartialTest {

    @BeforeClass
    public static void setup() {
        SchemaRegistry.enableAutoRegistration();
    }

    @Test
    public void testNonNestedSerialize() {
        PrimitiveCollection object = new PrimitiveCollection();

        byte[] expected = Skema.serialize(object);
        byte[] actual = new byte[expected.length];

        Operation operation = new Operation(object);

        for (int i = 0; i < expected.length; i++) {
            Skema.serialize(operation, actual, i, 1);
        }

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testNestedSerialize() {
        BoxedCollection object = new BoxedCollection();

        byte[] expected = Skema.serialize(object);
        byte[] actual = new byte[expected.length];

        Operation operation = new Operation(object);

        for (int i = 0; i < expected.length; i++) {
            Skema.serialize(operation, actual, i, 1);
        }

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testNonNestedDeserialize() {
        PrimitiveCollection expected = new PrimitiveCollection();
        PrimitiveCollection actual = ClassUtil.allocateInstance(PrimitiveCollection.class);

        byte[] bytes = Skema.serialize(expected);

        Operation operation = new Operation(actual);

        for (int i = 0; i < bytes.length; i++) {
            Skema.deserialize(operation, bytes, i, 1);
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testNestedDeserialize() {
        BoxedCollection expected = new BoxedCollection();
        BoxedCollection actual = ClassUtil.allocateInstance(BoxedCollection.class);

        byte[] bytes = Skema.serialize(expected);

        Operation operation = new Operation(actual);

        for (int i = 0; i < bytes.length; i++) {
            Skema.deserialize(operation, bytes, i, 1);
        }

        assertEquals(expected, actual);
    }
}

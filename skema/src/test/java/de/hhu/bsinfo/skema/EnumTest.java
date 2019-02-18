package de.hhu.bsinfo.skema;

import de.hhu.bsinfo.skema.data.PrimitiveCollection;
import de.hhu.bsinfo.skema.data.Result;
import de.hhu.bsinfo.skema.data.Status;
import de.hhu.bsinfo.skema.schema.SchemaRegistry;
import de.hhu.bsinfo.skema.util.ClassUtil;
import de.hhu.bsinfo.skema.util.Operation;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class EnumTest {

    @BeforeClass
    public static void setup() {
        SchemaRegistry.enableAutoRegistration();
    }

    @Test
    public void testSimple() {
        Status expected = Status.OK;
        byte[] bytes = Skema.serialize(expected);
        Status actual = Skema.deserialize(Status.class, bytes);
        assertSame(expected, actual);
    }

    @Test
    public void testNested() {
        Result expected = new Result(Status.OK);
        byte[] bytes = Skema.serialize(expected);
        Result actual = Skema.deserialize(Result.class, bytes);
        assertEquals(expected, actual);
    }

    @Test
    public void testPartialSerialize() {
        Result object = new Result(Status.OK);

        byte[] expected = Skema.serialize(object);
        byte[] actual = new byte[expected.length];

        Operation operation = new Operation(object);

        for (int i = 0; i < expected.length; i++) {
            Skema.serialize(operation, actual, i, 1);
        }

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testPartialDeserialize() {
        Result expected = new Result(Status.OK);
        Result actual = ClassUtil.allocateInstance(Result.class);

        byte[] bytes = Skema.serialize(expected);

        Operation operation = new Operation(actual);

        for (int i = 0; i < bytes.length; i++) {
            Skema.deserialize(operation, bytes, i, 1);
        }

        assertEquals(expected, actual);
    }
}

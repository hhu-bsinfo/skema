package de.hhu.bsinfo.skema;

import de.hhu.bsinfo.skema.data.Result;
import de.hhu.bsinfo.skema.data.Status;
import de.hhu.bsinfo.skema.scheme.SchemeRegistry;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class EnumTest {

    @BeforeClass
    public static void setup() {
        SchemeRegistry.register(Status.class);
        SchemeRegistry.register(Result.class);
    }

    @Test
    public void testSimple() {
        Status expected = Status.OK;
        byte[] bytes = SchemaSerializer.serialize(expected);
        Status actual = SchemaSerializer.deserialize(Status.class, bytes);
        assertSame(expected, actual);
    }

    @Test
    public void testNested() {
        Result expected = new Result(Status.OK);
        byte[] bytes = SchemaSerializer.serialize(expected);
        Result actual = SchemaSerializer.deserialize(Result.class, bytes);
        assertEquals(expected, actual);
    }
}

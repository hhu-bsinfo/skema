package de.hhu.bsinfo.autochunk.demo;

import de.hhu.bsinfo.autochunk.demo.data.Result;
import de.hhu.bsinfo.autochunk.demo.data.Status;
import de.hhu.bsinfo.autochunk.demo.data.TextMessage;
import de.hhu.bsinfo.autochunk.demo.util.UnsafeProvider;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class EnumTest {

    @BeforeClass
    public static void setup() {
        SchemaSerializer.register(Status.class);
        SchemaSerializer.register(Result.class);
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

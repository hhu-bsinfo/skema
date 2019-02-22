package de.hhu.bsinfo.skema;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.hhu.bsinfo.skema.data.BoxedCollection;
import de.hhu.bsinfo.skema.data.PrimitiveCollection;
import de.hhu.bsinfo.skema.data.Result;
import de.hhu.bsinfo.skema.data.Status;
import de.hhu.bsinfo.skema.data.TextMessage;
import de.hhu.bsinfo.skema.schema.SchemaRegistry;
import de.hhu.bsinfo.skema.util.Operation;
import de.hhu.bsinfo.skema.util.UnsafeProvider;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class SerializerTest {

    private static final Map<String, Object> INPUT = new HashMap<>();

    private static final long SEED = 17896217894623795L;

    @Parameterized.Parameters(name = "{0}")
    public static Iterable<Object> input() {
        return Arrays.asList("primitive", "boxed", "polymorphic", "enum");
    }

    @Parameterized.Parameter
    public Object m_key;

    private Object m_expected;

    @Before
    public void initializeTestObject() {
        m_expected = INPUT.get(m_key);
    }

    @BeforeClass
    public static void setup() {
        SchemaRegistry.enableAutoRegistration();
        Random random = new Random(SEED);
        INPUT.put("primitive", Skema.newRandomInstance(PrimitiveCollection.class, random));
        INPUT.put("boxed", Skema.newRandomInstance(BoxedCollection.class, random));
        INPUT.put("polymorphic", Skema.newRandomInstance(TextMessage.class, random));
        INPUT.put("enum", Skema.newRandomInstance(Result.class, random));
    }

    @Test
    public void testFullOnHeap() {
        byte[] bytes = Skema.serialize(m_expected);
        Object object = Skema.deserialize(m_expected.getClass(), bytes);
        assertEquals(m_expected, object);
    }

    @Test
    public void testFullOffHeap() {
        int size = Skema.sizeOf(m_expected);
        long address = Skema.allocate(size);
        Skema.serialize(m_expected, address);
        Object object = Skema.deserialize(m_expected.getClass(), address);
        Skema.free(address);
        assertEquals(m_expected, object);
    }

    @Test
    public void testPartialOnHeap() {
        int size = Skema.sizeOf(m_expected);
        byte[] buffer = new byte[size];

        Operation serializerOperation = new Operation(m_expected);
        for (int i = 0; i < buffer.length; i++) {
            Skema.serialize(serializerOperation, buffer, i, 1);
        }

        Object actual = Skema.newInstance(m_expected.getClass());
        Operation deserializerOperation = new Operation(actual);
        for (int i = 0; i < buffer.length; i++) {
            Skema.deserialize(deserializerOperation, buffer, i, 1);
        }

        assertEquals(m_expected, actual);
    }

    @Test
    public void testPartialOffHeap() {
        int size = Skema.sizeOf(m_expected);
        long address = Skema.allocate(size);

        Operation serializerOperation = new Operation(m_expected);
        for (int i = 0; i < size; i++) {
            Skema.serialize(serializerOperation, address + i, 1);
        }

        Object actual = Skema.newInstance(m_expected.getClass());
        Operation deserializerOperation = new Operation(actual);
        for (int i = 0; i < size; i++) {
            Skema.deserialize(deserializerOperation, address + i, 1);
        }

        Skema.free(address);

        assertEquals(m_expected, actual);
    }

}
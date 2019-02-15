package de.hhu.bsinfo.skema;

import org.junit.BeforeClass;
import org.junit.Test;

import de.hhu.bsinfo.skema.data.PrimitiveCollection;
import de.hhu.bsinfo.skema.scheme.Scheme;
import de.hhu.bsinfo.skema.scheme.SchemeRegistry;
import de.hhu.bsinfo.skema.util.UnsafeProvider;

import static org.junit.Assert.assertEquals;

public class OffHeapTest {

    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();

    @BeforeClass
    public static void setup() {
        SchemeRegistry.enableAutoRegistration();
    }

    @Test
    public void testSerialization() {

        Scheme scheme = SchemeRegistry.getSchema(PrimitiveCollection.class);

        PrimitiveCollection collection = new PrimitiveCollection();

        int size = scheme.getSize(collection);

        long address = UNSAFE.allocateMemory(size);

        int bytesWritten = SchemaSerializer.serialize(collection, address);

        assertEquals(size, bytesWritten);

        PrimitiveCollection result = SchemaSerializer.deserialize(PrimitiveCollection.class, address);

        assertEquals(collection, result);
    }


}

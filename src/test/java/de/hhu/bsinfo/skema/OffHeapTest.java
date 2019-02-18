package de.hhu.bsinfo.skema;

import org.junit.BeforeClass;
import org.junit.Test;

import de.hhu.bsinfo.skema.data.PrimitiveCollection;
import de.hhu.bsinfo.skema.schema.Schema;
import de.hhu.bsinfo.skema.schema.SchemaRegistry;
import de.hhu.bsinfo.skema.util.UnsafeProvider;

import static org.junit.Assert.assertEquals;

public class OffHeapTest {

    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();

    @BeforeClass
    public static void setup() {
        SchemaRegistry.enableAutoRegistration();
    }

    @Test
    public void testSerialization() {

        Schema schema = SchemaRegistry.getSchema(PrimitiveCollection.class);

        PrimitiveCollection collection = new PrimitiveCollection();

        int size = schema.getSize(collection);

        long address = UNSAFE.allocateMemory(size);

        int bytesWritten = Skema.serialize(collection, address);

        assertEquals(size, bytesWritten);

        PrimitiveCollection result = Skema.deserialize(PrimitiveCollection.class, address);

        assertEquals(collection, result);

        UNSAFE.freeMemory(address);
    }


}

package de.hhu.bsinfo.skema;

import org.junit.BeforeClass;
import org.junit.Test;

import de.hhu.bsinfo.skema.data.PrimitiveCollection;
import de.hhu.bsinfo.skema.scheme.SchemeRegistry;

import static org.junit.Assert.assertEquals;

public class OffsetTest {

    private static final int OFFSET = 27;

    @BeforeClass
    public static void setup() {
        SchemeRegistry.register(PrimitiveCollection.class);
    }

    @Test
    public void testOffset() {
        PrimitiveCollection collection = new PrimitiveCollection();
        byte[] bytes = SchemaSerializer.serialize(collection);
        byte[] bytesWithOffset = new byte[bytes.length + OFFSET];
        System.arraycopy(bytes, 0, bytesWithOffset, OFFSET, bytes.length);
        PrimitiveCollection result = SchemaSerializer.deserialize(PrimitiveCollection.class, bytesWithOffset, OFFSET);
        assertEquals(collection, result);
    }
}

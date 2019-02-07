package de.hhu.bsinfo.skema;

import org.junit.BeforeClass;
import org.junit.Test;

import de.hhu.bsinfo.skema.data.PrimitiveCollection;
import de.hhu.bsinfo.skema.scheme.SchemeRegistry;

import static org.junit.Assert.assertEquals;

public class PrimitiveTest {

    @BeforeClass
    public static void setup() {
        SchemeRegistry.register(PrimitiveCollection.class);
    }

    @Test
    public void testSerialize() {
        PrimitiveCollection collection = new PrimitiveCollection();
        byte[] bytes = SchemaSerializer.serialize(collection);
        PrimitiveCollection result = SchemaSerializer.deserialize(PrimitiveCollection.class, bytes);
        assertEquals(collection, result);
    }
}

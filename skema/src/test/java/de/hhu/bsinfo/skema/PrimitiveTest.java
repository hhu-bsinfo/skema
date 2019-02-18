package de.hhu.bsinfo.skema;

import org.junit.BeforeClass;
import org.junit.Test;

import de.hhu.bsinfo.skema.data.PrimitiveCollection;
import de.hhu.bsinfo.skema.schema.SchemaRegistry;

import static org.junit.Assert.assertEquals;

public class PrimitiveTest {

    @BeforeClass
    public static void setup() {
        SchemaRegistry.enableAutoRegistration();
    }

    @Test
    public void testSerialize() {
        PrimitiveCollection collection = new PrimitiveCollection();
        byte[] bytes = Skema.serialize(collection);
        PrimitiveCollection result = Skema.deserialize(PrimitiveCollection.class, bytes);
        assertEquals(collection, result);
    }
}

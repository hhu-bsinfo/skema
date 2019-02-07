package de.hhu.bsinfo.autochunk.demo;

import org.junit.BeforeClass;
import org.junit.Test;

import de.hhu.bsinfo.autochunk.demo.data.PrimitiveCollection;
import de.hhu.bsinfo.autochunk.demo.schema.SchemaRegistry;

import static org.junit.Assert.assertEquals;

public class PrimitiveTest {

    @BeforeClass
    public static void setup() {
        SchemaRegistry.register(PrimitiveCollection.class);
    }

    @Test
    public void testSerialize() {
        PrimitiveCollection collection = new PrimitiveCollection();
        byte[] bytes = SchemaSerializer.serialize(collection);
        PrimitiveCollection result = SchemaSerializer.deserialize(PrimitiveCollection.class, bytes);
        assertEquals(collection, result);
    }
}

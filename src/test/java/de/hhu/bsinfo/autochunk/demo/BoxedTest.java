package de.hhu.bsinfo.autochunk.demo;

import org.junit.BeforeClass;
import org.junit.Test;

import de.hhu.bsinfo.autochunk.demo.data.BoxedCollection;
import de.hhu.bsinfo.autochunk.demo.data.PrimitiveCollection;
import de.hhu.bsinfo.autochunk.demo.schema.SchemaRegistry;

import static org.junit.Assert.assertEquals;

public class BoxedTest {

    @BeforeClass
    public static void setup() {
        SchemaRegistry.register(BoxedCollection.class);
    }

    @Test
    public void testSerialize() {
        BoxedCollection collection = new BoxedCollection();
        byte[] bytes = SchemaSerializer.serialize(collection);
        BoxedCollection result = SchemaSerializer.deserialize(BoxedCollection.class, bytes);
        assertEquals(collection, result);
    }
}

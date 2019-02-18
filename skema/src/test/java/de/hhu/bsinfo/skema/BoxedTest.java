package de.hhu.bsinfo.skema;

import org.junit.BeforeClass;
import org.junit.Test;

import de.hhu.bsinfo.skema.data.BoxedCollection;
import de.hhu.bsinfo.skema.schema.SchemaRegistry;

import static org.junit.Assert.assertEquals;

public class BoxedTest {

    @BeforeClass
    public static void setup() {
        SchemaRegistry.enableAutoRegistration();
    }

    @Test
    public void testSerialize() {
        BoxedCollection collection = new BoxedCollection();
        byte[] bytes = Skema.serialize(collection);
        BoxedCollection result = Skema.deserialize(BoxedCollection.class, bytes);
        assertEquals(collection, result);
    }
}

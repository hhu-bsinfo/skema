package de.hhu.bsinfo.skema;

import org.junit.BeforeClass;
import org.junit.Test;

import de.hhu.bsinfo.skema.data.BoxedCollection;
import de.hhu.bsinfo.skema.scheme.SchemeRegistry;

import static org.junit.Assert.assertEquals;

public class BoxedTest {

    @BeforeClass
    public static void setup() {
        SchemeRegistry.enableAutoRegistration();
    }

    @Test
    public void testSerialize() {
        BoxedCollection collection = new BoxedCollection();
        byte[] bytes = SchemaSerializer.serialize(collection);
        BoxedCollection result = SchemaSerializer.deserialize(BoxedCollection.class, bytes);
        assertEquals(collection, result);
    }
}
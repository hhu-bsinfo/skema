package de.hhu.bsinfo.autochunk.demo;

import org.junit.BeforeClass;
import org.junit.Test;

import de.hhu.bsinfo.autochunk.demo.data.Measurement;
import de.hhu.bsinfo.autochunk.demo.data.Profile;
import de.hhu.bsinfo.autochunk.demo.data.Storyboard;
import de.hhu.bsinfo.autochunk.demo.data.Timestamp;
import de.hhu.bsinfo.autochunk.demo.schema.SchemaRegistry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConstantSizeTest {

    @BeforeClass
    public static void setup() {
        SchemaRegistry.register(Profile.class);
        SchemaRegistry.register(Storyboard.class);
        SchemaRegistry.register(Timestamp.class);
        SchemaRegistry.register(Measurement.class);
    }

    @Test
    public void testConstantSize() {
        assertTrue(SchemaRegistry.getSchema(Measurement.class).isConstant());
        assertTrue(SchemaRegistry.getSchema(Storyboard.class).isConstant());
    }

    @Test
    public void testDynamicSize() {
        assertFalse(SchemaRegistry.getSchema(Profile.class).isConstant());
        assertFalse(SchemaRegistry.getSchema(Timestamp.class).isConstant());
    }

}

package de.hhu.bsinfo.autochunk.demo;

import org.junit.BeforeClass;
import org.junit.Test;

import de.hhu.bsinfo.autochunk.demo.data.Measurement;
import de.hhu.bsinfo.autochunk.demo.data.Profile;
import de.hhu.bsinfo.autochunk.demo.data.Storyboard;
import de.hhu.bsinfo.autochunk.demo.data.Timestamp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConstantSizeTest {

    @BeforeClass
    public static void setup() {
        SchemaSerializer.register(Profile.class);
        SchemaSerializer.register(Storyboard.class);
        SchemaSerializer.register(Timestamp.class);
        SchemaSerializer.register(Measurement.class);
    }

    @Test
    public void testConstantSize() {
        assertTrue(SchemaSerializer.getSchema(Measurement.class).isConstant());
        assertTrue(SchemaSerializer.getSchema(Storyboard.class).isConstant());
    }

    @Test
    public void testDynamicSize() {
        assertFalse(SchemaSerializer.getSchema(Profile.class).isConstant());
        assertFalse(SchemaSerializer.getSchema(Timestamp.class).isConstant());
    }

}

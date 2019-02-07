package de.hhu.bsinfo.skema;

import org.junit.BeforeClass;
import org.junit.Test;

import de.hhu.bsinfo.skema.data.Measurement;
import de.hhu.bsinfo.skema.data.Profile;
import de.hhu.bsinfo.skema.data.Storyboard;
import de.hhu.bsinfo.skema.data.Timestamp;
import de.hhu.bsinfo.skema.scheme.SchemeRegistry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConstantSizeTest {

    @BeforeClass
    public static void setup() {
        SchemeRegistry.enableAutoRegistration();
    }

    @Test
    public void testConstantSize() {
        assertTrue(SchemeRegistry.getSchema(Measurement.class).isConstant());
        assertTrue(SchemeRegistry.getSchema(Storyboard.class).isConstant());
    }

    @Test
    public void testDynamicSize() {
        assertFalse(SchemeRegistry.getSchema(Profile.class).isConstant());
        assertFalse(SchemeRegistry.getSchema(Timestamp.class).isConstant());
    }

}

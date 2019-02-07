package de.hhu.bsinfo.skema;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.hhu.bsinfo.skema.data.Measurement;
import de.hhu.bsinfo.skema.data.Profile;
import de.hhu.bsinfo.skema.data.Storyboard;
import de.hhu.bsinfo.skema.data.Timestamp;
import de.hhu.bsinfo.skema.schema.SchemaRegistry;

import static org.junit.Assert.*;


@RunWith(Parameterized.class)
public class SerializerTest {

    @Parameterized.Parameters(name = "{0}")
    public static Iterable<Object> input() {
        return Arrays.asList(
                Profile.random(),
                Timestamp.random(),
                Measurement.random()
        );
    }

    @Parameterized.Parameter
    public Object m_object;

    @BeforeClass
    public static void setup() {
        SchemaRegistry.register(Profile.class);
        SchemaRegistry.register(Storyboard.class);
        SchemaRegistry.register(Timestamp.class);
        SchemaRegistry.register(Measurement.class);
    }

    @Test
    public void testSerialize() {
        byte[] bytes = SchemaSerializer.serialize(m_object);
        Object object = SchemaSerializer.deserialize(m_object.getClass(), bytes);
        assertEquals(m_object, object);
    }

}
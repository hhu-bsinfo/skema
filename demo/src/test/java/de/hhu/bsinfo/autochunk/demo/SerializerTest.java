package de.hhu.bsinfo.autochunk.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.hhu.bsinfo.autochunk.demo.data.Measurement;
import de.hhu.bsinfo.autochunk.demo.data.Profile;
import de.hhu.bsinfo.autochunk.demo.data.Storyboard;
import de.hhu.bsinfo.autochunk.demo.data.Timestamp;

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
        SchemaSerializer.register(Profile.class);
        SchemaSerializer.register(Storyboard.class);
        SchemaSerializer.register(Timestamp.class);
        SchemaSerializer.register(Measurement.class);
    }

    @Test
    public void testSerialize() {
        byte[] bytes = SchemaSerializer.serialize(m_object);
        Object object = SchemaSerializer.deserialize(m_object.getClass(), bytes);
        assertEquals(m_object, object);
    }

}
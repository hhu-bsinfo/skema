package de.hhu.bsinfo.skema.random;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hhu.bsinfo.skema.Skema;
import de.hhu.bsinfo.skema.data.BoxedCollection;
import de.hhu.bsinfo.skema.data.NestedObject;
import de.hhu.bsinfo.skema.data.PrimitiveCollection;
import de.hhu.bsinfo.skema.data.Status;

import static org.junit.Assert.*;

public class ObjectGeneratorTest {

    private static final long SEED = 42;

    @BeforeClass
    public static void setup() {
        Skema.enableAutoRegistration();
    }

    @Test
    public void testNewInstancePrimitive() {
        Random random = new Random(SEED);
        PrimitiveCollection first = ObjectGenerator.newInstance(PrimitiveCollection.class, random);

        random = new Random(SEED);
        PrimitiveCollection second = ObjectGenerator.newInstance(PrimitiveCollection.class, random);

        assertNotNull(first);
        assertEquals(first, second);
    }

    @Test
    public void testNewInstanceBoxed() {
        Random random = new Random(SEED);
        BoxedCollection first = ObjectGenerator.newInstance(BoxedCollection.class, random);

        random = new Random(SEED);
        BoxedCollection second = ObjectGenerator.newInstance(BoxedCollection.class, random);

        assertNotNull(first);
        assertEquals(first, second);
    }

    @Test
    public void testNewInstanceNested() {
        Random random = new Random(SEED);
        NestedObject first = ObjectGenerator.newInstance(NestedObject.class, random);

        random = new Random(SEED);
        NestedObject second = ObjectGenerator.newInstance(NestedObject.class, random);

        assertNotNull(first);
        assertEquals(first, second);
    }

    @Test
    public void testNewInstanceEnum() {
        Random random = new Random(SEED);
        Status first = ObjectGenerator.newInstance(Status.class, random);

        random = new Random(SEED);
        Status second = ObjectGenerator.newInstance(Status.class, random);

        assertNotNull(first);
        assertEquals(first, second);
    }
}
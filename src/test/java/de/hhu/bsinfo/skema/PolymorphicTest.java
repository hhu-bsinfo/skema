package de.hhu.bsinfo.skema;

import de.hhu.bsinfo.skema.data.TextMessage;
import de.hhu.bsinfo.skema.schema.SchemaRegistry;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PolymorphicTest {

    @BeforeClass
    public static void setup() {
        SchemaRegistry.register(TextMessage.class);
    }

    @Test
    public void testSerialize() {
        TextMessage message = new TextMessage((short) 0xABCD, (short) 0x1234, "TEST TEXT MESSAGE");
        byte[] bytes = SchemaSerializer.serialize(message);
        TextMessage result = SchemaSerializer.deserialize(TextMessage.class, bytes);
        assertEquals(message, result);
    }
}

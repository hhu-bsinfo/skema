package de.hhu.bsinfo.autochunk.demo;

import de.hhu.bsinfo.autochunk.demo.data.PrimitiveCollection;

public class Profiler {

    public static void main(String args[]) {
        SchemaSerializer.register(PrimitiveCollection.class);
        PrimitiveCollection collection = new PrimitiveCollection();
        int x = 0;
        for (int i = 0; i < 1000000000; i++) {
            byte[] bytes = SchemaSerializer.serialize(collection);
            PrimitiveCollection result = SchemaSerializer.deserialize(PrimitiveCollection.class, bytes);
            x += result.getInt();
        }
        System.out.println(x);
    }
}

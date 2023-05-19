package de.hhu.bsinfo.skema;

import de.hhu.bsinfo.skema.data.BoxedCollection;
import de.hhu.bsinfo.skema.data.Measurement;
import de.hhu.bsinfo.skema.schema.SchemaRegistry;

public class Profiler {

    public static void main(String args[]) {
        SchemaRegistry.register(BoxedCollection.class);

        var input = new BoxedCollection();
        int size = SchemaRegistry.getSchema(BoxedCollection.class).getSize(input);
        byte[] bytes = new byte[size];
        int x = 0;

        for (int i = 0; i < 1000000000; i++) {
            Skema.serialize(input, bytes);
            x += bytes.length;
        }

        System.out.println(x);
    }
}

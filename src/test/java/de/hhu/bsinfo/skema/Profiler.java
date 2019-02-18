package de.hhu.bsinfo.skema;

import de.hhu.bsinfo.skema.data.Measurement;
import de.hhu.bsinfo.skema.schema.SchemaRegistry;

public class Profiler {

    public static void main(String args[]) {
        SchemaRegistry.register(Measurement.class);

        Measurement input = new Measurement(42, 827.13, 0);
        Measurement output = new Measurement(0, 0.0, 0);
        int size = SchemaRegistry.getSchema(Measurement.class).getSize(input);
        byte[] bytes = new byte[size];
        int x = 0;

        for (int i = 0; i < 1000000000; i++) {
            Skema.serialize(input, bytes);
            Skema.deserialize(output, bytes);
            x += output.getId();
        }

        System.out.println(x);
    }
}

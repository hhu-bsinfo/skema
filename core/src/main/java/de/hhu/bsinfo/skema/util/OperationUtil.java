package de.hhu.bsinfo.skema.util;

import de.hhu.bsinfo.skema.schema.Schema;

public class OperationUtil {

    public static void saveArrayState(final Operation operation, final Object target, final int index, final int size) {
        operation.pushIndex(index + 1);
        operation.setTarget(target);
        operation.setFieldLeft(size);
        operation.setFieldProcessed(0);
        operation.setStatus(Operation.Status.INTERRUPTED);
    }

    public static void saveState(final Operation operation, final Schema.FieldSpec fieldSpec, final Object target, final int index, final int size) {
        operation.pushIndex(index + 1);
        operation.setFieldSpec(fieldSpec);
        operation.setTarget(target);
        operation.setFieldLeft(size);
        operation.setFieldProcessed(0);
        operation.setStatus(Operation.Status.INTERRUPTED);
    }
}

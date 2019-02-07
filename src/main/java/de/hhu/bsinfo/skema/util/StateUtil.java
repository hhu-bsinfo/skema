package de.hhu.bsinfo.skema.util;

import de.hhu.bsinfo.skema.scheme.Scheme;

public class StateUtil {

    public static void saveArrayState(final Operation p_operation, final Object p_target, final int p_index, final int p_size) {
        p_operation.pushIndex(p_index + 1);
        p_operation.setTarget(p_target);
        p_operation.setFieldLeft(p_size);
        p_operation.setFieldProcessed(0);
        p_operation.setStatus(Operation.Status.INTERRUPTED);
    }

    public static void saveState(final Operation p_operation, final Scheme.FieldSpec p_fieldSpec, final Object p_target, final int p_index, final int p_size) {
        p_operation.pushIndex(p_index + 1);
        p_operation.setFieldSpec(p_fieldSpec);
        p_operation.setTarget(p_target);
        p_operation.setFieldLeft(p_size);
        p_operation.setFieldProcessed(0);
        p_operation.setStatus(Operation.Status.INTERRUPTED);
    }
}
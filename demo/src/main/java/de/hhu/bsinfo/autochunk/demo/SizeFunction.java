package de.hhu.bsinfo.autochunk.demo;

@FunctionalInterface
interface SizeFunction {
    int sizeOf(final Object p_object, final Schema.FieldSpec p_fieldSpec);
}

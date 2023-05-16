package de.hhu.bsinfo.skema.util;

import de.hhu.bsinfo.skema.schema.Schema;

/**
 * A simple function calculating the size of an object's field using a provided field specification.
 */
@FunctionalInterface
interface SizeFunction {
    int sizeOf(final Object instance, final Schema.FieldSpec fieldSpec);
}

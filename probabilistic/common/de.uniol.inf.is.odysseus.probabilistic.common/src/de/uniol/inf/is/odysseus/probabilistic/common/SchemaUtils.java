/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.common;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

/**
 * Utility class for schema.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SchemaUtils {
    /** Data type used in transformation. */
    public static final String DATATYPE = "probabilistic";

    /**
     * Returns true if this schema contains a continuous probabilistic
     * attribute. More formally, returns true if and only if this schema
     * contains at least one attribute that is an
     * {@link SDFProbabilisticDatatype probabilistic attribute} that is
     * continuous
     * 
     * @param schema
     *            The {@link SDFSchema schema}
     * @return <code>true</code> if this schema contains a continuous
     *         {@link SDFProbabilisticDatatype probabilistic attribute}
     */
    public static boolean containsProbabilisticAttributes(final SDFSchema schema) {
        Objects.requireNonNull(schema);
        Objects.requireNonNull(schema.getAttributes());
        for (final SDFAttribute attribute : schema.getAttributes()) {
            if (SchemaUtils.isProbabilisticAttribute(attribute)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if this attribute is a continuous probabilistic attribute.
     * More formally, returns true if and only if this attribute is an
     * {@link SDFProbabilisticDatatype probabilistic attribute} that is
     * continuous
     * 
     * @param attribute
     *            The {@link SDFAttribute attribute}
     * @return <code>true</code> if this attribute is a continuous
     *         {@link SDFProbabilisticDatatype probabilistic attribute}
     */
    public static boolean isProbabilisticAttribute(final SDFAttribute attribute) {
        Objects.requireNonNull(attribute);
        if (attribute.getDatatype() instanceof SDFProbabilisticDatatype) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if this list contains a probabilistic attribute.
     * More formally, returns true if and only if this list contains at least
     * one attribute that is an {@link SDFProbabilisticDatatype probabilistic
     * attribute}
     * 
     * @param attributes
     *            The {@link Collection attributes}
     * @return <code>true</code> if this list contains a
     *         {@link SDFProbabilisticDatatype probabilistic attribute}
     */
    public static boolean containsProbabilisticAttributes(final Collection<SDFAttribute> attributes) {
        Objects.requireNonNull(attributes);
        for (final SDFAttribute attribute : attributes) {
            if (SchemaUtils.isProbabilisticAttribute(attribute)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the position indexes all continuous probabilistic attributes in
     * the schema.
     * 
     * @param schema
     *            The {@link SDFSchema schema}
     * @return An array of all attribute indexes in the schema that are
     *         continuous {@link SDFProbabilisticDatatype probabilistic
     *         attributes}
     */
    public static int[] getProbabilisticAttributePos(final SDFSchema schema) {
        Objects.requireNonNull(schema);
        final Collection<SDFAttribute> attributes = SchemaUtils.getProbabilisticAttributes(schema);
        final int[] pos = new int[attributes.size()];
        int i = 0;
        for (final SDFAttribute attribute : attributes) {
            pos[i++] = schema.indexOf(attribute);
        }
        return pos;
    }

    /**
     * Returns all attributes from the schema that are continuous probabilistic
     * attributes.
     * 
     * @param schema
     *            The {@link SDFSchema schema}
     * @return A list of all attributes in the schema that are continuous
     *         {@link SDFProbabilisticDatatype probabilistic attributes}
     */
    public static Collection<SDFAttribute> getProbabilisticAttributes(final SDFSchema schema) {
        Objects.requireNonNull(schema);
        Objects.requireNonNull(schema.getAttributes());
        final List<SDFAttribute> attributes = new LinkedList<SDFAttribute>();
        for (final SDFAttribute attribute : schema.getAttributes()) {
            if (SchemaUtils.isProbabilisticAttribute(attribute)) {
                attributes.add(attribute);
            }
        }
        return attributes;
    }

    /**
     * Returns all attributes from the list that are continuous probabilistic
     * attributes.
     * 
     * @param attributes
     *            The {@link Collection attributes}
     * @return A list of all attributes in the list that are continuous
     *         {@link SDFProbabilisticDatatype probabilistic attributes}
     */
    public static Collection<SDFAttribute> getProbabilisticAttributes(final Collection<SDFAttribute> attributes) {
        Objects.requireNonNull(attributes);
        final List<SDFAttribute> result = new LinkedList<SDFAttribute>();
        for (final SDFAttribute attribute : attributes) {
            if (SchemaUtils.isProbabilisticAttribute(attribute)) {
                result.add(attribute);
            }
        }
        return result;
    }

    /**
     * Returns the indexes of the given list of attributes in the given schema.
     * 
     * @param schema
     *            The schema
     * @param attributes
     *            The {@link Collection attributes}
     * @return An array with the indexes of the attributes in the schema
     */
    public static int[] getAttributePos(final SDFSchema schema, final Collection<SDFAttribute> attributes) {
        Objects.requireNonNull(schema);
        Objects.requireNonNull(attributes);
        final int[] pos = new int[attributes.size()];
        int i = 0;
        for (final SDFAttribute attribute : attributes) {
            Preconditions.checkArgument(schema.contains(attribute));
            pos[i] = schema.indexOf(attribute);
            i++;
        }
        return pos;
    }

    /**
     * Returns the index of the given attribute in the given schema.
     * 
     * @param schema
     *            The schema
     * @param attribute
     *            The attribute
     * @return The index of the attribute in the schema
     */
    public static int getAttributePos(final SDFSchema schema, final SDFAttribute attribute) {
        Objects.requireNonNull(schema);
        Objects.requireNonNull(attribute);
        return schema.indexOf(attribute);
    }

    /**
     * Utility constructor.
     */
    private SchemaUtils() {
        throw new UnsupportedOperationException();
    }
}

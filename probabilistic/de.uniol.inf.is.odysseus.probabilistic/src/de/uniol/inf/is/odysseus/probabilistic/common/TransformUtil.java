package de.uniol.inf.is.odysseus.probabilistic.common;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.Variable;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.mep.functions.AndOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.EqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * Utility class for transformation rules
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public final class TransformUtil {
	public static final String DATATYPE = "probabilistic";

	/**
	 * Returns true if this list contains a probabilistic attribute. More
	 * formally, returns true if and only if this list contains at least one
	 * attribute that is an {@link SDFProbabilisticDatatype probabilistic
	 * attribute}
	 * 
	 * @param attributes
	 *            The list of {@link SDFAttribute attributes}
	 * @return <code>true</code> if this list contains a
	 *         {@link SDFProbabilisticDatatype probabilistic attribute}
	 */
	public static boolean containsProbabilisticAttributes(
			final List<SDFAttribute> attributes) {
		boolean containsProbabilisticAttribute = false;
		for (final SDFAttribute attribute : attributes) {
			if (attribute.getDatatype() instanceof SDFProbabilisticDatatype) {
				containsProbabilisticAttribute = true;
			}
		}
		return containsProbabilisticAttribute;
	}

	/**
	 * Returns true if this list contains a continuous probabilistic attribute.
	 * More formally, returns true if and only if this list contains at least
	 * one attribute that is an {@link SDFProbabilisticDatatype probabilistic
	 * attribute} that is continuous
	 * 
	 * @param attributes
	 *            The list of {@link SDFAttribute attributes}
	 * @return <code>true</code> if this list contains a continuous
	 *         {@link SDFProbabilisticDatatype probabilistic attribute}
	 */
	public static boolean containsContinuousProbabilisticAttributes(
			final List<SDFAttribute> attributes) {
		for (final SDFAttribute attribute : attributes) {
			if (isContinuousProbabilisticAttribute(attribute)) {
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
	public static boolean isContinuousProbabilisticAttribute(
			SDFAttribute attribute) {
		if (attribute.getDatatype() instanceof SDFProbabilisticDatatype) {
			SDFProbabilisticDatatype datatype = (SDFProbabilisticDatatype) attribute
					.getDatatype();
			if (datatype.isContinuous()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if this list contains a discrete probabilistic attribute.
	 * More formally, returns true if and only if this list contains at least
	 * one attribute that is an {@link SDFProbabilisticDatatype probabilistic
	 * attribute} that is discrete
	 * 
	 * @param attributes
	 *            The list of {@link SDFAttribute attributes}
	 * @return <code>true</code> if this list contains a discrete
	 *         {@link SDFProbabilisticDatatype probabilistic attribute}
	 */
	public static boolean containsDiscreteProbabilisticAttributes(
			final List<SDFAttribute> attributes) {
		for (final SDFAttribute attribute : attributes) {
			if (isDiscreteProbabilisticAttribute(attribute)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if this attribute is a discrete probabilistic attribute.
	 * More formally, returns true if and only if this attribute is an
	 * {@link SDFProbabilisticDatatype probabilistic attribute} that is discrete
	 * 
	 * @param attribute
	 *            The {@link SDFAttribute attribute}
	 * @return <code>true</code> if this attribute is a discrete
	 *         {@link SDFProbabilisticDatatype probabilistic attribute}
	 */
	public static boolean isDiscreteProbabilisticAttribute(
			SDFAttribute attribute) {
		if (attribute.getDatatype() instanceof SDFProbabilisticDatatype) {
			SDFProbabilisticDatatype datatype = (SDFProbabilisticDatatype) attribute
					.getDatatype();
			if (datatype.isDiscrete()) {
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
	public static int[] getContinuousProbabilisticAttributePos(SDFSchema schema) {
		List<SDFAttribute> attributes = getContinuousProbabilisticAttributes(schema);
		int[] pos = new int[attributes.size()];
		for (int i = 0; i < attributes.size(); i++) {
			SDFAttribute attribute = attributes.get(i);
			pos[i] = schema.indexOf(attribute);
		}
		return pos;
	}

	/**
	 * Returns the position indexes all discrete probabilistic attributes in the
	 * schema.
	 * 
	 * @param schema
	 *            The {@link SDFSchema schema}
	 * @return An array of all attribute indexes in the schema that are discrete
	 *         {@link SDFProbabilisticDatatype probabilistic attributes}
	 */
	public static int[] getDiscreteProbabilisticAttributePos(SDFSchema schema) {
		List<SDFAttribute> attributes = getDiscreteProbabilisticAttributes(schema);
		int[] pos = new int[attributes.size()];
		for (int i = 0; i < attributes.size(); i++) {
			SDFAttribute attribute = attributes.get(i);
			pos[i] = schema.indexOf(attribute);
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
	public static List<SDFAttribute> getContinuousProbabilisticAttributes(
			SDFSchema schema) {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		for (SDFAttribute attribute : schema.getAttributes()) {
			if (isContinuousProbabilisticAttribute(attribute)) {
				attributes.add(attribute);
			}
		}
		return attributes;
	}

	/**
	 * Returns all attributes from the schema that are discrete probabilistic
	 * attributes.
	 * 
	 * @param schema
	 *            The {@link SDFSchema schema}
	 * @return A list of all attributes in the schema that are discrete
	 *         {@link SDFProbabilisticDatatype probabilistic attributes}
	 */

	public static List<SDFAttribute> getDiscreteProbabilisticAttributes(
			SDFSchema schema) {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		for (SDFAttribute attribute : schema.getAttributes()) {
			if (isDiscreteProbabilisticAttribute(attribute)) {
				attributes.add(attribute);
			}
		}
		return attributes;
	}

	/**
	 * Returns the indexes of the given list of attributes in the given schema.
	 * 
	 * @param schema
	 *            The schema
	 * @param attributes
	 *            The list of attributes
	 * @return An array with the idexes of the attributes in the schema
	 */
	public static int[] getAttributePos(SDFSchema schema,
			List<SDFAttribute> attributes) {
		int[] pos = new int[attributes.size()];
		for (int i = 0; i < attributes.size(); i++) {
			SDFAttribute attribute = attributes.get(i);
			pos[i] = schema.indexOf(attribute);
		}
		return pos;
	}

	/**
	 * Return true if the giiven predicate is of the form A.x=B.y AND A.y=B.z
	 * AND ...
	 * 
	 * @param expression
	 * @return
	 */
	public static boolean isEquiPredicate(IExpression<?> expression) {
		if (expression instanceof AndOperator) {
			return isEquiPredicate(((AndOperator) expression).getArgument(0))
					&& isEquiPredicate(((AndOperator) expression)
							.getArgument(1));

		}
		if (expression instanceof EqualsOperator) {
			EqualsOperator eq = (EqualsOperator) expression;
			IExpression<?> arg1 = eq.getArgument(0);
			IExpression<?> arg2 = eq.getArgument(1);
			if ((arg1 instanceof Variable) && (arg2 instanceof Variable)) {
				return true;
			}
		}
		return false;
	}

	private TransformUtil() {
	}
}

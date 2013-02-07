package de.uniol.inf.is.odysseus.probabilistic.transform;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
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
		boolean containsContinuousProbabilisticAttribute = false;
		for (final SDFAttribute attribute : attributes) {
			if (attribute.getDatatype() instanceof SDFProbabilisticDatatype) {
				SDFProbabilisticDatatype datatype = (SDFProbabilisticDatatype) attribute
						.getDatatype();
				if (datatype.isContinuous()) {
					containsContinuousProbabilisticAttribute = true;
				}
			}
		}
		return containsContinuousProbabilisticAttribute;
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
		boolean containsDiscreteProbabilisticAttribute = false;
		for (final SDFAttribute attribute : attributes) {
			if (attribute.getDatatype() instanceof SDFProbabilisticDatatype) {
				SDFProbabilisticDatatype datatype = (SDFProbabilisticDatatype) attribute
						.getDatatype();
				if (datatype.isDiscrete()) {
					containsDiscreteProbabilisticAttribute = true;
				}
			}
		}
		return containsDiscreteProbabilisticAttribute;
	}

	private TransformUtil() {
	}
}

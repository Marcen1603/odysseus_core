package de.uniol.inf.is.odysseus.probabilistic.sdf.schema;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class SDFProbabilisticDatatype extends SDFDatatype {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2867228296513432602L;

	public SDFProbabilisticDatatype(String URI) {
		super(URI);
	}

	public SDFProbabilisticDatatype(SDFDatatype sdfDatatype) {
		super(sdfDatatype);
	}

	public SDFProbabilisticDatatype(String datatypeName, KindOfDatatype type,
			SDFSchema schema) {
		super(datatypeName, type, schema);
	}

	public SDFProbabilisticDatatype(String datatypeName, KindOfDatatype type,
			SDFDatatype subType) {
		super(datatypeName, type, subType);
	}

	public static final SDFDatatype PROBABILISTIC_DOUBLE = new SDFProbabilisticDatatype(
			"ProbabilisticDouble");

	public static final SDFDatatype PROBABILISTIC_CONTINUOUS_DOUBLE = new SDFProbabilisticDatatype(
			"ProbabilisticContinuousDouble");

	public boolean isProbabilistic() {
		return this.getURI().equals(this.isContinuous() || this.isDiscrete());
	}

	public boolean isContinuous() {
		return this.getURI().equals(PROBABILISTIC_CONTINUOUS_DOUBLE.getURI());
	}

	public boolean isDiscrete() {
		return this.getURI().equals(PROBABILISTIC_DOUBLE.getURI());
	}

	@Override
	public boolean compatibleTo(SDFDatatype other) {
		if (other instanceof SDFProbabilisticDatatype) {
			SDFProbabilisticDatatype otherProbabilistic = (SDFProbabilisticDatatype) other;
			if (this.isDiscrete() && (otherProbabilistic.isDiscrete())
					|| this.isContinuous()
					&& (otherProbabilistic.isContinuous())) {
				return true;
			}
		}
		return super.compatibleTo(other);
	}
}

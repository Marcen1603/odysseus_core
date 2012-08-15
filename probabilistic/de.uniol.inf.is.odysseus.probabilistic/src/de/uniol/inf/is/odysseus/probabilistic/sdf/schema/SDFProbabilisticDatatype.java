package de.uniol.inf.is.odysseus.probabilistic.sdf.schema;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class SDFProbabilisticDatatype extends SDFDatatype {
	public SDFProbabilisticDatatype(String URI) {
		super(URI);
	}

	public SDFProbabilisticDatatype(SDFDatatype sdfDatatype) {
		super(sdfDatatype);
		// TODO Auto-generated constructor stub
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
		return this.getURI().equals(this.isContinuous() || this.isDiskret());
	}

	public boolean isContinuous() {
		return this.getURI().equals(PROBABILISTIC_CONTINUOUS_DOUBLE.getURI());
	}

	public boolean isDiskret() {
		return this.getURI().equals(PROBABILISTIC_DOUBLE.getURI());
	}

	@Override
	public boolean compatibleTo(SDFDatatype other) {
		if (other instanceof SDFProbabilisticDatatype) {
			SDFProbabilisticDatatype otherProbabilistic = (SDFProbabilisticDatatype) other;
			if (this.isDiskret() && (otherProbabilistic.isDiskret())
					|| this.isContinuous()
					&& (otherProbabilistic.isContinuous())) {
				return true;
			}
		}
		return super.compatibleTo(other);
	}
}

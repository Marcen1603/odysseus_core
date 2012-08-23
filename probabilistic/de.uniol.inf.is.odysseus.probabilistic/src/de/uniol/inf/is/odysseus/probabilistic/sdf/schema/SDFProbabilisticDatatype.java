package de.uniol.inf.is.odysseus.probabilistic.sdf.schema;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class SDFProbabilisticDatatype extends SDFDatatype {
    /**
	 * 
	 */
    private static final long serialVersionUID = 2867228296513432602L;

    public SDFProbabilisticDatatype(final String URI) {
        super(URI);
    }

    public SDFProbabilisticDatatype(final SDFDatatype sdfDatatype) {
        super(sdfDatatype);
    }

    public SDFProbabilisticDatatype(final String datatypeName, final KindOfDatatype type, final SDFSchema schema) {
        super(datatypeName, type, schema);
    }

    public SDFProbabilisticDatatype(final String datatypeName, final KindOfDatatype type, final SDFDatatype subType) {
        super(datatypeName, type, subType);
    }

    public static final SDFDatatype PROBABILISTIC_DOUBLE                         = new SDFProbabilisticDatatype(
                                                                                         "ProbabilisticDouble");

    public static final SDFDatatype PROBABILISTIC_CONTINUOUS_DOUBLE              = new SDFProbabilisticDatatype(
                                                                                         "ProbabilisticContinuousDouble");

    public static final SDFDatatype PROBABILISTIC_MULTIVARIANT_CONTINUOUS_DOUBLE = new SDFProbabilisticDatatype(
                                                                                         "ProbabilisticMultivariantContinuousDouble",
                                                                                         SDFDatatype.KindOfDatatype.MULTI_VALUE,
                                                                                         SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE);

    public boolean isProbabilistic() {
        return this.getURI().equals(this.isContinuous() || this.isDiscrete());
    }

    public boolean isContinuous() {
        return this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE.getURI())
                || this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_MULTIVARIANT_CONTINUOUS_DOUBLE.getURI());
    }

    public boolean isDiscrete() {
        return this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE.getURI());
    }

    @Override
    public boolean compatibleTo(final SDFDatatype other) {
        if (other instanceof SDFProbabilisticDatatype) {
            final SDFProbabilisticDatatype otherProbabilistic = (SDFProbabilisticDatatype) other;
            if ((this.isDiscrete() && (otherProbabilistic.isDiscrete()))
                    || (this.isContinuous() && (otherProbabilistic.isContinuous()))) {
                return true;
            }
        }
        return super.compatibleTo(other);
    }
}

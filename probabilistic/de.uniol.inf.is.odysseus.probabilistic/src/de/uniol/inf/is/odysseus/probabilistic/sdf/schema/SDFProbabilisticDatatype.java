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

    public static final SDFDatatype COVARIANCE_MATRIX                            = new SDFProbabilisticDatatype(
                                                                                         "CovarianceMatrix");
    public static final SDFDatatype MULTIVARIATE_COVARIANCE_MATRIX               = new SDFProbabilisticDatatype(
                                                                                         "MultivariateCovarianceMatrix");

    public static final SDFDatatype PROBABILISTIC_DOUBLE                         = new SDFProbabilisticDatatype(
                                                                                         "ProbabilisticDouble");

    public static final SDFDatatype PROBABILISTIC_CONTINUOUS_DOUBLE              = new SDFProbabilisticDatatype(
                                                                                         "ProbabilisticContinuousDouble");

    public static final SDFDatatype PROBABILISTIC_MULTIVARIATE_CONTINUOUS_DOUBLE = new SDFProbabilisticDatatype(
                                                                                         "ProbabilisticMultivariateContinuousDouble",
                                                                                         SDFDatatype.KindOfDatatype.MULTI_VALUE,
                                                                                         SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE);

    public boolean isProbabilistic() {
        return this.isContinuous() || this.isDiscrete();
    }

    public boolean isContinuous() {
        return this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE.getURI())
                || this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_MULTIVARIATE_CONTINUOUS_DOUBLE.getURI());
    }

    public boolean isDiscrete() {
        return this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE.getURI());
    }

    public boolean isCovarianceMatrix() {
        return this.getURI().equals(SDFProbabilisticDatatype.COVARIANCE_MATRIX);
    }

    public boolean isMultivariateCovarianceMatrix() {
        return this.getURI().equals(SDFProbabilisticDatatype.MULTIVARIATE_COVARIANCE_MATRIX);
    }

    @Override
    public boolean compatibleTo(final SDFDatatype other) {
        if (other instanceof SDFProbabilisticDatatype) {
            final SDFProbabilisticDatatype otherProbabilistic = (SDFProbabilisticDatatype) other;
            if ((this.isDiscrete() && (otherProbabilistic.isDiscrete()))
                    || (this.isContinuous() && (otherProbabilistic.isContinuous()))
                    || (this.isCovarianceMatrix() && (otherProbabilistic.isCovarianceMatrix()))
                    || (this.isMultivariateCovarianceMatrix() && (otherProbabilistic.isMultivariateCovarianceMatrix()))) {
                return true;
            }
        }
        return super.compatibleTo(other);
    }
}

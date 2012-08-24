package de.uniol.inf.is.odysseus.probabilistic.metadata;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.probabilistic.datatype.CovarianceMatrix;
import de.uniol.inf.is.odysseus.probabilistic.datatype.MultivariateCovarianceMatrix;

public class IntervalProbabilistic extends TimeInterval implements IProbabilistic {

    /**
	 * 
	 */
    private static final long    serialVersionUID = -9030157268224460919L;
    private final IProbabilistic probabilistic;

    public IntervalProbabilistic() {
        super();
        this.probabilistic = new Probabilistic();
    }

    public IntervalProbabilistic(final IntervalProbabilistic intervalProbabilistic) {
        super(intervalProbabilistic);
        this.probabilistic = intervalProbabilistic.probabilistic.clone();
    }

    @Override
    public IntervalProbabilistic clone() {
        return new IntervalProbabilistic(this);
    }

    @Override
    public String toString() {
        return "( i= " + super.toString() + " | " + " p=" + this.probabilistic + ")";
    }

    @Override
    public String csvToString() {
        return super.csvToString() + ";" + this.probabilistic.csvToString();
    }

    @Override
    public String getCSVHeader() {
        return super.getCSVHeader() + ";" + this.probabilistic.getCSVHeader();
    }

    @Override
    public double getExistence() {
        return this.probabilistic.getExistence();
    }

    @Override
    public void setExistence(final double existence) {
        this.probabilistic.setExistence(existence);
    }

    @Override
    public MultivariateCovarianceMatrix getCovarianceMatrices() {
        return this.probabilistic.getCovarianceMatrices();
    }

    @Override
    public CovarianceMatrix getCovarianceMatrix(final byte id) {
        return this.probabilistic.getCovarianceMatrix(id);
    }

    @Override
    public void setCovarianceMatrices(final MultivariateCovarianceMatrix covarianceMatrices) {
        this.probabilistic.setCovarianceMatrices(covarianceMatrices);
    }
}

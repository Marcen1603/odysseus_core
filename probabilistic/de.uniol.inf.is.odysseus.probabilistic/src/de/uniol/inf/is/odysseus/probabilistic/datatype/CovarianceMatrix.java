package de.uniol.inf.is.odysseus.probabilistic.datatype;

public class CovarianceMatrix {
    private final double[] entries;

    public CovarianceMatrix(final double[] entries) {
        this.entries = entries;
    }

    public double[] getEntries() {
        return this.entries;
    }

    public int size() {
        return (int) (-0.5 + Math.sqrt(0.25 + (this.entries.length * 2)));
    }
}

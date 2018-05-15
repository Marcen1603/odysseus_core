/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.common.base.distribution;

import java.util.Arrays;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class Sample {

    private final double[] singleton;
    private final Double probability;

    /**
     * @param singleton
     * @param probability
     */
    public Sample(double[] singleton, Double probability) {
        this.singleton = singleton;
        this.probability = probability;
    }

    /**
     * @return
     */
    public double[] getKey() {
        return singleton;
    }

    /**
     * @return
     */
    public Double getValue() {
        return probability;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.probability == null) ? 0 : this.probability.hashCode());
        result = prime * result + Arrays.hashCode(this.singleton);
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Sample other = (Sample) obj;
        if (this.probability == null) {
            if (other.probability != null)
                return false;
        } else if (!this.probability.equals(other.probability))
            return false;
        if (!Arrays.equals(this.singleton, other.singleton))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return Arrays.toString(this.singleton) + ": " + this.probability;
    }

}

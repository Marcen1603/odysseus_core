/**
 * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.probabilistic.common.datatype;

import java.io.Serializable;
import java.util.Objects;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.IClone;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class ProbabilisticDouble implements Serializable, IClone {
    /**
	 * 
	 */
    private static final long serialVersionUID = 537104992550497486L;
    /** The index of the associated distribution. */
    private int distribution;

    /**
     * Default constructor.
     * 
     * @param distribution
     *            The index of the associated distribution
     */
    public ProbabilisticDouble(final int distribution) {
        Preconditions.checkArgument(distribution >= 0);
        this.distribution = distribution;
    }

    /**
     * Clone constructor.
     * 
     * @param copy
     *            The object to clone from
     */
    public ProbabilisticDouble(final ProbabilisticDouble copy) {
        Objects.requireNonNull(copy);
        this.distribution = copy.distribution;
    }

    /**
     * Gets the value of the distribution property.
     * 
     * @return the distribution
     */
    public final int getDistribution() {
        return this.distribution;
    }

    /**
     * Sets the value of the distribution property.
     * 
     * @param distribution
     *            the distribution to set
     */
    public final void setDistribution(final int distribution) {
        Preconditions.checkArgument(distribution >= 0);
        this.distribution = distribution;
    }

    /*
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(this.distribution);
        sb.append(")");
        return sb.toString();
    }

    /*
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public final ProbabilisticDouble clone() {
        return new ProbabilisticDouble(this);
    }

    /*
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + this.distribution;
        return result;
    }

    /*
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if (this.getClass() == obj.getClass()) {
            final ProbabilisticDouble other = (ProbabilisticDouble) obj;
            return this.distribution == other.distribution;
        }
        else {
            if (obj.getClass() == Double.class) {
                return true;
            }
        }
        return false;
    }

}

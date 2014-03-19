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
package de.uniol.inf.is.odysseus.probabilistic.metadata;

import java.text.NumberFormat;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticTimeInterval extends TimeInterval implements IProbabilisticTimeInterval {

    /**
	 * 
	 */
    private static final long serialVersionUID = -9030157268224460919L;
    /** The classes. */
    @SuppressWarnings("unchecked")
    public static final Class<? extends IMetaAttribute>[] CLASSES = new Class[] { ITimeInterval.class, IProbabilistic.class };
    /** The tuple probability. */
    private final IProbabilistic probabilistic;

    /**
     * Default constructor.
     */
    public ProbabilisticTimeInterval() {
        super();
        this.probabilistic = new Probabilistic();
    }

    /**
     * Clone constructor.
     * 
     * @param copy
     *            The object to copy from
     */
    public ProbabilisticTimeInterval(final ProbabilisticTimeInterval copy) {
        super(copy);
        this.probabilistic = copy.probabilistic.clone();
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public ProbabilisticTimeInterval clone() {
        return new ProbabilisticTimeInterval(this);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public double getExistence() {
        return this.probabilistic.getExistence();
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void setExistence(final double existence) {
        Preconditions.checkArgument((existence >= 0.0) && (existence <= 1.0));
        this.probabilistic.setExistence(existence);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "( i= " + super.toString() + " | p=" + this.probabilistic + ")";
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public String csvToString(final char delimiter, final Character textSeperator, final NumberFormat floatingFormatter, final NumberFormat numberFormatter, final boolean withMetadata) {
        return super.csvToString(delimiter, textSeperator, floatingFormatter, numberFormatter, withMetadata) + delimiter
                + this.probabilistic.csvToString(delimiter, textSeperator, floatingFormatter, numberFormatter, withMetadata);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public String getCSVHeader(final char delimiter) {
        return super.getCSVHeader(delimiter) + delimiter + this.probabilistic.getCSVHeader(delimiter);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Class<? extends IMetaAttribute>[] getClasses() {
        return ProbabilisticTimeInterval.CLASSES;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "ProbabilisticTimeInterval";
    }
}

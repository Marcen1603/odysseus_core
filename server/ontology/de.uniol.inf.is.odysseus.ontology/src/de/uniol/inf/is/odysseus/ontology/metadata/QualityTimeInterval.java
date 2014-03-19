/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.ontology.metadata;

import java.text.NumberFormat;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class QualityTimeInterval extends TimeInterval implements IQuality, IQualityTimeInterval {

    @SuppressWarnings("unchecked")
    public final static Class<? extends IMetaAttribute>[] CLASSES = new Class[] { ITimeInterval.class, IQuality.class };

    private static final long serialVersionUID = -3129934770814427153L;
    private final IQuality quality;
    private double frequency;

    public QualityTimeInterval() {
        super();
        this.quality = new Quality();
        this.frequency = 1.0;
    }

    public QualityTimeInterval(final QualityTimeInterval copy) {
        super(copy);
        this.quality = copy.quality.clone();
        this.frequency = copy.frequency;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getTimeliness() {
        final PointInTime endTime = PointInTime.currentPointInTime();
        return Math.max(0.0, 1.0 - ((endTime.minus(this.getStart()).getMainPoint()) / (this.frequency * 1000.0)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFrequency(final double frequency) {
        Preconditions.checkArgument(frequency > 0.0);
        this.frequency = frequency;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getFrequency() {
        return this.frequency;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCompleteness(final double completeness) {
        this.quality.setCompleteness(completeness);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getCompleteness() {
        return this.quality.getCompleteness();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConsistency(final double consistency) {
        this.quality.setConsistency(consistency);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getConsistency() {
        return this.quality.getConsistency();
    }

    @Override
    public QualityTimeInterval clone() {
        return new QualityTimeInterval(this);
    }

    @Override
    public String toString() {
        return "( i= " + super.toString() + " | q=" + this.quality.toString().substring(0, this.quality.toString().length() - 1) + ", timeliness=" + this.getTimeliness() + "])";
    }

    @Override
    public String csvToString(final char delimiter, final Character textSeperator, final NumberFormat floatingFormatter, final NumberFormat numberFormatter, final boolean withMetadata) {
        final StringBuffer retBuffer = new StringBuffer(super.csvToString(delimiter, textSeperator, floatingFormatter, numberFormatter, withMetadata) + delimiter
                + this.quality.csvToString(delimiter, textSeperator, floatingFormatter, numberFormatter, withMetadata));
        if (numberFormatter != null) {
            retBuffer.append(delimiter).append(numberFormatter.format(this.getTimeliness()));
        }
        else {
            retBuffer.append(delimiter).append(this.getTimeliness());
        }
        return retBuffer.toString();
    }

    @Override
    public String getCSVHeader(final char delimiter) {
        return super.getCSVHeader(delimiter) + "+delimiter+" + this.quality.getCSVHeader(delimiter) + "+delimiter+" + this.getTimeliness();
    }

    @Override
    public Class<? extends IMetaAttribute>[] getClasses() {
        return QualityTimeInterval.CLASSES;
    }

    @Override
    public String getName() {
        return "QualityTimeInterval";
    }

}

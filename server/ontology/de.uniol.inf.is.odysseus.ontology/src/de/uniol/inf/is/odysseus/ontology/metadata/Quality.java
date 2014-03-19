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

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class Quality implements IQuality {
    /**
     * 
     */
    private static final long serialVersionUID = 1333608827870132800L;

    @SuppressWarnings("unchecked")
    public final static Class<? extends IMetaAttribute>[] CLASSES = new Class[] { IQuality.class };

    private double completeness;
    private double consistency;

    /**
 * 
 */
    public Quality() {
        this.completeness = 1.0;
        this.consistency = 1.0;
    }

    /**
     * @param copy
     */
    private Quality(final Quality copy) {
        this.completeness = copy.completeness;
        this.consistency = copy.consistency;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getCompleteness() {
        return this.completeness;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getConsistency() {
        return this.consistency;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCompleteness(final double completeness) {
        Preconditions.checkArgument((completeness >= 0.0) && (completeness <= 1.0));
        this.completeness = completeness;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConsistency(final double consistency) {
        Preconditions.checkArgument((consistency >= 0.0) && (consistency <= 1.0));
        this.consistency = consistency;
    }

    @Override
    public IQuality clone() {
        return new Quality(this);
    }

    @Override
    public String toString() {
        return "[completeness=" + this.completeness + ", consistency=" + this.consistency + "]";
    }

    @Override
    public String csvToString(final char delimiter, final Character textSeperator, final NumberFormat floatingFormatter, final NumberFormat numberFormatter, final boolean withMetadata) {
        final StringBuffer retBuffer = new StringBuffer();
        if (numberFormatter != null) {
            retBuffer.append(numberFormatter.format(this.completeness)).append(delimiter).append(numberFormatter.format(this.consistency));
        }
        else {
            retBuffer.append(this.completeness).append(delimiter).append(this.consistency);
        }
        return retBuffer.toString();
    }

    @Override
    public String getCSVHeader(final char delimiter) {
        return "timeliness" + delimiter + "completeness" + delimiter + "consistency";
    }

    @Override
    public Class<? extends IMetaAttribute>[] getClasses() {
        return Quality.CLASSES;
    }

    @Override
    public String getName() {
        return "Quality";
    }
}

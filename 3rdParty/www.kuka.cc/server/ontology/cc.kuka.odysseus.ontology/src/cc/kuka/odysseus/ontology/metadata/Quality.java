/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.ontology.metadata;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractBaseMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class Quality extends AbstractBaseMetaAttribute implements IQuality {
    /**
     *
     */
    private static final long serialVersionUID = 1333608827870132800L;

    @SuppressWarnings("unchecked")
    public final static Class<? extends IMetaAttribute>[] CLASSES = new Class[] { IQuality.class };
    public static final List<SDFMetaSchema> SCHEMA = new ArrayList<>(Quality.CLASSES.length);
    static {
        final List<SDFAttribute> attributes = new ArrayList<>();
        attributes.add(new SDFAttribute("Quality", "completeness", SDFDatatype.DOUBLE));
        attributes.add(new SDFAttribute("Quality", "consistency", SDFDatatype.DOUBLE));
        attributes.add(new SDFAttribute("Quality", "frequency", SDFDatatype.DOUBLE));
        Quality.SCHEMA.add(SDFSchemaFactory.createNewMetaSchema("Quality", Tuple.class, attributes, IQuality.class));
    }

    private double completeness;
    private double consistency;
    private double frequency;

    /**
     *
     */
    public Quality() {
        this.completeness = 1.0;
        this.consistency = 1.0;
        this.frequency = 1.0;
    }

    /**
     * @param copy
     */
    private Quality(final Quality copy) {
        this.completeness = copy.completeness;
        this.consistency = copy.consistency;
        this.frequency = copy.frequency;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void retrieveValues(final List<Tuple<?>> values) {
        @SuppressWarnings("rawtypes")
        final Tuple t = new Tuple(3, false);
        t.setAttribute(0, new Double(this.completeness));
        t.setAttribute(1, new Double(this.consistency));
        t.setAttribute(2, new Double(this.frequency));
        values.add(t);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void writeValue(Tuple<?> value) {
        completeness = value.getAttribute(0);
        consistency = value.getAttribute(1);
        frequency = value.getAttribute(2);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <K> K getValue(final int subtype, final int index) {
        switch (index) {
            case 0:
                return (K) new Double(this.completeness);
            case 1:
                return (K) new Double(this.consistency);
            case 2:
                return (K) new Double(this.frequency);
            default:
                break;
        }
        return null;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public List<SDFMetaSchema> getSchema() {
        return Quality.SCHEMA;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    protected IInlineMetadataMergeFunction<? extends IMetaAttribute> getInlineMergeFunction() {
        return new QualityMergeFunction();
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
    public double getFrequency() {
        return this.frequency;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFrequency(final double frequency) {
        Preconditions.checkArgument(frequency > 0.0);
        this.frequency = frequency;
    }

    @Override
    public IQuality clone() {
        return new Quality(this);
    }

    @Override
    public String toString() {
        return "[completeness=" + this.completeness + ", consistency=" + this.consistency + ", frequency=" + this.frequency + "]";
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

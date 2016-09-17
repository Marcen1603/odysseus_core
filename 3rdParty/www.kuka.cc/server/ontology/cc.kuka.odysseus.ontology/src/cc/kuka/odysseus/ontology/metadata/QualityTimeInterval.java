/*******************************************************************************
 * Copyright (C) 2015  Christian Kuka <christian@kuka.cc>
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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractCombinedMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class QualityTimeInterval extends AbstractCombinedMetaAttribute implements IQuality, ITimeInterval {

    @SuppressWarnings("unchecked")
    public final static Class<? extends IMetaAttribute>[] CLASSES = new Class[] { ITimeInterval.class, IQuality.class };
    public static final List<SDFMetaSchema> SCHEMA = new ArrayList<>(QualityTimeInterval.CLASSES.length);
    static {
        QualityTimeInterval.SCHEMA.addAll(TimeInterval.schema);
        QualityTimeInterval.SCHEMA.addAll(Quality.SCHEMA);
    }
    private static final long serialVersionUID = -3129934770814427153L;
    private final ITimeInterval timeInterval;
    private final IQuality quality;

    public QualityTimeInterval() {
        super();
        this.timeInterval = new TimeInterval();
        this.quality = new Quality();
    }

    public QualityTimeInterval(final QualityTimeInterval copy) {
        super();
        this.timeInterval = (ITimeInterval) copy.timeInterval.clone();
        this.quality = (IQuality) copy.quality.clone();
    }

    // ------------------------------------------------------------------------------
    // Methods that need to merge different types
    // ------------------------------------------------------------------------------

    @Override
    public void retrieveValues(final List<Tuple<?>> values) {
        this.timeInterval.retrieveValues(values);
        @SuppressWarnings("rawtypes")
        final Tuple t = new Tuple(4, false);
        t.setAttribute(0, new Double(this.quality.getCompleteness()));
        t.setAttribute(1, new Double(this.quality.getConsistency()));
        t.setAttribute(2, new Double(this.quality.getFrequency()));
        t.setAttribute(3, new Double(this.getTimeliness()));
        values.add(t);
    }

    @Override
    public void writeValues(List<Tuple<?>> values) {
        this.timeInterval.writeValue(values.get(0));
        this.quality.writeValue(values.get(1));
    }

    @Override
    public List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> getInlineMergeFunctions() {
        List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> list = new ArrayList<>();
        list.addAll(timeInterval.getInlineMergeFunctions());
        list.addAll(quality.getInlineMergeFunctions());
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <K> K getValue(final int subtype, final int index) {
        switch (subtype) {
            case 0:
                return this.timeInterval.getValue(0, index);
            case 1:
                if (index < 3) {
                    return this.quality.getValue(0, index);
                }
                return (K) new Double(this.getTimeliness());
            default:
                break;
        }
        return null;
    }

    @Override
    public Class<? extends IMetaAttribute>[] getClasses() {
        return QualityTimeInterval.CLASSES;
    }

    @Override
    public List<SDFMetaSchema> getSchema() {
        return QualityTimeInterval.SCHEMA;
    }

    @Override
    public String toString() {
        return "i=" + this.timeInterval.toString() + "| q=[completeness=" + this.quality.getCompleteness() + ", consistency=" + this.quality.getConsistency() + ", frequency="
                + this.quality.getFrequency() + ", timeliness=" + this.getTimeliness() + "]";
    }

    @Override
    public String getName() {
        return "QualityTimeInterval";
    }

    @Override
    public QualityTimeInterval clone() {
        return new QualityTimeInterval(this);
    }

    // ------------------------------------------------------------------------------
    // Delegates for timeInterval
    // ------------------------------------------------------------------------------

    @Override
    public PointInTime getStart() {
        return this.timeInterval.getStart();
    }

    @Override
    public PointInTime getEnd() {
        return this.timeInterval.getEnd();
    }

    @Override
    public void setStart(final PointInTime point) {
        this.timeInterval.setStart(point);
    }

    @Override
    public void setEnd(final PointInTime point) {
        this.timeInterval.setEnd(point);
    }

    @Override
    public void setStartAndEnd(final PointInTime start, final PointInTime end) {
        this.timeInterval.setStartAndEnd(start, end);
    }

    @Override
    public int compareTo(final ITimeInterval o) {
        return this.timeInterval.compareTo(o);
    }

    // ------------------------------------------------------------------------------
    // Delegates for quality
    // ------------------------------------------------------------------------------

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

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFrequency(final double frequency) {
        this.quality.setFrequency(frequency);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getFrequency() {
        return this.quality.getFrequency();
    }

    public double getTimeliness() {
        final PointInTime endTime = PointInTime.currentPointInTime();
        return Math.max(0.0, 1.0 - ((endTime.minus(this.getStart()).getMainPoint()) / (this.getFrequency() * 1000.0)));
    }

    public double getTimeliness(final PointInTime endTime) {
        return Math.max(0.0, 1.0 - ((endTime.minus(this.getStart()).getMainPoint()) / (this.getFrequency() * 1000.0)));
    }
}
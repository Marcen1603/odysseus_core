package de.uniol.inf.is.odysseus.salsa.physicaloperator;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.salsa.pool.SourcePool;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class SourcePO<T extends IMetaAttribute> extends
        AbstractSource<RelationalTuple<TimeInterval>> {
    private static Logger LOG = LoggerFactory.getLogger(SourcePO.class);
    private final SDFAttributeList schema;

    /**
     * @param schema
     */
    public SourcePO(final SDFAttributeList schema) {
        this.schema = schema;
    }

    /**
     * @param po
     */
    public SourcePO(final SourcePO<T> po) {
        this.schema = po.schema;
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.physicaloperator.AbstractSource#clone()
     */
    @Override
    public SourcePO<T> clone() {
        return new SourcePO<T>(this);
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.physicaloperator.AbstractSource#getOutputSchema()
     */
    @Override
    public SDFAttributeList getOutputSchema() {
        return this.schema;
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.physicaloperator.AbstractSource#process_close()
     */
    @Override
    protected void process_close() {
        SourcePool.unregisterSource("test");
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.physicaloperator.AbstractSource#process_open()
     */
    @Override
    protected void process_open() throws OpenFailedException {
        SourcePool.registerSource("test", this);
    }

    /**
     * @param data
     *            The data as a {@link Map} containing the attribute name and the value
     * @param timestamp
     *            The timestamp of measurement
     */
    public void transfer(final Map<String, Object> data, final long timestamp) {
        if (this.isOpen()) {
            final RelationalTuple<TimeInterval> event = new RelationalTuple<TimeInterval>(this
                    .getOutputSchema().size());
            int i = 0;

            for (final SDFAttribute attr : this.getOutputSchema()) {
                if (data.containsKey(attr.getAttributeName())) {
                    event.setAttribute(i++, data.get(attr.getAttributeName()));
                }
                else {
                    event.setAttribute(i++, "");
                }
            }
            final TimeInterval metadata = new TimeInterval(new PointInTime(timestamp));

            event.setMetadata(metadata);
            try {
                this.transfer(event);
            }
            catch (final Exception e) {
                SourcePO.LOG.error(e.getMessage(), e);
            }
        }
        else {
            SourcePO.LOG.warn("Source not open");
        }
    }
}

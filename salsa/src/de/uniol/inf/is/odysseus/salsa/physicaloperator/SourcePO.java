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
        SourcePool.unregisterSource("Laser1");
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.physicaloperator.AbstractSource#process_open()
     */
    @Override
    protected void process_open() throws OpenFailedException {
        SourcePool.registerSource("Laser1", this);
    }

    /**
     * @param timestamp
     *            The timestamp of measurement
     * @param data
     *            The data as a {@link Map} containing the values
     */
    public void transfer(final long timestamp, final Object[] data) {
        if (this.isOpen()) {
            final RelationalTuple<TimeInterval> event = new RelationalTuple<TimeInterval>(this
                    .getOutputSchema().size());
            for (int i = 0; i < data.length; i++) {
                event.setAttribute(i, data[i]);
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

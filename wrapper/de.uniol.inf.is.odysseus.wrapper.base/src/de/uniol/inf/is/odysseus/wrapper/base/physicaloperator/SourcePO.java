package de.uniol.inf.is.odysseus.wrapper.base.physicaloperator;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.wrapper.base.pool.SourcePool;

public class SourcePO<T extends IMetaAttribute> extends
        AbstractSource<RelationalTuple<TimeInterval>> {
    private static Logger LOG = LoggerFactory.getLogger(SourcePO.class);
    private final SDFAttributeList schema;
    private final String adapterName;
    private final Map<String, String> options = new HashMap<String, String>();

    /**
     * @param schema
     */
    public SourcePO(final SDFAttributeList schema, final String adapterName,
            final Map<String, String> options) {
        this.schema = schema;
        this.adapterName = adapterName;
        this.options.putAll(options);
    }

    /**
     * @param po
     */
    public SourcePO(final SourcePO<T> po) {
        this.schema = po.schema;
        this.adapterName = po.adapterName;
        this.options.putAll(po.options);
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.physicaloperator.AbstractSource#process_open()
     */
    @Override
    protected void process_open() throws OpenFailedException {
        SourcePool.registerSource(this.adapterName, this, this.options);
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.physicaloperator.AbstractSource#process_close()
     */
    @Override
    protected void process_close() {
        super.process_close();
        SourcePool.unregisterSource(this);
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.physicaloperator.AbstractSource#getOutputSchema()
     */
    @Override
    public SDFAttributeList getOutputSchema() {
        return this.schema;
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

    @Override
    public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
        if (!(ipo instanceof SourcePO<?>)) {
            return false;
        }
        return (((SourcePO<?>) ipo).adapterName.equals(this.adapterName))
                && (((SourcePO<?>) ipo).options.equals(this.options));
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.physicaloperator.AbstractSource#clone()
     */
    @Override
    public SourcePO<T> clone() {
        return new SourcePO<T>(this);
    }
}

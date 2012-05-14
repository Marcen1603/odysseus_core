package de.uniol.inf.is.odysseus.wrapper.base.physicaloperator;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.wrapper.base.pool.SourcePool;

public class SourcePO<T extends IMetaAttribute> extends
        AbstractSource<Tuple<TimeInterval>> {
    private static Logger LOG = LoggerFactory.getLogger(SourcePO.class);
    private final String adapterName;
    private final Map<String, String> options = new HashMap<String, String>();

    /**
     * @param schema
     */
    public SourcePO(final SDFSchema schema, final String adapterName,
            final Map<String, String> options) {
        setOutputSchema(schema);
        this.adapterName = adapterName;
        this.options.putAll(options);
        if (options.containsKey("name")) {
        	this.setName(options.get("name"));
        }
    }

    /**
     * @param po
     */
    public SourcePO(final SourcePO<T> po) {
        this.adapterName = po.adapterName;
        this.options.putAll(po.options);
        if (options.containsKey("name")) {
        	this.setName(options.get("name"));
        }
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource#process_open()
     */
    @Override
    protected void process_open() throws OpenFailedException {
        SourcePool.enableSource(this);
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource#process_close()
     */
    @Override
    protected void process_close() {
        super.process_close();
        SourcePool.disableSource(this);
    }

    /**
     * @param timestamp
     *            The timestamp of measurement
     * @param data
     *            The data as a {@link Map} containing the values
     */
    public void transfer(final long timestamp, final Object[] data) {
        if (this.isOpen()) {
            final Tuple<TimeInterval> event = new Tuple<TimeInterval>(this
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

    @Override
    public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
        if (!(ipo instanceof SourcePO<?>)) {
            return false;
        }
        return (((SourcePO<?>) ipo).adapterName.equals(this.adapterName))
                && (((SourcePO<?>) ipo).options.equals(this.options));
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource#clone()
     */
    @Override
    public SourcePO<T> clone() {
        return new SourcePO<T>(this);
    }

}

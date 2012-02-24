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
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.wrapper.base.pool.SinkPool;

public class SinkPO<T extends IMetaAttribute> extends AbstractSink<RelationalTuple<TimeInterval>> {
    private static Logger LOG = LoggerFactory.getLogger(SinkPO.class);
    private final SDFSchema schema;
    private final String adapterName;
    private final Map<String, String> options = new HashMap<String, String>();

    /**
     * @param schema
     */
    public SinkPO(final SDFSchema schema, final String adapterName,
            final Map<String, String> options) {
        this.schema = schema;
        this.adapterName = adapterName;
        this.options.putAll(options);
        if (options.containsKey("name")) {
        	this.setName(options.get("name"));
        }
    }

    /**
     * @param po
     */
    public SinkPO(final SinkPO<T> po) {
        this.schema = po.schema;
        this.adapterName = po.adapterName;
        this.options.putAll(po.options);
        if (options.containsKey("name")) {
        	this.setName(options.get("name"));
        }
    }

    @Override
    protected void process_open() throws OpenFailedException {
        SinkPool.registerSink(this.adapterName, this, this.options);
    }

    @Override
    protected void process_close() {
        super.process_close();
        SinkPool.unregisterSink(this);
    }

    @Override
    public void processPunctuation(final PointInTime timestamp, final int port) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void process_next(final RelationalTuple<TimeInterval> event, final int port,
            final boolean isReadOnly) {
        if (this.isOpen()) {
            try {
                SinkPool.transfer(this, event.getMetadata().getStart().getMainPoint(),
                        event.getAttributes());
            }
            catch (final Exception e) {
                SinkPO.LOG.error(e.getMessage(), e);
            }
        }
        else {
            SinkPO.LOG.warn("Sink not open");
        }

    }

    @Override
    public SinkPO<T> clone() {
        return new SinkPO<T>(this);
    }

    @Override
    public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
        if (!(ipo instanceof SinkPO<?>)) {
            return false;
        }
        return (((SinkPO<?>) ipo).adapterName.equals(this.adapterName))
                && (((SinkPO<?>) ipo).options.equals(this.options));
    }

    @Override
    public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
        if (!(ipo instanceof SinkPO<?>)) {
            return false;
        }
        return (((SinkPO<?>) ipo).adapterName.equals(this.adapterName))
                && (((SinkPO<?>) ipo).options.equals(this.options));
    }
}

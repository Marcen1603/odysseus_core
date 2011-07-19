package de.uniol.inf.is.odysseus.wrapper.base.physicaloperator;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.wrapper.base.pool.SinkPool;

public class SinkPO<T extends IMetaAttribute> extends AbstractSink<RelationalTuple<TimeInterval>> {
    private static Logger LOG = LoggerFactory.getLogger(SinkPO.class);
    private final SDFAttributeList schema;
    private final String adapterName;
    private final Map<String, String> options = new HashMap<String, String>();

    /**
     * @param schema
     */
    public SinkPO(final SDFAttributeList schema, final String adapterName,
            final Map<String, String> options) {
        this.schema = schema;
        this.adapterName = adapterName;
        this.options.putAll(options);
    }

    /**
     * @param po
     */
    public SinkPO(final SinkPO<T> po) {
        this.schema = po.schema;
        this.adapterName = po.adapterName;
        this.options.putAll(po.options);
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
    public void processPunctuation(PointInTime timestamp, int port) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void process_next(RelationalTuple<TimeInterval> event, int port, boolean isReadOnly) {
        if (this.isOpen()) {
            try {
                SinkPool.transfer(this.getName(), event.getMetadata().getStart().getMainPoint(), event.getAttributes());
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

}

package de.uniol.inf.is.odysseus.wrapper.base.pool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.wrapper.base.SinkAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.SinkConfiguration;
import de.uniol.inf.is.odysseus.wrapper.base.model.SinkSpec;
import de.uniol.inf.is.odysseus.wrapper.base.model.impl.SinkConfigurationImpl;
import de.uniol.inf.is.odysseus.wrapper.base.model.impl.SinkSpecImpl;

public class SinkPool<T extends IMetaAttribute> {
    private static Logger LOG = LoggerFactory.getLogger(SinkPool.class);
    private final Map<String, AbstractSink<RelationalTuple<TimeInterval>>> sinks = new ConcurrentHashMap<String, AbstractSink<RelationalTuple<TimeInterval>>>();
    private final Map<String, SinkAdapter> adapters = new ConcurrentHashMap<String, SinkAdapter>();
    private final Map<String, String> sinkAdapterMapping = new ConcurrentHashMap<String, String>();
    private final Map<String, SinkSpec> sinkSpecs = new ConcurrentHashMap<String, SinkSpec>();

    private static SinkPool<?> instance;

    public static SinkPool<?> getInstance() {
        if (SinkPool.instance == null) {
            SinkPool.instance = new SinkPool<TimeInterval>();
        }
        return SinkPool.instance;
    }

    public static void registerSink(final String adapterName,
            final AbstractSink<RelationalTuple<TimeInterval>> sink, Map<String, String> options) {
        SinkPool.getInstance()._registerSink(adapterName, sink, options);
    }

    public static void registerAdapter(SinkAdapter adapter) {
        SinkPool.getInstance()._registerAdapter(adapter);
    }

    public static void transfer(final String uri, final long timestamp, final Object[] data) {
        SinkPool.getInstance()._transfer(uri, timestamp, data);
    }

    public static void unregisterSink(final AbstractSink<RelationalTuple<TimeInterval>> sink) {
        SinkPool.getInstance()._unregisterSink(sink);
    }

    public static void unregisterAdapter(final SinkAdapter adapter) {
        SinkPool.getInstance()._unregisterAdapter(adapter);
    }

    private void _registerSink(final String adapterName,
            final AbstractSink<RelationalTuple<TimeInterval>> sink, Map<String, String> options) {
        final SinkSpec sinkSpec = new SinkSpecImpl(sink.getName());
        final SinkConfiguration configuration = new SinkConfigurationImpl();
        if (options != null) {
            configuration.putAll(options);
        }
        sinkSpec.setConfiguration(configuration);
        this.sinks.put(sink.getName(), sink);
        this.sinkSpecs.put(sink.getName(), sinkSpec);

        SinkAdapter adapter = this.adapters.get(adapterName);
        if (adapter != null) {
            adapter.registerSink(sinkSpec);

            sinkAdapterMapping.put(sink.getName(), adapter.getName());
            SinkPool.LOG.info("Physical Sink {} registered for adapter {}", sink, adapterName);
        }
        else {
            SinkPool.LOG.info("Adapter {} not found", adapterName);

        }
    }

    private void _registerAdapter(final SinkAdapter adapter) {
        this.adapters.put(adapter.getName(), adapter);
        SinkPool.LOG.info("Adapter {} registered", adapter.getName());
    }

    private void _transfer(final String sinkName, final long timestamp, final Object[] data) {
        if ((this.sinks.containsKey(sinkName)) && this.sinks.get(sinkName).isOpen()) {
            String adapterName = sinkAdapterMapping.get(sinkName);
            try {
                this.adapters.get(adapterName).transfer(sinkSpecs.get(sinkName), timestamp, data);
            }
            catch (final Exception e) {
                SinkPool.LOG.error(e.getMessage(), e);
            }
        }
    }

    private void _unregisterSink(final AbstractSink<RelationalTuple<TimeInterval>> sink) {
        if (sinkAdapterMapping.containsKey(sink.getName())) {
            SinkAdapter adapter = adapters.get(sinkAdapterMapping.get(sink.getName()));
            adapter.unregisterSink(sinkSpecs.get(sink.getName()));
            this.sinks.remove(sink.getName());
            this.sinkSpecs.remove(sink.getName());
            sinkAdapterMapping.remove(sink.getName());
            SinkPool.LOG.info("Physical Sink {} unregistered", sink);
        }
    }

    private void _unregisterAdapter(final SinkAdapter adapter) {
        this.adapters.remove(adapter.getName());
        SinkPool.LOG.info("Adapter {} unregistered", adapter.getName());
    }
}

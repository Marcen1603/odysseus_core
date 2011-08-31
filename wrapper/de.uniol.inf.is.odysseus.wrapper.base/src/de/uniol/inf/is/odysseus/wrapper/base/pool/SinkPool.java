package de.uniol.inf.is.odysseus.wrapper.base.pool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.base.SinkAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.SinkConfiguration;
import de.uniol.inf.is.odysseus.wrapper.base.model.SinkSpec;
import de.uniol.inf.is.odysseus.wrapper.base.model.impl.SinkConfigurationImpl;
import de.uniol.inf.is.odysseus.wrapper.base.model.impl.SinkSpecImpl;
import de.uniol.inf.is.odysseus.wrapper.base.physicaloperator.SinkPO;

public class SinkPool<T extends IMetaAttribute> {
    private static Logger LOG = LoggerFactory.getLogger(SinkPool.class);
    private final Map<SinkPO<?>, SinkSpec> sinks = new ConcurrentHashMap<SinkPO<?>, SinkSpec>();
    private final Map<String, SinkAdapter> adapters = new ConcurrentHashMap<String, SinkAdapter>();
    private final Map<String, String> sinkAdapterMapping = new ConcurrentHashMap<String, String>();

    private static SinkPool<?> instance;

    public synchronized static SinkPool<?> getInstance() {
        if (SinkPool.instance == null) {
            SinkPool.instance = new SinkPool<TimeInterval>();
        }
        return SinkPool.instance;
    }

    public static void registerSink(final String adapterName,
            final SinkPO<?> sink,
            final Map<String, String> options) {
        SinkPool.getInstance()._registerSink(adapterName, sink, options);
    }

    public static void registerAdapter(final SinkAdapter adapter) {
        SinkPool.getInstance()._registerAdapter(adapter);
    }

    public static void transfer(final SinkPO<?> sink,
            final long timestamp, final Object[] data) {
        SinkPool.getInstance()._transfer(sink, timestamp, data);
    }

    public static void unregisterSink(final SinkPO<?> sink) {
        SinkPool.getInstance()._unregisterSink(sink);
    }

    public static void unregisterAdapter(final SinkAdapter adapter) {
        SinkPool.getInstance()._unregisterAdapter(adapter);
    }

    private void _registerSink(final String adapterName,
            final SinkPO<?> sink,
            final Map<String, String> options) {
        final SinkSpec sinkSpec = new SinkSpecImpl(sink.getName());
        final SinkConfiguration configuration = new SinkConfigurationImpl();
        if (options != null) {
            configuration.putAll(options);
        }
        sinkSpec.setConfiguration(configuration);
        this.sinks.put(sink, sinkSpec);

        final SinkAdapter adapter = this.adapters.get(adapterName);
        if (adapter != null) {
            adapter.registerSink(sinkSpec);

            this.sinkAdapterMapping.put(sink.getName(), adapter.getName());
            SinkPool.LOG.info("Physical Sink {} registered for adapter {}", sink, adapterName);
        }
        else {
            SinkPool.LOG.info("Adapter {} not found", adapterName);

        }
    }

    private void _registerAdapter(final SinkAdapter adapter) {
        ((ConcurrentHashMap<String, SinkAdapter>) this.adapters).putIfAbsent(adapter.getName(),
                adapter);
        SinkPool.LOG.info("Adapter {} registered", adapter.getName());
    }

    private void _transfer(final SinkPO<?> sink,
            final long timestamp, final Object[] data) {
        if (this.sinks.containsKey(sink)) {
            final String adapterName = this.sinkAdapterMapping.get(sink.getName());
            try {
                this.adapters.get(adapterName).transfer(this.sinks.get(sink), timestamp, data);
            }
            catch (final Exception e) {
                SinkPool.LOG.error(e.getMessage(), e);
            }
        }
    }

    private void _unregisterSink(final SinkPO<?> sink) {
        if (this.sinkAdapterMapping.containsKey(sink.getName())) {
            final SinkAdapter adapter = this.adapters.get(this.sinkAdapterMapping.get(sink
                    .getName()));
            adapter.unregisterSink(this.sinks.get(sink));
            this.sinks.remove(sink.getName());
            this.sinkAdapterMapping.remove(sink.getName());
            SinkPool.LOG.info("Physical Sink {} unregistered", sink);
        }
    }

    private void _unregisterAdapter(final SinkAdapter adapter) {
        this.adapters.remove(adapter.getName());
        SinkPool.LOG.info("Adapter {} unregistered", adapter.getName());
    }
}

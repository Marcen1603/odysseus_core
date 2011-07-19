package de.uniol.inf.is.odysseus.wrapper.base.pool;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.wrapper.base.SourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.SourceSpec;
import de.uniol.inf.is.odysseus.wrapper.base.model.SourceConfiguration;
import de.uniol.inf.is.odysseus.wrapper.base.model.impl.SourceConfigurationImpl;
import de.uniol.inf.is.odysseus.wrapper.base.model.impl.SourceSpecImpl;

public class SourcePool<T extends IMetaAttribute> {
    private static Logger LOG = LoggerFactory.getLogger(SourcePool.class);
    private final Map<String, AbstractSource<RelationalTuple<TimeInterval>>> sources = new ConcurrentHashMap<String, AbstractSource<RelationalTuple<TimeInterval>>>();
    private final Map<String, SourceAdapter> adapters = new ConcurrentHashMap<String, SourceAdapter>();
    private final Map<String, String> sourceAdapterMapping = new ConcurrentHashMap<String, String>();
    private final Map<String, SourceSpec> sourceSpecs = new ConcurrentHashMap<String, SourceSpec>();

    private static SourcePool<?> instance;

    public static SourcePool<?> getInstance() {
        if (SourcePool.instance == null) {
            SourcePool.instance = new SourcePool<TimeInterval>();
        }
        return SourcePool.instance;
    }

    public static void registerSource(final String adapterName,
            final AbstractSource<RelationalTuple<TimeInterval>> source, Map<String, String> options) {
        SourcePool.getInstance()._registerSource(adapterName, source, options);
    }

    public static void registerAdapter(SourceAdapter adapter) {
        SourcePool.getInstance()._registerAdapter(adapter);
    }

    public static void transfer(final String uri, final long timestamp, final Object[] data) {
        SourcePool.getInstance()._transfer(uri, timestamp, data);
    }

    public static void unregisterSource(final AbstractSource<RelationalTuple<TimeInterval>> source) {
        SourcePool.getInstance()._unregisterSource(source);
    }

    public static void unregisterAdapter(final SourceAdapter adapter) {
        SourcePool.getInstance()._unregisterAdapter(adapter);
    }

    private void _registerSource(final String adapterName,
            final AbstractSource<RelationalTuple<TimeInterval>> source, Map<String, String> options) {
        final SourceSpec sourceSpec = new SourceSpecImpl(source.getName());

        final SourceConfiguration configuration = new SourceConfigurationImpl();
        if (options != null) {
            configuration.putAll(options);
        }
        sourceSpec.setConfiguration(configuration);
        // if (attributesConfiguration != null) {
        // for (final String attribute : attributesConfiguration.keySet()) {
        // final AttributeConfiguration attributeConfiguration = new AttributeConfigurationImpl();
        // sourceSpec.addAttribute(attribute, attributeConfiguration);
        // }
        // }
        this.sources.put(source.getName(), source);
        this.sourceSpecs.put(source.getName(), sourceSpec);

        SourceAdapter adapter = this.adapters.get(adapterName);
        if (adapter != null) {
            adapter.registerSource(sourceSpec);

            sourceAdapterMapping.put(source.getName(), adapter.getName());
            SourcePool.LOG
                    .info("Physical Source {} registered for adapter {}", source, adapterName);
        }
        else {
            SourcePool.LOG.info("Adapter {} not found", adapterName);

        }
    }

    private void _registerAdapter(final SourceAdapter adapter) {
        this.adapters.put(adapter.getName(), adapter);
        SourcePool.LOG.info("Adapter {} registered", adapter.getName());
    }

    private void _transfer(final String sourceName, final long timestamp, final Object[] data) {
        if ((this.sources.containsKey(sourceName)) && this.sources.get(sourceName).isOpen()) {
            final RelationalTuple<TimeInterval> event = new RelationalTuple<TimeInterval>(
                    data.length);
            for (int i = 0; i < data.length; i++) {
                event.setAttribute(i, data[i]);
            }
            final TimeInterval metadata = new TimeInterval(new PointInTime(timestamp));

            event.setMetadata(metadata);
            try {
                this.sources.get(sourceName).transfer(event);
            }
            catch (final Exception e) {
                SourcePool.LOG.error(e.getMessage(), e);
            }
        }
    }

    private void _unregisterSource(final AbstractSource<RelationalTuple<TimeInterval>> source) {
        if (sourceAdapterMapping.containsKey(source.getName())) {
            SourceAdapter adapter = adapters.get(sourceAdapterMapping.get(source.getName()));
            adapter.unregisterSource(sourceSpecs.get(source.getName()));
            this.sources.remove(source.getName());
            this.sourceSpecs.remove(source.getName());
            sourceAdapterMapping.remove(source.getName());
            SourcePool.LOG.info("Physical Source {} unregistered", source);
        }
    }

    private void _unregisterAdapter(final SourceAdapter adapter) {
        this.adapters.remove(adapter.getName());
        SourcePool.LOG.info("Adapter {} unregistered", adapter.getName());
    }
}

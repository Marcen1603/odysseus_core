package de.uniol.inf.is.odysseus.wrapper.base.pool;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.wrapper.base.SourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.SourceConfiguration;
import de.uniol.inf.is.odysseus.wrapper.base.model.SourceSpec;
import de.uniol.inf.is.odysseus.wrapper.base.model.impl.SourceConfigurationImpl;
import de.uniol.inf.is.odysseus.wrapper.base.model.impl.SourceSpecImpl;
import de.uniol.inf.is.odysseus.wrapper.base.physicaloperator.SourcePO;

public class SourcePool<T extends IMetaAttribute> {
    private static Logger LOG = LoggerFactory.getLogger(SourcePool.class);
    private final Map<SourceSpec, SourcePO<?>> sources = new ConcurrentHashMap<SourceSpec, SourcePO<?>>();
    private final Map<String, SourceAdapter> adapters = new ConcurrentHashMap<String, SourceAdapter>();
    private final Map<String, String> sourceAdapterMapping = new ConcurrentHashMap<String, String>();
    private final Set<SourcePO<?>> enableSources = new HashSet<SourcePO<?>>();
    private static SourcePool<?> instance;

    public synchronized static SourcePool<?> getInstance() {
        if (SourcePool.instance == null) {
            SourcePool.instance = new SourcePool<TimeInterval>();
        }
        return SourcePool.instance;
    }

    public static boolean hasSemanticallyEqualSource(SourcePO<?> other) {
        return SourcePool.getInstance()._hasSemanticallyEqualSource(other);
    }

    public static SourcePO<?> getSemanticallyEqualSource(SourcePO<?> other) {
        return SourcePool.getInstance()._getSemanticallyEqualSource(other);
    }

    public static void enableSource(final SourcePO<?> source) {
        SourcePool.getInstance()._enableSource(source);
    }

    public static void disableSource(final SourcePO<?> source) {
        SourcePool.getInstance()._disableSource(source);
    }

    public static void registerSource(final String adapterName, final SourcePO<?> source,
            final Map<String, String> options) {
        SourcePool.getInstance()._registerSource(adapterName, source, options);
    }

    public static void registerAdapter(final SourceAdapter adapter) {
        SourcePool.getInstance()._registerAdapter(adapter);
    }

    public static void transfer(final SourceSpec sourceSpec, final long timestamp,
            final Object[] data) {
        SourcePool.getInstance()._transfer(sourceSpec, timestamp, data);
    }

    public static void unregisterSource(final SourcePO<?> source) {
        SourcePool.getInstance()._unregisterSource(source);
    }

    public static void unregisterAdapter(final SourceAdapter adapter) {
        SourcePool.getInstance()._unregisterAdapter(adapter);
    }

    private boolean _hasSemanticallyEqualSource(SourcePO<?> other) {
        for (SourcePO<?> source : sources.values()) {
            if (source.isSemanticallyEqual(other)) {
                return true;
            }
        }
        return false;
    }

    private SourcePO<?> _getSemanticallyEqualSource(SourcePO<?> other) {
        for (SourcePO<?> source : sources.values()) {
            if (source.isSemanticallyEqual(other)) {
                return source;
            }
        }
        return null;
    }

    private void _enableSource(final SourcePO<?> source) {
        this.enableSources.add(source);
    }

    private void _disableSource(final SourcePO<?> source) {
        this.enableSources.remove(source);
    }

    private void _registerSource(final String adapterName, final SourcePO<?> source,
            final Map<String, String> options) {
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
        if (!this.sources.containsKey(sourceSpec)) {
            this.sources.put(sourceSpec, source);
            for (SDFAttribute attr : source.getOutputSchema().getAttributes()) {
            	sourceSpec.addAttribute(attr.getAttributeName(), null);
            }
            final SourceAdapter adapter = this.adapters.get(adapterName);
            if (adapter != null) {
                adapter.registerSource(sourceSpec);
                this.sourceAdapterMapping.put(source.getName(), adapter.getName());
                SourcePool.LOG.info("Physical Source {} registered for adapter {}", source,
                        adapterName);
            }
            else {
                SourcePool.LOG.info("Adapter {} not found", adapterName);
            }
        }
        else {
            SourcePool.LOG.info("SourceSpec  {} already registered", sourceSpec);
        }
    }

    private void _registerAdapter(final SourceAdapter adapter) {
        ((ConcurrentHashMap<String, SourceAdapter>) this.adapters).putIfAbsent(adapter.getName(),
                adapter);
        SourcePool.LOG.info("Adapter {} registered", adapter.getName());
    }

    private void _transfer(final SourceSpec sourceSpec, final long timestamp, final Object[] data) {
        if (this.sources.containsKey(sourceSpec)) {
            final Tuple<TimeInterval> event = new Tuple<TimeInterval>(
                    data.length);
            for (int i = 0; i < data.length; i++) {
                event.setAttribute(i, data[i]);
            }
            final TimeInterval metadata = new TimeInterval(new PointInTime(timestamp));
            event.setMetadata(metadata);
            try {
                SourcePO<?> source = this.sources.get(sourceSpec);
//                if (enableSources.contains(source)) {
                    source.transfer(event);
//                }
            }
            catch (final Exception e) {
                SourcePool.LOG.error(e.getMessage(), e);
            }
        }
    }

    private void _unregisterSource(final SourcePO<?> source) {
        if (this.sourceAdapterMapping.containsKey(source.getName())) {
            final SourceAdapter adapter = this.adapters.get(this.sourceAdapterMapping.get(source
                    .getName()));
            SourceSpec sourceSpec = null;
            for (SourceSpec spec : this.sources.keySet()) {
                if (this.sources.get(spec).equals(source)) {
                    sourceSpec = spec;
                }
            }
            if (sourceSpec != null) {
                adapter.unregisterSource(sourceSpec);
                this.sources.remove(sourceSpec);
            }
            this.sourceAdapterMapping.remove(source.getName());
            SourcePool.LOG.info("Physical Source {} unregistered", source);
        }
    }

    private void _unregisterAdapter(final SourceAdapter adapter) {
        this.adapters.remove(adapter.getName());
        SourcePool.LOG.info("Adapter {} unregistered", adapter.getName());
    }
}

package de.uniol.inf.is.odysseus.wrapper.base.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.wrapper.base.SourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.SourceControlService;
import de.uniol.inf.is.odysseus.wrapper.base.model.AttributeConfiguration;
import de.uniol.inf.is.odysseus.wrapper.base.model.Source;
import de.uniol.inf.is.odysseus.wrapper.base.model.SourceConfiguration;
import de.uniol.inf.is.odysseus.wrapper.base.model.impl.AttributeConfigurationImpl;
import de.uniol.inf.is.odysseus.wrapper.base.model.impl.SourceConfigurationImpl;
import de.uniol.inf.is.odysseus.wrapper.base.model.impl.SourceImpl;
import de.uniol.inf.is.odysseus.wrapper.base.pool.SourcePool;

public class SourceControlServiceImpl implements SourceControlService {
    private static Logger LOG = LoggerFactory.getLogger(SourceControlService.class);

    private final Map<String, List<SourceAdapter>> sourceAdapters = new ConcurrentHashMap<String, List<SourceAdapter>>();
    private final Map<String, Source> sources = new ConcurrentHashMap<String, Source>();
    private final Map<Source, SourceAdapter> sourceAdapterMapping = new ConcurrentHashMap<Source, SourceAdapter>();

    @Override
    public Set<String> getSourceAdapters() {
        return Collections.unmodifiableSet(this.sourceAdapters.keySet());
    }

    @Deprecated
    @Override
    public void registerSource(final String adapterType, final String name,
            final Map<String, String> sourceConfiguration,
            final Map<String, Map<String, String>> attributesConfiguration) {
        if (this.getSourceAdapters().contains(adapterType)) {
            final SourceAdapter adapter = this.sourceAdapters.get(adapterType).get(0);
            final Source source = new SourceImpl(name);

            final SourceConfiguration configuration = new SourceConfigurationImpl();
            if (sourceConfiguration != null) {
                configuration.putAll(sourceConfiguration);
            }
            source.setConfiguration(configuration);
            if (attributesConfiguration != null) {
                for (final String attribute : attributesConfiguration.keySet()) {
                    final AttributeConfiguration attributeConfiguration = new AttributeConfigurationImpl();
                    source.addAttribute(attribute, attributeConfiguration);
                }
            }
            this.sources.put(source.getName(), source);
            adapter.registerSource(source);
            this.sourceAdapterMapping.put(source, adapter);
            SourceControlServiceImpl.LOG.info("Register source {} at source adapter {}", name,
                    adapter.getName());
        }
    }

    @Deprecated
    @Override
    public void unregisterSource(final String name) {
        final Source source = this.sources.get(name);
        if (source != null) {
            final SourceAdapter adapter = this.sourceAdapterMapping.get(source);
            adapter.unregisterSource(source);
            this.sourceAdapterMapping.remove(source);
            this.sources.remove(name);
            SourceControlServiceImpl.LOG.info("Unregister source {}", name);
        }
    }

    protected void bindSourceAdapter(final SourceAdapter adapter) {
        // if (!this.sourceAdapters.containsKey(adapter.getName())) {
        // this.sourceAdapters.put(adapter.getName(), new ArrayList<SourceAdapter>());
        // }
        // this.sourceAdapters.get(adapter.getName()).add(adapter);
        SourcePool.registerAdapter(adapter);
        SourceControlServiceImpl.LOG.info("Bind source adapter {}", adapter.getName());
    }

    protected void unbindSourceAdapter(final SourceAdapter adapter) {
        // if (this.sourceAdapters.containsKey(adapter.getName())) {
        // this.sourceAdapters.get(adapter.getName()).remove(adapter);
        // SourceControlServiceImpl.LOG.info("Unbind source adapter {}", adapter.getName());
        // }
        SourcePool.unregisterAdapter(adapter);
    }

}

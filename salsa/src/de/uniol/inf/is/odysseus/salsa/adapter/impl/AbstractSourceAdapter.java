package de.uniol.inf.is.odysseus.salsa.adapter.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.salsa.adapter.Source;
import de.uniol.inf.is.odysseus.salsa.adapter.SourceAdapter;
import de.uniol.inf.is.odysseus.salsa.pool.SourcePool;

public abstract class AbstractSourceAdapter implements SourceAdapter {
    private static Logger LOG = LoggerFactory.getLogger(AbstractSourceAdapter.class);

    private final Map<String, Source> sources = new HashMap<String, Source>();

    public AbstractSourceAdapter() {

    }

    abstract protected void destroy(Source source);

    @Override
    public Collection<Source> getSources() {
        return Collections.unmodifiableCollection(this.sources.values());
    }

    abstract protected void init(Source source);

    @Override
    public void registerSource(final Source source) {
        this.sources.put(source.getName(), source);
        this.init(source);
        AbstractSourceAdapter.LOG.debug("New source {} registered", source.getName());
    }

    @Override
    public String toString() {
        return "AbstractSourceAdapter [sources=" + this.sources + "]";
    }

    protected void transfer(final Source source, final Map<String, Object> data,
            final long timestamp) {
        SourcePool.transfer(source.getName(), data, timestamp);
    }

    @Override
    public void unregisterSource(final Source source) {
        this.destroy(source);
        this.sources.remove(source.getName());
        AbstractSourceAdapter.LOG.debug("Source {} unregistered", source.getName());
    }

}

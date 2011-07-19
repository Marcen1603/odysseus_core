package de.uniol.inf.is.odysseus.wrapper.base;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.wrapper.base.model.SourceSpec;
import de.uniol.inf.is.odysseus.wrapper.base.pool.SourcePool;

public abstract class AbstractSourceAdapter implements SourceAdapter {
    private static Logger LOG = LoggerFactory.getLogger(AbstractSourceAdapter.class);

    private final Map<String, SourceSpec> sources = new HashMap<String, SourceSpec>();

    public AbstractSourceAdapter() {

    }

    abstract protected void destroy(SourceSpec source);

    @Override
    public Collection<SourceSpec> getSources() {
        return Collections.unmodifiableCollection(this.sources.values());
    }

    abstract protected void init(SourceSpec source);

    @Override
    public void registerSource(final SourceSpec source) {
        this.sources.put(source.getName(), source);
        this.init(source);
        AbstractSourceAdapter.LOG.debug("New source {} registered", source.getName());
    }

    @Override
    public String toString() {
        return "AbstractSourceAdapter " + this.getName() + " [sources=" + this.sources + "]";
    }

    protected void transfer(final String uri, final long timestamp, final Object[] data) {
        SourcePool.transfer(uri, timestamp, data);
    }

    @Override
    public void unregisterSource(final SourceSpec source) {
        this.destroy(source);
        this.sources.remove(source.getName());
        AbstractSourceAdapter.LOG.debug("Source {} unregistered", source.getName());
    }

}

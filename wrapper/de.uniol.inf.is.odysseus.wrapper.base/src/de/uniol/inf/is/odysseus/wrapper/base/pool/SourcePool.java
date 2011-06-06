package de.uniol.inf.is.odysseus.wrapper.base.pool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class SourcePool<T extends IMetaAttribute> {
    private static Logger LOG = LoggerFactory.getLogger(SourcePool.class);
    private final Map<String, AbstractSource<RelationalTuple<TimeInterval>>> sources = new ConcurrentHashMap<String, AbstractSource<RelationalTuple<TimeInterval>>>();

    private static SourcePool<?> instance;

    public static SourcePool<?> getInstance() {
        if (SourcePool.instance == null) {
            SourcePool.instance = new SourcePool<TimeInterval>();
        }
        return SourcePool.instance;
    }

    public static void registerSource(final String uri,
            final AbstractSource<RelationalTuple<TimeInterval>> source) {
        SourcePool.getInstance()._registerSource(uri, source);
    }

    public static void transfer(final String uri, final long timestamp, final Object[] data) {
        SourcePool.getInstance()._transfer(uri, timestamp, data);
    }

    public static void unregisterSource(final String uri) {
        SourcePool.getInstance()._unregisterSource(uri);
    }

    private void _registerSource(final String uri,
            final AbstractSource<RelationalTuple<TimeInterval>> source) {
        this.sources.put(uri, source);
        SourcePool.LOG.info("Physical Source {} registered", uri);
    }

    private void _transfer(final String uri, final long timestamp, final Object[] data) {
        if ((this.sources.containsKey(uri)) && this.sources.get(uri).isOpen()) {
            final RelationalTuple<TimeInterval> event = new RelationalTuple<TimeInterval>(
                    data.length);
            for (int i = 0; i < data.length; i++) {
                event.setAttribute(i, data[i]);
            }
            final TimeInterval metadata = new TimeInterval(new PointInTime(timestamp));

            event.setMetadata(metadata);
            try {
                this.sources.get(uri).transfer(event);
            }
            catch (final Exception e) {
                SourcePool.LOG.error(e.getMessage(), e);
            }
        }
    }

    private void _unregisterSource(final String uri) {
        this.sources.remove(uri);
        SourcePool.LOG.info("Physical Source {} unregistered", uri);
    }
}

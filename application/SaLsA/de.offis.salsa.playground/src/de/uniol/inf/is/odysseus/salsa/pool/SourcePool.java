package de.uniol.inf.is.odysseus.salsa.pool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.salsa.physicaloperator.SourcePO;
/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 *
 */
public class SourcePool {
    private static Logger LOG = LoggerFactory.getLogger(SourcePool.class);
    private final Map<String, SourcePO<?>> sources = new ConcurrentHashMap<String, SourcePO<?>>();

    private static SourcePool instance;

    public static SourcePool getInstance() {
        if (SourcePool.instance == null) {
            SourcePool.instance = new SourcePool();
        }
        return SourcePool.instance;
    }

    public static void registerSource(final String uri, final SourcePO<?> source) {
        SourcePool.getInstance()._registerSource(uri, source);
    }

    public static void transfer(final String uri, final long timestamp, final Object[] data) {
        SourcePool.getInstance()._transfer(uri, timestamp, data);
    }

    public static void unregisterSource(final String uri) {
        SourcePool.getInstance()._unregisterSource(uri);
    }

    private void _registerSource(final String uri, final SourcePO<?> source) {
        this.sources.put(uri, source);
        SourcePool.LOG.info("Physical Source {} registered", uri);
    }

    private void _transfer(final String uri, final long timestamp, final Object[] data) {
        if ((this.sources.containsKey(uri)) && this.sources.get(uri).isOpen()) {
            this.sources.get(uri).transfer(timestamp, data);
        }
    }

    private void _unregisterSource(final String uri) {
        this.sources.remove(uri);
        SourcePool.LOG.info("Physical Source {} unregistered", uri);
    }
}

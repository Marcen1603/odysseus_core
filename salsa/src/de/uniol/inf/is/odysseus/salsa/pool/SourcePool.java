package de.uniol.inf.is.odysseus.salsa.pool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.salsa.physicaloperator.SourcePO;

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
        // Just a simple generator for testing purpose
        final Thread runner = new Thread() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    final Map<String, Object> testData = new HashMap<String, Object>();
                    testData.put("scan", new Coordinate[] {
                            new Coordinate(0.5, 0.7), new Coordinate(0.3, 20.0),
                            new Coordinate(21.0, 5000), new Coordinate(5000.0, 5001.20)
                    });

                    source.transfer(testData, System.currentTimeMillis());
                    try {
                        Thread.sleep(1000);
                    }
                    catch (final InterruptedException e) {
                        SourcePool.LOG.error(e.getMessage(), e);
                    }
                }
            }
        };
        runner.start();
    }

    public static void transfer(final String name, final Map<String, Object> data,
            final long timestamp) {
        SourcePool.getInstance()._transfer(name, data, timestamp);
    }

    public static void unregisterSource(final String uri) {
        SourcePool.getInstance()._unregisterSource(uri);
    }

    private void _registerSource(final String uri, final SourcePO<?> source) {
        this.sources.put(uri, source);
        SourcePool.LOG.info("Physical Source {} registered", uri);
    }

    private void _transfer(final String name, final Map<String, Object> data, final long timestamp) {
        this.sources.get(name).transfer(data, timestamp);
    }

    private void _unregisterSource(final String uri) {
        this.sources.remove(uri);
        SourcePool.LOG.info("Physical Source {} unregistered", uri);
    }
}

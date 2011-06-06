package de.uniol.inf.is.odysseus.salsa.sensor.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.salsa.adapter.Source;
import de.uniol.inf.is.odysseus.salsa.adapter.impl.AbstractPushingSourceAdapter;
import de.uniol.inf.is.odysseus.salsa.sensor.SickConnection;
import de.uniol.inf.is.odysseus.salsa.sensor.model.Measurement;
import de.uniol.inf.is.odysseus.salsa.sensor.model.Sample;

public class SickPushingSourceAdapter extends AbstractPushingSourceAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(SickPushingSourceAdapter.class);
    private final Map<Source, Thread> connectionThreads = new ConcurrentHashMap<Source, Thread>();

    @Override
    protected void doDestroy(final Source source) {
        if (this.connectionThreads.containsKey(source)) {
            this.connectionThreads.get(source).interrupt();
        }
    }

    @Override
    protected void doInit(final Source source) {
        final SickConnection connection = new SickConnectionImpl();
        final String host = source.getConfiguration().get("host").toString();
        final int port = Integer.parseInt(source.getConfiguration().get("port").toString());
        SickPushingSourceAdapter.LOG.debug("Open connection to SICK sensor at {}:{}", host, port);
        connection.open(host, port);
        this.connectionThreads.put(source, new Thread() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    Measurement measurement;
                    while ((measurement = connection.getMeasurement()) != null) {
                        final List<Coordinate> coordinates = new ArrayList<Coordinate>(measurement
                                .getSamples().length);

                        for (final Sample sample : measurement.getSamples()) {
                            coordinates.add(sample.getDist1Vector());
                        }
                        final Map<String, Object> data = new HashMap<String, Object>();
                        data.put("scan", coordinates);
                        SickPushingSourceAdapter.this.transfer(source, data,
                                System.currentTimeMillis());
                    }
                }
                SickPushingSourceAdapter.LOG
                        .debug("Connection handler for SICK sensor interrupted");
                connection.close();
            }
        });
        this.connectionThreads.get(source).start();
    }

    @Override
    public String getName() {
        return "Sick";
    }
}

package de.uniol.inf.is.odysseus.salsa.sensor.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.salsa.adapter.Source;
import de.uniol.inf.is.odysseus.salsa.adapter.impl.AbstractPushingSourceAdapter;
import de.uniol.inf.is.odysseus.salsa.sensor.MeasurementListener;
import de.uniol.inf.is.odysseus.salsa.sensor.SickConnection;
import de.uniol.inf.is.odysseus.salsa.sensor.model.Measurement;
import de.uniol.inf.is.odysseus.salsa.sensor.model.Sample;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class SickPushingSourceAdapter extends AbstractPushingSourceAdapter implements
        MeasurementListener {
    private static final Logger LOG = LoggerFactory.getLogger(SickPushingSourceAdapter.class);
    private final GeometryFactory geometryFactory = new GeometryFactory();
    private final Map<Source, SickConnection> connections = new ConcurrentHashMap<Source, SickConnection>();

    @Override
    protected void doDestroy(final Source source) {
        if (this.connections.containsKey(source)) {
            this.connections.get(source).close();
            this.connections.remove(source);
        }
    }

    @Override
    protected void doInit(final Source source) {
        final String host = source.getConfiguration().get("host").toString();
        final int port = Integer.parseInt(source.getConfiguration().get("port").toString());
        final SickConnection connection = new SickConnectionImpl(host, port);
        SickPushingSourceAdapter.LOG.debug("Open connection to SICK sensor at {}:{}", host, port);
        connection.setListener(source.getName(), this);
        connection.open();
        this.connections.put(source, connection);
    }

    @Override
    public String getName() {
        return "Sick";
    }

    @Override
    public void onMeasurement(final String uri, final Measurement measurement) {
        if ((measurement != null) && (measurement.getSamples() != null)) {
            final Point[] coordinates = new Point[measurement.getSamples().length];
            for (int i = 0; i < measurement.getSamples().length; i++) {
                final Sample sample = measurement.getSamples()[i];
                coordinates[i] = geometryFactory.createPoint(sample.getDist1Vector());
            }
            SickPushingSourceAdapter.this.transfer(uri, System.currentTimeMillis(), new Object[] {
                geometryFactory.createMultiPoint(coordinates)
            });
        }

    }
}

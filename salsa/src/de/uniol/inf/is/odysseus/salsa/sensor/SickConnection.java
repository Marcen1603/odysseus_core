package de.uniol.inf.is.odysseus.salsa.sensor;

import de.uniol.inf.is.odysseus.salsa.sensor.model.Background;

public interface SickConnection {
    void close();

    Background getBackground();

    boolean isConnected();

    void open();

    void setListener(String uri, MeasurementListener listener);

    void startRecordBackground();

    void stopRecordBackground();
}

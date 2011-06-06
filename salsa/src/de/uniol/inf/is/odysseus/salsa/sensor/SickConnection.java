package de.uniol.inf.is.odysseus.salsa.sensor;

import de.uniol.inf.is.odysseus.salsa.sensor.model.Background;
import de.uniol.inf.is.odysseus.salsa.sensor.model.Measurement;

public interface SickConnection {
    void close();

    Background getBackground();

    Measurement getMeasurement();

    boolean isConnected();

    void open(String host, int port);

    void startRecordBackground();

    void stopRecordBackground();
}

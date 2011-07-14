package de.uniol.inf.is.odysseus.wrapper.sick;

import de.uniol.inf.is.odysseus.wrapper.sick.model.Background;
/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 *
 */
public interface SickConnection {
    void close();

    Background getBackground();

    boolean isConnected();

    void open();

    void setListener(String uri, MeasurementListener listener);

    void startRecordBackground();

    void stopRecordBackground();
}

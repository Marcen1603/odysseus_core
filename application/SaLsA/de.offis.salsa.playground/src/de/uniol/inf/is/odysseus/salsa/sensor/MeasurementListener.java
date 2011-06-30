package de.uniol.inf.is.odysseus.salsa.sensor;

import de.uniol.inf.is.odysseus.salsa.sensor.model.Measurement;
/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 *
 */
public interface MeasurementListener {
    void onMeasurement(String uri, Measurement measurement);
}

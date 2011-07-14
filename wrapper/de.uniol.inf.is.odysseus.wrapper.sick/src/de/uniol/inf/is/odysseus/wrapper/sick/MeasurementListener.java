package de.uniol.inf.is.odysseus.wrapper.sick;

import de.uniol.inf.is.odysseus.wrapper.sick.model.Measurement;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public interface MeasurementListener {
    void onMeasurement(String uri, Measurement measurement);
}

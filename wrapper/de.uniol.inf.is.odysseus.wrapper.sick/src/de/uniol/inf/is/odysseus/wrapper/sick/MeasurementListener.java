package de.uniol.inf.is.odysseus.wrapper.sick;

import de.uniol.inf.is.odysseus.wrapper.base.model.SourceSpec;
import de.uniol.inf.is.odysseus.wrapper.sick.model.Measurement;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public interface MeasurementListener {
    void onMeasurement(SourceSpec source, Measurement measurement, long timestamp);
}

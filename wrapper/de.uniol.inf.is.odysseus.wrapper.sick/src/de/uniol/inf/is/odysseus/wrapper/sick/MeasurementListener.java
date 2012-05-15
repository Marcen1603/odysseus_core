package de.uniol.inf.is.odysseus.wrapper.sick;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.wrapper.base.model.SourceSpec;
import de.uniol.inf.is.odysseus.wrapper.sick.model.Measurement;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public interface MeasurementListener {
	void onMeasurement(SourceSpec source, Coordinate origin, double angle,
			Measurement measurement, long timestamp);
}

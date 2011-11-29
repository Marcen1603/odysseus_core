package de.offis.salsa.lms;

import java.io.FileNotFoundException;
import java.io.IOException;

import de.offis.salsa.lms.model.Measurement;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public interface SickConnection {
	void close() throws IOException;

	boolean isConnected();

	void open() throws FileNotFoundException;

	void onMeasurement(Measurement measurement, long timestamp);

}

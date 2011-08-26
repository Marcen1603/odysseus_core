package de.offis.salsa.lms;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public interface SickConnection {
	void close() throws IOException;

	boolean isConnected();

	void open() throws FileNotFoundException;

}

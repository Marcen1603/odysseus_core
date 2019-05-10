package de.uniol.inf.is.odysseus.client.communication.transformStrategies;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * This interface is responsible for transforming a sequence of bytes into a specific data type (int, long, double, ...).
 * @author Thore Stratmann
 */
public interface ITransformStrategy {

	Object transformBytesToObject(DataInputStream inputStream) throws IOException;
}

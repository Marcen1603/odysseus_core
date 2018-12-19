package de.uniol.inf.is.odysseus.client.communication.transformStrategies;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author Tobias
 * @since 25.06.2015.
 */
public class TimestampTransformStrategy implements ITransformStrategy {

    @Override
    public Object transformBytesToObject(DataInputStream inputStream) throws IOException {
        return inputStream.readLong();
    }
}

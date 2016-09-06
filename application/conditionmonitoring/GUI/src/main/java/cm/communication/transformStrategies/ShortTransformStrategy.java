package cm.communication.transformStrategies;


import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author Thore Stratmann
 */
public class ShortTransformStrategy implements ITransformStrategy{

	@Override
	public Object transformBytesToObject(DataInputStream inputStream) throws IOException {
        return inputStream.readShort();
	}
}

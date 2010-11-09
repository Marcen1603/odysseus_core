package de.uniol.inf.is.odysseus.broker.sensors.generator;

/**
 * A factory for creating StreamType objects.
 * 
 * @author Dennis Geesen
 */
public class StreamTypeFactory {

	/**
	 * Creates a new StreamType object.
	 *
	 * @param type the type to create
	 * @return the created stream
	 */
	public static IStreamType createNewRun(StreamType type) {
		switch (type) {
		case LIDAR:
			return new SensorObject("LIDAR", 500);
		case RADAR:
			return new SensorObject("RADAR", 400);				
//		case VIDEO:
//			return new SensorObject("VIDEO", 500, 4, true);
		default:
			return new SensorObject(type.toString(), 1000);
		}				
	}
}

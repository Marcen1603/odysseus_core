package de.uniol.inf.is.odysseus.broker.sensors.generator;

public class StreamTypeFactory {

	public static IStreamType createNewRun(StreamType type) {
		switch (type) {
		case LIDAR:
			return new SensorObject("LIDAR", 1000);
		case RADAR:
			return new SensorObject("RADAR", 2000);		
		}
		return null;
	}
}

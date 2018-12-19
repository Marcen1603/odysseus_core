package de.uniol.inf.is.odysseus.client.communication.transformStrategies;

/**
 * This class represents a factory for implementations of ITransformStrategy
 * 
 * @author Thore Stratmann
 */
public class TransformStrategyFactory {
	private static TransformStrategyFactory dataTypeFactory;

	public static TransformStrategyFactory getInstance() {
		if (dataTypeFactory == null) {
			dataTypeFactory = new TransformStrategyFactory();
		}
		return dataTypeFactory;
	}

	public ITransformStrategy getTransformStrategy(String dataType) {
		if (dataType.equalsIgnoreCase("string")) {
			return new StringTransformStrategy();
		} else if (dataType.equalsIgnoreCase("integer")) {
			return new IntegerTransformStrategy();
		} else if (dataType.equalsIgnoreCase("double")) {
			return new DoubleTransformStrategy();
		} else if (dataType.equalsIgnoreCase("long")) {
			return new LongTransformStrategy();
		} else if (dataType.equalsIgnoreCase("float")) {
			return new FloatTransformStrategy();
		} else if (dataType.equalsIgnoreCase("short")) {
			return new ShortTransformStrategy();
		} else if (dataType.equalsIgnoreCase("byte")) {
			return new ByteTransformStrategy();
		} else if (dataType.equalsIgnoreCase("boolean")) {
			return new BooleanTransformStrategy();
		} else if (dataType.equalsIgnoreCase("starttimestamp")) {
			return new StartTimestampTransformStrategy();
		} else if (dataType.equalsIgnoreCase("endtimestamp")) {
			return new EndTimestampTransformStrategy();
		} else if (dataType.equalsIgnoreCase("timestamp")) {
			return new TimestampTransformStrategy();
		} else if (dataType.equalsIgnoreCase("SpatialGeometry")) {
			return new SpatialGeometryTransformStrategy();
		} else {
			return null;
		}
	}

}

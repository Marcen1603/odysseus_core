package de.uniol.inf.is.odysseus.new_transformation.costmodel.base;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.new_transformation.stream_characteristics.StreamCharacteristicCollection;

public class TempTransformationOperator {
	private final List<TempTransformationOperator> inputs = new LinkedList<TempTransformationOperator>();
	private final List<TempTransformationOperator> outputs = new LinkedList<TempTransformationOperator>();
	private final Map<String, Object> propertyMap = new HashMap<String, Object>();
	private final String type;
	private StreamCharacteristicCollection streamCharacteristics;

	public TempTransformationOperator(String type) {
		this.type = type;
	}

	public StreamCharacteristicCollection getStreamCharacteristics() {
		return streamCharacteristics;
	}

	public void setStreamCharacteristics(StreamCharacteristicCollection streamCharacteristics) {
		this.streamCharacteristics = streamCharacteristics;
	}

	public List<TempTransformationOperator> getInputs() {
		return inputs;
	}

	public void addInput(TempTransformationOperator operator) {
		inputs.add(operator);
	}

	public List<TempTransformationOperator> getOutputs() {
		return outputs;
	}

	public void addOutput(TempTransformationOperator operator) {
		outputs.add(operator);
	}

	public void setProperty(String key, Object value) {
		propertyMap.put(key, value);
	}

	public <T> T getProperty(String key) {
		return (T) propertyMap.get(key);
	}

	public String getType() {
		return type;
	}
}

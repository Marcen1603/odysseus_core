package de.uniol.inf.is.odysseus.wrapper.mosaik.physicaloperator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.wrapper.mosaik.logicaloperator.PyPowerMeasurementsConverterAO;

/**
 * Logical operator to convert mosaik pypower measurements with the following
 * JSON format to a set of tuples (1 JSON input to n tuples; not a list in a
 * tuple).<br />
 * <br />
 * Input:<br />
 * {<br />
 * "timestamp":959400,<br />
 * "zmq_0":{<br />
 * "Q":{<br />
 * "Power-0.0-node_c5":0.0,<br />
 * …<br />
 * },<br />
 * "Va":{<br />
 * "Power-0.0-node_c5":-0.011979076669770414,<br />
 * …<br />
 * },<br />
 * "Vm":{<br />
 * "Power-0.0-node_c5":229.84804626601294,<br />
 * …<br />
 * },<br />
 * "P":{<br />
 * "PV-0.PV_14":0.0,<br />
 * …<br />
 * },<br />
 * "P_out":{<br />
 * "Household-0.House_2":151.66,<br />
 * …<br />
 * }<br />
 * }<br />
 * }<br />
 * <br />
 * Output:<br />
 * measurement type | grid element | measurement | timestamp<br />
 * "Q" | "node_c5" | 0.0 | 959400<br />
 * "Va" | "node_c5" | 0.0 | -0.011979076669770414<br />
 * "Vm" | "node_c5" | 0.0 | 229.84804626601294
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class PyPowerMeasurementsConverterPO<M extends IMetaAttribute> extends AbstractPipe<KeyValueObject<M>, Tuple<M>> {

	private static final Logger log = LoggerFactory.getLogger("PyPowerMeasurementsConverter");

	private PyPowerMeasurementsConverterAO logical;

	public PyPowerMeasurementsConverterPO(PyPowerMeasurementsConverterAO logicalOp) {
		super();
		this.logical = logicalOp;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(KeyValueObject<M> object, int port) {
		List<Tuple<M>> tuples = new ArrayList<>();

		long timestamp = ((Integer) object.getAttribute("timestamp")).intValue();
		object.path(logical.getRootKey()).forEach(typeObj -> {
			String typeString = ((KeyValueObject<?>) typeObj).toString(false);
			ObjectMapper mapper = new ObjectMapper();
			try {
				JsonNode typeJSON = mapper.readTree(typeString);
				Iterator<Entry<String, JsonNode>> outerFields = typeJSON.fields();
				while(outerFields.hasNext()) {
					Entry<String, JsonNode> outerField = outerFields.next();
					String measurementType = outerField.getKey();
					if(!logical.getMeasurementTypes().contains(measurementType)) {
						continue;
					}
					Iterator<Entry<String, JsonNode>> innerFields = outerField.getValue().fields();
					while(innerFields.hasNext()) {
						Entry<String, JsonNode> innerField = innerFields.next();
						String gridElement = innerField.getKey().replaceFirst(logical.getNodePrefix(), "");
						double measurement = innerField.getValue().asDouble();
						Tuple<M> tuple = new Tuple<>(4, false);
						tuple.setAttribute(0, measurementType);
						tuple.setAttribute(1, gridElement);
						tuple.setAttribute(2, measurement);
						tuple.setAttribute(3, timestamp);
						tuple.setMetadata((M) object.getMetadata().clone());
						tuples.add(tuple);
					}
				}
			} catch (IOException e) {
				log.error("Could not convert JSON!", e);
			}
		});

		transfer(tuples);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

}
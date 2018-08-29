package de.uniol.inf.is.odysseus.wrapper.mosaik.logicaloperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

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
@LogicalOperator(name = "PyPowerMeasurementsConverter", doc = "Operator to convert mosaik pypower measurements in a JSON format (input KV object) to a set of tuples", category = LogicalOperatorCategory.TRANSFORM, minInputPorts = 1, maxInputPorts = 1)
public class PyPowerMeasurementsConverterAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -4264257928512533242L;

	/**
	 * JSON key above measurement types. In the above example, it is "zmq_0".
	 */
	private String rootKey;

	/**
	 * List of measurement types to consider, e.g. P, Q, Vm and Va.
	 */
	private List<String> measurementTypes;

	/**
	 * Prefix of node keys, e.g. Power-0.0-
	 */
	private String nodePrefix;

	public PyPowerMeasurementsConverterAO() {
		super();
	}

	public PyPowerMeasurementsConverterAO(PyPowerMeasurementsConverterAO other) {
		super();
		rootKey = other.rootKey;
		measurementTypes = new ArrayList<>(other.measurementTypes);
		nodePrefix = other.nodePrefix;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new PyPowerMeasurementsConverterAO(this);
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		String uri = getInputSchema(0).getBaseSourceNames().stream().collect(Collectors.joining());
		List<SDFAttribute> attributes = Arrays
				.asList(new SDFAttribute[] { new SDFAttribute(uri, "measurementType", SDFDatatype.STRING),
						new SDFAttribute(uri, "gridElement", SDFDatatype.STRING),
						new SDFAttribute(uri, "measurement", SDFDatatype.DOUBLE),
						new SDFAttribute(uri, "timestamp", SDFDatatype.LONG) });
		SDFSchema schema = SDFSchemaFactory.createNewSchema(uri, Tuple.class, attributes);
		return SDFSchemaFactory.createNewWithMetaSchema(schema, getInputSchema().getMetaschema());
	}

	public String getRootKey() {
		return rootKey;
	}

	@Parameter(name = "rootKey", doc = "JSON key above measurement types.", type = StringParameter.class)
	public void setRootKey(String key) {
		rootKey = key;
	}

	public List<String> getMeasurementTypes() {
		return measurementTypes;
	}

	@Parameter(name = "measurementTypes", doc = "List of measurement types to consider, e.g. P, Q, Vm and Va.", type = StringParameter.class, isList = true)
	public void setMeasurementTypes(List<String> measurementTypes) {
		this.measurementTypes = measurementTypes;
	}

	public String getNodePrefix() {
		return nodePrefix;
	}

	@Parameter(name = "nodePrefix", doc = "Prefix of node keys, e.g. Power-0.0-", type = StringParameter.class)
	public void setNodePrefix(String nodePrefix) {
		this.nodePrefix = nodePrefix;
	}

}

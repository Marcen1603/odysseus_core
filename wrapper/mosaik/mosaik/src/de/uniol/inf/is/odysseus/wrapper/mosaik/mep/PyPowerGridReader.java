package de.uniol.inf.is.odysseus.wrapper.mosaik.mep;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.graph.datatype.SDFGraphDatatype;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.keyvalue.datatype.SDFKeyValueDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * MEP function to create a graph out of a (modified) mosaik-pypower topology json. <br />
 * <br />
 * Example:<br />
 * {<br />
 * "base_mva": 0.0,<br />
 * "frequency":50.0,<br />
 * "buses":[<br />  
 * {<br />  
 * "name":"tr_sec",<br />
 * "type":"PQ",<br />
 * "initialVoltage":230.0,<br />
 * "inputConnectors":[<br />
 * {<br />
 * "measurement":"Vm",<br />
 * "type":"PhaseToEarthVoltage",<br />
 * "phase":"phaseA"<br />
 * },<br />
 * {<br />
 * "measurement":"Va",<br />
 * "type":"PMU",<br />
 * "phase":"phaseA"<br />
 * }<br />
 * ]<br />
 * },<br />
 * {<br />
 * ...<br />
 * }<br />
 * ],<br />
 * "branches":[<br />  
 * {<br />  
 * "name":"branch_1",<br />
 * "type":"SCLine",<br />
 * "source":"tr_sec",<br />
 * "target":"node_b1",<br />
 * "length":0.1,<br />
 * "RpL":0.2542,<br />
 * "XpL":0.080425,<br />
 * "CpL":0.0,<br />
 * "Imax":240.0<br />
 * },<br />
 * {<br />
 * ...<br />
 * }<br />
 * ]<br />
 * }<br />
 * <br />
 * The first argument is the json that hold the information about the topology.
 * The second argument is the id for the graph object. The output is a
 * {@link Graph} object (SDFDatatype = {@link SDFGraphDatatype}).
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class PyPowerGridReader extends AbstractFunction<Graph> {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 6995535175826820983L;

	/**
	 * The logger for this class.
	 */
	private static final Logger log = LoggerFactory.getLogger("PyPowerGridReader");

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = "PyPowerGridReader";

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 2;

	/**
	 * The expected data types of the inputs. One row for each input. Different data
	 * types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] { { SDFKeyValueDatatype.KEYVALUEOBJECT },
			{ SDFDatatype.STRING } };

	/**
	 * The data type of the outputs.
	 */
	private static final SDFDatatype outputType = SDFGraphDatatype.GRAPH;

	/**
	 * Creates a new MEP function.
	 */
	public PyPowerGridReader() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public Graph getValue() {
		try {
			KeyValueObject<?> input = getInputValue(0);
			String graphId = getInputValue(1);

			Graph graph = new SingleGraph(graphId);

			// general attributes are the base apparent power [MVA] from
			// pypower and the grid frequency [Hz]. The latter is originally not
			// from mosaik.
			graph.addAttribute("basePower_MVA", input.getNumberAttribute("base_mva"));
			// MVA to VA
			graph.addAttribute("frequency", input.getNumberAttribute("frequency"));

			// attributes for buses are (0) the name, (1) the type, (2) the
			// base voltage [kV], (3) input connectors for the state estimator (optional and
			// not from mosaik) and
			// (4) output connectors for the state estimator (optional and not from mosaik)
			input.path("buses").forEach(busObj -> {
				KeyValueObject<?> bus = (KeyValueObject<?>) busObj;
				Node node = graph.addNode((String) bus.getAttribute("name"));
				node.addAttribute("type", (String) bus.getAttribute("type"));
				node.addAttribute("baseVoltage_kV", bus.getNumberAttribute("initialVoltage"));
				if (!bus.path2("/inputConnectors").isEmpty()) {
					List<Map<String, Object>> inputConnectors = new ArrayList<>();
					bus.path("inputConnectors").forEach(
							connectorObj -> inputConnectors.add(((KeyValueObject<?>) connectorObj).getAsKeyValueMap()));
					node.addAttribute("inputConnectors", inputConnectors);
				}
				if (!bus.path2("/outputConnectors").isEmpty()) {
					List<Map<String, Object>> outputConnectors = new ArrayList<>();
					bus.path("outputConnectors").forEach(connectorObj -> outputConnectors
							.add(((KeyValueObject<?>) connectorObj).getAsKeyValueMap()));
					node.addAttribute("outputConnectors", outputConnectors);
				}
			});

			// attributes for branches are (0) the name, (1) the source bus, (2)
			// the target bus, (3) the length [km], (4) the resistance per unit
			// length [Ohm/km], (5) the reactance per unit length [Ohm/km], (6)
			// the capacity per unit length [nF/km], (7) max current [A], (8) the type (not
			// from mosaik),
			// (9) input connectors for the state estimator (optional and not from mosaik)
			// and
			// (10) output connectors for the state estimator (optional and not from mosaik)
			input.path("branches").forEach(branchObj -> {
				KeyValueObject<?> branch = (KeyValueObject<?>) branchObj;
				String fromName = (String) branch.getAttribute("source");
				String toName = (String) branch.getAttribute("target");
				Node fromBus = graph.getNode(fromName);
				Node toBus = graph.getNode(toName);
				Edge edge = graph.addEdge(fromName + "-" + toName, fromBus, toBus);
				edge.addAttribute("length_km", (Double) branch.getAttribute("length"));
				edge.addAttribute("RpL_Opkm", (Double) branch.getAttribute("RpL"));
				edge.addAttribute("XpL_Opkm", (Double) branch.getAttribute("XpL"));
				edge.addAttribute("R0pL_Opkm", (Double) branch.getAttribute("R0pL"));
				edge.addAttribute("X0pL_Opkm", (Double) branch.getAttribute("X0pL"));
				edge.addAttribute("CpL_nFpkm", (Double) branch.getAttribute("CpL"));
				edge.addAttribute("Imax", (Double) branch.getAttribute("Imax"));
				edge.addAttribute("type", (String) branch.getAttribute("type"));
				if (!branch.path2("/inputConnectors").isEmpty()) {
					List<Map<String, Object>> inputConnectors = new ArrayList<>();
					branch.path("inputConnectors").forEach(
							connectorObj -> inputConnectors.add(((KeyValueObject<?>) connectorObj).getAsKeyValueMap()));
					edge.addAttribute("inputConnectors", inputConnectors);
				}
				if (!branch.path2("/outputConnectors").isEmpty()) {
					List<Map<String, Object>> outputConnectors = new ArrayList<>();
					branch.path("outputConnectors").forEach(connectorObj -> outputConnectors
							.add(((KeyValueObject<?>) connectorObj).getAsKeyValueMap()));
					edge.addAttribute("outputConnectors", outputConnectors);
				}
			});
			
			if(!input.path2("/trafos").isEmpty()) {
				input.path("trafos").forEach(trafoObj -> {
					KeyValueObject<?> trafo = (KeyValueObject<?>) trafoObj;
					String fromName = (String) trafo.getAttribute("source");
					String toName = (String) trafo.getAttribute("target");
					Node fromBus = graph.getNode(fromName);
					Node toBus = graph.getNode(toName);
					Edge edge = graph.addEdge(fromName + "-" + toName, fromBus, toBus);
					edge.addAttribute("type", (String) trafo.getAttribute("type"));
					edge.addAttribute("nominalNode", (String) trafo.getAttribute("nominalNode"));
					edge.addAttribute("tapNode", (String) trafo.getAttribute("tapNode"));
					edge.addAttribute("shortCircuitR", (Double) trafo.getAttribute("shortCircuitR"));
					edge.addAttribute("shortCircuitL", (Double) trafo.getAttribute("shortCircuitL"));
					edge.addAttribute("turnsRatio", (Double) trafo.getAttribute("turnsRatio"));
					if (!trafo.path2("/inputConnectors").isEmpty()) {
						List<Map<String, Object>> inputConnectors = new ArrayList<>();
						trafo.path("inputConnectors").forEach(
								connectorObj -> inputConnectors.add(((KeyValueObject<?>) connectorObj).getAsKeyValueMap()));
						edge.addAttribute("inputConnectors", inputConnectors);
					}
					if (!trafo.path2("/outputConnectors").isEmpty()) {
						List<Map<String, Object>> outputConnectors = new ArrayList<>();
						trafo.path("outputConnectors").forEach(connectorObj -> outputConnectors
								.add(((KeyValueObject<?>) connectorObj).getAsKeyValueMap()));
						edge.addAttribute("outputConnectors", outputConnectors);
					}
				});
			}
			
			return graph;
		} catch (Throwable e) {
			log.error("Could not create graph!", e);
			return null;
		}
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}
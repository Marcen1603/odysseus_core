package de.uniol.inf.is.odysseus.wrapper.mosaik.mep;

import java.util.List;

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
 * MEP function to create a graph out of a mosaik-pypower topology json. <br />
 * <br />
 * Example:<br />
 * {<br />
 * "base_mva": 10,<br />
 * "bus": [<br />
 * ["tr_pri", "REF", 20.0],<br />
 * ["node_a1", "PQ", 0.23],<br />
 * ...<br />
 * ],<br />
 * "trafo": [<br />
 * ["transformer", "tr_pri", "tr_sec", 0.25, 4.2, 0.00275, 6.9, 360.8]<br />
 * ],<br />
 * "branch": [<br />
 * ["branch_1", "tr_sec", "node_a1", 0.100, 0.2542, 0.080425, 0.0, 240.0],<br />
 * ["branch_2", "node_a1", "node_a2", 0.375, 0.2542, 0.080425, 0.0,
 * 240.0],<br />
 * ...<br />
 * ],<br />
 * "frequency": 50,<br />
 * "branchtype": [<br />
 * {"branch": "branch_1", "type": "SCLine"},<br />
 * {"branch": "branch_2", "type": "SCLine"},<br />
 * ...<br />
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
	 * The expected data types of the inputs. One row for each input. Different
	 * data types in a row mark different possible data types for the input.
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
			graph.addAttribute("basePower", (Integer) input.getNumberAttribute("base_mva") * Math.pow(10, 6)); // MVA
																												// to
																												// VA
			if (input.containsKey("frequency")) {
				graph.addAttribute("frequency", input.getNumberAttribute("frequency"));
			}

			// attributes for buses are (0) the id, (1) the type and (2) the
			// base voltage [kV]
			input.path("bus").stream().forEach(busObj -> {
				List<Object> bus = ((KeyValueObject<?>) busObj).path("$");
				Node node = graph.addNode(getStringWithoutQuotes(bus.get(0)));
				node.addAttribute("nodeType", getStringWithoutQuotes(bus.get(1)));
				node.addAttribute("baseVoltage", (Double) bus.get(2) * Math.pow(10, 3)); // kV
																							// to
																							// V
			});

			// attributes for branches are (0) the id, (1) the source bus, (2)
			// the target bus, (3) the power rating [MVA], (4) the short-circuit
			// voltage [%], (5) the short-circuit losses [MV], (6) max current
			// on primary side [A], (7) max current on secondary side [A]
			if(input.containsKey("trafo")) {
				input.path("trafo").stream().forEach(trafoObj -> {
					List<Object> trafo = ((KeyValueObject<?>) trafoObj).path("$");
					Node fromBus = graph.getNode(getStringWithoutQuotes(trafo.get(1)));
					Node toBus = graph.getNode(getStringWithoutQuotes(trafo.get(2)));
					Edge edge = graph.addEdge(getStringWithoutQuotes(trafo.get(0)), fromBus, toBus);
					edge.addAttribute("Sr", (Double) trafo.get(3) * Math.pow(10, 6)); // MVA
																						// to
																						// VA
					edge.addAttribute("Uk", (Double) trafo.get(4));
					edge.addAttribute("Pk", (Double) trafo.get(5) * Math.pow(10, 6)); // MV
																						// to
																						// V
					edge.addAttribute("ImaxP", (Double) trafo.get(6));
					edge.addAttribute("ImaxS", (Double) trafo.get(7));
				});
			}

			// attributes for branches are (0) the id, (1) the source bus, (2)
			// the target bus, (3) the length [km], (4) the resistance per unit
			// length [Ohm/km], (5) the reactance per unit length [Ohm/km], (6)
			// the capacity per unit length [nF/km], (7) max current [A]
			input.path("branch").stream().forEach(branchObj -> {
				List<Object> branch = ((KeyValueObject<?>) branchObj).path("$");
				Node fromBus = graph.getNode(getStringWithoutQuotes(branch.get(1)));
				Node toBus = graph.getNode(getStringWithoutQuotes(branch.get(2)));
				Edge edge = graph.addEdge(getStringWithoutQuotes(branch.get(0)), fromBus, toBus);
				edge.addAttribute("length", (Double) branch.get(3) * Math.pow(10, 3)); // km
				// to
				// m
				edge.addAttribute("RpL", (Double) branch.get(4) * Math.pow(10, -3)); // Ohm/km
				// to
				// Ohm/m
				edge.addAttribute("XpL", (Double) branch.get(5) * Math.pow(10, -3)); // Ohm/km
				// to
				// Ohm/m
				edge.addAttribute("CpL", (Double) branch.get(6) * Math.pow(10, 9) * Math.pow(10, -3)); // nF/km
				// to
				// F/m
				edge.addAttribute("Imax", (Double) branch.get(7));
			});

			// additional there are line types for each branch (originally not
			// from mosaik).
			input.path("branchtype").stream().forEach(branchTypeObj -> {
				KeyValueObject<?> branchType = (KeyValueObject<?>) branchTypeObj;
				Edge edge = graph.getEdge(branchType.getAttribute("branch"));
				String type = branchType.getAttribute("type");
				edge.addAttribute("type", type);
			});
			return graph;
		} catch (Throwable e) {
			log.error("Could not create graph!", e);
			return null;
		}
	}

	private static String getStringWithoutQuotes(Object object) {
		return ((String) object).replaceAll("\"", "");
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}
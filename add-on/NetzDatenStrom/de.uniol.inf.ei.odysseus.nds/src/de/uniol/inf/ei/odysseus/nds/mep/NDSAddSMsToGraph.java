package de.uniol.inf.ei.odysseus.nds.mep;

import java.util.List;
import java.util.Map;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.graph.datatype.SDFGraphDatatype;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.keyvalue.datatype.SDFKeyValueDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * MEP function to add smart meter and smart meter gateways in a very specific
 * string notation to a low voltage topology (as a {@link Graph}) in the
 * research project NetzDatenStrom. <br />
 * <br />
 * It is assumed that the input is either a string list or a
 * {@link KeyValueObject} list and that each list entry is formated exactly as
 * follows: <br/>
 * <br />
 * [{"coordinates": {...}, "id": "<smart meter gateway id>", "node": "<id of
 * node gateway is connected to>", "smartmeters": [{"id": "<smart meter id>",
 * "models": [...]}, {"id": "<smart meter id>", "models": [...]}, ...], "tags":
 * {...}}]<br />
 * <br />
 * The output is a {@link Graph} object (SDFDatatype =
 * {@link SDFGraphDatatype}).
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class NDSAddSMsToGraph extends AbstractFunction<Graph> {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -1056778304121719824L;

	/**
	 * The logger for this class.
	 */
	private static final Logger log = LoggerFactory.getLogger("NetzDatenStromWrapper");

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = "NDSAddSMsToGraph";

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 2;

	/**
	 * The expected data types of the inputs. One row for each input. Different
	 * data types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] {
			{ SDFDatatype.LIST_STRING, SDFKeyValueDatatype.LIST_KEYVALUEOBJECT }, { SDFGraphDatatype.GRAPH } };

	/**
	 * The data type of the outputs.
	 */
	private static final SDFDatatype outputType = SDFGraphDatatype.GRAPH;

	/**
	 * The fix key of the smart meter gateway id.
	 */
	private static final String smgwIdField = "id";

	/**
	 * The fix key of the node id.
	 */
	private static final String nodeIdField = "node";

	/**
	 * The fix key of the smart meters object.
	 */
	private static final String smsField = "smartmeters";

	/**
	 * The fix key of the smart meter id.
	 */
	private static final String smIdField = "id";

	/**
	 * Creates a new MEP function.
	 */
	public NDSAddSMsToGraph() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public Graph getValue() {
		try {
			@SuppressWarnings("unchecked")
			List<String> strSMGWs = (List<String>) getInputValue(0);
			Graph topology = getInputValue(1);

			JSONArray smgws = new JSONArray(strSMGWs);
			for (int i = 0; i < smgws.length(); i++) {
				// Attention: The elements in the array are Odysseus
				// KeyValueObjects!
				KeyValueObject<?> smgwObj = (KeyValueObject<?>) smgws.get(i);
				String smgwId = smgwObj.getAttribute(smgwIdField);
				String nodeId = smgwObj.getAttribute(nodeIdField);

				// if node/edge exists, the existing node/edge is returned.
				// nothing is changed in the graph
				Node node = topology.getNode(nodeId);
				Node smgw = topology.addNode(smgwId);

				// add assets to smgw
				Map<String, Object> smgwAssets = smgwObj.getAsKeyValueMap();
				smgwAssets.keySet().stream().filter(key -> !(key.equals(smgwIdField) || key.equals(nodeIdField) || key.startsWith(smsField))).forEach(key -> {
					smgw.addAttribute(key, smgwAssets.get(key));
				});

				topology.addEdge(nodeId + "-" + smgwId, node, smgw);

				List<Object> sms = smgwObj.path(smsField);
				sms.stream().forEach(smObj -> {
					KeyValueObject<?> smKVObj = (KeyValueObject<?>) smObj;
					String smid = smKVObj.getAttribute(smIdField);
					Node sm = topology.addNode(smid);

					// add assets to smgw
					Map<String, Object> smAssets = smKVObj.getAsKeyValueMap();
					smAssets.keySet().stream().filter(key -> !key.equals(smIdField)).forEach(key -> {
						sm.addAttribute(key, smAssets.get(key));
					});

					topology.addEdge(smgwId + "-" + smid, smgw, sm);
				});
			}
			return topology;
		} catch (Throwable e) {
			log.error("Could not add smart meter informations to graph!", e);
			return null;
		}
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}
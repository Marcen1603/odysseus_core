package de.uniol.inf.is.odysseus.graph.mep;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.graph.datatype.SDFGraphDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * MEP function to get all {@link Node}s of a {@link Graph}. The input is the
 * graph and the output is a list of the found {@link Node}s.
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class GetAllNodes extends AbstractFunction<List<Node>> {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -5994252512892303918L;

	/**
	 * The logger for this class.
	 */
	private static final Logger log = LoggerFactory.getLogger("GraphMEP");

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = "GetAllNodes";

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 1;

	/**
	 * The expected data types of the inputs. One row for each input. Different
	 * data types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] { { SDFGraphDatatype.GRAPH } };

	/**
	 * The data type of the output.
	 */
	private static final SDFDatatype outputType = SDFGraphDatatype.NODE_LIST;

	/**
	 * Creates a new MEP function.
	 */
	public GetAllNodes() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public List<Node> getValue() {
		try {
			return new ArrayList<Node>(((Graph) getInputValue(0)).getNodeSet());
		} catch (Throwable e) {
			log.error("Could not extract node list from graph!", e);
			return null;
		}
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}

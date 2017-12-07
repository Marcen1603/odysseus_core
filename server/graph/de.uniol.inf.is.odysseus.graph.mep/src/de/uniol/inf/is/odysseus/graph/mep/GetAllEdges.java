package de.uniol.inf.is.odysseus.graph.mep;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.graph.datatype.SDFGraphDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * MEP function to get all {@link Edge}s of a {@link Graph}. The input is the
 * graph and the output is a list of the found {@link Edge}s.
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class GetAllEdges extends AbstractFunction<List<Edge>> {

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
	private static final String name = "GetAllEdges";

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
	private static final SDFDatatype outputType = SDFGraphDatatype.EDGE_LIST;

	/**
	 * Creates a new MEP function.
	 */
	public GetAllEdges() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public List<Edge> getValue() {
		try {
			return new ArrayList<Edge>(((Graph) getInputValue(0)).getEdgeSet());
		} catch (Throwable e) {
			log.error("Could not extract edge list from graph!", e);
			return null;
		}
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}

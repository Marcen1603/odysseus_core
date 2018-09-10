package de.uniol.inf.is.odysseus.graph.mep;

import java.util.List;
import java.util.stream.Collectors;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.NullAttributeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.graph.datatype.SDFGraphDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * MEP function to find all {@link Node}s in a {@link Graph}, which id fulfills
 * a given pattern. The inputs are:<br />
 * <ul>
 * <li>The graph</li>
 * <li>The pattern of the nodes</li>
 * </ul>
 * The output is a list of the found {@link Node}s.
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class FindNodesByPatternMEP extends AbstractFunction<List<Node>> {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 5700604178910288804L;

	/**
	 * The logger for this class.
	 */
	private static final Logger log = LoggerFactory.getLogger("GraphMEP");

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = "FindNodesByPattern";

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 2;

	/**
	 * The expected data types of the inputs. One row for each input. Different
	 * data types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] { { SDFGraphDatatype.GRAPH },
			{ SDFDatatype.STRING } };

	/**
	 * The data type of the output.
	 */
	private static final SDFDatatype outputType = SDFGraphDatatype.NODE_LIST;

	/**
	 * Creates a new MEP function.
	 */
	public FindNodesByPatternMEP() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public List<Node> getValue() {
		try {
			Graph graph = getInputValue(0);
			if (graph == null) {
				throw new NullAttributeException("Graph is null!");
			}
			String pattern = getInputValue(1);
			if (pattern == null) {
				throw new NullAttributeException("Pattern is null!");
			}

			return graph.getNodeSet().stream().filter(node -> node.getId().matches(pattern))
					.collect(Collectors.toList());
		} catch (Throwable e) {
			log.error("Could not search for node patterns in graph!", e);
			return null;
		}
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}

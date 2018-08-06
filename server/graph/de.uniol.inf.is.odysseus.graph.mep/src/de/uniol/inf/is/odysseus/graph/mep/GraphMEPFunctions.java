package de.uniol.inf.is.odysseus.graph.mep;

import java.util.Arrays;
import java.util.List;

import org.graphstream.graph.Graph;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.graph.datatype.SDFGraphDatatype;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

/**
 * Provides the MEP functions for graphs (see {@link Graph} and
 * {@link SDFGraphDatatype}) : <br />
 * <ul>
 * <li>{@link AddAttributeToEdge}</li>
 * <li>{@link AddAttributeToGraph}</li>
 * <li>{@link AddAttributeToNode}</li>
 * <li>{@link AddEdgeToGraph}</li>
 * <li>{@link AddNodeToGraph}</li>
 * <li>{@link FindNodesByPatternMEP}</li>
 * <li>{@link GetAllEdges}</li>
 * <li>{@link GetAllNodes}</li>
 * <li>{@link GetGraphElementIdMEP}</li>
 * <li> {@link GetGraphElementProperty}</li>
 * </ul>
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class GraphMEPFunctions implements IFunctionProvider {

	/**
	 * Instances of the provided MEP functions.
	 */
	private static final IMepFunction<?>[] functions = new IMepFunction[] { new AddAttributeToEdge(),
			new AddAttributeToGraph(), new AddAttributeToNode(), new AddEdgeToGraph(), new AddNodeToGraph(),
			new FindNodesByPatternMEP(), new GetAllEdges(), new GetAllNodes(), new GetGraphElementIdMEP(), new GetGraphElementProperty() };

	@Override
	public List<IMepFunction<?>> getFunctions() {
		return Arrays.asList(functions);
	}

}
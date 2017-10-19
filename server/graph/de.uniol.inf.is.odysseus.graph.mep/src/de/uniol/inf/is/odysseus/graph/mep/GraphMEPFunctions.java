package de.uniol.inf.is.odysseus.graph.mep;

import java.util.Arrays;
import java.util.List;

import org.graphstream.graph.Graph;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.graph.datatype.SDFGraphDatatype;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

/**
 * Provides the MEP functions for graphs (see {@link Graph} and {@link SDFGraphDatatype}) : <br />
 * <ul>
 * <li>{@link FindNodesByPatternMEP}</li>
 * </ul>
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class GraphMEPFunctions implements IFunctionProvider {

	/**
	 * Instances of the provided MEP functions.
	 */
	private static final IMepFunction<?>[] functions = new IMepFunction[] { new FindNodesByPatternMEP() };

	@Override
	public List<IMepFunction<?>> getFunctions() {
		return Arrays.asList(functions);
	}

}
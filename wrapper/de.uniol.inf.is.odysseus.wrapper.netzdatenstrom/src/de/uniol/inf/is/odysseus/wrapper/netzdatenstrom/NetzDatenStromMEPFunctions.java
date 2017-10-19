package de.uniol.inf.is.odysseus.wrapper.netzdatenstrom;

import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;
import de.uniol.inf.is.odysseus.wrapper.netzdatenstrom.topology.NDSAddSMsToGraph;
import de.uniol.inf.is.odysseus.wrapper.netzdatenstrom.topology.NDSGraphFromJSONString;

/**
 * Provides the MEP functions that closely related to the research project
 * "NetzDatenStrom". Provides MEP functions are: <br />
 * <ul>
 * <li>{@link NDSGraphFromJSONString}</li>
 * </ul>
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class NetzDatenStromMEPFunctions implements IFunctionProvider {

	/**
	 * Instances of the provided MEP functions.
	 */
	private static final IMepFunction<?>[] functions = new IMepFunction[] { new NDSGraphFromJSONString(), new NDSAddSMsToGraph() };

	@Override
	public List<IMepFunction<?>> getFunctions() {
		return Arrays.asList(functions);
	}

}
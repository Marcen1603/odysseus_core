package de.uniol.inf.is.odysseus.wrapper.iec60870.mep;

import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

/**
 * Provides MEP functions for data types of the OJ104 library used in the
 * IEC60870 protocol handler: <br />
 * <ul>
 * <li>{@link ToAsduFunction}</li>
 * </ul>
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class IEC60870MEPFunctions implements IFunctionProvider {

	/**
	 * Instances of the provided MEP functions.
	 */
	private static final IMepFunction<?>[] functions = new IMepFunction[] { new ToAsduFunction() };

	@Override
	public List<IMepFunction<?>> getFunctions() {
		return Arrays.asList(functions);
	}

}
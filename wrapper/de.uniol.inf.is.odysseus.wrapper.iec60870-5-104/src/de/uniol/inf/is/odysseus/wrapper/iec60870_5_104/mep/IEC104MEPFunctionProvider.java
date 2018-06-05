package de.uniol.inf.is.odysseus.wrapper.iec60870_5_104.mep;

import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

/**
 * Provides the MEP functions that depend on the IEC 60870-5-104 transport
 * handlers (and the j60870 library). Provides MEP functions are: <br />
 * <ul>
 * <li>{@link TimestampFromASDUFunction}</li>
 * <li>{@link TimestampToASDUFunction}</li>
 * </ul>
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class IEC104MEPFunctionProvider implements IFunctionProvider {

	/**
	 * Instances of the provided MEP functions.
	 */
	private static final IMepFunction<?>[] functions = new IMepFunction[] { new TimestampFromASDUFunction(),
			new TimestampToASDUFunction() };

	@Override
	public List<IMepFunction<?>> getFunctions() {
		return Arrays.asList(functions);
	}

}
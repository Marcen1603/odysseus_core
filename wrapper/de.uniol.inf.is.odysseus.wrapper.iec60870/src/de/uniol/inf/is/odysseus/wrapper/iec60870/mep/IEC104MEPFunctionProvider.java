package de.uniol.inf.is.odysseus.wrapper.iec60870.mep;

import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

/**
 * Provides the MEP functions that depend on the IEC 60870-5-104 protocol
 * handlers (and the oj104 library). Provided MEP functions are: <br />
 * <ul>
 * <li>{@link Get104ElementSequences}</li>
 * <li>{@link Get104TimeTag}</li>
 * <li>{@link Set104TimeTag}</li>
 * </ul>
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class IEC104MEPFunctionProvider implements IFunctionProvider {

	/**
	 * Instances of the provided MEP functions.
	 */
	private static final IMepFunction<?>[] functions = new IMepFunction[] { new Get104ElementSequences(), new Get104TimeTag(), new Set104TimeTag() };

	@Override
	public List<IMepFunction<?>> getFunctions() {
		return Arrays.asList(functions);
	}

}
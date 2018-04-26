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
 * <li>{@link Get104TimeTagClass}</li>
 * <li>{@link GetTimestampFrom104TimeTag}</li>
 * <li>{@link Is104TimeTagInvalid}</li>
 * <li>{@link Is104TimeTagSubstituted}</li>
 * <li>{@link Split104ElementSequence}</li>
 * </ul>
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class IEC104MEPFunctionProvider implements IFunctionProvider {

	/**
	 * Instances of the provided MEP functions.
	 */
	private static final IMepFunction<?>[] functions = new IMepFunction[] { new Get104ElementSequences(),
			new Split104ElementSequence(), new GetTimestampFrom104TimeTag(), new Is104TimeTagInvalid(),
			new Is104TimeTagSubstituted(), new Get104TimeTagClass() };

	@Override
	public List<IMepFunction<?>> getFunctions() {
		return Arrays.asList(functions);
	}

}
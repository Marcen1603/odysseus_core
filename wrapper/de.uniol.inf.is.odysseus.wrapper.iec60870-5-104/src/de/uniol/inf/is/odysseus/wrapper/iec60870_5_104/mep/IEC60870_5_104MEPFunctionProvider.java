package de.uniol.inf.is.odysseus.wrapper.iec60870_5_104.mep;

import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

// TODO javaDoc
public class IEC60870_5_104MEPFunctionProvider implements IFunctionProvider {

	private static final IFunction<?>[] functions = new IFunction[] { new TimestampFromASDUFunction(),
			new TimestampToASDUFunction() };

	@Override
	public List<IFunction<?>> getFunctions() {
		return Arrays.asList(functions);
	}

}
package de.uniol.inf.is.odysseus.text;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.server.mep.IFunctionProvider;
import de.uniol.inf.is.odysseus.text.function.ColognePhoneticFunction;
import de.uniol.inf.is.odysseus.text.function.LevensteinFunction;
import de.uniol.inf.is.odysseus.text.function.MetaphoneFunction;
import de.uniol.inf.is.odysseus.text.function.SoundexFunction;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TextFunctionProvider implements IFunctionProvider {
	private static final Logger LOG = LoggerFactory
			.getLogger(TextFunctionProvider.class);

	public TextFunctionProvider() {

	}

	@Override
	public List<IFunction<?>> getFunctions() {

		final List<IFunction<?>> functions = new ArrayList<IFunction<?>>();

		functions.add(new SoundexFunction());
		functions.add(new ColognePhoneticFunction());
		functions.add(new MetaphoneFunction());
		functions.add(new LevensteinFunction());
		TextFunctionProvider.LOG.info(String.format("Register functions: %s",
				functions));
		return functions;
	}

}

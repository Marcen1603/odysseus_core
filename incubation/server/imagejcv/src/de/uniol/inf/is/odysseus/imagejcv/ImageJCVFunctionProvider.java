package de.uniol.inf.is.odysseus.imagejcv;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

/**
 * @author Kristian Bruns
 */
public class ImageJCVFunctionProvider implements IFunctionProvider {
	private static final Logger LOG = LoggerFactory.getLogger(ImageJCVFunctionProvider.class);
	
	public ImageJCVFunctionProvider() {
	}
	
	@Override
	public List<IFunction<?>> getFunctions() {
		final List<IFunction<?>> functions = new ArrayList<IFunction<?>>();
		ImageJCVFunctionProvider.LOG.info(String.format("Register functions: %s", functions));
		return functions;
	}
}

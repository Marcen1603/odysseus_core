package de.uniol.inf.is.odysseus.imagejcv;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;
import de.uniol.inf.is.odysseus.imagejcv.functions.*;

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
		functions.add(new CountFacesFunction());
		functions.add(new FillImageFunction());
		functions.add(new GetAmountOfBlackFunction());
		functions.add(new GetCircleFunction());
		functions.add(new GetImageFunction());
		functions.add(new InverseImageFunction());
		functions.add(new ResizeImageFunction());
		functions.add(new RotateImageFunction());
		functions.add(new SetImageFunction());
		functions.add(new SubImageFunction());
		functions.add(new ThresholdFunction());
		functions.add(new ToImageFunction());
		functions.add(new ToImageMatrixFunction());
		functions.add(new ToMatrixFunction());
		ImageJCVFunctionProvider.LOG.info(String.format("Register functions: %s", functions));
		return functions;
	}
}

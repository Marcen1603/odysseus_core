package de.uniol.inf.is.odysseus.transformation.greedy.transformators.windowAO;

import java.util.Set;

import de.uniol.inf.is.odysseus.base.ITransformation;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.intervalapproach.window.SlidingElementWindowTIPO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowType;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.IPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TransformedPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.MetadataUpdatePO;

public class SlidingElementWindowTIPOTransformator implements IPOTransformator<WindowAO> {
	@Override
	public boolean canExecute(WindowAO windowAO, TransformationConfiguration config) {
		Set<String> metaTypes = config.getMetaTypes();
		WindowType windowType = windowAO.getWindowType();

		return (metaTypes.contains("de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval"))
				&& (windowType == WindowType.SLIDING_TUPLE_WINDOW || windowType == WindowType.JUMPING_TUPLE_WINDOW)
				&& !windowAO.isPartitioned();
	}

	@SuppressWarnings("unchecked")
	@Override
	public TransformedPO transform(WindowAO windowAO, TransformationConfiguration config, ITransformation transformation)
			throws TransformationException {
		SlidingElementWindowTIPO windowPO = new SlidingElementWindowTIPO(windowAO);
		windowPO.setOutputSchema(windowAO.getOutputSchema());
		MetadataUpdatePO mPO = WindowPOMetadata.createMetadata(windowPO, config);

		if (mPO == null) {
			return new TransformedPO(windowPO);
		}
		return new TransformedPO(windowPO, mPO);
	}

	@Override
	public int getPriority() {
		return 0;
	}
}

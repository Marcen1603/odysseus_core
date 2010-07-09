package de.uniol.inf.is.odysseus.parser.pql;

import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowType;
import de.uniol.inf.is.odysseus.parser.pql.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public class WindowAOBuilder extends AbstractOperatorBuilder {
	IParameter<Long> size = new DirectParameter<Long>("SIZE",
			REQUIREMENT.OPTIONAL);
	IParameter<Long> advance = new DirectParameter<Long>("ADVANCE",
			REQUIREMENT.OPTIONAL);
	IParameter<String> type = new DirectParameter<String>("TYPE",
			REQUIREMENT.MANDATORY);
	ListParameter<SDFAttribute> partition = new ListParameter<SDFAttribute>(
			"PARTITION", REQUIREMENT.OPTIONAL, new ResolvedSDFAttributeParameter("partition attribute",
					REQUIREMENT.MANDATORY));

	public WindowAOBuilder() {
		setParameters(advance, size, type, partition);
	}

	@Override
	protected ILogicalOperator createOperator(List<ILogicalOperator> inputOps) {
		checkInputSize(inputOps, 1);

		String windowTypeStr = type.getValue();
		WindowAO windowAO = new WindowAO();
		WindowType windowType;
		long windowAdvance = advance.hasValue() ? advance.getValue() : 1;
		if (windowTypeStr.toUpperCase().equals("UNBOUNDED")) {
			windowType = WindowType.UNBOUNDED;
		} else {
			if (windowTypeStr.toUpperCase().equals("TIME")) {
				if (windowAdvance == 1) {
					windowType = WindowType.SLIDING_TIME_WINDOW;
				} else {
					windowType = WindowType.JUMPING_TIME_WINDOW;
				}
			} else {
				if (windowTypeStr.toUpperCase().equals("TUPLE")) {
					if (windowAdvance == 1) {
						windowType = WindowType.SLIDING_TUPLE_WINDOW;
					} else {
						windowType = WindowType.JUMPING_TUPLE_WINDOW;
					}
				} else {
					throw new IllegalArgumentException("invalid window type: "
							+ windowTypeStr);
				}
				if (partition.hasValue()) {
					windowAO.setPartitionBy(partition.getValue());
				}

			}
			long windowSize = size.hasValue() ? size.getValue() : 1;
			windowAO.setWindowSize(windowSize);
			windowAO.setWindowAdvance(windowAdvance);
		}
		windowAO.setWindowType(windowType);

		return windowAO;
	}

}

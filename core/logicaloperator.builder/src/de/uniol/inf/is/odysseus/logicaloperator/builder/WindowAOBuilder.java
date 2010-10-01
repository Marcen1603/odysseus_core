package de.uniol.inf.is.odysseus.logicaloperator.builder;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public class WindowAOBuilder extends AbstractOperatorBuilder {
	private static final String TUPLE = "TUPLE";
	private static final String TIME = "TIME";
	private static final String UNBOUNDED = "UNBOUNDED";
	IParameter<Long> size = new DirectParameter<Long>("SIZE",
			REQUIREMENT.OPTIONAL);
	IParameter<Long> advance = new DirectParameter<Long>("ADVANCE",
			REQUIREMENT.OPTIONAL);
	IParameter<String> type = new DirectParameter<String>("TYPE",
			REQUIREMENT.MANDATORY);
	ListParameter<SDFAttribute> partition = new ListParameter<SDFAttribute>(
			"PARTITION", REQUIREMENT.OPTIONAL,
			new ResolvedSDFAttributeParameter("partition attribute",
					REQUIREMENT.MANDATORY));
	DirectParameter<Long> slide = new DirectParameter<Long>("SLIDE",
			REQUIREMENT.OPTIONAL);

	public WindowAOBuilder() {
		super(1, 1);
		setParameters(advance, size, type, partition, slide);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		String windowTypeStr = type.getValue();
		WindowAO windowAO = new WindowAO();
		WindowType windowType;
		long windowAdvance = advance.hasValue() ? advance.getValue() : 1;
		if (windowTypeStr.toUpperCase().equals(UNBOUNDED)) {
			windowType = WindowType.UNBOUNDED;
		} else {
			if (windowTypeStr.toUpperCase().equals(TIME)) {
				if (windowAdvance == 1) {
					windowType = WindowType.SLIDING_TIME_WINDOW;
				} else {
					windowType = WindowType.JUMPING_TIME_WINDOW;
				}
			} else {
				// internalValidation() ensures this has to be TUPLE
				if (windowAdvance == 1) {
					windowType = WindowType.SLIDING_TUPLE_WINDOW;
				} else {
					windowType = WindowType.JUMPING_TUPLE_WINDOW;
				}
				if (partition.hasValue()) {
					windowAO.setPartitionBy(partition.getValue());
				}

			}
			long windowSize = size.hasValue() ? size.getValue() : 1;
			windowAO.setWindowSize(windowSize);
			windowAO.setWindowAdvance(windowAdvance);
		}
		if (slide.hasValue()) {
			windowAO.setWindowSlide(slide.getValue());
		}
		windowAO.setWindowType(windowType);

		return windowAO;
	}

	@Override
	protected boolean internalValidation() {
		String windowTypeStr = type.getValue().toUpperCase();
		if (windowTypeStr.equals(TIME)) {
			if (partition.hasValue()) {
				addError(new IllegalParameterException(
						"can't use partition in time window"));
				return false;
			}
		} else {
			boolean isValid = true;
			if (windowTypeStr.equals(UNBOUNDED)) {
				if (size.hasValue()) {
					isValid = false;
					addError(new IllegalParameterException(
							"can't use size in unbounded window"));
				}
				if (advance.hasValue()) {
					isValid = false;
					addError(new IllegalParameterException(
							"can't use advance in unbounded window"));
				}
				if (partition.hasValue()) {
					isValid = false;
					addError(new IllegalParameterException(
							"can't use partition in unbounded window"));
				}
				if (slide.hasValue()) {
					isValid = false;
					addError(new IllegalParameterException(
							"can't use slide in unbounded window"));
				}
			} else {
				if (!windowTypeStr.equals(TUPLE)) {
					isValid = false;
					addError(new IllegalParameterException(
							"invalid window type: " + windowTypeStr));
				}
				if (slide.hasValue()) {
					isValid = false;
					addError(new IllegalParameterException(
							"can't use slide in tuple window"));
				}
			}
			return isValid;
		}
		return true;
	}
}

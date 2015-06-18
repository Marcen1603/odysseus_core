package de.uniol.inf.is.odysseus.multithreaded.interoperator.strategy;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.parameter.MultithreadedOperatorSettings;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.RangeFragmentAO;

public abstract class AbstractMultithreadedTransformationStrategy<T extends ILogicalOperator>
		implements IMultithreadedTransformationStrategy<T> {

	@SuppressWarnings("unchecked")
	public Class<T> getOperatorType() {
		ParameterizedType parameterizedType = (ParameterizedType) getClass()
				.getGenericSuperclass();
		return (Class<T>) parameterizedType.getActualTypeArguments()[0];
	}
	
	protected boolean areSettingsValid(MultithreadedOperatorSettings settingsForOperator){
		if (settingsForOperator.getDegreeOfParallelization() == 1){
			return false;
		}
		return true;
	}

	protected AbstractFragmentAO createFragmentAO(
			Class<? extends AbstractFragmentAO> fragmentClass,
			int degreeOfParallelization, String namePostfix,
			List<SDFAttribute> hashAttributes, String rangeAttributeString,
			List<String> rangeRanges) throws InstantiationException,
			IllegalAccessException {
		AbstractFragmentAO fragmentAO = fragmentClass.newInstance();
		fragmentAO.setNumberOfFragments(degreeOfParallelization);
		

		if (fragmentAO instanceof HashFragmentAO) {
			HashFragmentAO hashFragmentAO = (HashFragmentAO) fragmentAO;
			if (hashAttributes != null && !hashAttributes.isEmpty()) {
				hashFragmentAO.setAttributes(hashAttributes);
			} else {
				throw new IllegalArgumentException(
						"Attributes must not be null for creating HashFragment");
			}
		} else if (fragmentAO instanceof RangeFragmentAO) {
			RangeFragmentAO rangeFragmentAO = (RangeFragmentAO) fragmentAO;
			if (rangeAttributeString != null && rangeRanges != null
					&& !rangeAttributeString.isEmpty()
					&& !rangeRanges.isEmpty()) {
				rangeFragmentAO.setRanges(rangeRanges);
				rangeFragmentAO.setAttribute(rangeAttributeString);
			}
		}
		// set postfix
		fragmentAO.setName(fragmentAO.getName()+namePostfix);

		return fragmentAO;
	}

}

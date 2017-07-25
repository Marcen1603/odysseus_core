package de.uniol.inf.is.odysseus.admission.status.mep;

import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

/**
 * This class creates all functions, which the user can execute via PQL.
 */
public class AdmissionFunctionProvider implements IFunctionProvider {

	@Override
	public List<IMepFunction<?>> getFunctions() {
		List<IMepFunction<?>> list = Lists.newArrayList();
		list.add(new AddQueryToAdmissionFunction());
		list.add(new RemoveQueryFromAdmissionFunction());
		list.add(new RunLoadSheddingAdmissionFunction());
		list.add(new RollbackLoadSheddingAdmissionFunction());
		list.add(new ChooseLoadSheddingComponentFunction());
		list.add(new ChooseLoadSheddingGrowthFunction());
		list.add(new ChooseDefaultLoadSheddingFactorFunction());
		list.add(new MeasureStatusFunction());
		list.add(new ChooseLoadSheddingSelectionStrategy());
		return list;
	}

}

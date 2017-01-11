package mep;

import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

public class AdmissionFunctionProvider implements IFunctionProvider {

	@Override
	public List<IFunction<?>> getFunctions() {
		List<IFunction<?>> list = Lists.newArrayList();
		list.add(new AddQueryToAdmissionFunktion());
		list.add(new RemoveQueryFromAdmissionFunction());
		list.add(new RunLoadSheddingAdmissionFunction());
		list.add(new RollbackLoadSheddingAdmissionFunction());
		return list;
	}

}

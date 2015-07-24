package de.uniol.inf.is.odysseus.iql.odl.lookup;

import java.util.Collection;
import java.util.HashSet;

import javax.inject.Inject;

import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.iql.basic.lookup.AbstractIQLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.types.impl.useroperator.AbstractODLPO;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory;

public class ODLLookUp extends AbstractIQLLookUp<ODLTypeFactory>{

	@Inject
	public ODLLookUp(ODLTypeFactory typeFactory) {
		super(typeFactory);
	}

	public Collection<JvmOperation> getOnMethods() {
		JvmTypeReference typeRef = typeFactory.getTypeRef(AbstractODLPO.class);
		Collection<JvmOperation> result = new HashSet<>();
		for (JvmOperation op : super.getProtectedMethods(typeRef)) {
			if (op.getSimpleName().startsWith("on")) {
				result.add(op);
			}
		}
		return result;
	}

}

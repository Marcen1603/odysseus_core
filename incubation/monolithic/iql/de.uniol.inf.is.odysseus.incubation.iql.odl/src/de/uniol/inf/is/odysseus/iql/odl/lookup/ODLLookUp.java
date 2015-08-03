package de.uniol.inf.is.odysseus.iql.odl.lookup;

import java.util.Collection;
import java.util.HashSet;

import javax.inject.Inject;

import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.iql.basic.lookup.AbstractIQLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.types.impl.useroperator.AbstractODLPO;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeOperatorsFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeUtils;

public class ODLLookUp extends AbstractIQLLookUp<ODLTypeFactory, ODLTypeOperatorsFactory, ODLTypeUtils>{

	@Inject
	public ODLLookUp(ODLTypeFactory typeFactory, ODLTypeOperatorsFactory typeOperatorsFactory,ODLTypeUtils typeUtils) {
		super(typeFactory, typeOperatorsFactory, typeUtils);
	}

	public Collection<JvmOperation> getOnMethods() {
		JvmTypeReference typeRef = typeUtils.createTypeRef(AbstractODLPO.class, typeFactory.getSystemResourceSet());
		Collection<JvmOperation> result = new HashSet<>();
		for (JvmOperation op : super.getProtectedMethods(typeRef, false)) {
			if (op.getSimpleName().startsWith("on")) {
				result.add(op);
			}
		}
		return result;
	}

}

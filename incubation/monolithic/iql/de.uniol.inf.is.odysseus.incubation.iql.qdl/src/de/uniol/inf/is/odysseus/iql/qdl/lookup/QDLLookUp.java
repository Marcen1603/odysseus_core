package de.uniol.inf.is.odysseus.iql.qdl.lookup;

import java.util.Collection;
import java.util.HashSet;

import javax.inject.Inject;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.iql.basic.lookup.AbstractIQLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeFactory;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeOperatorsFactory;

public class QDLLookUp extends AbstractIQLLookUp<QDLTypeFactory, QDLTypeOperatorsFactory>{


	@Inject
	public QDLLookUp(QDLTypeFactory typeFactory, QDLTypeOperatorsFactory typeOperatorsFactory) {
		super(typeFactory, typeOperatorsFactory);
	}

	public Collection<JvmType> getTypesWihtoutOperators(Resource context) {
		Collection<JvmType> types = super.getAllTypes(context);
		Collection<JvmType> result = new HashSet<>();
		JvmTypeReference logicalOp = typeFactory.getTypeRef(ILogicalOperator.class);
		for (JvmType type : types) {
			if (isAssignable(logicalOp, type)) {
				result.add(type);
			}
		}
		return result;
	}
}

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
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeUtils;

public class QDLLookUp extends AbstractIQLLookUp<QDLTypeFactory, QDLTypeOperatorsFactory, QDLTypeUtils>{


	@Inject
	public QDLLookUp(QDLTypeFactory typeFactory, QDLTypeOperatorsFactory typeOperatorsFactory, QDLTypeUtils typeUtils) {
		super(typeFactory, typeOperatorsFactory, typeUtils);
	}

	public Collection<JvmType> getTypesWihtoutOperators(Collection<String> usedNamespaces, Resource context) {
		Collection<JvmType> types = super.getAllTypes(usedNamespaces, context);
		Collection<JvmType> result = new HashSet<>();
		JvmTypeReference logicalOp = typeUtils.createTypeRef(ILogicalOperator.class, typeFactory.getSystemResourceSet());
		for (JvmType type : types) {
			if (isAssignable(logicalOp, type)) {
				result.add(type);
			}
		}
		return result;
	}
}

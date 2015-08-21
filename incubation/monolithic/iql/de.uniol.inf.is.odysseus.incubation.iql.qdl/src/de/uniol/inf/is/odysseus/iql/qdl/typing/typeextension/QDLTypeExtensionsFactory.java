package de.uniol.inf.is.odysseus.iql.qdl.typing.typeextension;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.iql.basic.typing.extension.AbstractIQLTypeExtensionsFactory;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.types.impl.query.AbstractQDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.typing.factory.IQDLTypeFactory;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;

@Singleton
public class QDLTypeExtensionsFactory extends AbstractIQLTypeExtensionsFactory<IQDLTypeFactory, IQDLTypeUtils> implements IQDLTypeExtensionsFactory {

	@Inject
	public QDLTypeExtensionsFactory(IQDLTypeFactory typeFactory, IQDLTypeUtils typeUtils) {
		super(typeFactory, typeUtils);
	}
	
	
	@Override
	protected JvmTypeReference getExtendedType(JvmDeclaredType declaredType) {
		if (declaredType instanceof QDLQuery) {
			return typeUtils.createTypeRef(AbstractQDLQuery.class, typeFactory.getSystemResourceSet());
		} else {
			return declaredType.getExtendedClass();
		}
	}

}

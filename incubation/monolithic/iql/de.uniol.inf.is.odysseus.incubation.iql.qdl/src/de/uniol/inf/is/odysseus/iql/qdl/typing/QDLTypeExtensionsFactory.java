package de.uniol.inf.is.odysseus.iql.qdl.typing;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.iql.basic.typing.extension.AbstractIQLTypeExtensionsFactory;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.types.impl.query.AbstractQDLQuery;

@Singleton
public class QDLTypeExtensionsFactory extends AbstractIQLTypeExtensionsFactory<QDLTypeFactory, QDLTypeUtils> {

	@Inject
	public QDLTypeExtensionsFactory(QDLTypeFactory typeFactory, QDLTypeUtils typeUtils) {
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

package de.uniol.inf.is.odysseus.iql.qdl.typing.typeextension;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.iql.basic.typing.extension.AbstractIQLTypeExtensionsDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.types.impl.query.AbstractQDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;

@Singleton
public class QDLTypeExtensionsDictionary extends AbstractIQLTypeExtensionsDictionary<IQDLTypeDictionary, IQDLTypeUtils> implements IQDLTypeExtensionsDictionary {

	@Inject
	public QDLTypeExtensionsDictionary(IQDLTypeDictionary typeDictionary, IQDLTypeUtils typeUtils) {
		super(typeDictionary, typeUtils);
	}
	
	
	@Override
	protected JvmTypeReference getExtendedType(JvmDeclaredType declaredType) {
		if (declaredType instanceof QDLQuery) {
			return typeUtils.createTypeRef(AbstractQDLQuery.class, typeDictionary.getSystemResourceSet());
		} else {
			return declaredType.getExtendedClass();
		}
	}

}

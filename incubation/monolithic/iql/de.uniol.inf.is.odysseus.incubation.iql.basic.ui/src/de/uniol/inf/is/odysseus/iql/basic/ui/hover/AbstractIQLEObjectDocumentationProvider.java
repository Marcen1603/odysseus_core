package de.uniol.inf.is.odysseus.iql.basic.ui.hover;


import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.documentation.impl.MultiLineCommentDocumentationProvider;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.IIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;


public abstract class AbstractIQLEObjectDocumentationProvider<F extends IIQLTypeDictionary, U extends IIQLTypeUtils> extends MultiLineCommentDocumentationProvider {

	protected U typeUtils;
	protected F typeDictionary;

	public AbstractIQLEObjectDocumentationProvider(F typeDictionary, U typeUtils) {
		this.typeDictionary = typeDictionary;
		this.typeUtils = typeUtils;
	}
	
	@Override
	public String getDocumentation(EObject object) {
		if (object instanceof IQLArgumentsMapKeyValue) {
			return getDocumentationIQLArgumentsMapKeyValue((IQLArgumentsMapKeyValue) object);
		} else if (object instanceof JvmField) {
			return getDocumentationJvmField((JvmField) object);
		} else if (object instanceof JvmOperation) {
			return getDocumentationJvmOperation((JvmOperation) object);
		} else if (object instanceof JvmDeclaredType) {
			return getDocumentationJvmDeclaredType((JvmDeclaredType) object);
		} else if (object instanceof IQLNewExpression) {
			return getDocumentationJvmDeclaredType((IQLNewExpression) object);
		}else {
			return super.getDocumentation(object);
		}
	}
	
	protected String getDocumentationJvmDeclaredType(IQLNewExpression expr) {		
		return "";
	}
	
	protected String getDocumentationJvmDeclaredType(JvmDeclaredType type) {		
		return "";
	}
	
	
	protected String getDocumentationJvmField(JvmField attr) {		
		return "";
	}
	
	protected String getDocumentationJvmOperation(JvmOperation method) {		
		return "";
	}
	
	protected String getDocumentationIQLArgumentsMapKeyValue(IQLArgumentsMapKeyValue keyValue) {		
		return "";
	}
}

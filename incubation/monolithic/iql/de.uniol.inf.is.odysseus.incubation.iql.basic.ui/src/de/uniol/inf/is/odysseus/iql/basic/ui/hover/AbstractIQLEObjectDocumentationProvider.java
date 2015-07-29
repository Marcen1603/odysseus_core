package de.uniol.inf.is.odysseus.iql.basic.ui.hover;


import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.documentation.impl.MultiLineCommentDocumentationProvider;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.IIQLTypeFactory;


public abstract class AbstractIQLEObjectDocumentationProvider<F extends IIQLTypeFactory> extends MultiLineCommentDocumentationProvider {

	protected F typeFactory;
	
	public AbstractIQLEObjectDocumentationProvider(F typeFactory) {
		this.typeFactory = typeFactory;
	}
	
	@Override
	public String getDocumentation(EObject object) {
		if (object instanceof IQLArgumentsMapKeyValue) {
			return getDocumentationIQLArgumentsMapKeyValue((IQLArgumentsMapKeyValue) object);
		} else if (object instanceof JvmField) {
			return getDocumentationJvmField((JvmField) object);
		} else if (object instanceof JvmOperation) {
			return getDocumentationJvmOperation((JvmOperation) object);
		} else if (object instanceof JvmGenericType) {
			return getDocumentationJvmGenericType((JvmGenericType) object);
		}else {
			return super.getDocumentation(object);
		}
	}
	
	protected String getDocumentationJvmGenericType(JvmGenericType type) {		
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

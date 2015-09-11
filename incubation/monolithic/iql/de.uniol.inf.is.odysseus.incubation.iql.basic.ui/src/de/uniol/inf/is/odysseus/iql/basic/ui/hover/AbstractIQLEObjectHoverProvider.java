package de.uniol.inf.is.odysseus.iql.basic.ui.hover;




import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.ui.editor.hover.html.DefaultEObjectHoverProvider;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;

public abstract class AbstractIQLEObjectHoverProvider<U extends IIQLTypeUtils, L extends IIQLLookUp> extends DefaultEObjectHoverProvider {

	protected L lookUp;
	protected U typeUtils;

	
	public AbstractIQLEObjectHoverProvider(U typeUtils, L lookUp) {
		this.typeUtils = typeUtils;
		this.lookUp = lookUp;
	}
	
	@Override
	public String getFirstLine(EObject object) {
		if (object instanceof JvmField) {
			return getFirstLine((JvmField) object);
		} else if (object instanceof JvmOperation) {
			return getFirstLine((JvmOperation) object);
		} else if (object instanceof IQLArgumentsMapKeyValue) {
			return getFirstLine((IQLArgumentsMapKeyValue) object);
		} else if (object instanceof JvmType) {
			return getFirstLine((JvmType) object);
		}else {
			return super.getFirstLine(object);
		}
	}
	
	protected String getFirstLine(JvmType type) {
		return typeUtils.getLongName(type, false);
	}
	
	protected String getFirstLine(IQLArgumentsMapKeyValue keyValue) {		
		if (keyValue.getKey() instanceof JvmField) {
			return keyValue.getKey().getSimpleName() + " : "+toString(((JvmField) keyValue.getKey()).getType());
		} else {
			JvmOperation op = (JvmOperation) keyValue.getKey();
			return keyValue.getKey().getSimpleName() + " : "+toString(op.getParameters().get(0).getParameterType());
		}
	}
	
	protected String getFirstLine(JvmField attr) {
		return attr.getSimpleName()+ " : "+toString(attr.getType());
	}

	
	protected String getFirstLine(JvmOperation op) {
		StringBuilder b = new StringBuilder();
		b.append(op.getSimpleName());
		b.append("(");
		if (op.getParameters() != null) {
			int i = 0; 
			for (JvmFormalParameter parameter : op.getParameters()) {
				if (i > 0 ){
					b.append(", ");
				}
				i++;
				b.append(toString(parameter.getParameterType()) +" "+parameter.getName());
			}
		}
		b.append(")");
		if (op.getReturnType() != null) {
			b.append(" : "+toString(op.getReturnType()));
		}
		return b.toString();
	}
	
	private String toString(JvmTypeReference typeRef) {
		StringBuilder b = new StringBuilder();
		b.append(typeUtils.getShortName(typeRef, true));
		return b.toString();
	}

}

package de.uniol.inf.is.odysseus.iql.basic.ui.hover;


import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.ui.editor.hover.html.DefaultEObjectHoverProvider;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement;
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;

public abstract class AbstractIQLEObjectHoverProvider<U extends IIQLTypeUtils, L extends IIQLLookUp> extends DefaultEObjectHoverProvider {

	protected L lookUp;
	protected U typeUtils;

	@Inject
	private IQLQualifiedNameConverter converter;
	
	public AbstractIQLEObjectHoverProvider(U typeUtils, L lookUp) {
		this.typeUtils = typeUtils;
		this.lookUp = lookUp;
	}
	
	@Override
	public String getFirstLine(EObject object) {
		if (object instanceof JvmField) {
			return getFirstLineJvmField((JvmField) object);
		} else if (object instanceof IQLArgumentsMapKeyValue) {
			return getFirstLineIQLArgumentsMapKeyValue((IQLArgumentsMapKeyValue) object);
		}else {
			return super.getFirstLine(object);
		}
	}
	
	protected String getFirstLineIQLArgumentsMapKeyValue(IQLArgumentsMapKeyValue keyValue) {
		JvmTypeReference typeRef = null;
		IQLVariableStatement stmt = EcoreUtil2.getContainerOfType(keyValue, IQLVariableStatement.class);
		IQLNewExpression expr = EcoreUtil2.getContainerOfType(keyValue, IQLNewExpression.class);
		if (stmt != null) {
			typeRef = ((IQLVariableDeclaration)stmt.getVar()).getRef();
		} else {
			typeRef = expr.getRef();
		}
		Map<String, JvmTypeReference> properties = lookUp.getProperties(typeRef);
		
		for (Entry<String, JvmTypeReference> entry : properties.entrySet()) {
			if (entry.getKey().equalsIgnoreCase(keyValue.getKey())) {
				return keyValue.getKey() + " : "+toString(entry.getValue());
			}
		}			
		return keyValue.getKey() + " : "+"";
	}
	
	protected String getFirstLineJvmField(JvmField attr) {
		return attr.getSimpleName()+ " : "+toString(attr.getType());
	}
	
	private String toString(JvmTypeReference typeRef) {
		StringBuilder b = new StringBuilder();
		b.append(converter.toDisplayString(typeUtils.getShortName(typeRef, true)));
		return b.toString();
	}

}

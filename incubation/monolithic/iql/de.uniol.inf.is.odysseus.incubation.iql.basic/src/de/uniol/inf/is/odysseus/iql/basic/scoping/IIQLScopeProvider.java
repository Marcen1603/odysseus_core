package de.uniol.inf.is.odysseus.iql.basic.scoping;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmOperation;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;

public interface IIQLScopeProvider {

	Collection<JvmIdentifiableElement> getScopeIQLTerminalExpressionVariable(EObject expr);

	Collection<JvmOperation> getScopeIQLTerminalExpressionMethod(EObject expr);

	Collection<JvmOperation> getScopeIQLMethodSelection(IQLMemberSelectionExpression expr);

	Collection<JvmField> getScopeIQLAttributeSelection(IQLMemberSelectionExpression expr);

}

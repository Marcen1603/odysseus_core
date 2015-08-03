package de.uniol.inf.is.odysseus.iql.odl.scoping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.scoping.AbstractIQLScopeProvider;
import de.uniol.inf.is.odysseus.iql.odl.lookup.ODLLookUp;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLExpressionParser;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeUtils;


public class ODLScopeProvider extends AbstractIQLScopeProvider<ODLTypeFactory, ODLLookUp, ODLExpressionParser, ODLTypeUtils>{

	@Inject
	public ODLScopeProvider(ODLTypeFactory typeFactory, ODLLookUp lookUp,ODLExpressionParser exprParser, ODLTypeUtils typeUtils) {
		super(typeFactory, lookUp, exprParser, typeUtils);
	}
	
	@Override
	public Collection<JvmIdentifiableElement> getScopeIQLTerminalExpressionVariable(EObject expr) {
		JvmGenericType type = EcoreUtil2.getContainerOfType(expr, JvmGenericType.class);
		if (type instanceof ODLOperator) {
			ODLOperator op = (ODLOperator) type;

			Collection<JvmIdentifiableElement> vars = new ArrayList<>();
			EObject container = expr;
			while (container != null && !(container instanceof JvmGenericType)) {
				vars.addAll(EcoreUtil2.getAllContentsOfType(container, IQLVariableDeclaration.class));
				vars.addAll(EcoreUtil2.getAllContentsOfType(container, JvmFormalParameter.class));
				container = container.eContainer();
			}
			Collection<JvmTypeReference> importedTypes = typeFactory.getImportedTypes(expr);
			vars.addAll(lookUp.getPublicAttributes(typeUtils.createTypeRef(op), importedTypes, true));
			vars.addAll(lookUp.getProtectedAttributes(typeUtils.createTypeRef(op), importedTypes, true));
			return vars;	
		} else {
			return super.getScopeIQLTerminalExpressionVariable(expr);
		}
	}
	
	@Override
	public Collection<JvmOperation> getScopeTerminalExpressionMethod(EObject expr) {
		JvmGenericType type = EcoreUtil2.getContainerOfType(expr, JvmGenericType.class);
		if (type instanceof ODLOperator) {
			ODLOperator op = (ODLOperator) type;
			Collection<JvmOperation> methods = new HashSet<>();
			Collection<JvmTypeReference> importedTypes = typeFactory.getImportedTypes(expr);
			methods.addAll(lookUp.getPublicMethods(typeUtils.createTypeRef(op), importedTypes,true));
			methods.addAll(lookUp.getProtectedMethods(typeUtils.createTypeRef(op), importedTypes,true));
			return methods;	
		} else {
			return super.getScopeTerminalExpressionMethod(expr);
		}
	}

}

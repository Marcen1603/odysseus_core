package de.uniol.inf.is.odysseus.iql.qdl.scoping;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.scoping.AbstractIQLScopeProvider;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.QDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLExpressionParser;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeFactory;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeUtils;


public class QDLScopeProvider extends AbstractIQLScopeProvider<QDLTypeFactory, QDLLookUp, QDLExpressionParser, QDLTypeUtils> {

	@Inject
	public QDLScopeProvider(QDLTypeFactory typeFactory, QDLLookUp lookUp,QDLExpressionParser exprParser, QDLTypeUtils typeUtils) {
		super(typeFactory, lookUp, exprParser, typeUtils);
	}

	@Override	
	public Collection<JvmIdentifiableElement> getScopeIQLTerminalExpressionVariable(EObject expr) {
		JvmGenericType type = EcoreUtil2.getContainerOfType(expr, JvmGenericType.class);
		if (type instanceof QDLQuery) {
			QDLQuery query = (QDLQuery) type;
			Collection<JvmIdentifiableElement> vars = new ArrayList<>();
			EObject container = expr;
			while (container != null && !(container instanceof JvmGenericType)) {
				vars.addAll(EcoreUtil2.getAllContentsOfType(container, IQLVariableDeclaration.class));
				vars.addAll(EcoreUtil2.getAllContentsOfType(container, JvmFormalParameter.class));
				container = container.eContainer();
			}
			Collection<JvmTypeReference> importedTypes = typeFactory.getImportedTypes(expr);
			vars.addAll(lookUp.getPublicAttributes(typeUtils.createTypeRef(query), importedTypes, true));
			vars.addAll(lookUp.getProtectedAttributes(typeUtils.createTypeRef(query), importedTypes, true));

			for (IQLClass source : typeFactory.getSourceTypes()) {
				for (JvmField attr : lookUp.getPublicAttributes(typeUtils.createTypeRef(source), false)) {
					if (attr.getSimpleName().equalsIgnoreCase(source.getSimpleName())) {
						vars.add(attr);
					}
				}
			}
			return vars;
		} else {
			return super.getScopeIQLTerminalExpressionVariable(expr);
		}	
	}
	
}

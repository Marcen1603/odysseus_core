package de.uniol.inf.is.odysseus.iql.qdl.scoping;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.scoping.AbstractIQLScopeProvider;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.QDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLExpressionParser;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeFactory;


public class QDLScopeProvider extends AbstractIQLScopeProvider<QDLTypeFactory, QDLLookUp, QDLExpressionParser> {

	@Inject
	public QDLScopeProvider(QDLTypeFactory typeFactory, QDLLookUp lookUp,QDLExpressionParser exprParser) {
		super(typeFactory, lookUp, exprParser);
	}

	@Override
	public Collection<JvmIdentifiableElement> getScopeIQLTerminalExpressionVariable(EObject expr) {
		Collection<JvmIdentifiableElement> vars = super.getScopeIQLTerminalExpressionVariable(expr);
		for (IQLClass source : typeFactory.getSourceTypes()) {
			for (JvmField attr : lookUp.getPublicAttributes(typeFactory.getTypeRef(source), false)) {
				if (attr.getSimpleName().equalsIgnoreCase(source.getSimpleName())) {
					vars.add(attr);
				}
			}
		}

		return vars;	
	}
}

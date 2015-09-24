package de.uniol.inf.is.odysseus.iql.odl.ui.highlighting;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightedPositionAcceptor;

import de.uniol.inf.is.odysseus.iql.basic.ui.highlighting.AbstractIQLHighlightingConfiguration;
import de.uniol.inf.is.odysseus.iql.basic.ui.highlighting.AbstractSemanticHighlightingCalculator;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter;
import de.uniol.inf.is.odysseus.iql.odl.services.ODLGrammarAccess;


public class ODLSemanticHighlightingCalculator extends AbstractSemanticHighlightingCalculator {

	@Inject
	protected ODLGrammarAccess odlGrammarAccess;
	
	@Override
	protected boolean highlightElement(EObject object, IHighlightedPositionAcceptor acceptor) {
		if (object instanceof ODLParameter) {
			return highlightElement((ODLParameter)object, acceptor);
        } else {
        	return super.highlightElement(object, acceptor);
        }	
	}
	
	protected boolean highlightElement(ODLParameter object, IHighlightedPositionAcceptor acceptor) {
        INode node = NodeModelUtils.getNode(object);
		highlightElement(node,  odlGrammarAccess.getODLParameterAccess().getSimpleNameIDTerminalRuleCall_5_0(), acceptor, AbstractIQLHighlightingConfiguration.ATTRIBUTE);
		return false;
	}
}

package de.uniol.inf.is.odysseus.iql.basic.ui.highlighting;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultSemanticHighlightingCalculator;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightedPositionAcceptor;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMember;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMetadata;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaStatement;

public class AbstractSemanticHighlightingCalculator extends	DefaultSemanticHighlightingCalculator {

	@Override
	protected boolean highlightElement(EObject object, IHighlightedPositionAcceptor acceptor) {
        INode node = NodeModelUtils.getNode(object);
		if (object instanceof IQLJavaStatement) {
            acceptor.addPosition(node.getOffset(), node.getLength(), AbstractIQLHighlightingConfiguration.JAVA_ID);
        } else if (object instanceof IQLJavaMember) {
            acceptor.addPosition(node.getOffset(), node.getLength(), AbstractIQLHighlightingConfiguration.JAVA_ID);
        } else if (object instanceof IQLJavaMetadata) {
            acceptor.addPosition(node.getOffset(), node.getLength(), AbstractIQLHighlightingConfiguration.JAVA_ID);
        } else {
        	return false;
        }
		return true;
	}
}

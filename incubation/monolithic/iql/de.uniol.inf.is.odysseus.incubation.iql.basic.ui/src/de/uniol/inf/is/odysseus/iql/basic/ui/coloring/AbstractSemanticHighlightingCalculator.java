package de.uniol.inf.is.odysseus.iql.basic.ui.coloring;

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultSemanticHighlightingCalculator;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightedPositionAcceptor;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaKeywords;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMember;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMetadata;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaStatement;

public class AbstractSemanticHighlightingCalculator extends	DefaultSemanticHighlightingCalculator {

	public void provideHighlightingFor(XtextResource resource,	IHighlightedPositionAcceptor acceptor) {
		
		Iterator<EObject> iter = EcoreUtil2.getAllContents(resource, true);
		while (iter.hasNext()) {
	        EObject current = iter.next();
	        INode node = NodeModelUtils.getNode(current);
	        if (current instanceof IQLJavaStatement) {
                acceptor.addPosition(node.getOffset(), node.getLength(), AbstractIQLHighlightingConfiguration.JAVA_ID);
	        } else if (current instanceof IQLJavaMember) {
                acceptor.addPosition(node.getOffset(), node.getLength(), AbstractIQLHighlightingConfiguration.JAVA_ID);
	        } else if (current instanceof IQLJavaMetadata) {
                acceptor.addPosition(node.getOffset(), node.getLength(), AbstractIQLHighlightingConfiguration.JAVA_ID);
	        } else if (current instanceof IQLJavaKeywords) {
                acceptor.addPosition(node.getOffset(), node.getLength(), AbstractIQLHighlightingConfiguration.JAVA_KEYWORDS_ID);
	        }
		}  

	}
}

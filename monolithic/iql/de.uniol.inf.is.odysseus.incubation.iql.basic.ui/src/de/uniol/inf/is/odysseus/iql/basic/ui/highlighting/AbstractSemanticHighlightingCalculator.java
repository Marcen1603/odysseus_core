package de.uniol.inf.is.odysseus.iql.basic.ui.highlighting;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.CrossReference;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmPrimitiveType;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultSemanticHighlightingCalculator;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightedPositionAcceptor;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayType;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMember;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMetadata;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJvmElementCallExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelection;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSimpleTypeRef;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.services.BasicIQLGrammarAccess;

public class AbstractSemanticHighlightingCalculator extends	DefaultSemanticHighlightingCalculator {
	
	@Inject
	protected BasicIQLGrammarAccess basicIQLGrammarAccess;
	
	
	
	
	@Override
	protected boolean highlightElement(EObject object, IHighlightedPositionAcceptor acceptor) {
		if (object instanceof IQLJavaStatement) {
			return highlightElement((IQLJavaStatement)object, acceptor);
        } else if (object instanceof IQLJavaMember) {
        	return highlightElement((IQLJavaMember)object, acceptor);
        } else if (object instanceof IQLJavaMetadata) {
        	return highlightElement((IQLJavaMetadata)object, acceptor);
        } else if (object instanceof IQLAttribute) {
        	return highlightElement((IQLAttribute)object, acceptor);
        } else if (object instanceof JvmFormalParameter) {
        	return highlightElement((JvmFormalParameter)object, acceptor);
        }  else if (object instanceof IQLVariableDeclaration) {
        	return highlightElement((IQLVariableDeclaration)object, acceptor);
        } else if (object instanceof IQLJvmElementCallExpression) {
        	return highlightElement((IQLJvmElementCallExpression)object, acceptor);
        } else if (object instanceof IQLMemberSelection) {
        	return highlightElement((IQLMemberSelection)object, acceptor);
        } else if (object instanceof IQLSimpleTypeRef) {
        	return highlightElement((IQLSimpleTypeRef)object, acceptor);
        } else if (object instanceof IQLArrayType) {
        	return highlightElement((IQLArrayType)object, acceptor);
        } else {
        	return false;
        }
	}
	
	protected boolean highlightElement(IQLJavaStatement object, IHighlightedPositionAcceptor acceptor) {
        INode node = NodeModelUtils.getNode(object);
		acceptor.addPosition(node.getOffset(), node.getLength(), AbstractIQLHighlightingConfiguration.JAVA_ID);
		return true;
	}
	
	protected boolean highlightElement(IQLJavaMember object, IHighlightedPositionAcceptor acceptor) {
        INode node = NodeModelUtils.getNode(object);
		acceptor.addPosition(node.getOffset(), node.getLength(), AbstractIQLHighlightingConfiguration.JAVA_ID);
		return true;
	}
	
	protected boolean highlightElement(IQLJavaMetadata object, IHighlightedPositionAcceptor acceptor) {
        INode node = NodeModelUtils.getNode(object);
		acceptor.addPosition(node.getOffset(), node.getLength(), AbstractIQLHighlightingConfiguration.JAVA_ID);
		return true;
	}
	
	protected boolean highlightElement(IQLAttribute object, IHighlightedPositionAcceptor acceptor) {
        INode node = NodeModelUtils.getNode(object);
		highlightElement(node,  basicIQLGrammarAccess.getIQLAttributeAccess().getSimpleNameIDTerminalRuleCall_2_0(), acceptor, AbstractIQLHighlightingConfiguration.ATTRIBUTE);
		return false;
	}
	
	protected boolean highlightElement(JvmFormalParameter object, IHighlightedPositionAcceptor acceptor) {
        INode node = NodeModelUtils.getNode(object);
		highlightElement(node, basicIQLGrammarAccess.getJvmFormalParameterAccess().getNameIDTerminalRuleCall_1_0(),acceptor, AbstractIQLHighlightingConfiguration.VARIABLE);
		return false;
	}
	
	protected boolean highlightElement(IQLVariableDeclaration object, IHighlightedPositionAcceptor acceptor) {
        INode node = NodeModelUtils.getNode(object);
		highlightElement(node, basicIQLGrammarAccess.getIQLVariableDeclarationAccess().getNameIDTerminalRuleCall_2_0(),acceptor, AbstractIQLHighlightingConfiguration.VARIABLE);
		return false;
	}
	
	protected boolean highlightElement(IQLSimpleTypeRef object, IHighlightedPositionAcceptor acceptor) {
		if (object.getType() instanceof JvmPrimitiveType) {
    		INode node = NodeModelUtils.getNode(object);
    		highlightElement(node, basicIQLGrammarAccess.getIQLSimpleTypeRefAccess().getTypeJvmTypeCrossReference_1_0(),acceptor, AbstractIQLHighlightingConfiguration.KEYWORD_ID);
        }
        return false;
	}
	
	protected boolean highlightElement(IQLArrayType object, IHighlightedPositionAcceptor acceptor) {
        if (object.getComponentType() instanceof JvmPrimitiveType) {
    		INode node = NodeModelUtils.getNode(object);
    		highlightElement(node, basicIQLGrammarAccess.getIQLArrayTypeAccess().getComponentTypeJvmTypeCrossReference_1_0(),acceptor, AbstractIQLHighlightingConfiguration.KEYWORD_ID);
        }
        return false;
	}
	
	
	protected boolean highlightElement(IQLMemberSelection object, IHighlightedPositionAcceptor acceptor) {
		if (object.getMember() instanceof JvmField) {
	        INode node = NodeModelUtils.getNode(object);
			highlightElement(node, basicIQLGrammarAccess.getIQLMemberSelectionAccess().getMemberJvmMemberCrossReference_0_0(),acceptor, AbstractIQLHighlightingConfiguration.ATTRIBUTE);
		} 
		return false;
	}
	
	protected boolean highlightElement(IQLJvmElementCallExpression object, IHighlightedPositionAcceptor acceptor) {
		if (object.getElement() instanceof JvmField) {
	        INode node = NodeModelUtils.getNode(object);
			highlightElement(node, basicIQLGrammarAccess.getIQLOtherExpressionsAccess().getElementJvmIdentifiableElementCrossReference_0_1_0(),acceptor, AbstractIQLHighlightingConfiguration.ATTRIBUTE);
		} else if (object.getElement() instanceof JvmFormalParameter) {
	        INode node = NodeModelUtils.getNode(object);
			highlightElement(node, basicIQLGrammarAccess.getIQLOtherExpressionsAccess().getElementJvmIdentifiableElementCrossReference_0_1_0(),acceptor, AbstractIQLHighlightingConfiguration.VARIABLE);
		} else if (object.getElement() instanceof IQLVariableDeclaration) {
	        INode node = NodeModelUtils.getNode(object);
			highlightElement(node, basicIQLGrammarAccess.getIQLOtherExpressionsAccess().getElementJvmIdentifiableElementCrossReference_0_1_0(),acceptor, AbstractIQLHighlightingConfiguration.VARIABLE);
		}
		return false;
	}
	
	protected void highlightElement(INode rootNode, CrossReference ruleCall, IHighlightedPositionAcceptor acceptor, String color) {
		for (INode node : rootNode.getAsTreeIterable()) {
			if (node.getGrammarElement() == ruleCall) {
				acceptor.addPosition(node.getOffset(), node.getLength(), color);
			}
		}
	}
		
	protected void highlightElement(INode rootNode, RuleCall ruleCall, IHighlightedPositionAcceptor acceptor, String color) {
		for (INode node : rootNode.getAsTreeIterable()) {
			if (node.getGrammarElement() == ruleCall) {
				acceptor.addPosition(node.getOffset(), node.getLength(), color);
			}
		}
	}
}

package de.uniol.inf.is.odysseus.pqlhack.parser.visitor;

import de.uniol.inf.is.odysseus.pqlhack.parser.ASTAccessOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTAlgebraOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTAndPredicate;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTBasicPredicate;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTCompareOperator;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTDefaultPredictionDefinition;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTExpression;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTFunctionExpression;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTFunctionName;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTJoinOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTLogicalPlan;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTNotPredicate;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTNumber;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTOrPredicate;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTPredicate;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTPredictionDefinition;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTPredictionFunctionDefinition;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTPredictionOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTProjectionIdentifier;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTProjectionOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTRelationalJoinOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTRelationalNestOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTRelationalProjectionOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTRelationalSelectionOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTRelationalUnnestOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTSelectionOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTSimplePredicate;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTSimpleToken;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTSlidingTimeWindow;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTString;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTTimestampAttribute;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTWindowOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ProceduralExpressionParserVisitor;
import de.uniol.inf.is.odysseus.pqlhack.parser.SimpleNode;

public class DefaultVisitor implements ProceduralExpressionParserVisitor{

	@Override
	public Object visit(SimpleNode node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTLogicalPlan node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTAlgebraOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTProjectionOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);	}

	@Override
	public Object visit(ASTProjectionIdentifier node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSelectionOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);	}

	@Override
	public Object visit(ASTJoinOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTWindowOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSlidingTimeWindow node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTAccessOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTPredictionOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTPredicate node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSimplePredicate node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTBasicPredicate node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTOrPredicate node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTAndPredicate node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTNotPredicate node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSimpleToken node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTNumber node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTString node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTIdentifier node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTCompareOperator node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTPredictionDefinition node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTTimestampAttribute node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTPredictionFunctionDefinition node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTDefaultPredictionDefinition node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTFunctionExpression node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTFunctionName node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTRelationalSelectionOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTRelationalJoinOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTRelationalProjectionOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTRelationalNestOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTRelationalUnnestOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

}

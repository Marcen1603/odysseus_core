package de.uniol.inf.is.odysseus.pqlhack.parser.visitor;

import de.uniol.inf.is.odysseus.pqlhack.parser.ASTAccessOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTAlgebraOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTAndPredicate;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTAssociationEvalOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTAssociationGenOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTAssociationSelOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTAssociationSrcOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTBasicPredicate;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTBenchmarkOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTBenchmarkOpExt;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTBrokerOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTBufferOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTCompareOperator;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTDefaultPredictionDefinition;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTEvaluateOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTExistOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTExistPredicate;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTExistVariablesDeclaration;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTExpression;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTFilterCovarianceOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTFilterEstimateOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTFilterGainOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTFunctionExpression;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTFunctionName;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTJoinOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTKeyValueList;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTKeyValuePair;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTLogicalPlan;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTNotPredicate;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTNumber;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTOrPredicate;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTPredicate;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTPredictionAssignOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTPredictionAssignOrOp;
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
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTSchemaConvertOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTSelectionOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTSimplePredicate;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTSimpleToken;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTSlidingTimeWindow;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTString;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTTestOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTWindowOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ProceduralExpressionParserVisitor;
import de.uniol.inf.is.odysseus.pqlhack.parser.SimpleNode;

public class DefaultVisitor implements ProceduralExpressionParserVisitor{

	public Object visit(SimpleNode node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTLogicalPlan node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTAlgebraOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTProjectionOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);	}

	
	public Object visit(ASTProjectionIdentifier node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTSelectionOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);	}

	
	public Object visit(ASTJoinOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTWindowOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTSlidingTimeWindow node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTAccessOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTPredictionAssignOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTPredicate node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTSimplePredicate node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTBasicPredicate node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTOrPredicate node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTAndPredicate node, Object data) {
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTNotPredicate node, Object data) {
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTSimpleToken node, Object data) {
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTNumber node, Object data) {
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTString node, Object data) {
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTIdentifier node, Object data) {
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTCompareOperator node, Object data) {
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTPredictionDefinition node, Object data) {
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTPredictionFunctionDefinition node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTDefaultPredictionDefinition node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTFunctionExpression node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTFunctionName node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTRelationalSelectionOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTRelationalJoinOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTRelationalProjectionOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTRelationalNestOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTRelationalUnnestOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTBrokerOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTPredictionAssignOrOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTKeyValuePair node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTAssociationGenOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTAssociationEvalOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTAssociationSelOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTAssociationSrcOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTKeyValueList node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSchemaConvertOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTPredictionOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTBenchmarkOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTEvaluateOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTTestOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTBufferOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTExistOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

  @Override
  public Object visit(ASTFilterGainOp node, Object data)
  {
    return node.childrenAccept(this, data);
  }

  @Override
  public Object visit(ASTFilterEstimateOp node, Object data)
  {
    return node.childrenAccept(this, data);
  }

  @Override
  public Object visit(ASTFilterCovarianceOp node, Object data)
  {
    return node.childrenAccept(this, data);
  }

@Override
public Object visit(ASTBenchmarkOpExt node, Object data) {
	return node.childrenAccept(this, data);
}

}

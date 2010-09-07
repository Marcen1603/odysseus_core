package de.uniol.inf.is.odysseus.pqlhack.parser.visitor;

import java.util.ArrayList;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.broker.dictionary.BrokerDictionary;
import de.uniol.inf.is.odysseus.broker.transaction.BrokerAssociationMapping;
import de.uniol.inf.is.odysseus.broker.transaction.QueuePortMapping;
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
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTBrokerInitOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTBrokerOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTBufferOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTCompareOperator;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTDefaultPredictionDefinition;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTEvaluateOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTExistOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTExpression;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTFilterCovarianceOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTFilterEstimateOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTFilterGainOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTFunctionExpression;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTFunctionName;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTJDVESinkOp;
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
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTPunctuationOp;
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
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTTmpDataBouncerOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTWindowOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ProceduralExpressionParserVisitor;
import de.uniol.inf.is.odysseus.pqlhack.parser.SimpleNode;


/**
 * This visitor sets the queue port mappings in the broker operator.
 * This means, the visitor traverses the whole query plan. Each time
 * a new ASTNode is visited the preceding (parent) node is passed.
 * If the next (child) node is a Broker-Node, the broker dictionary
 * gets a new mapping from queue in-port to data out-port. The port numbers
 * are incremented each time a mapping is found. Since the operators
 * are not subscribed to each other by this visitor, the order of subscription
 * in the next visitor must be the same order as the mappings are detected here.
 * I assume that this will hold automatically since the AST will not be changed.
 * 
 * the visit(ASTxxx node, Object data) methods usually call
 * return node.childrenAccept(this, data);
 * 
 * data must be set as follows:
 * if ASTxxx represents a production rule in the grammar, that
 * can have an AlgebraOp() production rule as a child, then data is <true>
 * if ASTxxx cannot have AlgebraOp() as a child, than data is <false>
 * 
 * only in visit(SimpleNode node, Object data) data must be passed directly
 * to the following method.
 * 
 * By this, in visit(ASTBroker node, Object data) we know, whether the broker
 * operator has preceeding operators or not.
 * 
 * @author Andr� Bolles
 *
 */
public class InitBrokerVisitor implements ProceduralExpressionParserVisitor{

	private ArrayList<String> brokerNames;
	
	private HashMap<String, BrokerAssociationMapping> associationMappings;
	
	
	public InitBrokerVisitor(){
		this.brokerNames = new ArrayList<String>();
		this.associationMappings = new HashMap<String, BrokerAssociationMapping>();
	}
	
	public ArrayList<String> getBrokerNames(){
		return this.brokerNames;
	}
	
	public Object visit(ASTBrokerOp broker, Object hasPreceedingOperator) {
		ArrayList returnData = new ArrayList();
		returnData.add(broker.getName());
		
		// in create logical plan visitor
		// first the sources are created and then
		// followings ops are finished. So
		// to get the correct queue port mappings
		// we first have to go to the sources and
		// after that we have to set the queue port
		// mappings.
		// first go down the tree and that upwards
		// again
		// if the children return data, then an association
		// source is writing into this broker. In this
		// case it can happen that the corresponding
		// association comes from this broker
		// so that a queue port mapping must be matched.
		Object childData = broker.childrenAccept(this, true);
		String assName = null;
		if(childData != null && childData instanceof String){
			assName = (String)childData;
		}
		
		if(!brokerNames.contains(broker.getName())){
			this.brokerNames.add(broker.getName());
		}
		
		// check if the preceding node is an algebra operator
		if(hasPreceedingOperator != null && ((Boolean)hasPreceedingOperator).booleanValue()){
			// check if the broker has a queue statement.
			if(broker.hasQueue()){
				// the broker must exist, since it has been created
				// by CREATE BROKER
				int curIn = BrokerDictionary.getInstance().getCurrentInputPort(broker.getName());
				int curOut = BrokerDictionary.getInstance().getCurrentOutputPort(broker.getName());
				
				QueuePortMapping qpm = new QueuePortMapping(curOut, curIn);
				returnData.add(curOut);
				
				BrokerDictionary.getInstance().setCurrentInputPort(broker.getName(), curIn+1);
				BrokerDictionary.getInstance().setCurrentOuputPort(broker.getName(), curOut+1);
				
				BrokerDictionary.getInstance().addQueuePortMapping(broker.getName(), qpm);
			}
			
			// if the broker has no queue statement, then the current output port must be incremented
			// the parent node is an algebra, the broker is the input for the algebra op.
			else{
				int curOut = BrokerDictionary.getInstance().getCurrentOutputPort(broker.getName());
				returnData.add(curOut);
				BrokerDictionary.getInstance().setCurrentOuputPort(broker.getName(), curOut+1);
			}

		}		
		
		// if there are child operators, than increment the no of input ports by 
		// the no of child ops.
		int curIn = BrokerDictionary.getInstance().getCurrentInputPort(broker.getName());
		BrokerDictionary.getInstance().setCurrentInputPort(broker.getName(), curIn + broker.getNoOfChildOps());
		
		// I don't whether it is possible that the broker has proceeding operator
		// in the statement an at the same time an association child, but maybe.
		// Therefore the following is only if and not else if.
		if(assName != null){
			// get the out port for the association and a corresponding queue port;
			// at the moment an association operator name is only connected to
			// one single broker
			QueuePortMapping qpm = new QueuePortMapping(this.associationMappings.get(assName).getOutPort(), curIn);
			BrokerDictionary.getInstance().addQueuePortMapping(broker.getName(), qpm);
		}
		
		return returnData;
	}

	@Override
	public Object visit(SimpleNode node, Object data) {
		for(int i = 0; i<node.jjtGetNumChildren(); i++){
			
		}
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTLogicalPlan node, Object data) {
		return node.childrenAccept(this, false);
	}

	@Override
	public Object visit(ASTAlgebraOp node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTProjectionOp node, Object data) {
		return node.childrenAccept(this, true);
	}

	@Override
	public Object visit(ASTProjectionIdentifier node, Object data) {
		return node.childrenAccept(this, false);
	}

	@Override
	public Object visit(ASTRelationalProjectionOp node, Object data) {
		return node.childrenAccept(this, true);
	}

	@Override
	public Object visit(ASTSelectionOp node, Object data) {
		return node.childrenAccept(this, true);
	}

	@Override
	public Object visit(ASTRelationalSelectionOp node, Object data) {
		return node.childrenAccept(this, true);
	}

	@Override
	public Object visit(ASTRelationalJoinOp node, Object data) {
		return node.childrenAccept(this, true);
	}

	@Override
	public Object visit(ASTRelationalNestOp node, Object data) {
		return node.childrenAccept(this, true);
	}

	@Override
	public Object visit(ASTRelationalUnnestOp node, Object data) {
		return node.childrenAccept(this, true);
	}

	@Override
	public Object visit(ASTJoinOp node, Object data) {
		return node.childrenAccept(this, true);
	}

	@Override
	public Object visit(ASTWindowOp node, Object data) {
		// window op is an abstract grammar clause
		// from this clause you cannot directly go
		// an algebra op
		return node.childrenAccept(this, false);
	}

	@Override
	public Object visit(ASTSlidingTimeWindow node, Object data) {
		return node.childrenAccept(this, true);
	}

	@Override
	public Object visit(ASTAccessOp node, Object data) {
		return node.childrenAccept(this, true);
	}

	@Override
	public Object visit(ASTPredictionAssignOp node, Object data) {
		return node.childrenAccept(this, true);
	}

	@Override
	public Object visit(ASTPredicate node, Object data) {
		return node.childrenAccept(this, false);
	}

	@Override
	public Object visit(ASTSimplePredicate node, Object data) {
		return node.childrenAccept(this, false);
	}

	@Override
	public Object visit(ASTBasicPredicate node, Object data) {
		return node.childrenAccept(this, false);
	}

	@Override
	public Object visit(ASTOrPredicate node, Object data) {
		return node.childrenAccept(this, false);
	}

	@Override
	public Object visit(ASTAndPredicate node, Object data) {
		return node.childrenAccept(this, false);
	}

	@Override
	public Object visit(ASTNotPredicate node, Object data) {
		return node.childrenAccept(this, false);
	}

	@Override
	public Object visit(ASTExpression node, Object data) {
		return node.childrenAccept(this, false);
	}

	@Override
	public Object visit(ASTSimpleToken node, Object data) {
		return node.childrenAccept(this, false);
	}

	@Override
	public Object visit(ASTFunctionExpression node, Object data) {
		return node.childrenAccept(this, false);
	}

	@Override
	public Object visit(ASTFunctionName node, Object data) {
		return node.childrenAccept(this, false);
	}

	@Override
	public Object visit(ASTNumber node, Object data) {
		return node.childrenAccept(this, false);
	}

	@Override
	public Object visit(ASTString node, Object data) {
		return node.childrenAccept(this, false);
	}

	@Override
	public Object visit(ASTIdentifier node, Object data) {
		return node.childrenAccept(this, false);
	}

	@Override
	public Object visit(ASTCompareOperator node, Object data) {
		return node.childrenAccept(this, false);
	}

	@Override
	public Object visit(ASTPredictionDefinition node, Object data) {
		return node.childrenAccept(this, false);
	}

	@Override
	public Object visit(ASTDefaultPredictionDefinition node, Object data) {
		return node.childrenAccept(this, false);
	}

	@Override
	public Object visit(ASTPredictionFunctionDefinition node, Object data) {
		return node.childrenAccept(this, false);
	}

	@Override
	public Object visit(ASTPredictionAssignOrOp node, Object data) {
		return node.childrenAccept(this, true);
	}

	@Override
	public Object visit(ASTKeyValuePair node, Object data) {
		return node.childrenAccept(this, false);
	}

	@Override
	public Object visit(ASTAssociationGenOp node, Object data) {
		return node.childrenAccept(this, true);
	}

	@Override
	public Object visit(ASTAssociationEvalOp node, Object data) {
		return node.childrenAccept(this, true);
	}

	@Override
	public Object visit(ASTAssociationSelOp node, Object data) {
		
		
		Object returnValue = node.jjtGetChild(0).jjtAccept(this, true);
		if(returnValue != null){
			String brokerName = ((String)((ArrayList)returnValue).get(0));
			int outPort = ((Integer)((ArrayList)returnValue).get(1)).intValue();
			
			// get name of this op
			ASTIdentifier identifier = (ASTIdentifier) node.jjtGetChild(1);
			String opName = identifier.getName();
			
			this.associationMappings.put(opName, new BrokerAssociationMapping(opName, brokerName, outPort));
		}
		
		return null;
	}

	@Override
	public Object visit(ASTAssociationSrcOp node, Object data) {
		ASTIdentifier identifier = (ASTIdentifier) node.jjtGetChild(0);
		String srcName = identifier.getName();
		return srcName;
	}

	@Override
	public Object visit(ASTKeyValueList node, Object data) {
		return node.childrenAccept(this, false);
	}

	@Override
	public Object visit(ASTSchemaConvertOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, true);
	}

	@Override
	public Object visit(ASTPredictionOp node, Object data) {
		return node.childrenAccept(this, true);
	}

	@Override
	public Object visit(ASTBenchmarkOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, true);
	}

	@Override
	public Object visit(ASTEvaluateOp node, Object data) {
		return node.childrenAccept(this, true);
	}

	@Override
	public Object visit(ASTTestOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, true);
	}

	@Override
	public Object visit(ASTBufferOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, true);
	}

	@Override
	public Object visit(ASTExistOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, true);
	}

  @Override
  public Object visit(ASTFilterGainOp node, Object data)
  {
    // filter op is a production rule in the grammar which can
    // have AlgebraOp() as a child. so data:=true
    return node.childrenAccept(this, true);
  }

  @Override
  public Object visit(ASTFilterEstimateOp node, Object data)
  {
    // filter op is a production rule in the grammar which can
    // have AlgebraOp() as a child. so data:=true
    return node.childrenAccept(this, true);
  }

  @Override
  public Object visit(ASTFilterCovarianceOp node, Object data)
  {
    // filter op is a production rule in the grammar which can
    // have AlgebraOp() as a child. so data:=true
    return node.childrenAccept(this, true);
  }

	@Override
	public Object visit(ASTBenchmarkOpExt node, Object data) {
		return node.childrenAccept(this, true);
	}

	@Override
	public Object visit(ASTPunctuationOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, true);
	}

	@Override
	public Object visit(ASTBrokerInitOp node, Object data) {
		return node.childrenAccept(this, true);
	}

	@Override
	public Object visit(ASTTmpDataBouncerOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, true);
	}

	@Override
	public Object visit(ASTJDVESinkOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, true);
	}

}
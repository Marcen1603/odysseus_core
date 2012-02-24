package de.uniol.inf.is.odysseus.mining.smql.visitor;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.mining.AbstractParameter;
import de.uniol.inf.is.odysseus.mining.IParameter;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTCleanPhase;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTCorrectionMethod;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTCorrectionMethodDiscard;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTCorrectionMethodFunction;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTCreateKnowledgeDiscoveryProcess;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTDetectionMethod;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTFloat;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTInteger;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTNumber;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTOutlierDetection;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTOutlierDetections;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTParameter;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTParameterList;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTPercent;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTProcessPhases;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTStart;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTStreamSQLWindow;
import de.uniol.inf.is.odysseus.mining.smql.parser.Node;
import de.uniol.inf.is.odysseus.mining.smql.parser.SMQLParserVisitor;
import de.uniol.inf.is.odysseus.mining.smql.parser.SimpleNode;

public abstract class AbstractSMQLParserVisitor implements SMQLParserVisitor {

	private IDataDictionary dataDictionary;
	private ISession user;
	private List<ILogicalOperator> topOperators = new ArrayList<ILogicalOperator>();
	private SMQLParserVisitor currentMainVisitor;

	public void setUser(ISession user) {
		this.user = user;		
	}	
	
	public void setDataDictionary(IDataDictionary dataDictionary) {		
		this.dataDictionary = dataDictionary;
	}
	
	public ISession getUser(){
		return this.user;
	}
	
	public IDataDictionary getDataDictionary(){
		return this.dataDictionary;
	}
	
	public void setCurrentMainVisitor(SMQLParserVisitor parent){
		this.currentMainVisitor = parent;
	}
	
	public SMQLParserVisitor getCurrentMainVisitor(){
		return this.currentMainVisitor;
	}
	

	public List<ILogicalOperator> getTopOperators() {
		return this.topOperators;
	}
	
	public void addTopOperator(ILogicalOperator op){
		this.topOperators.add(op);
	}		
	
	public boolean hasWindow(SimpleNode node, int childPosition){
		if(node.jjtGetChild(childPosition) instanceof ASTStreamSQLWindow){
			return true;
		}
		return false;
	}
	
	public WindowAO createWindowAOFromChildNode(SimpleNode node, int childPosition){
		Node child = node.jjtGetChild(childPosition);
		WindowAO window = (WindowAO) child.jjtAccept(this, null);
		return window;
	}

	@Override
	public Object visit(SimpleNode node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTStart node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTIdentifier node, Object data) {
		return node.jjtGetValue().toString();
	}

	@Override
	public Object visit(ASTInteger node, Object data) {
		int value = Integer.parseInt(node.jjtGetValue().toString());
		return value;
	}

	@Override
	public Object visit(ASTFloat node, Object data) {
		float value = Float.parseFloat(node.jjtGetValue().toString());
		return value;
	}

	@Override
	public Object visit(ASTNumber node, Object data) {
		Object value = node.jjtGetChild(0).jjtAccept(this, data);
		if(value instanceof Integer){
			return ((Integer)value).doubleValue();
		}else{
			if(value instanceof Long){
				return ((Long)value).doubleValue();
			}else{
				// last chance: cast to double
				return (Double) value;
			}
		}
	}

	@Override
	public Object visit(ASTParameter node, Object data) {
		String name = node.jjtGetChild(0).jjtAccept(this, null).toString();
		Object value = node.jjtGetChild(1).jjtAccept(this, null);
		if(value instanceof String){			
			AbstractParameter<String> parameter = new AbstractParameter<String>(name, (String)value);
			return parameter;
		}else{			
			if(value instanceof Double){
				AbstractParameter<Double> parameter = new AbstractParameter<Double>(name, (Double)value);
				return parameter;
			}else{
				// should be an integer
				AbstractParameter<Integer> parameter = new AbstractParameter<Integer>(name, (Integer)value);
				return parameter;
			}			
		}		
	}

	@Override
	public Object visit(ASTParameterList node, Object data) {
		List<IParameter<?>> parameters = new ArrayList<IParameter<?>>();
		for(int i=0;i<node.jjtGetNumChildren();i++){
			SimpleNode child = (SimpleNode) node.jjtGetChild(i);
			parameters.add((IParameter<?>) child.jjtAccept(this, null));
		}
		return parameters;		
	}
	
	
	@Override
	public Object visit(ASTStreamSQLWindow windowNode, Object data) {		
		WindowAO window = new WindowAO();				
		window.setWindowType(windowNode.getType());		
		if (!windowNode.isUnbounded()) {
			window.setWindowSize(windowNode.getSize());
			Long advance = windowNode.getAdvance();
			window.setWindowAdvance(advance != null ? advance : 1);
			if (windowNode.getSlide() != null) {
				window.setWindowSlide(windowNode.getSlide());
			}
		}
		return window;
	}		
	
	@Override
	public Object visit(ASTPercent node, Object data) {		
		return data;
	}
	
	@Override
	public abstract Object visit(ASTCreateKnowledgeDiscoveryProcess node, Object data);

	@Override
	public abstract Object visit(ASTProcessPhases node, Object data);

	@Override
	public abstract Object visit(ASTCleanPhase node, Object data);

	@Override
	public abstract Object visit(ASTOutlierDetections node, Object data);

	@Override
	public abstract Object visit(ASTOutlierDetection node, Object data);

	@Override
	public abstract Object visit(ASTDetectionMethod node, Object data);
	
	@Override
	public abstract Object visit(ASTCorrectionMethod node, Object data);

	@Override
	public abstract Object visit(ASTCorrectionMethodDiscard node, Object data);

	@Override
	public abstract Object visit(ASTCorrectionMethodFunction node, Object data);
}

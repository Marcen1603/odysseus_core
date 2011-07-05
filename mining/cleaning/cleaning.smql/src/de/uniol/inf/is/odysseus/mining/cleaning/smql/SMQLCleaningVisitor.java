package de.uniol.inf.is.odysseus.mining.cleaning.smql;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.mining.cleaning.detection.OutOfDomainDetection;
import de.uniol.inf.is.odysseus.mining.cleaning.detection.OutOfRangeDetection;
import de.uniol.inf.is.odysseus.mining.cleaning.detection.SigmaRuleDetection;
import de.uniol.inf.is.odysseus.mining.cleaning.detection.SimplePredicateDetection;
import de.uniol.inf.is.odysseus.mining.cleaning.detection.SimpleValueDetection;
import de.uniol.inf.is.odysseus.mining.cleaning.logicaloperator.DetectionSplitAO;
import de.uniol.inf.is.odysseus.mining.cleaning.model.IDetection;
import de.uniol.inf.is.odysseus.mining.smql.ISMQLFeature;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTCleanPhase;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTCorrectionMethod;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTCorrectionMethodDiscard;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTCorrectionMethodFunction;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTCreateKnowledgeDiscoveryProcess;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTDetectionMethod;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTDetectionMethodFunction;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTDetectionMethodOutOfDomain;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTDetectionMethodOutOfRange;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTDetectionMethodSigmaRule;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTDetectionMethodSimplePredicate;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTDetectionMethodSimpleValue;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTOutlierDetection;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTOutlierDetections;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTProcessPhases;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTStreamSQLWindow;
import de.uniol.inf.is.odysseus.mining.smql.parser.SMQLParserVisitor;
import de.uniol.inf.is.odysseus.mining.smql.visitor.AbstractSMQLParserVisitor;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class SMQLCleaningVisitor<T> extends AbstractSMQLParserVisitor implements ISMQLFeature{

	
	private Map<String, DetectionSplitAO<?>> streamsToClean = new HashMap<String, DetectionSplitAO<?>>();
	
	@Override
	public Object visit(ASTCreateKnowledgeDiscoveryProcess node, Object data) {
		return getCurrentMainVisitor().visit(node, data);
	}

	@Override
	public Object visit(ASTProcessPhases node, Object data) {
		return getCurrentMainVisitor().visit(node, data);
	}

	@Override
	public Object visit(ASTCleanPhase node, Object data) {
		String streamName = (String)node.jjtGetChild(0).jjtAccept(this, null);
		DetectionSplitAO<?> detectionAO;
		if(this.streamsToClean.containsKey(streamName)){
			 detectionAO = streamsToClean.get(streamName);
		}else{
			ILogicalOperator topOperator = super.getDataDictionary().getViewOrStream(streamName, super.getUser());
			detectionAO = new DetectionSplitAO<RelationalTuple<?>>();
			topOperator.subscribeSink(detectionAO, 0, 0, topOperator.getOutputSchema());	
			detectionAO.setOutputSchema(topOperator.getOutputSchema());
			this.streamsToClean.put(streamName, detectionAO);
		}		
		ILogicalOperator topAO = (ILogicalOperator) node.jjtGetChild(1).jjtAccept(this, detectionAO);				
		return topAO;
	}

	@Override
	public Object visit(ASTOutlierDetections node, Object data) {
		ILogicalOperator detectionAO = (ILogicalOperator) data;		
		ILogicalOperator topAO = (ILogicalOperator) node.childrenAccept(this, detectionAO);
		return topAO;
	}

	@Override
	public Object visit(ASTOutlierDetection node, Object data) {
		@SuppressWarnings("unchecked")
		DetectionSplitAO<T> detectionAO = (DetectionSplitAO<T>) data;	
		String attributeName = (String) node.jjtGetChild(1).jjtAccept(this, null);
		AttributeOperator ao = new AttributeOperator(detectionAO, attributeName);
		@SuppressWarnings("unchecked")
		IDetection<T> detection = (IDetection<T>) node.jjtGetChild(0).jjtAccept(this, ao);
		detectionAO.addDetection(detection); 		
		return detectionAO;
	}	


	@Override
	public Object visit(ASTDetectionMethod node, Object data) {
		IDetection<?> detection = (IDetection<?>) node.jjtGetChild(0).jjtAccept(this, data);		
		return detection;
	}

	@Override
	public Object visit(ASTDetectionMethodOutOfRange node, Object data) {
		
		@SuppressWarnings("unchecked")
		AttributeOperator ao = (AttributeOperator) data;
		if(node.jjtGetChild(0) instanceof ASTStreamSQLWindow){
			WindowAO window = (WindowAO) node.jjtGetChild(0).jjtAccept(this, ao.getOperator());
			
			
			
		}
		int count = 0;
		// TODO: Fenstersemantik!
		String type = "AVG";
		// TODO: korrektes schema? oder beide zusammen?!
		OutOfRangeDetection method = new OutOfRangeDetection(ao.getAttribute(), count, type, ao.getOperator().getOutputSchema());		
		return method;
	}

	@Override
	public Object visit(ASTDetectionMethodSimpleValue node, Object data) {
		double value = (Double) node.jjtGetChild(0).jjtAccept(this, data);
		@SuppressWarnings("unchecked")
		AttributeOperator ao = (AttributeOperator) data;
		return new SimpleValueDetection(ao.getAttribute(), ao.operator.getOutputSchema(), value);
	}

	@Override
	public Object visit(ASTDetectionMethodSigmaRule node, Object data) {
		int value = (Integer) node.jjtGetChild(0).jjtAccept(this, data);
		@SuppressWarnings("unchecked")
		AttributeOperator ao = (AttributeOperator) data;
		return new SigmaRuleDetection(ao.getAttribute(), ao.operator.getOutputSchema(), value);
	}

	@Override
	public Object visit(ASTDetectionMethodSimplePredicate node, Object data) {
		String value = (String) node.jjtGetChild(0).jjtAccept(this, data);
		@SuppressWarnings("unchecked")
		AttributeOperator ao = (AttributeOperator) data;
		return new SimplePredicateDetection(ao.getAttribute(), ao.operator.getOutputSchema(), value);
	}

	@Override
	public Object visit(ASTDetectionMethodFunction node, Object data) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("METHOD NOT YET IMPLEMENTED");		
	}

	@Override
	public Object visit(ASTDetectionMethodOutOfDomain node, Object data) {
		double min = (Double) node.jjtGetChild(0).jjtAccept(this, data);
		double max = (Double) node.jjtGetChild(1).jjtAccept(this, data);
		AttributeOperator ao = (AttributeOperator) data;
		return new OutOfDomainDetection(ao.getAttribute(), min, max, ao.operator.getOutputSchema());
	}
	
	
	@Override
	public Object visit(ASTCorrectionMethod node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTCorrectionMethodDiscard node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTCorrectionMethodFunction node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	//**********************************
	// DEFAULT IMPLEMENTATION
	//**********************************
	
	@Override
	public String getName() {
		return "SMQLCleaning";
	}

	@Override
	public SMQLParserVisitor getVisitor() {
		return this;
	}
	
	//**********************************
	// DEFAULT IMPLEMENTATION - END
	//**********************************

	//**********************************
	// Inner class is just a combination
	// of an operator and an attribute
	//**********************************
	private class AttributeOperator{
		
		private String attribute;
		private ILogicalOperator operator;

		public AttributeOperator(ILogicalOperator op, String attribute){
			this.setOperator(op);
			this.setAttribute(attribute);
		}

		public void setAttribute(String attribute) {
			this.attribute = attribute;
		}

		public String getAttribute() {
			return attribute;
		}

		public void setOperator(ILogicalOperator operator) {
			this.operator = operator;
		}

		public ILogicalOperator getOperator() {
			return operator;
		}
		
	}
	
	
}

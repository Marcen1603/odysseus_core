package de.uniol.inf.is.odysseus.mining.cleaning.smql;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.mining.cleaning.detection.stateful.IBinaryDetection;
import de.uniol.inf.is.odysseus.mining.cleaning.detection.stateful.OutOfRangeDetection;
import de.uniol.inf.is.odysseus.mining.cleaning.detection.stateful.SigmaRuleDetection;
import de.uniol.inf.is.odysseus.mining.cleaning.detection.stateless.IUnaryDetection;
import de.uniol.inf.is.odysseus.mining.cleaning.detection.stateless.OutOfDomainDetection;
import de.uniol.inf.is.odysseus.mining.cleaning.detection.stateless.SimplePredicateDetection;
import de.uniol.inf.is.odysseus.mining.cleaning.detection.stateless.SimpleValueDetection;
import de.uniol.inf.is.odysseus.mining.cleaning.logicaloperator.StatefulDetectionAO;
import de.uniol.inf.is.odysseus.mining.cleaning.logicaloperator.StatelessDetectionAO;
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
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTDetectionMethodStateful;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTDetectionMethodStateless;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTOutlierDetection;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTOutlierDetections;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTPercent;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTProcessPhases;
import de.uniol.inf.is.odysseus.mining.smql.parser.SMQLParserVisitor;
import de.uniol.inf.is.odysseus.mining.smql.visitor.AbstractSMQLParserVisitor;
import de.uniol.inf.is.odysseus.physicaloperator.AggregateFunction;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class SMQLCleaningVisitor extends AbstractSMQLParserVisitor implements ISMQLFeature {

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
		String streamName = (String) node.jjtGetChild(0).jjtAccept(this, null);		
		ILogicalOperator topOperator = super.getDataDictionary().getViewOrStream(streamName, super.getUser());						
		return node.jjtGetChild(1).jjtAccept(this, topOperator);
	}

	@Override
	public Object visit(ASTOutlierDetections node, Object data) {
		List<ILogicalOperator> topOperatoren = new ArrayList<ILogicalOperator>();
		for(int i=0;i<node.jjtGetNumChildren();i++){
			topOperatoren.add((ILogicalOperator) node.jjtGetChild(i).jjtAccept(this, data));
		}
		return topOperatoren;
	}

	@Override
	public Object visit(ASTOutlierDetection node, Object data) {
		// TODO: ab hier bestehen die probleme...		
		String attributeName = (String) node.jjtGetChild(1).jjtAccept(this, null);
		// add source (current top-operator) and the attribute that has to be checked
		AttributeOperator ao = new AttributeOperator((ILogicalOperator)data, attributeName);
		// accept detection method
		// TODO:... danach kommt dann noch die correctionMethod!
		return node.jjtGetChild(0).jjtAccept(this, ao);
	}

	@Override
	public Object visit(ASTDetectionMethod node, Object data) {
		return node.jjtGetChild(0).jjtAccept(this, data);
	}

	@Override
	public Object visit(ASTDetectionMethodStateless node, Object data) {
		AttributeOperator ao = (AttributeOperator) data;
		ILogicalOperator topOp = ao.getOperator();		
		StatelessDetectionAO<RelationalTuple<?>> detectionAO = new StatelessDetectionAO<RelationalTuple<?>>();
		detectionAO.setOutputSchema(topOp.getOutputSchema());
		@SuppressWarnings("unchecked")
		IUnaryDetection<RelationalTuple<?>> detection = (IUnaryDetection<RelationalTuple<?>>) node.jjtGetChild(0).jjtAccept(this, data);
		detectionAO.addDetection(detection);
		topOp.subscribeSink(detectionAO, 0, 0, topOp.getOutputSchema());
		return detectionAO;
	}

	@Override
	public Object visit(ASTDetectionMethodStateful node, Object data) {
		AttributeOperator ao = (AttributeOperator) data;
		ILogicalOperator topOp = ao.getOperator();	
		String attributeName = ao.getAttribute();
		// baue zuerst die Detection-Methode
		@SuppressWarnings("unchecked")
		IBinaryDetection<RelationalTuple<?>> detection = (IBinaryDetection<RelationalTuple<?>>) node.jjtGetChild(0).jjtAccept(this, data);
		
		// teilplan 1: baue window
		WindowAO window = (WindowAO) node.jjtGetChild(1).jjtAccept(this, null);
		topOp.subscribeSink(window, 0, 0, topOp.getOutputSchema());
		// teilplan 1: baue aggregate
		AggregateAO aggregate = new AggregateAO();
		window.subscribeSink(aggregate, 0, 0, window.getInputSchema());
		DirectAttributeResolver dar = new DirectAttributeResolver(window.getOutputSchema());
		SDFAttribute attribute = dar.getAttribute(attributeName);
		AggregateFunction aggFunction = new AggregateFunction("AVG");
		SDFAttribute aggAttribute = new SDFAttribute(aggFunction.getName()+"("+attributeName+")", SDFDatatype.DOUBLE);
		aggregate.addAggregation(attribute, aggFunction, aggAttribute);		
		//erstelle daten-seite				
		StatefulDetectionAO<RelationalTuple<?>> detectionAO = new StatefulDetectionAO<RelationalTuple<?>>();
		detectionAO.setOutputSchema(topOp.getOutputSchema());
		detectionAO.addDetection(detection);
		
		aggregate.subscribeSink(detectionAO, 1, 0, aggregate.getOutputSchema());
		topOp.subscribeSink(detectionAO, 0, 0, topOp.getOutputSchema());				
				
		return detectionAO;
	}

	@Override
	public Object visit(ASTDetectionMethodOutOfRange node, Object data) {

		AttributeOperator ao = (AttributeOperator) data;		
		boolean isPercent = false;
		if (node.jjtGetChild(1) instanceof ASTPercent) {
			isPercent = true;			
		}			
		double abweichung = (Double) node.jjtGetChild(0).jjtAccept(this, null);
		OutOfRangeDetection method = new OutOfRangeDetection(ao.getAttribute(), abweichung, isPercent);
		return method;
	}

	@Override
	public Object visit(ASTDetectionMethodSimpleValue node, Object data) {
		double value = (Double) node.jjtGetChild(0).jjtAccept(this, data);		
		AttributeOperator ao = (AttributeOperator) data;
		return new SimpleValueDetection(ao.getAttribute(), value);
	}

	@Override
	public Object visit(ASTDetectionMethodSigmaRule node, Object data) {
		int value = (Integer) node.jjtGetChild(0).jjtAccept(this, data);		
		AttributeOperator ao = (AttributeOperator) data;
		return new SigmaRuleDetection(ao.getAttribute(), ao.operator.getOutputSchema(), value);
	}

	@Override
	public Object visit(ASTDetectionMethodSimplePredicate node, Object data) {
		String value = (String) node.jjtGetChild(0).jjtAccept(this, data);		
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
		return new OutOfDomainDetection(ao.getAttribute(), min, max);
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

	// **********************************
	// DEFAULT IMPLEMENTATION
	// **********************************

	@Override
	public String getName() {
		return "SMQLCleaning";
	}

	@Override
	public SMQLParserVisitor getVisitor() {
		return this;
	}

	// **********************************
	// DEFAULT IMPLEMENTATION - END
	// **********************************

	// **********************************
	// Inner class is just a combination
	// of an operator and an attribute
	// **********************************
	private class AttributeOperator {

		private String attribute;
		private ILogicalOperator operator;

		public AttributeOperator(ILogicalOperator op, String attribute) {
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

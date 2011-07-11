package de.uniol.inf.is.odysseus.mining.smql.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.mining.smql.ISMQLFeature;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTCleanPhase;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTCorrectionMethod;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTCorrectionMethodDiscard;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTCorrectionMethodFunction;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTCorrectionMethodFunctionStateful;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTCorrectionMethodFunctionStateless;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTCorrectionMethodMaxValue;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTCorrectionMethodMinValue;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTCorrectionMethodSimpleValue;
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
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTProcessPhases;
import de.uniol.inf.is.odysseus.mining.smql.parser.SMQLParserVisitor;
import de.uniol.inf.is.odysseus.mining.smql.parser.SimpleNode;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class StandardSMQLParserVisitor extends AbstractSMQLParserVisitor {

	private static final String CLEANING_CLASS = "SMQLCleaning";
	private IDataDictionary dataDictionary;
	private User user;		
	private Map<String, ISMQLFeature> languageFeatures;

	public StandardSMQLParserVisitor(User user, IDataDictionary dataDictionary, Map<String, ISMQLFeature> languageFeatures) {
		this.languageFeatures = languageFeatures;
		this.user = user;
		this.dataDictionary = dataDictionary;
	}	

	private SMQLParserVisitor loadExternalVisitor(String className) {
		try {
			AbstractSMQLParserVisitor visitor = (AbstractSMQLParserVisitor) this.languageFeatures.get(className).getVisitor();
			visitor.setCurrentMainVisitor(this);
			visitor.setDataDictionary(dataDictionary);
			visitor.setUser(user);
			return visitor;			
		} catch (Exception e) {
			throw new RuntimeException("Missing Plugin for SMQL-Feature (Can't load class \""+className+"\")");
		}
	}
	
	private Object delegateToExternalVisitor(SimpleNode node, Object data, String className){
		SMQLParserVisitor visitor = loadExternalVisitor(className);		
		return node.jjtAccept(visitor, data);		
	}

	@Override
	public Object visit(ASTCreateKnowledgeDiscoveryProcess node, Object data) {
		String name = (String)node.jjtGetChild(0).jjtAccept(this, null);
		List<?> ops = (List<?>) node.jjtGetChild(1).jjtAccept(this, null);
		for(Object o: ops){
			super.addTopOperator((ILogicalOperator) o);
		}
		return ops;
	}

	@Override
	public Object visit(ASTProcessPhases node, Object data) {
		List<ILogicalOperator> operators = new ArrayList<ILogicalOperator>();
		for(int i=0;i<node.jjtGetNumChildren();i++){
			SimpleNode child = (SimpleNode) node.jjtGetChild(i);
			@SuppressWarnings("unchecked")
			List<ILogicalOperator> phaseOperators = (List<ILogicalOperator>) child.jjtAccept(this, null);
			operators.addAll(phaseOperators);
		}
		return operators;		
	}

	@Override
	public Object visit(ASTCleanPhase node, Object data) {
		return delegateToExternalVisitor(node, data, CLEANING_CLASS);	
	}

	@Override
	public Object visit(ASTOutlierDetections node, Object data) {
		return delegateToExternalVisitor(node, data, CLEANING_CLASS);	
	}

	@Override
	public Object visit(ASTOutlierDetection node, Object data) {
		return delegateToExternalVisitor(node, data, CLEANING_CLASS);	
	}

	@Override
	public Object visit(ASTDetectionMethod node, Object data) {
		return delegateToExternalVisitor(node, data, CLEANING_CLASS);	
	}

	@Override
	public Object visit(ASTCorrectionMethod node, Object data) {
		return delegateToExternalVisitor(node, data, CLEANING_CLASS);	
	}

	@Override
	public Object visit(ASTCorrectionMethodDiscard node, Object data) {
		return delegateToExternalVisitor(node, data, CLEANING_CLASS);	
	}

	@Override
	public Object visit(ASTCorrectionMethodFunction node, Object data) {
		return delegateToExternalVisitor(node, data, CLEANING_CLASS);	
	}

	public void print() {
		//this.kdp.toString();		
	}

	@Override
	public Object visit(ASTDetectionMethodOutOfRange node, Object data) {
		return delegateToExternalVisitor(node, data, CLEANING_CLASS);
	}

	@Override
	public Object visit(ASTDetectionMethodSimpleValue node, Object data) {
		return delegateToExternalVisitor(node, data, CLEANING_CLASS);
	}

	@Override
	public Object visit(ASTDetectionMethodSigmaRule node, Object data) {
		return delegateToExternalVisitor(node, data, CLEANING_CLASS);
	}

	@Override
	public Object visit(ASTDetectionMethodSimplePredicate node, Object data) {
		return delegateToExternalVisitor(node, data, CLEANING_CLASS);
	}

	@Override
	public Object visit(ASTDetectionMethodFunction node, Object data) {
		return delegateToExternalVisitor(node, data, CLEANING_CLASS);
	}

	@Override
	public Object visit(ASTDetectionMethodOutOfDomain node, Object data) {
		return delegateToExternalVisitor(node, data, CLEANING_CLASS);
	}

	@Override
	public Object visit(ASTDetectionMethodStateless node, Object data) {
		return delegateToExternalVisitor(node, data, CLEANING_CLASS);
	}

	@Override
	public Object visit(ASTDetectionMethodStateful node, Object data) {
		return delegateToExternalVisitor(node, data, CLEANING_CLASS);
	}

	@Override
	public Object visit(ASTCorrectionMethodFunctionStateless node, Object data) {
		return delegateToExternalVisitor(node, data, CLEANING_CLASS);
	}

	@Override
	public Object visit(ASTCorrectionMethodFunctionStateful node, Object data) {
		return delegateToExternalVisitor(node, data, CLEANING_CLASS);
	}

	@Override
	public Object visit(ASTCorrectionMethodMaxValue node, Object data) {
		return delegateToExternalVisitor(node, data, CLEANING_CLASS);
	}

	@Override
	public Object visit(ASTCorrectionMethodMinValue node, Object data) {
		return delegateToExternalVisitor(node, data, CLEANING_CLASS);
	}

	@Override
	public Object visit(ASTCorrectionMethodSimpleValue node, Object data) {
		return delegateToExternalVisitor(node, data, CLEANING_CLASS);
	}	
	

}

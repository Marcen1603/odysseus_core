package de.uniol.inf.is.odysseus.mining.cleaning.smql;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.mining.cleaning.CleanPhase;
import de.uniol.inf.is.odysseus.mining.cleaning.detection.OutOfRangeDetection;
import de.uniol.inf.is.odysseus.mining.cleaning.model.Cleaning;
import de.uniol.inf.is.odysseus.mining.cleaning.model.IDetection;
import de.uniol.inf.is.odysseus.mining.smql.ISMQLFeature;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTCleanPhase;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTCorrectionMethod;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTCorrectionMethodDiscard;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTCorrectionMethodFunction;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTCreateKnowledgeDiscoveryProcess;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTDetectionMethod;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTOutlierDetection;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTOutlierDetections;
import de.uniol.inf.is.odysseus.mining.smql.parser.ASTProcessPhases;
import de.uniol.inf.is.odysseus.mining.smql.parser.SMQLParserVisitor;
import de.uniol.inf.is.odysseus.mining.smql.visitor.AbstractSMQLParserVisitor;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public class SMQLCleaningVisitor extends AbstractSMQLParserVisitor implements ISMQLFeature{

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
		CleanPhase cp = new CleanPhase();				
		@SuppressWarnings("unchecked")
		List<Cleaning> cleanings = (List<Cleaning>) node.jjtGetChild(1).jjtAccept(this, streamName);
		for(Cleaning c : cleanings){
			cp.addCleaning(c);
		}
		return cp;
	}

	@Override
	public Object visit(ASTOutlierDetections node, Object data) {
		String streamName = (String) data;
		List<Cleaning> cleanings = new ArrayList<Cleaning>();
		for(int i=0;i<node.jjtGetNumChildren();i++){
			cleanings.add((Cleaning) node.jjtGetChild(i).jjtAccept(this, streamName));
		}
		return cleanings;
	}

	@Override
	public Object visit(ASTOutlierDetection node, Object data) {
		String streamName = (String) data;
		Cleaning clean = new Cleaning();		
		clean.setStreamName(streamName);
		String attributeName = (String) node.jjtGetChild(1).jjtAccept(this, null);
		
		IDataDictionary dd = super.getDataDictionary();			
		ILogicalOperator op = dd.getViewOrStream(streamName, this.getUser());
		
		
		IDetection detection = new OutOfRangeDetection(attributeName, 0.0d, 360, op.getOutputSchema());
		detection.init();
		clean.setDetection(detection);
		
		return clean;
	}	


	@Override
	public Object visit(ASTDetectionMethod node, Object data) {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public String getName() {
		return "SMQLCleaning";
	}

	@Override
	public SMQLParserVisitor getVisitor() {
		return this;
	}

	
}

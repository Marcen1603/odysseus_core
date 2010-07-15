package de.uniol.inf.is.odysseus.pqlhack.parser.visitor;

import java.util.ArrayList;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.assoziation.logicaloperator.HypothesisEvaluationAO;
import de.uniol.inf.is.odysseus.assoziation.logicaloperator.HypothesisGenerationAO;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.base.predicate.ComplexPredicate;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.base.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.base.predicate.OrPredicate;
import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.JoinAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.ProjectAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.SelectAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowType;
import de.uniol.inf.is.odysseus.logicaloperator.objectrelational.objecttracking.ObjectTrackingNestAO;
import de.uniol.inf.is.odysseus.logicaloperator.objectrelational.objecttracking.ObjectTrackingUnnestAO;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.ObjectTrackingJoinAO;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.ObjectTrackingPredictionAssignAO;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.ObjectTrackingProjectAO;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.ObjectTrackingSelectAO;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.AttributeResolver;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTAccessOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTAlgebraOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTAndPredicate;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTAssociationEvalOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTAssociationGenOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTAssociationSelOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTAssociationSrcOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTBasicPredicate;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTBrokerOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTCompareOperator;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTDefaultPredictionDefinition;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTExpression;
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
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTTestOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTWindowOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.JJTProceduralExpressionParserState;
import de.uniol.inf.is.odysseus.pqlhack.parser.ProceduralExpressionParserVisitor;
import de.uniol.inf.is.odysseus.pqlhack.parser.SimpleNode;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.scars.base.ObjectRelationalPredicate;
import de.uniol.inf.is.odysseus.scars.base.SDFObjectRelationalExpression;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.logicaloperator.PredictionAssignAO;
import de.uniol.inf.is.odysseus.scars.operator.test.ao.TestAO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * This visitor creates the logical plan from a procedural expression.
 * IMPORTANT: data[0] contains the attribute resolver
 *            data[1] contains the child operator
 *            data[2] contains the output port of the child operator to which the parent is connected
 * @author Andre Bolles
 *
 */
@SuppressWarnings("unchecked")
public class CreateLogicalPlanVisitor implements ProceduralExpressionParserVisitor{
	
	public Object visit(SimpleNode node, Object data) {
		return null;
	}
	
	public Object visit(ASTLogicalPlan node, Object data) {
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTAlgebraOp node, Object data) {
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTProjectionOp node, Object data) {
		IAttributeResolver attrRes = (IAttributeResolver)((ArrayList)data).get(0);
		
		ObjectTrackingProjectAO projection = new ObjectTrackingProjectAO();
		// first the output schema is empty, it will be 
		// filled by the projection attributes
		projection.setOutputSchema(new SDFAttributeListExtended());
		
		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(attrRes);
		
		// the first child is the input operator
		ArrayList returnData = ((ArrayList)node.jjtGetChild(0).jjtAccept(this, newData));
		
		
		AbstractLogicalOperator inputForProjection = (AbstractLogicalOperator) returnData.get(1);
		int sourceOutPort = ((Integer)returnData.get(2)).intValue();
		
		projection.subscribeToSource(inputForProjection, 0, sourceOutPort, inputForProjection.getOutputSchema());
		
		// the further children are the identifiers
		SDFAttributeListExtended outAttributes = new SDFAttributeListExtended();
		for(int i = 1; i<node.jjtGetNumChildren(); i++){
			ASTProjectionIdentifier attrIdentifier = (ASTProjectionIdentifier)node.jjtGetChild(i);			
			String attrString = ((ASTIdentifier)attrIdentifier.jjtGetChild(0)).getName();
			SDFAttribute attr = attrRes.getAttribute(attrString);
			
			outAttributes.add(attr);			
		}
		projection.setOutAttributes(outAttributes);
		
//		// the restrict list must be set
//		int[] restrictList = new int[projection.getOutputSchema().getAttributeCount()];
//		for(int i = 0; i<projection.getOutputSchema().getAttributeCount(); i++){
//			SDFAttribute outAttr = projection.getOutputSchema().getAttribute(i);
//			for(int u = 0; u<projection.getInputSchema().getAttributeCount(); u++){
//				
//				SDFAttribute inAttr = projection.getInputSchema().getAttribute(u);
//				if(outAttr.compareTo(inAttr) == 0){
//					restrictList[i] = u;
//				}
//			}
//		}
//		
//		projection.setRestrictList(restrictList);
		
		((ArrayList)data).add(projection);
		((ArrayList)data).add(new Integer(0));
		
		return data;
	}
	
	
	public Object visit(ASTRelationalProjectionOp node, Object data) {
		IAttributeResolver attrRes = (IAttributeResolver)((ArrayList)data).get(0);
		
		ProjectAO projection = new ProjectAO();
		
		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(attrRes);
		
		// the first child is the input operator
		ArrayList returnData = ((ArrayList)node.jjtGetChild(0).jjtAccept(this, newData));
		AbstractLogicalOperator inputForProjection = (AbstractLogicalOperator) returnData.get(1);
		int sourceOutPort = ((Integer)returnData.get(2)).intValue();
		projection.subscribeToSource(inputForProjection, 0, sourceOutPort, inputForProjection.getOutputSchema());
		
		
		// the further children are the identifiers
		SDFAttributeList outAttributes = new SDFAttributeList();
		for(int i = 1; i<node.jjtGetNumChildren(); i++){
			ASTProjectionIdentifier attrIdentifier = (ASTProjectionIdentifier)node.jjtGetChild(i);			
			String attrString = ((ASTIdentifier)attrIdentifier.jjtGetChild(0)).getName();
			SDFAttribute attr = attrRes.getAttribute(attrString);
			
			outAttributes.add(attr);			
		}
		projection.setOutputSchema(outAttributes);
		
//		// the restrict list must be set
//		int[] restrictList = new int[projection.getOutputSchema().getAttributeCount()];
//		for(int i = 0; i<projection.getOutputSchema().getAttributeCount(); i++){
//			SDFAttribute outAttr = projection.getOutputSchema().getAttribute(i);
//			for(int u = 0; u<projection.getInputSchema().getAttributeCount(); u++){
//				
//				SDFAttribute inAttr = projection.getInputSchema().getAttribute(u);
//				if(outAttr.compareTo(inAttr) == 0){
//					restrictList[i] = u;
//				}
//			}
//		}
//		
//		projection.setRestrictList(restrictList);
		
		((ArrayList)data).add(projection);
		((ArrayList)data).add(new Integer(0));
		
		return data;
	}

	
	public Object visit(ASTSelectionOp node, Object data) {
		ObjectTrackingSelectAO selection = new ObjectTrackingSelectAO();
		selection.setWindowSize(node.getWindowSize());
		
		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList)data).get(0));
		
		// the first child is the input operator
		ArrayList returnData = ((ArrayList)node.jjtGetChild(0).jjtAccept(this, newData));
		AbstractLogicalOperator inputForSelection =	(AbstractLogicalOperator) returnData.get(1);
		int sourceOutPort = ((Integer)returnData.get(2)).intValue();
		selection.subscribeToSource(inputForSelection, 0, sourceOutPort, inputForSelection.getOutputSchema());
		
		newData = new ArrayList();
		newData.add(((ArrayList)data).get(0));
		
		// the second child is a predicate
		IPredicate predicate = (IPredicate)((ArrayList)node.jjtGetChild(1).jjtAccept(this, newData)).get(1);
		initPredicate(predicate, selection.getInputSchema(), null);
		
		selection.setPredicate(predicate);
		selection.init((IAttributeResolver)((ArrayList)data).get(0));
		
		((ArrayList)data).add(selection);
		((ArrayList)data).add(new Integer(0));
		
		return data;
	}

	
	public Object visit(ASTJoinOp node, Object data) {
		ObjectTrackingJoinAO join = new ObjectTrackingJoinAO();
		
		// the window size determine how many evaluations
		// are considered for determining the most often true
		// condition of a range predicate.
		join.setWindowSize(node.getWindowSize());
		
		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList)data).get(0));
		
		// in both following lines the index 1 in the get method
		// can be used, since in both lines the collection
		// only contains the attribute resolver
		ArrayList leftInData = (ArrayList)node.jjtGetChild(0).jjtAccept(this, newData);
		AbstractLogicalOperator leftIn = (AbstractLogicalOperator)leftInData.get(1);
		int leftInSourceOutPort = ((Integer)leftInData.get(2)).intValue();
		
		newData = new ArrayList();
		newData.add(((ArrayList)data).get(0));
		
		ArrayList rightInData = (ArrayList)node.jjtGetChild(1).jjtAccept(this, newData);
		AbstractLogicalOperator rightIn = (AbstractLogicalOperator)rightInData.get(1);
		int rightInSourceOutPort = ((Integer)rightInData.get(2)).intValue();
			
		join.subscribeToSource(leftIn, 0, leftInSourceOutPort, leftIn.getOutputSchema());
		join.subscribeToSource(rightIn, 1, rightInSourceOutPort, rightIn.getOutputSchema());
		
		// setting the predicate and initializing the operator
		newData.clear();
		newData.add(((ArrayList)data).get(0));
		IPredicate predicate = (IPredicate)((ArrayList)node.jjtGetChild(2).jjtAccept(this, newData)).get(1);
		initPredicate(predicate, join.getInputSchema(0), join.getInputSchema(1));
		
		join.setPredicate(predicate);
		join.init((IAttributeResolver)((ArrayList)data).get(0));
		
		((ArrayList)data).add(join);
		((ArrayList)data).add(new Integer(0));
		
		return data;
	}

	
	public Object visit(ASTWindowOp node, Object data) {
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTSlidingTimeWindow node, Object data) {
		WindowAO win = new WindowAO(WindowType.SLIDING_TIME_WINDOW);
		
		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList)data).get(0));
		
		// the input must be set first, since the output schema
		// of the window's input is used for determining an
		// ON attribute
		ArrayList returnData = (ArrayList)node.jjtGetChild(0).jjtAccept(this, newData);
		AbstractLogicalOperator inputForWindow = (AbstractLogicalOperator)returnData.get(1);
		int sourceOutPort = ((Integer)returnData.get(2)).intValue();
		
		win.subscribeToSource(inputForWindow, 0, sourceOutPort, inputForWindow.getOutputSchema());
		
		win.setWindowSize(((ASTNumber)node.jjtGetChild(1)).getValue());
		win.setWindowAdvance(((ASTNumber)node.jjtGetChild(2)).getValue());
		
		// there exists a timestamp attribute
		if(node.jjtGetNumChildren() == 4){
			
			newData = new ArrayList();
			newData.add(((ArrayList)data).get(0));
			newData.add(win);

			node.jjtGetChild(3).jjtAccept(this, newData);
		}
		
		((ArrayList)data).add(win);
		((ArrayList)data).add(new Integer(0));
		
		return data;
	}

	
	public Object visit(ASTAccessOp node, Object data) {
		AttributeResolver attributeResolver = (AttributeResolver) ((ArrayList)data).get(0);
		
		if(node.hasAlias()){
			((ArrayList)data).add(attributeResolver.getSource(node.getAlias()));
		}else{
			((ArrayList)data).add(attributeResolver.getSource(((ASTIdentifier)node.jjtGetChild(0)).getName()));
		}
		
		((ArrayList)data).add(new Integer(0));
		
		return data;
	}

	
	public Object visit(ASTPredictionAssignOp node, Object data) {
		ObjectTrackingPredictionAssignAO prediction = new ObjectTrackingPredictionAssignAO();
		
		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList)data).get(0));
		
		ArrayList returnData = (ArrayList)node.jjtGetChild(0).jjtAccept(this, newData);
		AbstractLogicalOperator inputForPrediction = (AbstractLogicalOperator)returnData.get(1);
		int sourceOutPort = ((Integer)returnData.get(2)).intValue();
		

		prediction.subscribeToSource(inputForPrediction, 0, sourceOutPort, inputForPrediction.getOutputSchema());
				
		for(int i = 1; i<node.jjtGetNumChildren(); i++){
			
			// handle the standard prediction definitions
			if(node.jjtGetChild(i) instanceof ASTPredictionDefinition){
				ASTPredictionDefinition predDef = (ASTPredictionDefinition)node.jjtGetChild(i);
				
				SDFExpression[] expressions = new SDFExpression[prediction.getOutputSchema().getAttributeCount()];
				
				// aside from the last child, all children
				// must be ASTPredictionFunctionDefinitions
				for(int u = 0; u<predDef.jjtGetNumChildren() - 1; u++){
					ASTPredictionFunctionDefinition predFctDef = 
						(ASTPredictionFunctionDefinition)predDef.jjtGetChild(u);
					
					ASTIdentifier attr = (ASTIdentifier)predFctDef.jjtGetChild(0);
					ASTExpression predFct = (ASTExpression)predFctDef.jjtGetChild(1);
					
					SDFExpression predFctExpr = null; 
					try{
						predFctExpr = new SDFExpression("", predFct.toString(), (IAttributeResolver)((ArrayList)data).get(0));
						predFctExpr.initAttributePositions(prediction.getOutputSchema());
					}catch(Exception e){
						e.printStackTrace();
					}
					
					// find out to which attribute the expression belongs
					// if it is for the first attribute add the expression
					// to expr[0]. If it is for the second attribute add
					// it to expr[1] and so on.
					
					String attrName = attr.getName();
					
					IAttributeResolver attrRes = (IAttributeResolver)((ArrayList)data).get(0);
					
					for(int v = 0; v<prediction.getOutputSchema().getAttributeCount(); v++){
						if(prediction.getOutputSchema().getAttribute(v).equals(attrRes.getAttribute(attrName))){
							expressions[v] = predFctExpr;
						}
					}
				}
				
				// only the attribute resolver will be passed
				// to the children
				newData = new ArrayList();
				newData.add(((ArrayList)data).get(0));
				
				// the last child of a prediction definition will be a predicate
				IPredicate predicate = (IPredicate)((ArrayList)predDef.jjtGetChild(predDef.jjtGetNumChildren()-1).jjtAccept(this, newData)).get(1);
				
				initPredicate(predicate, prediction.getInputSchema(), null);
				
				prediction.setPredictionFunction(expressions, predicate);	
			}
			
			// handle the default prediction definition
			else if(node.jjtGetChild(i) instanceof ASTDefaultPredictionDefinition){
				ASTDefaultPredictionDefinition predDef = (ASTDefaultPredictionDefinition)node.jjtGetChild(i);
				
				SDFExpression[] expressions = new SDFExpression[prediction.getOutputSchema().getAttributeCount()];
				
				// aside from the last child, all children
				// must be ASTPredictionFunctionDefinitions
				for(int u = 0; u<predDef.jjtGetNumChildren() - 1; u++){
					ASTPredictionFunctionDefinition predFctDef = 
						(ASTPredictionFunctionDefinition)predDef.jjtGetChild(u);
					
					ASTIdentifier attr = (ASTIdentifier)predFctDef.jjtGetChild(0);
					ASTExpression predFct = (ASTExpression)predFctDef.jjtGetChild(1);
					
					SDFExpression predFctExpr = null; 
					try{
						predFctExpr = new SDFExpression("", predFct.toString(), (IAttributeResolver)((ArrayList)data).get(0));
					}catch(Exception e){
						e.printStackTrace();
					}
					
					// find out to which attribute the expression belongs
					// if it is for the first attribute add the expression
					// to expr[0]. If it is for the second attribute add
					// it to expr[1] and so on.
					
					String attrName = attr.getName();
					
					IAttributeResolver attrRes = (IAttributeResolver)((ArrayList)data).get(0);
					
					for(int v = 0; v<prediction.getOutputSchema().getAttributeCount(); v++){
						if(prediction.getOutputSchema().getAttribute(v).equals(attrRes.getAttribute(attrName))){
							expressions[v] = predFctExpr;
						}
					}
				}
			}		
		}
		
		((ArrayList)data).add(prediction);
		((ArrayList)data).add(new Integer(0));
		
		return data;
	}
	
	
	public Object visit(ASTPredicate node, Object data) {
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTSimplePredicate node, Object data) {
		return node.childrenAccept(this, data);
	}

	
	public Object visit(ASTBasicPredicate node, Object data) {
		SDFObjectRelationalExpression expression;
//		try {
			expression = new SDFObjectRelationalExpression("", node.getPredicate(),
					(IAttributeResolver) ((ArrayList) data).get(0));
//		} catch (SDFExpressionParseException e) {
//			throw new RuntimeException(e);
//		}
		
		((ArrayList)data).add(new ObjectRelationalPredicate(expression));
		
		return data;
	}

	
	public Object visit(ASTAndPredicate node, Object data) {
		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList)data).get(0));
		
		
		IPredicate<? super RelationalTuple<?>> left = (IPredicate<? super RelationalTuple<?>>) ((ArrayList)node
				.jjtGetChild(0).jjtAccept(this, newData)).get(1);
		
		// pass only the attribute resolver to the children
		newData = new ArrayList();
		newData.add(((ArrayList)data).get(0));
		
		IPredicate<? super RelationalTuple<?>> right = (IPredicate<? super RelationalTuple<?>>) ((ArrayList)node
				.jjtGetChild(1).jjtAccept(this, newData)).get(1);
		
		((ArrayList)data).add(new AndPredicate<RelationalTuple<?>>(left, right));
		return data;
	}

	
	public Object visit(ASTOrPredicate node, Object data) {
		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList)data).get(0));
		
		IPredicate<? super RelationalTuple<?>> left = (IPredicate<? super RelationalTuple<?>>) ((ArrayList)node
				.jjtGetChild(0).jjtAccept(this, newData)).get(1);
		
		// pass only the attribute resolver to the children
		newData = new ArrayList();
		newData.add(((ArrayList)data).get(0));
		
		IPredicate<? super RelationalTuple<?>> right = (IPredicate<? super RelationalTuple<?>>) ((ArrayList)node
				.jjtGetChild(1).jjtAccept(this, newData)).get(1);
		
		((ArrayList)data).add(new OrPredicate<RelationalTuple<?>>(left, right));
		return data;
	}

	
	public Object visit(ASTNotPredicate node, Object data) {
		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList)data).get(0));
		
		IPredicate<RelationalTuple<?>> predicate = (IPredicate<RelationalTuple<?>>) ((ArrayList)node
				.jjtGetChild(0).jjtAccept(this, newData)).get(1);
		
		((ArrayList)data).add(new NotPredicate<RelationalTuple<?>>(predicate));
		return data;
	}


	
	public Object visit(ASTExpression node, Object data) {
		return null;
	}

	
	public Object visit(ASTSimpleToken node, Object data) {
		return null;
	}

	
	public Object visit(ASTNumber node, Object data) {
		return null;
	}

	
	public Object visit(ASTString node, Object data) {
		return null;
	}

	
	public Object visit(ASTIdentifier node, Object data) {
		return null;
	}

	
	public Object visit(ASTCompareOperator node, Object data) {
		return null;
	}

	
	public Object visit(ASTPredictionDefinition node, Object data) {
		return null;
	}

	
	public Object visit(ASTProjectionIdentifier node, Object data) {
		IAttributeResolver attrRes = (IAttributeResolver)((ArrayList)data).get(0);
		ProjectAO projectAO = (ProjectAO)((ArrayList)data).get(1);
		
		String attrString = ((ASTIdentifier)node.jjtGetChild(0)).getName();
		SDFAttribute attr = attrRes.getAttribute(attrString);
		
		SDFAttributeList outputSchema = projectAO.getOutputSchema();
		outputSchema.add(attr);
		
		projectAO.setOutputSchema(outputSchema);
		
		return null;
	}

	
	public Object visit(ASTPredictionFunctionDefinition node, Object data) {
		return null;
	}
	
	public static void initPredicate(IPredicate predicate,
			SDFAttributeList left, SDFAttributeList right) {
		if (predicate instanceof ComplexPredicate) {
			ComplexPredicate compPred = (ComplexPredicate) predicate;
			initPredicate(compPred.getLeft(), left, right);
			initPredicate(compPred.getRight(), left, right);
			return;
		}
		if (predicate instanceof NotPredicate) {
			initPredicate(((NotPredicate) predicate).getChild(), left, right);
			return;
		}
		if (predicate instanceof RelationalPredicate) {
			((RelationalPredicate) predicate).init(left, right);
		}
		if (predicate instanceof ObjectRelationalPredicate) {
			((ObjectRelationalPredicate) predicate).init(left, right);
		}
		// NOTE: Das ProbabilityPredicate muss nicht mit linkem
		// und rechtem Schema initialisiert werden.
	}

	
	public Object visit(ASTDefaultPredictionDefinition node, Object data) {
		return null;
	}

	
	public Object visit(ASTFunctionExpression node, Object data) {
		return null;
	}

	
	public Object visit(ASTFunctionName node, Object data) {
		return null;
	}

	
	public Object visit(ASTRelationalSelectionOp node, Object data) {
		SelectAO selection = new SelectAO();
		
		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList)data).get(0));
		
		// the first child is the input operator
		ArrayList returnData = (ArrayList)node.jjtGetChild(0).jjtAccept(this, newData);
		AbstractLogicalOperator inputForSelection =	(AbstractLogicalOperator)returnData.get(1);
		int sourceOutPort = ((Integer)returnData.get(2)).intValue();
		selection.subscribeToSource(inputForSelection, 0, sourceOutPort, inputForSelection.getOutputSchema());
		
		newData = new ArrayList();
		newData.add(((ArrayList)data).get(0));
		
		// the second child is a predicate
		IPredicate predicate = (IPredicate)((ArrayList)node.jjtGetChild(1).jjtAccept(this, newData)).get(1);
		initPredicate(predicate, selection.getInputSchema(), null);
		
		selection.setPredicate(predicate);
		
		((ArrayList)data).add(selection);
		((ArrayList)data).add(new Integer(0));
		
		return data;
	}

	
	public Object visit(ASTRelationalJoinOp node, Object data) {
		JoinAO join = new JoinAO();
		
		
		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList)data).get(0));
		
		// in both following lines the index 1 in the get method
		// can be used, since in both lines the collection
		// only contains the attribute resolver
		ArrayList leftReturnData = (ArrayList)node.jjtGetChild(0).jjtAccept(this, newData);
		AbstractLogicalOperator leftIn = (AbstractLogicalOperator) leftReturnData.get(1);
		int leftSourceOutPort = ((Integer)leftReturnData.get(2)).intValue();
		
		newData = new ArrayList();
		newData.add(((ArrayList)data).get(0));
		ArrayList rightReturnData = (ArrayList)node.jjtGetChild(1).jjtAccept(this, newData);
		AbstractLogicalOperator rightIn = (AbstractLogicalOperator)rightReturnData.get(1);
		int rightSourceOutPort = ((Integer)rightReturnData.get(2)).intValue();
			
		join.subscribeToSource(leftIn, 0, leftSourceOutPort, leftIn.getOutputSchema());
		join.subscribeToSource(rightIn, 1, rightSourceOutPort, rightIn.getOutputSchema());
		
		// setting the predicate and initializing the operator
		newData.clear();
		newData.add(((ArrayList)data).get(0));
		IPredicate predicate = (IPredicate)((ArrayList)node.jjtGetChild(2).jjtAccept(this, newData)).get(1);
		initPredicate(predicate, join.getInputSchema(0), join.getInputSchema(1));
		
		join.setPredicate(predicate);
		
		((ArrayList)data).add(join);
		((ArrayList)data).add(new Integer(0));
		
		return data;
	}

	
	public Object visit(ASTRelationalNestOp node, Object data) {
//		Class<?> visitorClass;
//		try {
//			visitorClass = Class
//					.forName("de.uniol.inf.is.odysseus.parser.pql.objectrelational.Visitor");
//			Object visitorInstance = visitorClass.newInstance();
//			Method m = visitorClass.getDeclaredMethod("visit",
//					ASTRelationalNestOp.class, Object.class);
//			AbstractLogicalOperator sourceOp = (AbstractLogicalOperator) m
//				.invoke(visitorInstance, node, data);	
//			return sourceOp;
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Objectrelational Plugin is missing in parser.", e.getCause());
//		} catch (Exception e) {
//			throw new RuntimeException("Error while parsing relational nest clause", e.getCause());
//		}	 	   
        ObjectTrackingNestAO op;
        op = new ObjectTrackingNestAO();
        
        AbstractLogicalOperator input;
        AttributeResolver attrRes;
        SDFAttributeListExtended nestingAttributes;
        ASTIdentifier nestAttributeIdentifier;
        String nestAttributeName;
        ArrayList newData;
        
        attrRes = (AttributeResolver) ((ArrayList) data).get(0);
        
        nestingAttributes = new SDFAttributeListExtended();
        newData = new ArrayList();
        
        newData.add(attrRes);
                
        ArrayList returnData = (ArrayList)node.jjtGetChild(0).jjtAccept(this, newData);
        input = (AbstractLogicalOperator) returnData.get(1);
        int sourceOutPort = ((Integer)returnData.get(2)).intValue();
        
        op.subscribeToSource(input, 0, sourceOutPort, input.getOutputSchema());        
        
        nestAttributeIdentifier = 
            (ASTIdentifier) node.jjtGetChild(1);
        
        nestAttributeName = nestAttributeIdentifier.getName();
        
        System.out.println("nestAttributeName " + nestAttributeName);
        
        for(int i = 2; i < node.jjtGetNumChildren(); i++){
            ASTIdentifier attrIdentifier = (ASTIdentifier) node.jjtGetChild(i);          
            String attrString = ((ASTIdentifier) attrIdentifier).getName();
            SDFAttribute attr = attrRes.getAttribute(attrString);
            nestingAttributes.add(attr);            
        }                
        
        op.setNestingAttributes(nestingAttributes);
        op.setNestAttributeName(nestAttributeName);        
        
        attrRes.addAttribute(op.getNestAttribute());
        
        ((ArrayList) data).add(op);
        ((ArrayList) data).add(new Integer(0));
        
        return data;
	}

	
	public Object visit(ASTRelationalUnnestOp node, Object data) {
//		Class<?> visitorClass;
//		try {
//			visitorClass = Class
//					.forName("de.uniol.inf.is.odysseus.parser.pql.objectrelational.Visitor");
//			Object visitorInstance = visitorClass.newInstance();
//			Method m = visitorClass.getDeclaredMethod("visit",
//					ASTRelationalNestOp.class, Object.class);
//		
//			AbstractLogicalOperator sourceOp = (AbstractLogicalOperator) m
//				.invoke(visitorInstance, node, newData);	
//			
//			((ArrayList)data).add(sourceOp);
//			((ArrayList)data).add(new Integer(0));
//			return data;
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Objectrelational Plugin is missing in parser.", e.getCause());
//		} catch (Exception e) {
//			throw new RuntimeException("Error while parsing relational nest clause", e.getCause());
//		}
		
	    ObjectTrackingUnnestAO op;
        op = new ObjectTrackingUnnestAO();
        
        AbstractLogicalOperator input;
        IAttributeResolver attrRes;        
        ASTIdentifier nestAttributeIdentifier;
        String nestAttributeName;
        ArrayList newData;
		
        attrRes = (IAttributeResolver) ((ArrayList) data).get(0);
        
        newData = new ArrayList();
        
        newData.add(attrRes);
        
        input = (AbstractLogicalOperator) 
            ((ArrayList)node.jjtGetChild(0).jjtAccept(this, newData)).get(1);
        
        op.subscribeTo(input, input.getOutputSchema());        
        
        nestAttributeIdentifier = 
            (ASTIdentifier) node.jjtGetChild(1);
        
        nestAttributeName = nestAttributeIdentifier.getName();          
        op.setNestAttribute(attrRes.getAttribute(nestAttributeName));
        
        ((ArrayList) data).add(op);
        ((ArrayList) data).add(new Integer(0));
        
        return data; 
	}

	
	public Object visit(ASTTestOp node, Object data) {
		TestAO op = new TestAO();
		
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList)data).get(0));
		
		ArrayList returnData = (ArrayList)node.jjtGetChild(0).jjtAccept(this, newData);
		AbstractLogicalOperator inputForTest = (AbstractLogicalOperator)returnData.get(1);
		int sourceOutPort = ((Integer)returnData.get(2)).intValue();
		
		op.subscribeToSource(inputForTest, 0, sourceOutPort, inputForTest.getOutputSchema());
		
		((ArrayList)data).add(op);
		((ArrayList)data).add(new Integer(0));

		return data;
	}


	@Override
	public Object visit(ASTBrokerOp node, Object data) {
		// We cannot use the same code as for ASTAccessOp, since
		// the broker also can but not has to have input operators.
		AttributeResolver attributeResolver = (AttributeResolver) ((ArrayList)data).get(0);
		
		ArrayList newData = new ArrayList();
		newData.add(attributeResolver);
		
		String brokerName = ((ASTIdentifier)node.jjtGetChild(0)).getName();
		BrokerAO broker = (BrokerAO)attributeResolver.getSource(brokerName);
		int curOutPort = broker.getSubscriptions().size();
		
		// first get the queue of the broker, if it has one
		// must be first, because otherwise the queue port mappings
		// would not match
		ArrayList returnData = null;
		if(node.hasQueue()){
			returnData = (ArrayList)node.jjtGetChild(1).jjtAccept(this, newData);
			ILogicalOperator inputQueue = (ILogicalOperator)returnData.get(1);
			int sourceOutPort = ((Integer)returnData.get(2)).intValue();
			
			if(!broker.getQueueSchema().compatibleTo(inputQueue.getOutputSchema())){
				throw new RuntimeException("Schema mismatch! \nBrokerQueueSchema: " + broker.getQueueSchema().toString() + "\nInputQueueSchema: " + inputQueue.getOutputSchema() );
			}
			
			int curInPort = broker.getSubscribedToSource().size();
			
			broker.subscribeToSource(inputQueue, curInPort, sourceOutPort, inputQueue.getOutputSchema());
		}
		
		// get the input ops of the broker
		// if no queue, then the second child is already a preceding operator
		// if queue, then the third child is the first preceding operator
		// the first child is the identifier
		int start = node.hasQueue() ? 2 : 1;
		for(int i = start; i<node.jjtGetNumChildren(); i++){
			returnData = (ArrayList)node.jjtGetChild(i).jjtAccept(this, newData);
			ILogicalOperator inputOp = (ILogicalOperator)returnData.get(1);
			int sourceOutPort = ((Integer)returnData.get(2)).intValue();
			
			int curInPort = broker.getSubscribedToSource().size();
			broker.subscribeToSource(inputOp, curInPort, sourceOutPort, inputOp.getOutputSchema());
		}
		
		((ArrayList)data).add(broker);
		((ArrayList)data).add(new Integer(curOutPort));
		return data;
	}


	@Override
	public Object visit(ASTPredictionAssignOrOp node, Object data) {
		PredictionAssignAO prediction = new PredictionAssignAO();
		
		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList)data).get(0));
		
		ArrayList returnData = (ArrayList)node.jjtGetChild(0).jjtAccept(this, newData);
		AbstractLogicalOperator inputForPrediction = (AbstractLogicalOperator)returnData.get(1);
		int sourceOutPort = ((Integer)returnData.get(2)).intValue();
		

		prediction.subscribeToSource(inputForPrediction, 0, sourceOutPort, inputForPrediction.getOutputSchema());
				
		for(int i = 1; i<node.jjtGetNumChildren(); i++){
			
			// handle the standard prediction definitions
			if(node.jjtGetChild(i) instanceof ASTPredictionDefinition){
				ASTPredictionDefinition predDef = (ASTPredictionDefinition)node.jjtGetChild(i);
				
				SDFObjectRelationalExpression[] expressions = new SDFObjectRelationalExpression[prediction.getOutputSchema().getAttributeCount()];
				
				// aside from the last child, all children
				// must be ASTPredictionFunctionDefinitions
				for(int u = 0; u<predDef.jjtGetNumChildren() - 1; u++){
					ASTPredictionFunctionDefinition predFctDef = 
						(ASTPredictionFunctionDefinition)predDef.jjtGetChild(u);
					
					ASTIdentifier attr = (ASTIdentifier)predFctDef.jjtGetChild(0);
					ASTExpression predFct = (ASTExpression)predFctDef.jjtGetChild(1);
					
					SDFObjectRelationalExpression predFctExpr = null; 
					try{
						predFctExpr = new SDFObjectRelationalExpression("", predFct.toString(), (IAttributeResolver)((ArrayList)data).get(0));
						predFctExpr.initAttributePaths(prediction.getOutputSchema());
					}catch(Exception e){
						e.printStackTrace();
					}
					
					// find out to which attribute the expression belongs
					// if it is for the first attribute add the expression
					// to expr[0]. If it is for the second attribute add
					// it to expr[1] and so on.
					
					String attrName = attr.getName();
					
					IAttributeResolver attrRes = (IAttributeResolver)((ArrayList)data).get(0);
					
					for(int v = 0; v<prediction.getOutputSchema().getAttributeCount(); v++){
						if(prediction.getOutputSchema().getAttribute(v).equals(attrRes.getAttribute(attrName))){
							expressions[v] = predFctExpr;
						}
					}
				}
				
				// only the attribute resolver will be passed
				// to the children
				newData = new ArrayList();
				newData.add(((ArrayList)data).get(0));
				
				// the last child of a prediction definition will be a predicate
				IPredicate predicate = (IPredicate)((ArrayList)predDef.jjtGetChild(predDef.jjtGetNumChildren()-1).jjtAccept(this, newData)).get(1);
				
				initPredicate(predicate, prediction.getInputSchema(), null);
				
				prediction.setPredictionFunction(expressions, predicate);	
			}
			
			// handle the default prediction definition
			else if(node.jjtGetChild(i) instanceof ASTDefaultPredictionDefinition){
				ASTDefaultPredictionDefinition predDef = (ASTDefaultPredictionDefinition)node.jjtGetChild(i);
				
				SDFObjectRelationalExpression[] expressions = new SDFObjectRelationalExpression[prediction.getOutputSchema().getAttributeCount()];
				
				// aside from the last child, all children
				// must be ASTPredictionFunctionDefinitions
				for(int u = 0; u<predDef.jjtGetNumChildren() - 1; u++){
					ASTPredictionFunctionDefinition predFctDef = 
						(ASTPredictionFunctionDefinition)predDef.jjtGetChild(u);
					
					ASTIdentifier attr = (ASTIdentifier)predFctDef.jjtGetChild(0);
					ASTExpression predFct = (ASTExpression)predFctDef.jjtGetChild(1);
					
					SDFObjectRelationalExpression predFctExpr = null; 
					try{
						predFctExpr = new SDFObjectRelationalExpression("", predFct.toString(), (IAttributeResolver)((ArrayList)data).get(0));
					}catch(Exception e){
						e.printStackTrace();
					}
					
					// find out to which attribute the expression belongs
					// if it is for the first attribute add the expression
					// to expr[0]. If it is for the second attribute add
					// it to expr[1] and so on.
					
					String attrName = attr.getName();
					
					IAttributeResolver attrRes = (IAttributeResolver)((ArrayList)data).get(0);
					
					for(int v = 0; v<prediction.getOutputSchema().getAttributeCount(); v++){
						if(prediction.getOutputSchema().getAttribute(v).equals(attrRes.getAttribute(attrName))){
							expressions[v] = predFctExpr;
						}
					}
				}
			}		
		}
		
		// prediction.initListPath(...)
		
		((ArrayList)data).add(prediction);
		((ArrayList)data).add(new Integer(0));
		
		return data;
	}

	@Override
	public Object visit(ASTKeyValueList node, Object data) {
		return null;
	}
	
	
	@Override
	public Object visit(ASTKeyValuePair node, Object data) {
		return null;
	}


	@Override
	public Object visit(ASTAssociationGenOp node, Object data) {
		IAttributeResolver attrRes = (IAttributeResolver)((ArrayList)data).get(0);
		
		HypothesisGenerationAO gen = new HypothesisGenerationAO();
		
		ArrayList<Object> childData = (ArrayList<Object>) node.jjtGetChild(0).jjtAccept(this, data);
		int sourceOutPort = ((Integer)childData.get(2)).intValue();
		ILogicalOperator childOp = (ILogicalOperator) childData.get(0);
		childOp.subscribeSink(gen, 0, sourceOutPort, childOp.getOutputSchema());
		
		childData = (ArrayList<Object>) node.jjtGetChild(1).jjtAccept(this, data);
		sourceOutPort = ((Integer)childData.get(2)).intValue();
		childOp = (ILogicalOperator) childData.get(0);
		childOp.subscribeSink(gen, 1, sourceOutPort, childOp.getOutputSchema());
		
        gen.initPaths(((ASTIdentifier) node.jjtGetChild(3)).getName(), ((ASTIdentifier) node.jjtGetChild(2)).getName());
		
		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(attrRes);
		newData.add(gen);
		newData.add(new Integer(0));
		return newData;
	}


	@Override
	public Object visit(ASTAssociationEvalOp node, Object data) {
		IAttributeResolver attrRes = (IAttributeResolver)((ArrayList)data).get(0);
		
		HypothesisEvaluationAO eval = new HypothesisEvaluationAO();
		// first the output schema is empty, it will be 
		// filled by the projection attributes
		
		ArrayList<Object> childData = (ArrayList<Object>) node.jjtGetChild(0).jjtAccept(this, data);
		int sourceOutPort = ((Integer)childData.get(2)).intValue();
		ILogicalOperator childOp = (ILogicalOperator) childData.get(0);
		childOp.subscribeSink(eval, 0, sourceOutPort, childOp.getOutputSchema());
		
        ASTIdentifier identifier = (ASTIdentifier) node.jjtGetChild(1);
        eval.setFunctionID(identifier.getName());
        
        HashMap<String, String> params = buildKeyMap((ASTKeyValueList) node.jjtGetChild(2));
        eval.setAlgorithmParameter(params);
        
        identifier = (ASTIdentifier) node.jjtGetChild(3);
        eval.initPaths(((ASTIdentifier) node.jjtGetChild(4)).getName(), ((ASTIdentifier) node.jjtGetChild(3)).getName());
                
        params = buildKeyMap((ASTKeyValueList) node.jjtGetChild(5));
        eval.setMeasurementPairs(params);
        
		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(attrRes);
		newData.add(eval);
		newData.add(new Integer(0));
		return newData;
	}


	@Override
	public Object visit(ASTAssociationSelOp node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Object visit(ASTAssociationSrcOp node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private HashMap<String, String> buildKeyMap(ASTKeyValueList list) {
        HashMap<String, String> params = new HashMap<String, String>();
		for (int i = 0; i < list.jjtGetNumChildren(); i++) {
        	ASTKeyValuePair valuePair = (ASTKeyValuePair) list.jjtGetChild(i);
        	
        	String key =  ((ASTIdentifier) valuePair.jjtGetChild(0)).getName();
        	String value =  ((ASTIdentifier) valuePair.jjtGetChild(1)).getName();
        	
			params.put(key, value);
		}
		return params;
	}
	

}

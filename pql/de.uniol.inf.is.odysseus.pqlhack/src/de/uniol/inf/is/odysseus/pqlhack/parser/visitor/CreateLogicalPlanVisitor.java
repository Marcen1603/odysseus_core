package de.uniol.inf.is.odysseus.pqlhack.parser.visitor;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.base.predicate.ComplexPredicate;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.base.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.base.predicate.OrPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.JoinAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.ProjectAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.SelectAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.PredictionAO;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.AttributeResolver;
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
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTSelectionOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTSimplePredicate;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTSimpleToken;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTSlidingTimeWindow;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTString;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTTimestampAttribute;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTWindowOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ProceduralExpressionParserVisitor;
import de.uniol.inf.is.odysseus.pqlhack.parser.SimpleNode;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

public class CreateLogicalPlanVisitor implements ProceduralExpressionParserVisitor{

	@Override
	public Object visit(SimpleNode node, Object data) {
		// TODO Auto-generated method stub
		return null;
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
		ProjectAO projection = new ProjectAO();
		// first the output schema is empty, it will be 
		// filled by the projection attributes
		projection.setOutputSchema(new SDFAttributeList());
		
		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList)data).get(0));
		
		// the first child is the input operator
		AbstractLogicalOperator inputForProjection = 
			(AbstractLogicalOperator)((ArrayList)node.jjtGetChild(0).jjtAccept(this, newData)).get(1);
		
		projection.setInputSchema(inputForProjection.getOutputSchema());
		projection.subscribeTo(inputForProjection);
		
		// the further children are the identifiers
		for(int i = 1; i<node.jjtGetNumChildren(); i++){
			newData = new ArrayList();
			newData.add(((ArrayList)data).get(0));
			newData.add(projection);
			
			node.jjtGetChild(i).jjtAccept(this, newData);
		}
		
		// the restrict list must be set
		int[] restrictList = new int[projection.getOutputSchema().getAttributeCount()];
		for(int i = 0; i<projection.getOutputSchema().getAttributeCount(); i++){
			SDFAttribute outAttr = projection.getOutputSchema().getAttribute(i);
			for(int u = 0; u<projection.getInputSchema().getAttributeCount(); u++){
				
				SDFAttribute inAttr = projection.getInputSchema().getAttribute(u);
				if(outAttr.compareTo(inAttr) == 0){
					restrictList[i] = u;
				}
			}
		}
		
		projection.setRestrictList(restrictList);
		
		((ArrayList)data).add(projection);
		
		return data;
	}

	@Override
	public Object visit(ASTSelectionOp node, Object data) {
		SelectAO selection = new SelectAO();
		
		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList)data).get(0));
		
		// the first child is the input operator
		AbstractLogicalOperator inputForSelection =
			(AbstractLogicalOperator)((ArrayList)node.jjtGetChild(0).jjtAccept(this, newData)).get(1);
		selection.subscribeTo(inputForSelection);
		
		newData = new ArrayList();
		newData.add(((ArrayList)data).get(0));
		
		// the second child is a predicate
		IPredicate predicate = (IPredicate)((ArrayList)node.jjtGetChild(1).jjtAccept(this, newData)).get(1);
		initPredicate(predicate, selection.getInputSchema(), null);
		
		selection.setPredicate(predicate);
		
		selection.setInputSchema(inputForSelection.getOutputSchema());
		selection.setOutputSchema(inputForSelection.getOutputSchema());
		
		((ArrayList)data).add(selection);
		
		return data;
	}

	@Override
	public Object visit(ASTJoinOp node, Object data) {
		// TODO Auto-generated method stub
		JoinAO join = new JoinAO();
		
		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList)data).get(0));
		
		// in both following lines the index 1 in the get method
		// can be used, since in both lines the collection
		// only contains the attribute resolver
		AbstractLogicalOperator leftIn = (AbstractLogicalOperator)((ArrayList)node.jjtGetChild(0).jjtAccept(this, newData)).get(1);
		
		newData = new ArrayList();
		newData.add(((ArrayList)data).get(0));
		AbstractLogicalOperator rightIn = (AbstractLogicalOperator)((ArrayList)node.jjtGetChild(1).jjtAccept(this, newData)).get(1);
		
		join.setInputSchema(0, leftIn.getOutputSchema());
		join.setInputSchema(1, rightIn.getOutputSchema());
		
		join.subscribeTo(leftIn, 0, 0);
		join.subscribeTo(rightIn, 1, 0);
		
		SDFAttributeList newList = new SDFAttributeList(leftIn
				.getOutputSchema());
		newList.addAll(rightIn.getOutputSchema());
		join.setOutputSchema(newList);
		
		((ArrayList)data).add(join);
		
		return data;
	}

	@Override
	public Object visit(ASTWindowOp node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSlidingTimeWindow node, Object data) {
		// TODO Auto-generated method stub
		WindowAO win = new WindowAO();
		
		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList)data).get(0));
		
		// the input must be set first, since the output schema
		// of the window's input is used for determining an
		// ON attribute
		AbstractLogicalOperator inputForWindow = (AbstractLogicalOperator)((ArrayList)node.jjtGetChild(0).jjtAccept(this, newData)).get(1);
		win.subscribeTo(inputForWindow);
		
		win.setWindowSize(Integer.parseInt(((ASTNumber)node.jjtGetChild(1)).getValue()));
		win.setWindowAdvance(Integer.parseInt(((ASTNumber)node.jjtGetChild(2)).getValue()));
		
		win.setInputSchema(inputForWindow.getOutputSchema());
		win.setOutputSchema(inputForWindow.getOutputSchema());
		
		// there exists a timestamp attribute
		if(node.jjtGetNumChildren() == 4){
			
			newData = new ArrayList();
			newData.add(((ArrayList)data).get(0));
			newData.add(win);

			node.jjtGetChild(3).jjtAccept(this, newData);
		}
		
		((ArrayList)data).add(win);
		
		return data;
	}

	@Override
	public Object visit(ASTAccessOp node, Object data) {
		AttributeResolver attributeResolver = (AttributeResolver) ((ArrayList)data).get(0);
		
		if(node.hasAlias()){
			((ArrayList)data).add(attributeResolver.getSource(node.getAlias()));
		}else{
			((ArrayList)data).add(attributeResolver.getSource(((ASTIdentifier)node.jjtGetChild(0)).getName()));
		}
		return data;
	}

	@Override
	public Object visit(ASTPredictionOp node, Object data) {
		// TODO Auto-generated method stub
		PredictionAO prediction = new PredictionAO();
		
		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList)data).get(0));
		
		AbstractLogicalOperator inputForPrediction =
			(AbstractLogicalOperator)((ArrayList)node.jjtGetChild(0).jjtAccept(this, newData)).get(1);
		
		prediction.setInputSchema(0, inputForPrediction.getOutputSchema());
		prediction.setOutputSchema(inputForPrediction.getOutputSchema());
		
		prediction.subscribeTo(inputForPrediction);
		
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
		
		return data;
	}
	
	@Override
	public Object visit(ASTPredicate node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSimplePredicate node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTBasicPredicate node, Object data) {
		SDFExpression expression;
		try {
			expression = new SDFExpression("", node.getPredicate(),
					(IAttributeResolver) ((ArrayList) data).get(0));
		} catch (org.nfunk.jep.ParseException e) {
			throw new RuntimeException(e);
		}
		
		((ArrayList)data).add(new RelationalPredicate(expression));
		
		return data;
	}

	@SuppressWarnings("unchecked")
	@Override
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

	@SuppressWarnings("unchecked")
	@Override
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

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTNotPredicate node, Object data) {
		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList)data).get(0));
		
		IPredicate<RelationalTuple<?>> predicate = (IPredicate<RelationalTuple<?>>) ((ArrayList)node
				.jjtGetChild(0).jjtAccept(this, newData)).get(1);
		
		((ArrayList)data).add(new NotPredicate<RelationalTuple<?>>(predicate));
		return data;
	}


	@Override
	public Object visit(ASTExpression node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTSimpleToken node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTNumber node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTString node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTIdentifier node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTCompareOperator node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTPredictionDefinition node, Object data) {
		return null;
	}

	@Override
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

	@Override
	public Object visit(ASTTimestampAttribute node, Object data) {

		IAttributeResolver attrRes = (IAttributeResolver)((ArrayList)data).get(0);
		WindowAO window = (WindowAO)((ArrayList)data).get(1);
		ILogicalOperator inputForWindow = window.getInputAO();
		
		ASTIdentifier onId = (ASTIdentifier)node.jjtGetChild(0);
		
		if (onId != null) {
			SDFAttribute onAttribute = attrRes.getAttribute(onId.getName());
			if (onAttribute == null) {
				throw new RuntimeException("invalid attribute in ON: "
						+ onId.getName());
			}
			window.setWindowOn(onAttribute);
		}
		
		return null;
	}

	@Override
	public Object visit(ASTPredictionFunctionDefinition node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
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
		if (predicate instanceof IRelationalPredicate) {
			((RelationalPredicate) predicate).init(left, right);
		}
		// NOTE: Das ProbabilityPredicate muss nicht mit linkem
		// und rechtem Schema initialisiert werden.
	}

	@Override
	public Object visit(ASTDefaultPredictionDefinition node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTFunctionExpression node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTFunctionName node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

}

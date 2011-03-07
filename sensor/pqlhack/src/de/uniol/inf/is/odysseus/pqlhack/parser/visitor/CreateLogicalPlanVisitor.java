/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.pqlhack.parser.visitor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.ExistenceAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.logicaloperator.objectrelational.objecttracking.ObjectTrackingNestAO;
import de.uniol.inf.is.odysseus.logicaloperator.objectrelational.objecttracking.ObjectTrackingUnnestAO;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.ObjectTrackingJoinAO;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.ObjectTrackingPredictionAssignAO;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.ObjectTrackingProjectAO;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.ObjectTrackingSelectAO;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.PunctuationAO;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.SchemaConvertAO;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.AttributeResolver;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTAccessOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTAlgebraOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTAndPredicate;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTAssociationExpressionEvalOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTAssociationExpressionGateOp;
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
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTDistanceObjectSelectorOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTDistanceObjectSelectorOp_Andre;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTEvaluateOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTExistOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTExpression;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTFilterExpCovarianceOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTFilterExpEstimateOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTFilterExpGainOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTFunctionExpression;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTFunctionName;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTHost;
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
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTScarsXMLProfilerOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTSchemaConvertOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTSelectionOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTSimplePredicate;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTSimpleToken;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTSlidingTimeWindow;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTString;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTTestOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTTmpDataBouncerOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTWindowOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.Node;
import de.uniol.inf.is.odysseus.pqlhack.parser.ProceduralExpressionParserVisitor;
import de.uniol.inf.is.odysseus.pqlhack.parser.SimpleNode;
import de.uniol.inf.is.odysseus.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.predicate.ComplexPredicate;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.predicate.OrPredicate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.scars.base.ObjectRelationalPredicate;
import de.uniol.inf.is.odysseus.scars.base.SDFObjectRelationalExpression;
import de.uniol.inf.is.odysseus.scars.objecttracking.evaluation.logicaloperator.EvaluationAO;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.PredictionExpression;
import de.uniol.inf.is.odysseus.scars.objecttracking.temporarydatabouncer.logicaloperator.TemporaryDataBouncerAO;
import de.uniol.inf.is.odysseus.scars.operator.association.ao.AssociationDictionary;
import de.uniol.inf.is.odysseus.scars.operator.association.ao.HypothesisExpressionEvaluationAO;
import de.uniol.inf.is.odysseus.scars.operator.association.ao.HypothesisExpressionGatingAO;
import de.uniol.inf.is.odysseus.scars.operator.association.ao.HypothesisGenerationAO;
import de.uniol.inf.is.odysseus.scars.operator.association.ao.HypothesisSelectionAO;
import de.uniol.inf.is.odysseus.scars.operator.filter.ao.FilterExpressionCovarianceUpdateAO;
import de.uniol.inf.is.odysseus.scars.operator.filter.ao.FilterExpressionEstimateUpdateAO;
import de.uniol.inf.is.odysseus.scars.operator.filter.ao.FilterExpressionGainAO;
import de.uniol.inf.is.odysseus.scars.operator.jdvesink.logicaloperator.JDVESinkAO;
import de.uniol.inf.is.odysseus.scars.operator.objectselector.logicaloperator.DistanceObjectSelectorAO;
import de.uniol.inf.is.odysseus.scars.operator.objectselector.logicaloperator.DistanceObjectSelectorAOAndre;
import de.uniol.inf.is.odysseus.scars.operator.prediction.ao.PredictionAO;
import de.uniol.inf.is.odysseus.scars.operator.prediction.ao.PredictionAssignAO;
import de.uniol.inf.is.odysseus.scars.operator.test.ao.TestAO;
import de.uniol.inf.is.odysseus.scars.xmlprofiler.logicaloperator.XMLProfilerAO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.AmgigiousAttributeException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.NoSuchAttributeException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.infs.is.odysseus.scars.operator.brokerinit.BrokerInitAO;

/**
 * This visitor creates the logical plan from a procedural expression.
 * IMPORTANT: data[0] contains the attribute resolver data[1] contains the child
 * operator data[2] contains the output port of the child operator to which the
 * parent is connected
 *
 * @author Andre Bolles
 *
 */
@SuppressWarnings("unchecked")
public class CreateLogicalPlanVisitor implements ProceduralExpressionParserVisitor {

	private User user;
	private IDataDictionary dd;
	
	public CreateLogicalPlanVisitor(User user, IDataDictionary dd){
		this.user = user;
		this.dd = dd;
	}

	@Override
	public Object visit(SimpleNode node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTLogicalPlan node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTAlgebraOp node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTProjectionOp node, Object data) {
		IAttributeResolver attrRes = (IAttributeResolver) ((ArrayList) data).get(0);

		ObjectTrackingProjectAO projection = new ObjectTrackingProjectAO();
		// first the output schema is empty, it will be
		// filled by the projection attributes
		projection.setOutputSchema(new SDFAttributeListExtended());

		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(attrRes);

		// the first child is the input operator
		ArrayList returnData = ((ArrayList) node.jjtGetChild(0).jjtAccept(this, newData));

		AbstractLogicalOperator inputForProjection = (AbstractLogicalOperator) returnData.get(1);
		int sourceOutPort = ((Integer) returnData.get(2)).intValue();

		projection.subscribeToSource(inputForProjection, 0, sourceOutPort, inputForProjection.getOutputSchema());

		// the further children are the identifiers
		SDFAttributeListExtended outAttributes = new SDFAttributeListExtended();
		for (int i = 1; i < node.jjtGetNumChildren(); i++) {
			ASTProjectionIdentifier attrIdentifier = (ASTProjectionIdentifier) node.jjtGetChild(i);
			String attrString = ((ASTIdentifier) attrIdentifier.jjtGetChild(0)).getName();

			try {
				SDFAttribute attr = attrRes.getAttribute(attrString);
				outAttributes.add(attr);
			} catch (AmgigiousAttributeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAttributeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		projection.setOutAttributes(outAttributes);

		// // the restrict list must be set
		// int[] restrictList = new
		// int[projection.getOutputSchema().getAttributeCount()];
		// for(int i = 0; i<projection.getOutputSchema().getAttributeCount();
		// i++){
		// SDFAttribute outAttr = projection.getOutputSchema().getAttribute(i);
		// for(int u = 0; u<projection.getInputSchema().getAttributeCount();
		// u++){
		//
		// SDFAttribute inAttr = projection.getInputSchema().getAttribute(u);
		// if(outAttr.compareTo(inAttr) == 0){
		// restrictList[i] = u;
		// }
		// }
		// }
		//
		// projection.setRestrictList(restrictList);

		((ArrayList) data).add(projection);
		((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTRelationalProjectionOp node, Object data) {
		IAttributeResolver attrRes = (IAttributeResolver) ((ArrayList) data).get(0);

		ProjectAO projection = new ProjectAO();

		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(attrRes);

		// the first child is the input operator
		ArrayList returnData = ((ArrayList) node.jjtGetChild(0).jjtAccept(this, newData));
		AbstractLogicalOperator inputForProjection = (AbstractLogicalOperator) returnData.get(1);
		int sourceOutPort = ((Integer) returnData.get(2)).intValue();
		projection.subscribeToSource(inputForProjection, 0, sourceOutPort, inputForProjection.getOutputSchema());

		// the further children are the identifiers
		SDFAttributeList outAttributes = new SDFAttributeList();
		for (int i = 1; i < node.jjtGetNumChildren(); i++) {
			ASTProjectionIdentifier attrIdentifier = (ASTProjectionIdentifier) node.jjtGetChild(i);
			String attrString = ((ASTIdentifier) attrIdentifier.jjtGetChild(0)).getName();
			try {
				SDFAttribute attr = attrRes.getAttribute(attrString);
				outAttributes.add(attr);
			} catch (AmgigiousAttributeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAttributeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		projection.setOutputSchema(outAttributes);

		// // the restrict list must be set
		// int[] restrictList = new
		// int[projection.getOutputSchema().getAttributeCount()];
		// for(int i = 0; i<projection.getOutputSchema().getAttributeCount();
		// i++){
		// SDFAttribute outAttr = projection.getOutputSchema().getAttribute(i);
		// for(int u = 0; u<projection.getInputSchema().getAttributeCount();
		// u++){
		//
		// SDFAttribute inAttr = projection.getInputSchema().getAttribute(u);
		// if(outAttr.compareTo(inAttr) == 0){
		// restrictList[i] = u;
		// }
		// }
		// }
		//
		// projection.setRestrictList(restrictList);

		((ArrayList) data).add(projection);
		((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTSelectionOp node, Object data) {
		ObjectTrackingSelectAO selection = new ObjectTrackingSelectAO();
		selection.setWindowSize(node.getWindowSize());

		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));

		// the first child is the input operator
		ArrayList returnData = ((ArrayList) node.jjtGetChild(0).jjtAccept(this, newData));
		AbstractLogicalOperator inputForSelection = (AbstractLogicalOperator) returnData.get(1);
		int sourceOutPort = ((Integer) returnData.get(2)).intValue();
		selection.subscribeToSource(inputForSelection, 0, sourceOutPort, inputForSelection.getOutputSchema());

		newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));

		// the second child is a predicate
		IPredicate predicate = (IPredicate) ((ArrayList) node.jjtGetChild(1).jjtAccept(this, newData)).get(1);
		initPredicate(predicate, selection.getInputSchema(), null);

		selection.setPredicate(predicate);
		selection.init((IAttributeResolver) ((ArrayList) data).get(0));

		((ArrayList) data).add(selection);
		((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTJoinOp node, Object data) {
		ObjectTrackingJoinAO join = new ObjectTrackingJoinAO();

		// the window size determine how many evaluations
		// are considered for determining the most often true
		// condition of a range predicate.
		join.setWindowSize(node.getWindowSize());

		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));

		// in both following lines the index 1 in the get method
		// can be used, since in both lines the collection
		// only contains the attribute resolver
		ArrayList leftInData = (ArrayList) node.jjtGetChild(0).jjtAccept(this, newData);
		AbstractLogicalOperator leftIn = (AbstractLogicalOperator) leftInData.get(1);
		int leftInSourceOutPort = ((Integer) leftInData.get(2)).intValue();

		newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));

		ArrayList rightInData = (ArrayList) node.jjtGetChild(1).jjtAccept(this, newData);
		AbstractLogicalOperator rightIn = (AbstractLogicalOperator) rightInData.get(1);
		int rightInSourceOutPort = ((Integer) rightInData.get(2)).intValue();

		join.subscribeToSource(leftIn, 0, leftInSourceOutPort, leftIn.getOutputSchema());
		join.subscribeToSource(rightIn, 1, rightInSourceOutPort, rightIn.getOutputSchema());

		// setting the predicate and initializing the operator
		newData.clear();
		newData.add(((ArrayList) data).get(0));
		IPredicate predicate = (IPredicate) ((ArrayList) node.jjtGetChild(2).jjtAccept(this, newData)).get(1);
		initPredicate(predicate, join.getInputSchema(0), join.getInputSchema(1));

		join.setPredicate(predicate);
		join.init((IAttributeResolver) ((ArrayList) data).get(0));

		((ArrayList) data).add(join);
		((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTWindowOp node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSlidingTimeWindow node, Object data) {
		WindowAO win = new WindowAO(WindowType.SLIDING_TIME_WINDOW);

		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));

		// the input must be set first, since the output schema
		// of the window's input is used for determining an
		// ON attribute
		ArrayList returnData = (ArrayList) node.jjtGetChild(0).jjtAccept(this, newData);
		AbstractLogicalOperator inputForWindow = (AbstractLogicalOperator) returnData.get(1);
		int sourceOutPort = ((Integer) returnData.get(2)).intValue();

		win.subscribeToSource(inputForWindow, 0, sourceOutPort, inputForWindow.getOutputSchema());

		win.setWindowSize(((ASTNumber) node.jjtGetChild(1)).getValue());
		win.setWindowAdvance(((ASTNumber) node.jjtGetChild(2)).getValue());

		// there exists a timestamp attribute
		if (node.jjtGetNumChildren() == 4) {

			newData = new ArrayList();
			newData.add(((ArrayList) data).get(0));
			newData.add(win);

			node.jjtGetChild(3).jjtAccept(this, newData);
		}

		((ArrayList) data).add(win);
		((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTAccessOp node, Object data) {
		AttributeResolver attributeResolver = (AttributeResolver) ((ArrayList) data).get(0);

		if (node.hasAlias()) {
			((ArrayList) data).add(attributeResolver.getSource(node.getAlias()));
		} else {
			((ArrayList) data).add(attributeResolver.getSource(((ASTIdentifier) node.jjtGetChild(0)).getName()));
		}

		/*
		 * TODO This is a quick hack to make the access-op work for sources parsed
		 * by cql (see ticket 181) this should be replaced by a better management of
		 * sources
		 */

		if (((ArrayList) data).get(1) == null) {
			((ArrayList) data).remove(1);
			((ArrayList) data).add(dd.getViewOrStream(((ASTIdentifier) node.jjtGetChild(0)).getName(), user));
		}

		((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTPredictionAssignOp node, Object data) {
		ObjectTrackingPredictionAssignAO prediction = new ObjectTrackingPredictionAssignAO();

		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));

		ArrayList returnData = (ArrayList) node.jjtGetChild(0).jjtAccept(this, newData);
		AbstractLogicalOperator inputForPrediction = (AbstractLogicalOperator) returnData.get(1);
		int sourceOutPort = ((Integer) returnData.get(2)).intValue();

		prediction.subscribeToSource(inputForPrediction, 0, sourceOutPort, inputForPrediction.getOutputSchema());

		for (int i = 1; i < node.jjtGetNumChildren(); i++) {

			// handle the standard prediction definitions
			if (node.jjtGetChild(i) instanceof ASTPredictionDefinition) {
				ASTPredictionDefinition predDef = (ASTPredictionDefinition) node.jjtGetChild(i);

				SDFExpression[] expressions = new SDFExpression[prediction.getOutputSchema().getAttributeCount()];

				// aside from the last child, all children
				// must be ASTPredictionFunctionDefinitions
				for (int u = 0; u < predDef.jjtGetNumChildren() - 1; u++) {
					ASTPredictionFunctionDefinition predFctDef = (ASTPredictionFunctionDefinition) predDef.jjtGetChild(u);

					ASTIdentifier attr = (ASTIdentifier) predFctDef.jjtGetChild(0);
					ASTExpression predFct = (ASTExpression) predFctDef.jjtGetChild(1);

					SDFExpression predFctExpr = null;
					try {
						predFctExpr = new SDFExpression("", predFct.toString(), (IAttributeResolver) ((ArrayList) data).get(0));
						predFctExpr.initAttributePositions(prediction.getOutputSchema());
					} catch (Exception e) {
						e.printStackTrace();
					}

					// find out to which attribute the expression belongs
					// if it is for the first attribute add the expression
					// to expr[0]. If it is for the second attribute add
					// it to expr[1] and so on.

					String attrName = attr.getName();

					IAttributeResolver attrRes = (IAttributeResolver) ((ArrayList) data).get(0);

					for (int v = 0; v < prediction.getOutputSchema().getAttributeCount(); v++) {
						try {
							if (prediction.getOutputSchema().getAttribute(v).equals(attrRes.getAttribute(attrName))) {
								expressions[v] = predFctExpr;
							}
						} catch (AmgigiousAttributeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchAttributeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				// only the attribute resolver will be passed
				// to the children
				newData = new ArrayList();
				newData.add(((ArrayList) data).get(0));

				// the last child of a prediction definition will be a predicate
				IPredicate predicate = (IPredicate) ((ArrayList) predDef.jjtGetChild(predDef.jjtGetNumChildren() - 1)
						.jjtAccept(this, newData)).get(1);

				initPredicate(predicate, prediction.getInputSchema(), null);

				prediction.setPredictionFunction(expressions, predicate);
			}

			// handle the default prediction definition
			else if (node.jjtGetChild(i) instanceof ASTDefaultPredictionDefinition) {
				ASTDefaultPredictionDefinition predDef = (ASTDefaultPredictionDefinition) node.jjtGetChild(i);

				SDFExpression[] expressions = new SDFExpression[prediction.getOutputSchema().getAttributeCount()];

				// aside from the last child, all children
				// must be ASTPredictionFunctionDefinitions
				for (int u = 0; u < predDef.jjtGetNumChildren() - 1; u++) {
					ASTPredictionFunctionDefinition predFctDef = (ASTPredictionFunctionDefinition) predDef.jjtGetChild(u);

					ASTIdentifier attr = (ASTIdentifier) predFctDef.jjtGetChild(0);
					ASTExpression predFct = (ASTExpression) predFctDef.jjtGetChild(1);

					SDFExpression predFctExpr = null;
					try {
						predFctExpr = new SDFExpression("", predFct.toString(), (IAttributeResolver) ((ArrayList) data).get(0));
					} catch (Exception e) {
						e.printStackTrace();
					}

					// find out to which attribute the expression belongs
					// if it is for the first attribute add the expression
					// to expr[0]. If it is for the second attribute add
					// it to expr[1] and so on.

					String attrName = attr.getName();

					IAttributeResolver attrRes = (IAttributeResolver) ((ArrayList) data).get(0);

					for (int v = 0; v < prediction.getOutputSchema().getAttributeCount(); v++) {
						try {
							if (prediction.getOutputSchema().getAttribute(v).equals(attrRes.getAttribute(attrName))) {
								expressions[v] = predFctExpr;
							}
						} catch (AmgigiousAttributeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchAttributeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}

		((ArrayList) data).add(prediction);
		((ArrayList) data).add(new Integer(0));

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
		SDFObjectRelationalExpression expression;
		// try {
		expression = new SDFObjectRelationalExpression("", node.getPredicate(),
				(IAttributeResolver) ((ArrayList) data).get(0));
		// } catch (SDFExpressionParseException e) {
		// throw new RuntimeException(e);
		// }

		((ArrayList) data).add(new ObjectRelationalPredicate(expression));
		// ((ArrayList) data).add(new RelationalPredicate(expression));
		return data;
	}

	@Override
	public Object visit(ASTAndPredicate node, Object data) {
		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));

		IPredicate<? super RelationalTuple<?>> left = (IPredicate<? super RelationalTuple<?>>) ((ArrayList) node
				.jjtGetChild(0).jjtAccept(this, newData)).get(1);

		// pass only the attribute resolver to the children
		newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));

		IPredicate<? super RelationalTuple<?>> right = (IPredicate<? super RelationalTuple<?>>) ((ArrayList) node
				.jjtGetChild(1).jjtAccept(this, newData)).get(1);

		((ArrayList) data).add(new AndPredicate<RelationalTuple<?>>(left, right));
		return data;
	}

	@Override
	public Object visit(ASTOrPredicate node, Object data) {
		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));

		IPredicate<? super RelationalTuple<?>> left = (IPredicate<? super RelationalTuple<?>>) ((ArrayList) node
				.jjtGetChild(0).jjtAccept(this, newData)).get(1);

		// pass only the attribute resolver to the children
		newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));

		IPredicate<? super RelationalTuple<?>> right = (IPredicate<? super RelationalTuple<?>>) ((ArrayList) node
				.jjtGetChild(1).jjtAccept(this, newData)).get(1);

		((ArrayList) data).add(new OrPredicate<RelationalTuple<?>>(left, right));
		return data;
	}

	@Override
	public Object visit(ASTNotPredicate node, Object data) {
		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));

		IPredicate<RelationalTuple<?>> predicate = (IPredicate<RelationalTuple<?>>) ((ArrayList) node.jjtGetChild(0)
				.jjtAccept(this, newData)).get(1);

		((ArrayList) data).add(new NotPredicate<RelationalTuple<?>>(predicate));
		return data;
	}

	@Override
	public Object visit(ASTExpression node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTSimpleToken node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTNumber node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTString node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTIdentifier node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTCompareOperator node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTPredictionDefinition node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTProjectionIdentifier node, Object data) {
		IAttributeResolver attrRes = (IAttributeResolver) ((ArrayList) data).get(0);
		ProjectAO projectAO = (ProjectAO) ((ArrayList) data).get(1);

		String attrString = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		SDFAttributeList outputSchema = projectAO.getOutputSchema();
		try {
			SDFAttribute attr = attrRes.getAttribute(attrString);
			outputSchema.add(attr);
		} catch (AmgigiousAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		projectAO.setOutputSchema(outputSchema);

		return null;
	}

	@Override
	public Object visit(ASTPredictionFunctionDefinition node, Object data) {
		return null;
	}

	public static void initPredicate(IPredicate predicate, SDFAttributeList left, SDFAttributeList right) {
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

	@Override
	public Object visit(ASTDefaultPredictionDefinition node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTFunctionExpression node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTFunctionName node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTRelationalSelectionOp node, Object data) {
		SelectAO selection = new SelectAO();

		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));

		// the first child is the input operator
		ArrayList returnData = (ArrayList) node.jjtGetChild(0).jjtAccept(this, newData);
		AbstractLogicalOperator inputForSelection = (AbstractLogicalOperator) returnData.get(1);
		int sourceOutPort = ((Integer) returnData.get(2)).intValue();
		selection.subscribeToSource(inputForSelection, 0, sourceOutPort, inputForSelection.getOutputSchema());

		newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));

		// the second child is a predicate
		IPredicate predicate = (IPredicate) ((ArrayList) node.jjtGetChild(1).jjtAccept(this, newData)).get(1);
		initPredicate(predicate, selection.getInputSchema(), null);

		selection.setPredicate(predicate);

		((ArrayList) data).add(selection);
		((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTRelationalJoinOp node, Object data) {
		JoinAO join = new JoinAO();

		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));

		// in both following lines the index 1 in the get method
		// can be used, since in both lines the collection
		// only contains the attribute resolver
		ArrayList leftReturnData = (ArrayList) node.jjtGetChild(0).jjtAccept(this, newData);
		AbstractLogicalOperator leftIn = (AbstractLogicalOperator) leftReturnData.get(1);
		int leftSourceOutPort = ((Integer) leftReturnData.get(2)).intValue();

		newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));
		ArrayList rightReturnData = (ArrayList) node.jjtGetChild(1).jjtAccept(this, newData);
		AbstractLogicalOperator rightIn = (AbstractLogicalOperator) rightReturnData.get(1);
		int rightSourceOutPort = ((Integer) rightReturnData.get(2)).intValue();

		join.subscribeToSource(leftIn, 0, leftSourceOutPort, leftIn.getOutputSchema());
		join.subscribeToSource(rightIn, 1, rightSourceOutPort, rightIn.getOutputSchema());

		// setting the predicate and initializing the operator
		newData.clear();
		newData.add(((ArrayList) data).get(0));
		IPredicate predicate = (IPredicate) ((ArrayList) node.jjtGetChild(2).jjtAccept(this, newData)).get(1);
		initPredicate(predicate, join.getInputSchema(0), join.getInputSchema(1));

		join.setPredicate(predicate);

		((ArrayList) data).add(join);
		((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTRelationalNestOp node, Object data) {
		// Class<?> visitorClass;
		// try {
		// visitorClass = Class
		// .forName("de.uniol.inf.is.odysseus.parser.pql.objectrelational.Visitor");
		// Object visitorInstance = visitorClass.newInstance();
		// Method m = visitorClass.getDeclaredMethod("visit",
		// ASTRelationalNestOp.class, Object.class);
		// AbstractLogicalOperator sourceOp = (AbstractLogicalOperator) m
		// .invoke(visitorInstance, node, data);
		// return sourceOp;
		// } catch (ClassNotFoundException e) {
		// throw new
		// RuntimeException("Objectrelational Plugin is missing in parser.",
		// e.getCause());
		// } catch (Exception e) {
		// throw new
		// RuntimeException("Error while parsing relational nest clause",
		// e.getCause());
		// }
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

		ArrayList returnData = (ArrayList) node.jjtGetChild(0).jjtAccept(this, newData);
		input = (AbstractLogicalOperator) returnData.get(1);
		int sourceOutPort = ((Integer) returnData.get(2)).intValue();

		op.subscribeToSource(input, 0, sourceOutPort, input.getOutputSchema());

		nestAttributeIdentifier = (ASTIdentifier) node.jjtGetChild(1);

		nestAttributeName = nestAttributeIdentifier.getName();

		System.out.println("nestAttributeName " + nestAttributeName);

		for (int i = 2; i < node.jjtGetNumChildren(); i++) {
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

	@Override
	public Object visit(ASTRelationalUnnestOp node, Object data) {
		// Class<?> visitorClass;
		// try {
		// visitorClass = Class
		// .forName("de.uniol.inf.is.odysseus.parser.pql.objectrelational.Visitor");
		// Object visitorInstance = visitorClass.newInstance();
		// Method m = visitorClass.getDeclaredMethod("visit",
		// ASTRelationalNestOp.class, Object.class);
		//
		// AbstractLogicalOperator sourceOp = (AbstractLogicalOperator) m
		// .invoke(visitorInstance, node, newData);
		//
		// ((ArrayList)data).add(sourceOp);
		// ((ArrayList)data).add(new Integer(0));
		// return data;
		// } catch (ClassNotFoundException e) {
		// throw new
		// RuntimeException("Objectrelational Plugin is missing in parser.",
		// e.getCause());
		// } catch (Exception e) {
		// throw new
		// RuntimeException("Error while parsing relational nest clause",
		// e.getCause());
		// }

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

		input = (AbstractLogicalOperator) ((ArrayList) node.jjtGetChild(0).jjtAccept(this, newData)).get(1);

		op.subscribeTo(input, input.getOutputSchema());

		nestAttributeIdentifier = (ASTIdentifier) node.jjtGetChild(1);

		nestAttributeName = nestAttributeIdentifier.getName();
		try {
			op.setNestAttribute(attrRes.getAttribute(nestAttributeName));
		} catch (AmgigiousAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		((ArrayList) data).add(op);
		((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTTestOp node, Object data) {
		TestAO op = new TestAO(node.getName());

		ArrayList newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));

		ArrayList returnData = (ArrayList) node.jjtGetChild(0).jjtAccept(this, newData);
		AbstractLogicalOperator inputForTest = (AbstractLogicalOperator) returnData.get(1);
		int sourceOutPort = ((Integer) returnData.get(2)).intValue();

		op.subscribeToSource(inputForTest, 0, sourceOutPort, inputForTest.getOutputSchema());

		((ArrayList) data).add(op);
		((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTBrokerOp node, Object data) {
		// We cannot use the same code as for ASTAccessOp, since
		// the broker also can but not has to have input operators.
		AttributeResolver attributeResolver = (AttributeResolver) ((ArrayList) data).get(0);

		ArrayList newData = new ArrayList();
		newData.add(attributeResolver);

		String brokerName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		BrokerAO broker = (BrokerAO) attributeResolver.getSource(brokerName);
		int curOutPort = broker.getSubscriptions().size();

		// first get the queue of the broker, if it has one
		// must be first, because otherwise the queue port mappings
		// would not match
		ArrayList returnData = null;
		if (node.hasQueue()) {
			returnData = (ArrayList) node.jjtGetChild(1).jjtAccept(this, newData);
			ILogicalOperator inputQueue = (ILogicalOperator) returnData.get(1);
			int sourceOutPort = ((Integer) returnData.get(2)).intValue();

			if (!broker.getQueueSchema().compatibleTo(inputQueue.getOutputSchema())) {
				throw new RuntimeException("Schema mismatch! \nBrokerQueueSchema: " + broker.getQueueSchema().toString()
						+ "\nInputQueueSchema: " + inputQueue.getOutputSchema());
			}

			int curInPort = broker.getSubscribedToSource().size();

			broker.subscribeToSource(inputQueue, curInPort, sourceOutPort, inputQueue.getOutputSchema());
		}

		// get the input ops of the broker
		// if no queue, then the second child is already a preceding operator
		// if queue, then the third child is the first preceding operator
		// the first child is the identifier
		int start = node.hasQueue() ? 2 : 1;
		for (int i = start; i < node.jjtGetNumChildren(); i++) {
			returnData = (ArrayList) node.jjtGetChild(i).jjtAccept(this, newData);
			ILogicalOperator inputOp = (ILogicalOperator) returnData.get(1);
			int sourceOutPort = ((Integer) returnData.get(2)).intValue();

			int curInPort = broker.getSubscribedToSource().size();
			broker.subscribeToSource(inputOp, curInPort, sourceOutPort, inputOp.getOutputSchema());
		}

		((ArrayList) data).add(broker);
		((ArrayList) data).add(new Integer(curOutPort));
		return data;
	}

	private PredictionExpression[] getPredictionExpressions(Node predictDefinition, SDFAttributeList outputSchema) {
		ArrayList<PredictionExpression> expressions = new ArrayList<PredictionExpression>();

		// aside from the last child, all children
		// must be ASTPredictionFunctionDefinitions
		for (int u = 0; u < predictDefinition.jjtGetNumChildren() - 1; u++) {
			ASTPredictionFunctionDefinition predFctDef = (ASTPredictionFunctionDefinition) predictDefinition.jjtGetChild(u);

			ASTIdentifier attr = (ASTIdentifier) predFctDef.jjtGetChild(0);
			ASTExpression predFct = (ASTExpression) predFctDef.jjtGetChild(1);

			PredictionExpression predFctExpr = null;
			try {
				predFctExpr = new PredictionExpression(attr.toString(), predFct.toString());
				predFctExpr.initAttributePaths(outputSchema);
			} catch (Exception e) {
				e.printStackTrace();
			}

			expressions.add(predFctExpr);
		}

		return expressions.toArray(new PredictionExpression[0]);
	}

	@Override
	public Object visit(ASTPredictionAssignOrOp node, Object data) {
		PredictionAssignAO prediction = new PredictionAssignAO();

		// pass only the attribute resolver to the children
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));

		ArrayList returnData = (ArrayList) node.jjtGetChild(0).jjtAccept(this, newData);
		AbstractLogicalOperator inputForPrediction = (AbstractLogicalOperator) returnData.get(1);
		int sourceOutPort = ((Integer) returnData.get(2)).intValue();

		prediction.subscribeToSource(inputForPrediction, 0, sourceOutPort, inputForPrediction.getOutputSchema());

		ASTIdentifier listIdentifier = (ASTIdentifier) node.jjtGetChild(1);
		prediction.initListPath(inputForPrediction.getOutputSchema(), listIdentifier.toString());

		for (int i = 2; i < node.jjtGetNumChildren(); i++) {

			// handle the standard prediction definitions
			if (node.jjtGetChild(i) instanceof ASTPredictionDefinition) {
				ASTPredictionDefinition predDef = (ASTPredictionDefinition) node.jjtGetChild(i);

				// only the attribute resolver will be passed
				// to the children
				newData = new ArrayList();
				newData.add(((ArrayList) data).get(0));

				// the last child of a prediction definition will be a predicate
				IPredicate predicate = (IPredicate) ((ArrayList) predDef.jjtGetChild(predDef.jjtGetNumChildren() - 1)
						.jjtAccept(this, newData)).get(1);

				initPredicate(predicate, prediction.getInputSchema(), null);

				prediction.setPredictionFunction(getPredictionExpressions(predDef, prediction.getOutputSchema()), predicate);
			}
			// handle the default prediction definition
			else if (node.jjtGetChild(i) instanceof ASTDefaultPredictionDefinition) {
				ASTDefaultPredictionDefinition predDef = (ASTDefaultPredictionDefinition) node.jjtGetChild(i);

				prediction.setDefaultPredictionFunction(getPredictionExpressions(predDef, prediction.getOutputSchema()));
			}
		}

		((ArrayList) data).add(prediction);
		((ArrayList) data).add(new Integer(0));

		return data;
	}

	/**
	 * no implementation needed: will parsed by buildKeyMap()
	 */
	@Override
	public Object visit(ASTKeyValueList node, Object data) {
		return null;
	}

	/**
	 * no implementation needed: will parsed by buildKeyMap()
	 */
	@Override
	public Object visit(ASTKeyValuePair node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTAssociationGenOp node, Object data) {
		IAttributeResolver attrRes = (IAttributeResolver) ((ArrayList) data).get(0);

		HypothesisGenerationAO gen = new HypothesisGenerationAO();

		ArrayList newData = new ArrayList();
		newData.add(attrRes);

		ArrayList<Object> childData = (ArrayList<Object>) node.jjtGetChild(0).jjtAccept(this, newData);
		int sourceOutPort = ((Integer) childData.get(2)).intValue();
		ILogicalOperator childOp = (ILogicalOperator) childData.get(1);
		gen.subscribeToSource(childOp, 0, sourceOutPort, childOp.getOutputSchema());

		newData = new ArrayList();
		newData.add(attrRes);

		childData = (ArrayList<Object>) node.jjtGetChild(1).jjtAccept(this, newData);
		sourceOutPort = ((Integer) childData.get(2)).intValue();
		childOp = (ILogicalOperator) childData.get(1);
		gen.subscribeToSource(childOp, 1, sourceOutPort, childOp.getOutputSchema());

		gen.initPaths(((ASTIdentifier) node.jjtGetChild(3)).getName(), ((ASTIdentifier) node.jjtGetChild(2)).getName());

		if(node.jjtGetNumChildren() > 4) {
			ASTIdentifier identifier = (ASTIdentifier) node.jjtGetChild(4);
			gen.setASSOCIATION_RECORD_NAME(identifier.getName());
		} if(node.jjtGetNumChildren() > 5) {
			ASTIdentifier identifier = (ASTIdentifier) node.jjtGetChild(5);
			gen.setPREDICTED_OBJECTS_NAME(identifier.getName());
		} if(node.jjtGetNumChildren() > 6) {
			ASTIdentifier identifier = (ASTIdentifier) node.jjtGetChild(6);
			gen.setSCANNED_OBJECTS_NAME(identifier.getName());
		}

		// pass only the attribute resolver to the children
		((ArrayList) data).add(gen);
		((ArrayList) data).add(new Integer(0));

		return data;
	}


	@Override
	public Object visit(ASTAssociationSelOp node, Object data) {
		AttributeResolver attrRes = (AttributeResolver) ((ArrayList) data).get(0);

		HypothesisSelectionAO selection = new HypothesisSelectionAO();

		ArrayList newData = new ArrayList();
		newData.add(attrRes);

		// subscribe to source
		ArrayList<Object> inputOpNode = (ArrayList<Object>) node.jjtGetChild(0).jjtAccept(this, newData);
		int sourceOutPort = ((Integer) inputOpNode.get(2)).intValue();
		ILogicalOperator inputOp = (ILogicalOperator) inputOpNode.get(1);
		selection.subscribeToSource(inputOp, 0, sourceOutPort, inputOp.getOutputSchema());

		// get name of this op
		ASTIdentifier identifier = (ASTIdentifier) node.jjtGetChild(1);
		String opName = identifier.getName();

		// get path to car data
		identifier = (ASTIdentifier) node.jjtGetChild(2);
		String pathNew = identifier.getName();

		// get path to old card data
		identifier = (ASTIdentifier) node.jjtGetChild(3);
		String pathOld = identifier.getName();

		// path initialization
		selection.setID(opName);
		selection.setNewObjListPath(pathNew);
		selection.setOldObjListPath(pathOld);

		// saving op for source lookup
		AssociationDictionary.getInstance().addSource(opName, selection);

		// constructing return values
		((ArrayList) data).add(selection);
		((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTAssociationSrcOp node, Object data) {
		AttributeResolver attrRes = (AttributeResolver) ((ArrayList) data).get(0);

		// get name and lookup operator
		ASTIdentifier identifier = (ASTIdentifier) node.jjtGetChild(0);
		String srcName = identifier.getName();
		HypothesisSelectionAO associationSource = AssociationDictionary.getInstance().getSource(srcName);
		if (associationSource == null) {
			throw new RuntimeException("The source cannot be found: " + srcName);
		}

		// get output-port of selection
		String number = ((ASTNumber) node.jjtGetChild(1)).getValue();
		Integer outputPort = new Integer(number);

		TestAO renameAO = new TestAO(srcName);
		renameAO.subscribeToSource(associationSource, 0, outputPort, associationSource.getOutputSchema());

		// constructing return values
		((ArrayList) data).add(renameAO);
		((ArrayList) data).add(new Integer(0));

		return data;
	}

	private HashMap<String, String> buildKeyMap(ASTKeyValueList list) {
		HashMap<String, String> params = new HashMap<String, String>();
		if (list != null) {
			for (int i = 0; i < list.jjtGetNumChildren(); i++) {
				ASTKeyValuePair valuePair = (ASTKeyValuePair) list.jjtGetChild(i);

				String key = ((ASTIdentifier) valuePair.jjtGetChild(0)).getName();

				Node valueNode = valuePair.jjtGetChild(1);
				if (valueNode instanceof ASTIdentifier) {
					params.put(key, ((ASTIdentifier) valueNode).getName());
				} else {
					params.put(key, (((ASTNumber) valueNode).getValue()));
				}
			}
		}
		return params;
	}

	@Override
	public Object visit(ASTSchemaConvertOp node, Object data) {
		ArrayList<Object> newData = new ArrayList<Object>();
		newData.add(((ArrayList) data).get(0));

		SchemaConvertAO scOp = new SchemaConvertAO();

		ArrayList returnData = ((ArrayList) node.jjtGetChild(0).jjtAccept(this, newData));
		AbstractLogicalOperator inputForSchemaConvert = (AbstractLogicalOperator) returnData.get(1);

		int sourceOutPort = ((Integer) returnData.get(2)).intValue();
		scOp.subscribeToSource(inputForSchemaConvert, 0, sourceOutPort, inputForSchemaConvert.getOutputSchema());

		((ArrayList) data).add(scOp);
		((ArrayList) data).add(new Integer(0));

		return data;

	}

	@Override
	public Object visit(ASTPredictionOp node, Object data) {
		PredictionAO ao = new PredictionAO();

		ArrayList newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));

		ArrayList leftInData = (ArrayList) node.jjtGetChild(0).jjtAccept(this, newData);
		AbstractLogicalOperator leftIn = (AbstractLogicalOperator) leftInData.get(1);
		int leftInSourceOutPort = ((Integer) leftInData.get(2)).intValue();

		newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));

		ArrayList rightInData = (ArrayList) node.jjtGetChild(1).jjtAccept(this, newData);
		AbstractLogicalOperator rightIn = (AbstractLogicalOperator) rightInData.get(1);
		int rightInSourceOutPort = ((Integer) rightInData.get(2)).intValue();

		String attributeName = ((ASTIdentifier) node.jjtGetChild(2)).getName();

		SDFAttributeList schema = rightIn.getOutputSchema();
		ao.initNeededAttributeIndices(schema, attributeName);

		ao.subscribeToSource(leftIn, 0, leftInSourceOutPort, leftIn.getOutputSchema());
		ao.subscribeToSource(rightIn, 1, rightInSourceOutPort, rightIn.getOutputSchema());

		newData.clear();
		newData.add(((ArrayList) data).get(0));

		((ArrayList) data).add(ao);
		((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTBenchmarkOp node, Object data) {

		// first child is preceeding operator
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));
		ArrayList inData = (ArrayList) node.jjtGetChild(0).jjtAccept(this, newData);
		AbstractLogicalOperator inputForBench = (AbstractLogicalOperator) inData.get(1);
		int inputSourceOutPort = ((Integer) inData.get(2)).intValue();

		// second child is selectivity
		double selectivity = Double.parseDouble(((ASTNumber) node.jjtGetChild(1)).getValue());

		// third child is duration
		int duration = Integer.parseInt(((ASTNumber) node.jjtGetChild(2)).getValue());

		try {
			Class<?> benchClass = Class.forName("de.uniol.inf.is.odysseus.benchmarker.impl.BenchmarkAO");

			// get the correct constructor
			Class[] params = new Class[2];
			params[0] = int.class;
			params[1] = double.class;

			Constructor constr = benchClass.getConstructor(params);

			Object[] paramValues = new Object[2];
			paramValues[0] = new Integer(duration);
			paramValues[1] = new Double(selectivity);

			Object bench = constr.newInstance(paramValues);

			Method m = benchClass.getMethod("subscribeToSource", ILogicalOperator.class, int.class, int.class,
					SDFAttributeList.class);

			m.invoke(bench, inputForBench, 0, inputSourceOutPort, inputForBench.getOutputSchema());

			((ArrayList) data).add(bench);
			((ArrayList) data).add(new Integer(0));

		} catch (ClassNotFoundException cnf) {
			cnf.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// old code
		// BenchmarkAO bench = new BenchmarkAO(duration, selectivity);
		// bench.subscribeToSource(inputForBench, 0, inputSourceOutPort,
		// inputForBench.getOutputSchema());
		//
		// ((ArrayList) data).add(bench);
		// ((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTBenchmarkOpExt node, Object data) {
		// first child is preceeding operator
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));
		ArrayList inData = (ArrayList) node.jjtGetChild(0).jjtAccept(this, newData);
		AbstractLogicalOperator inputForBench = (AbstractLogicalOperator) inData.get(1);
		int inputSourceOutPort = ((Integer) inData.get(2)).intValue();

		// second child is selectivity
		double selectivity = Double.parseDouble(((ASTNumber) node.jjtGetChild(1)).getValue());

		// third child is duration
		int duration = Integer.parseInt(((ASTNumber) node.jjtGetChild(2)).getValue());

		try {
			Class<?> benchClass = Class.forName("de.uniol.inf.is.odysseus.benchmarker.impl.BenchmarkAOExt");

			// get the correct constructor
			Class[] params = new Class[2];
			params[0] = int.class;
			params[1] = double.class;
			Constructor constr = benchClass.getConstructor(params);

			Object[] paramValues = new Object[2];
			paramValues[0] = new Integer(duration);
			paramValues[1] = new Double(selectivity);

			Object bench = constr.newInstance(paramValues);

			// Method[] ms = benchClass.getMethods();
			// for(Method m : ms){
			// Class[] paramTypes = m.getParameterTypes();
			// int i = 0;
			// }

			Method m = benchClass.getMethod("subscribeToSource", ILogicalOperator.class, int.class, int.class,
					SDFAttributeList.class);

			m.invoke(bench, inputForBench, 0, inputSourceOutPort, inputForBench.getOutputSchema());

			((ArrayList) data).add(bench);
			((ArrayList) data).add(new Integer(0));

		} catch (ClassNotFoundException cnf) {
			cnf.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// old code
		// BenchmarkAOExt bench = new BenchmarkAOExt(duration, selectivity);
		//
		// bench.subscribeToSource(inputForBench, 0, inputSourceOutPort,
		// inputForBench.getOutputSchema());
		//
		// ((ArrayList) data).add(bench);
		// ((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTTmpDataBouncerOp node, Object data) {
		IAttributeResolver attrRes = (IAttributeResolver) ((ArrayList) data).get(0);

		TemporaryDataBouncerAO bouncerAO = new TemporaryDataBouncerAO();

		ArrayList newData = new ArrayList();
		newData.add(attrRes);

		// subscribe bei der Assoziation (HypothesisSelektion)
		ArrayList<Object> childData = (ArrayList<Object>) node.jjtGetChild(0).jjtAccept(this, newData);
		int sourceOutPort = ((Integer) childData.get(2)).intValue();
		ILogicalOperator childOp = (ILogicalOperator) childData.get(1);
		bouncerAO.subscribeToSource(childOp, 0, sourceOutPort, childOp.getOutputSchema());

		// Assoziations Objektpfad
		ASTIdentifier identifier = (ASTIdentifier) node.jjtGetChild(1);
		String brokerObjListPath = identifier.getName();

		// Init AO paths
		bouncerAO.setObjListPath(brokerObjListPath);

		// get threshold
		ASTNumber number = (ASTNumber) node.jjtGetChild(2);

		Double threshold = 0.0;
		try {
			threshold = new Double(number.getValue());
		} catch (Exception e) {
		}

		// init operator
		ASTIdentifier operator = (ASTIdentifier) node.jjtGetChild(3);
		String op = "LESS";
		try {
			op = operator.getName();
		} catch (Exception e) {

		}

		// set threshold
		bouncerAO.setThreshold(threshold);

		// set operator
		bouncerAO.setOperator(op);

		((ArrayList) data).add(bouncerAO);
		((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTEvaluateOp node, Object data) {
		IAttributeResolver attrRes = (IAttributeResolver) ((ArrayList) data).get(0);

		EvaluationAO evalAO = new EvaluationAO();

		ArrayList newData = new ArrayList();
		newData.add(attrRes);

		// subscribe bei der Assoziation (HypothesisSelektion)
		ArrayList<Object> childData = (ArrayList<Object>) node.jjtGetChild(0).jjtAccept(this, newData);
		int sourceOutPort = ((Integer) childData.get(2)).intValue();
		ILogicalOperator childOp = (ILogicalOperator) childData.get(1);
		evalAO.subscribeToSource(childOp, 0, sourceOutPort, childOp.getOutputSchema());

		// Assoziations Objektpfad
		ASTIdentifier identifier = (ASTIdentifier) node.jjtGetChild(1);
		String associationObjListPath = identifier.getName();

		newData = new ArrayList();
		newData.add(attrRes);

		// subscribe bei der Filterung
		childData = (ArrayList<Object>) node.jjtGetChild(2).jjtAccept(this, newData);
		sourceOutPort = ((Integer) childData.get(2)).intValue();
		childOp = (ILogicalOperator) childData.get(1);
		evalAO.subscribeToSource(childOp, 1, sourceOutPort, childOp.getOutputSchema());

		// get filteringObjListPaths
		identifier = (ASTIdentifier) node.jjtGetChild(3);
		String filteringObjListPaths = identifier.getName();

		newData = new ArrayList();
		newData.add(attrRes);

		// subscribe bei bei temporären Broker
		childData = (ArrayList<Object>) node.jjtGetChild(4).jjtAccept(this, newData);
		sourceOutPort = ((Integer) childData.get(2)).intValue();
		childOp = (ILogicalOperator) childData.get(1);
		evalAO.subscribeToSource(childOp, 2, sourceOutPort, childOp.getOutputSchema());

		// get brokerObjListPath
		identifier = (ASTIdentifier) node.jjtGetChild(5);
		String brokerObjListPath = identifier.getName();

		// Init AO paths
		evalAO.initPaths(associationObjListPath, filteringObjListPaths, brokerObjListPath);

		// get threshold
		ASTNumber number = (ASTNumber) node.jjtGetChild(6);

		Double threshold = 0.0;
		try {
			threshold = new Double(number.getValue());
		} catch (Exception e) {
		}

		// set threshold
		evalAO.setThreshold(threshold);

		((ArrayList) data).add(evalAO);
		((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTBufferOp node, Object data) {
		// first child is preceeding operator
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));

		ArrayList inData = (ArrayList) node.jjtGetChild(0).jjtAccept(this, newData);
		AbstractLogicalOperator inputForBuffer = (AbstractLogicalOperator) inData.get(1);
		int inputSourceOutPort = ((Integer) inData.get(2)).intValue();

		// second child is selectivity
		String type = ((ASTIdentifier) node.jjtGetChild(1)).getName();

		try {
			Class<?> bufferClass = Class.forName("de.uniol.inf.is.odysseus.logicaloperator.BufferAO");

			Object buffer = bufferClass.newInstance();

			Method mSetType = bufferClass.getDeclaredMethod("setType", String.class);

			mSetType.invoke(buffer, type);

			Method mSubscribeToSource = bufferClass.getMethod("subscribeToSource", ILogicalOperator.class, int.class,
					int.class, SDFAttributeList.class);

			mSubscribeToSource.invoke(buffer, inputForBuffer, 0, inputSourceOutPort, inputForBuffer.getOutputSchema());

			((ArrayList) data).add(buffer);
			((ArrayList) data).add(new Integer(0));

		} catch (ClassNotFoundException cnf) {
			cnf.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// old Code
		// BufferAO buffer = new BufferAO();
		// buffer.setType(type);
		//
		// buffer.subscribeToSource(inputForBuffer, 0, inputSourceOutPort,
		// inputForBuffer.getOutputSchema());
		//
		// ((ArrayList) data).add(buffer);
		// ((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTExistOp node, Object data) {
		// first child is preceeding operator
		ArrayList newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));

		ExistenceAO exist = new ExistenceAO();

		if (node.getType().equals("exist")) {
			exist.setType(ExistenceAO.Type.EXISTS);
		} else if (node.getType().equals("not exist")) {
			exist.setType(ExistenceAO.Type.NOT_EXISTS);
		} else {
			throw new RuntimeException("ExistenceAO-Type '" + node.getType() + "' is invalid.");
		}

		// get left and right input operators
		// the right input is only for checking
		// the left input contains the data
		// that is passed by this operator
		ArrayList inputData = (ArrayList) node.jjtGetChild(0).jjtAccept(this, newData);
		AbstractLogicalOperator leftInput = (AbstractLogicalOperator) inputData.get(1);
		int leftSourceOutPort = ((Integer) inputData.get(2)).intValue();

		newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));

		inputData = (ArrayList) node.jjtGetChild(1).jjtAccept(this, newData);
		AbstractLogicalOperator rightInput = (AbstractLogicalOperator) inputData.get(1);
		int rightSourceOutPort = ((Integer) inputData.get(2)).intValue();

		exist.subscribeToSource(leftInput, 0, leftSourceOutPort, leftInput.getOutputSchema());
		exist.subscribeToSource(rightInput, 1, rightSourceOutPort, rightInput.getOutputSchema());

		// get the predicate
		newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));

		inputData = (ArrayList) node.jjtGetChild(2).jjtAccept(this, newData);
		IPredicate predicate = (IPredicate) inputData.get(1);

		initPredicate(predicate, exist.getInputSchema(0), exist.getInputSchema(1));

		exist.setPredicate(predicate);

		((ArrayList) data).add(exist);
		((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTPunctuationOp node, Object data) {
		// TODO Auto-generated method stub
		PunctuationAO puncOp = new PunctuationAO();

		ArrayList newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));

		// the first child is the input operator;
		ArrayList returnData = ((ArrayList) node.jjtGetChild(0).jjtAccept(this, newData));
		AbstractLogicalOperator inputForPunctuation = (AbstractLogicalOperator) returnData.get(1);
		int sourceOutPort = ((Integer) returnData.get(2)).intValue();
		puncOp.subscribeToSource(inputForPunctuation, 0, sourceOutPort, inputForPunctuation.getOutputSchema());

		// the second child is the number of elements
		// to be transferred before a punctuation is
		// generated.
		int noElems = Integer.parseInt(((ASTNumber) node.jjtGetChild(1)).getValue());

		puncOp.setPunctuationCount(noElems);

		((ArrayList) data).add(puncOp);
		((ArrayList) data).add(new Integer(0));
		return data;
	}

	@Override
	public Object visit(ASTBrokerInitOp node, Object data) {
		BrokerInitAO ao = new BrokerInitAO();

		ArrayList newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));

		ArrayList returnData = (ArrayList) node.jjtGetChild(0).jjtAccept(this, newData);
		AbstractLogicalOperator inputOp = (AbstractLogicalOperator) returnData.get(1);
		int sourceOutPort = ((Integer) returnData.get(2)).intValue();

		ao.subscribeToSource(inputOp, 0, sourceOutPort, inputOp.getOutputSchema());

		ao.setSize(Integer.valueOf(((ASTNumber) node.jjtGetChild(1)).getValue()));

		((ArrayList) data).add(ao);
		((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTJDVESinkOp node, Object data) {
		JDVESinkAO ao = new JDVESinkAO();

		ArrayList newData = new ArrayList();
		newData.add(((ArrayList) data).get(0));

		ArrayList returnData = (ArrayList) node.jjtGetChild(0).jjtAccept(this, newData);
		AbstractLogicalOperator inputOp = (AbstractLogicalOperator) returnData.get(1);
		int sourceOutPort = ((Integer) returnData.get(2)).intValue();

		ao.subscribeToSource(inputOp, 0, sourceOutPort, inputOp.getOutputSchema());

		ao.setHostAdress(((ASTHost) node.jjtGetChild(1)).getValue());
		ao.setPort(Integer.valueOf(((ASTNumber) node.jjtGetChild(2)).getValue()));
		ao.setServerType(((ASTIdentifier) node.jjtGetChild(3)).getName());

		((ArrayList) data).add(ao);
		((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTHost node, Object data) {
		return node.getValue();
	}

	@Override
	public Object visit(ASTScarsXMLProfilerOp node, Object data) {
		IAttributeResolver attrRes = (IAttributeResolver) ((ArrayList) data).get(0);

		XMLProfilerAO profilerAO = new XMLProfilerAO();

		ArrayList newData = new ArrayList();
		newData.add(attrRes);

		ArrayList<Object> childData = (ArrayList<Object>) node.jjtGetChild(0).jjtAccept(this, newData);
		int sourceOutPort = ((Integer) childData.get(2)).intValue();
		ILogicalOperator childOp = (ILogicalOperator) childData.get(1);
		profilerAO.subscribeToSource(childOp, 0, sourceOutPort, childOp.getOutputSchema());

		ASTIdentifier identifier = (ASTIdentifier) node.jjtGetChild(1);
		String fileName = identifier.getName();

		ASTIdentifier identifier2 = (ASTIdentifier) node.jjtGetChild(2);
		String operatorName = identifier2.getName();

		profilerAO.setFileName(fileName);
		profilerAO.setOperatorName(operatorName);

		((ArrayList) data).add(profilerAO);
		((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTDistanceObjectSelectorOp node, Object data) {
		IAttributeResolver attrRes = (IAttributeResolver) ((ArrayList) data).get(0);

		DistanceObjectSelectorAO ao = new DistanceObjectSelectorAO();

		ArrayList newData = new ArrayList();
		newData.add(attrRes);

		ArrayList<Object> childData = (ArrayList<Object>) node.jjtGetChild(0).jjtAccept(this, newData);
		int sourceOutPort = ((Integer) childData.get(2)).intValue();
		ILogicalOperator childOp = (ILogicalOperator) childData.get(1);
		ao.subscribeToSource(childOp, 0, sourceOutPort, childOp.getOutputSchema());

		// Assoziations Objektpfad
		ASTIdentifier identifier = (ASTIdentifier) node.jjtGetChild(1);
		ao.setTrackedObjectList(identifier.getName());

		identifier = (ASTIdentifier) node.jjtGetChild(2);
		ao.setTrackedObjectX(identifier.getName());

		identifier = (ASTIdentifier) node.jjtGetChild(3);
		ao.setTrackedObjectY(identifier.getName());

		// get threshold
		ASTNumber number = (ASTNumber) node.jjtGetChild(4);
		Double threshold = 0.0;
		try {
			threshold = new Double(number.getValue());
		} catch (Exception e) {
		}
		// set threshold
		ao.setDistanceThresholdXLeft(threshold);

		number = (ASTNumber) node.jjtGetChild(5);
		threshold = 0.0;
		try {
			threshold = new Double(number.getValue());
		} catch (Exception e) {
		}
		// set threshold
		ao.setDistanceThresholdXRight(threshold);

		number = (ASTNumber) node.jjtGetChild(6);
		threshold = 0.0;
		try {
			threshold = new Double(number.getValue());
		} catch (Exception e) {
		}
		// set threshold
		ao.setDistanceThresholdYLeft(threshold);

		number = (ASTNumber) node.jjtGetChild(7);
		threshold = 0.0;
		try {
			threshold = new Double(number.getValue());
		} catch (Exception e) {
		}
		// set threshold
		ao.setDistanceThresholdYRight(threshold);

		((ArrayList) data).add(ao);
		((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTDistanceObjectSelectorOp_Andre node, Object data) {
		IAttributeResolver attrRes = (IAttributeResolver) ((ArrayList) data).get(0);

		DistanceObjectSelectorAOAndre ao = new DistanceObjectSelectorAOAndre(attrRes);

		ArrayList newData = new ArrayList();
		newData.add(attrRes);

		ArrayList<Object> childData = (ArrayList<Object>) node.jjtGetChild(0).jjtAccept(this, newData);
		int sourceOutPort = ((Integer) childData.get(2)).intValue();
		ILogicalOperator childOp = (ILogicalOperator) childData.get(1);
		ao.subscribeToSource(childOp, 0, sourceOutPort, childOp.getOutputSchema());

		// Assoziations Objektpfad
		ASTIdentifier identifier = (ASTIdentifier) node.jjtGetChild(1);
		ao.setTrackedObjectList(identifier.getName());


		// add predicates
		String condStr1 = "(cos(streamCarsBroker.scan:cars:car:heading) * streamCarsBroker.scan:cars:car:velocity * 0.001) > 0.0";
		SDFObjectRelationalExpression cond1Expr = new SDFObjectRelationalExpression("", condStr1, attrRes);
		IPredicate cond1 = new ObjectRelationalPredicate(cond1Expr);
		String solStr1 = "(10000 - streamCarsBroker.scan:cars:car:posx) / ((cos(streamCarsBroker.scan:cars:car:heading) * streamCarsBroker.scan:cars:car:velocity * 0.001))";
		SDFObjectRelationalExpression sol1 = new SDFObjectRelationalExpression("", solStr1, attrRes);

		String condStr2 = "(cos(streamCarsBroker.scan:cars:car:heading) * streamCarsBroker.scan:cars:car:velocity * 0.001) < 0.0";
		SDFObjectRelationalExpression cond2Expr = new SDFObjectRelationalExpression("", condStr2, attrRes);
		IPredicate cond2 = new ObjectRelationalPredicate(cond2Expr);
		String solStr2 = solStr1;
		SDFObjectRelationalExpression sol2 = new SDFObjectRelationalExpression("", solStr2, attrRes);

		String condStr3 = "(cos(streamCarsBroker.scan:cars:car:heading) * streamCarsBroker.scan:cars:car:velocity * 0.001) == 0 AND streamCarsBroker.scan:cars:car:posx < 10000";
		SDFObjectRelationalExpression cond3Expr = new SDFObjectRelationalExpression("", condStr3, attrRes);
		IPredicate cond3 = new ObjectRelationalPredicate(cond3Expr);
		String solStr3 = "1 < 2";
		SDFObjectRelationalExpression sol3 = new SDFObjectRelationalExpression("", solStr3, attrRes);

		String condStr4 = "(cos(streamCarsBroker.scan:cars:car:heading) * streamCarsBroker.scan:cars:car:velocity * 0.001) == 0 AND streamCarsBroker.scan:cars:car:posx > 10000";
		SDFObjectRelationalExpression cond4Expr = new SDFObjectRelationalExpression("", condStr4, attrRes);
		IPredicate cond4 = new ObjectRelationalPredicate(cond4Expr);
		String solStr4 = "1 > 2";
		SDFObjectRelationalExpression sol4 = new SDFObjectRelationalExpression("", solStr4, attrRes);

		initPredicate(cond1, childOp.getOutputSchema(), null);
		initPredicate(cond2, childOp.getOutputSchema(), null);
		initPredicate(cond3, childOp.getOutputSchema(), null);
		initPredicate(cond4, childOp.getOutputSchema(), null);

		HashMap<IPredicate, SDFObjectRelationalExpression> solutions = new HashMap<IPredicate, SDFObjectRelationalExpression>();
		solutions.put(cond1, sol1);
		solutions.put(cond2, sol2);
		solutions.put(cond3, sol3);
		solutions.put(cond4, sol4);

		ao.setSolutions(solutions);

//
//		identifier = (ASTIdentifier) node.jjtGetChild(2);
//		ao.setTrackedObjectX(identifier.getName());
//
//		identifier = (ASTIdentifier) node.jjtGetChild(3);
//		ao.setTrackedObjectY(identifier.getName());
//
//		// get threshold
//		ASTNumber number = (ASTNumber) node.jjtGetChild(4);
//		Double threshold = 0.0;
//		try {
//			threshold = new Double(number.getValue());
//		} catch (Exception e) {
//		}
//		// set threshold
//		ao.setDistanceThresholdXLeft(threshold);
//
//		number = (ASTNumber) node.jjtGetChild(5);
//		threshold = 0.0;
//		try {
//			threshold = new Double(number.getValue());
//		} catch (Exception e) {
//		}
//		// set threshold
//		ao.setDistanceThresholdXRight(threshold);
//
//		number = (ASTNumber) node.jjtGetChild(6);
//		threshold = 0.0;
//		try {
//			threshold = new Double(number.getValue());
//		} catch (Exception e) {
//		}
//		// set threshold
//		ao.setDistanceThresholdYLeft(threshold);
//
//		number = (ASTNumber) node.jjtGetChild(7);
//		threshold = 0.0;
//		try {
//			threshold = new Double(number.getValue());
//		} catch (Exception e) {
//		}
//		// set threshold
//		ao.setDistanceThresholdYRight(threshold);

		((ArrayList) data).add(ao);
		((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTAssociationExpressionEvalOp node, Object data) {

		IAttributeResolver attrRes = (IAttributeResolver) ((ArrayList) data).get(0);

		HypothesisExpressionEvaluationAO ao = new HypothesisExpressionEvaluationAO();

		ArrayList newData = new ArrayList();
		newData.add(attrRes);

		ArrayList<Object> childData = (ArrayList<Object>) node.jjtGetChild(0).jjtAccept(this, newData);
		int sourceOutPort = ((Integer) childData.get(2)).intValue();
		ILogicalOperator childOp = (ILogicalOperator) childData.get(1);
		ao.subscribeToSource(childOp, 0, sourceOutPort, childOp.getOutputSchema());

		ASTIdentifier identifier = (ASTIdentifier) node.jjtGetChild(1);
		ao.setScanObjListPath(identifier.getName());

		identifier = (ASTIdentifier) node.jjtGetChild(2);
		ao.setPredObjListPath(identifier.getName());
		
		ASTExpression expression = (ASTExpression) node.jjtGetChild(3);
		ao.setExpressionString(expression.toString());

		((ArrayList) data).add(ao);
		((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTAssociationExpressionGateOp node, Object data) {
		IAttributeResolver attrRes = (IAttributeResolver) ((ArrayList) data).get(0);

		HypothesisExpressionGatingAO ao = new HypothesisExpressionGatingAO();

		ArrayList newData = new ArrayList();
		newData.add(attrRes);

		ArrayList<Object> childData = (ArrayList<Object>) node.jjtGetChild(0).jjtAccept(this, newData);
		int sourceOutPort = ((Integer) childData.get(2)).intValue();
		ILogicalOperator childOp = (ILogicalOperator) childData.get(1);
		ao.subscribeToSource(childOp, 0, sourceOutPort, childOp.getOutputSchema());

		ASTIdentifier identifier = (ASTIdentifier) node.jjtGetChild(1);
		ao.setScanObjListPath(identifier.getName());

		identifier = (ASTIdentifier) node.jjtGetChild(2);
		ao.setPredObjListPath(identifier.getName());

		ASTExpression expression = (ASTExpression) node.jjtGetChild(3);
		ao.setExpressionString(expression.toString());

		((ArrayList) data).add(ao);
		((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTFilterExpGainOp node, Object data) {
		IAttributeResolver attrRes = (IAttributeResolver) ((ArrayList) data).get(0);

		FilterExpressionGainAO ao = new FilterExpressionGainAO();

		ArrayList newData = new ArrayList();
		newData.add(attrRes);

		ArrayList<Object> childData = (ArrayList<Object>) node.jjtGetChild(0).jjtAccept(this, newData);
		int sourceOutPort = ((Integer) childData.get(2)).intValue();
		ILogicalOperator childOp = (ILogicalOperator) childData.get(1);
		ao.subscribeToSource(childOp, 0, sourceOutPort, childOp.getOutputSchema());

		ASTIdentifier identifier = (ASTIdentifier) node.jjtGetChild(1);
		ao.setScannedListPath(identifier.getName());

		identifier = (ASTIdentifier) node.jjtGetChild(2);
		ao.setPredictedListPath(identifier.getName());

		ASTExpression expression = (ASTExpression) node.jjtGetChild(3);
		ao.setExpressionString(expression.toString());
		
		ArrayList<String> restrictedScanVariables = new ArrayList<String>();
		ArrayList<String> restrictedPredVariables = new ArrayList<String>();
		List<String> currentList;
		currentList = restrictedScanVariables;
		for (int i = 4; i < node.jjtGetNumChildren(); i++) {

			identifier = (ASTIdentifier) node.jjtGetChild(i);
			
			if(identifier.getName().equals("SEPARATOR")) {
				currentList = restrictedPredVariables;
				continue;
			}
			currentList.add(identifier.getName());
		}
		ao.setRestrictedScanVariables(restrictedScanVariables);
		ao.setRestrictedPredVariables(restrictedPredVariables);

		((ArrayList) data).add(ao);
		((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTFilterExpEstimateOp node, Object data) {
		IAttributeResolver attrRes = (IAttributeResolver) ((ArrayList) data).get(0);

		FilterExpressionEstimateUpdateAO ao = new FilterExpressionEstimateUpdateAO();

		ArrayList newData = new ArrayList();
		newData.add(attrRes);

		ArrayList<Object> childData = (ArrayList<Object>) node.jjtGetChild(0).jjtAccept(this, newData);
		int sourceOutPort = ((Integer) childData.get(2)).intValue();
		ILogicalOperator childOp = (ILogicalOperator) childData.get(1);
		ao.subscribeToSource(childOp, 0, sourceOutPort, childOp.getOutputSchema());

		ASTIdentifier identifier = (ASTIdentifier) node.jjtGetChild(1);
		ao.setNewObjListPath(identifier.getName());

		identifier = (ASTIdentifier) node.jjtGetChild(2);
		ao.setOldObjListPath(identifier.getName());
		
		Map<String, String> functionList = new HashMap<String, String>();
		ASTDefaultPredictionDefinition prediction = (ASTDefaultPredictionDefinition) node.jjtGetChild(3);
		for (int i = 0; i < prediction.jjtGetNumChildren(); i++) {
			ASTPredictionFunctionDefinition function = (ASTPredictionFunctionDefinition) prediction.jjtGetChild(i);
			
			ASTIdentifier target = (ASTIdentifier) function.jjtGetChild(0);
			ASTExpression functionString = (ASTExpression) function.jjtGetChild(1);
			
			functionList.put(target.getName(), functionString.toString());
		}
		ao.setExpressions(functionList);
		
		((ArrayList) data).add(ao);
		((ArrayList) data).add(new Integer(0));

		return data;
	}

	@Override
	public Object visit(ASTFilterExpCovarianceOp node, Object data) {
		IAttributeResolver attrRes = (IAttributeResolver) ((ArrayList) data).get(0);

		FilterExpressionCovarianceUpdateAO ao = new FilterExpressionCovarianceUpdateAO();

		ArrayList newData = new ArrayList();
		newData.add(attrRes);

		ArrayList<Object> childData = (ArrayList<Object>) node.jjtGetChild(0).jjtAccept(this, newData);
		int sourceOutPort = ((Integer) childData.get(2)).intValue();
		ILogicalOperator childOp = (ILogicalOperator) childData.get(1);
		ao.subscribeToSource(childOp, 0, sourceOutPort, childOp.getOutputSchema());

		ASTIdentifier identifier = (ASTIdentifier) node.jjtGetChild(1);
		ao.setScannedListPath(identifier.getName());

		identifier = (ASTIdentifier) node.jjtGetChild(2);
		ao.setPredictedListPath(identifier.getName());

		ASTExpression expression = (ASTExpression) node.jjtGetChild(3);
		ao.setExpressionString(expression.toString());

		((ArrayList) data).add(ao);
		((ArrayList) data).add(new Integer(0));

		return data;
	}

}

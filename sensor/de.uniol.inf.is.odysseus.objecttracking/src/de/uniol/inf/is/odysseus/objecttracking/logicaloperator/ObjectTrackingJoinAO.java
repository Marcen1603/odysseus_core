package de.uniol.inf.is.odysseus.objecttracking.logicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import com.maplesoft.externalcall.MapleException;
import com.maplesoft.openmaple.Algebraic;
import com.maplesoft.openmaple.Engine;

import de.uniol.inf.is.odysseus.base.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.base.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.base.predicate.OrPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.base.JoinAO;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.objecttracking.metadata.LinearProbabilityPredictionFunction;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.AndRangePredicate;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.IRangePredicate;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.ISolution;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.OrRangePredicate;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.RelationalRangePredicate;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.MapleResultParserFacade;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListMetadataTypes;
import de.uniol.inf.is.odysseus.objecttracking.util.MapleFacade;
import de.uniol.inf.is.odysseus.objecttracking.util.OdysseusMapleCallBack;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

@SuppressWarnings("unchecked")
public class ObjectTrackingJoinAO extends JoinAO implements IHasRangePredicates{

	private SDFAttributeListExtended outputSchema;
	
	private boolean initialized;
	
	/**
	 * In this.predictionFunctions we have different prediction functions for
	 * different tuples according to predicate these tuples fulfill. 
	 * Each prediction functions consists of SDFExpressions
	 * for each attribute. So, what we have to do is to generate a range predicate
	 * for each prediction function. This range predicate will be stored in this
	 * map under the same key (of type IPredicate) that is used for the prediction
	 * function from which this range predicate is generated.
	 * The tuples that have a prediction function also have the predicate
	 * that leads to the prediction function, so we can use this predicate attached
	 * to the tuple as a key and return the resulting rangePredicate.
	 */
	private Map<IPredicate, IRangePredicate> rangePredicates;
	
	
	public Map<IPredicate, IRangePredicate> getRangePredicates() {
		return rangePredicates;
	}


	/** 
	 * The first measurement attribute has not necessarily to be the
	 * first attribute in the schema and so on. So, we have to find the measurement
	 * attribute positions.
	 * 
	 * This array contains the positions of the measurement values.
	 * restrictList[0] contains the position of the first measurement attribute in the
	 * schema, restrictList[1] contains the position of the second measurement attribute
	 * in the schema and so on.
	 * 
	 * This list is only necessary for computing the new covariance matrix.
	 * TODO: Manipulation der Covariance-Matrix in den RangePredicates berücksichtigen.
	 */
	private int[] restrictList;
	
	/**
	 * This list contains true, for each field in restrictList, if the corresponding
	 * attribute is from right schema
	 */
	private boolean[] fromRightChannel;
	
	public ObjectTrackingJoinAO(){
		super();
		this.initialized = false;
	}

	public ObjectTrackingJoinAO(ObjectTrackingJoinAO original) {
		super(original);
		this.rangePredicates = original.rangePredicates;
		this.initialized = original.initialized;
	}

	public ObjectTrackingJoinAO(IPredicate<?> predicate) {
		super(predicate);
	}

	@Override
	public ObjectTrackingJoinAO clone() {
		return new ObjectTrackingJoinAO(this);
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		// the output schema contains the attributes
		// from the left and the attributes from the right.
		// It also contains the prediction functions from
		// the left and from the right.
		
		// The Sum of all InputSchema
		if (outputSchema == null || recalcOutputSchemata){
			outputSchema = new SDFAttributeListExtended();
			
			Map<IPredicate, IPredictionFunction> newPredictionFunctions = new HashMap<IPredicate, IPredictionFunction>();
			
			// there can only be two input schemas
			SDFAttributeListExtended leftInputSchema = (SDFAttributeListExtended)getSubscribedToSource(0).getSchema();
			SDFAttributeListExtended rightInputSchema = (SDFAttributeListExtended)getSubscribedToSource(1).getSchema();
			
			outputSchema.addAttributes(leftInputSchema);
			outputSchema.addAttributes(rightInputSchema);
			
			Map<IPredicate, IPredictionFunction> leftFcts = (Map<IPredicate, IPredictionFunction>) leftInputSchema.getMetadata(SDFAttributeListMetadataTypes.PREDICTION_FUNCTIONS);
			Map<IPredicate, IPredictionFunction> rightFcts = (Map<IPredicate, IPredictionFunction>) rightInputSchema.getMetadata(SDFAttributeListMetadataTypes.PREDICTION_FUNCTIONS);
			
			for(Entry<IPredicate, IPredictionFunction> leftEntry: leftFcts.entrySet()){
				for(Entry<IPredicate, IPredictionFunction> rightEntry: rightFcts.entrySet()){
					AndPredicate newPredicate = new AndPredicate(leftEntry.getKey().clone(), rightEntry.getKey().clone());
					IPredictionFunction newFunction = new LinearProbabilityPredictionFunction();
					
					IPredictionFunction leftFct = leftEntry.getValue();
					IPredictionFunction rightFct = rightEntry.getValue();
					
					SDFExpression[] newExpressions = new SDFExpression[leftFct.getExpressions().length + rightFct.getExpressions().length];
					for(int i = 0; i<leftFct.getExpressions().length; i++){
						newExpressions[i] = 
							leftFct.getExpressions()[i] != null ? 
									leftFct.getExpressions()[i].clone() : 
									null;
					}
					for(int i = leftFct.getExpressions().length; i<newExpressions.length; i++){
						newExpressions[i] = 
							rightFct.getExpressions()[i - leftFct.getExpressions().length] != null ? 
									rightFct.getExpressions()[i - leftFct.getExpressions().length].clone() : 
									null;
					}
					
					// now the attribute positions in each expression have to be reinitialized
					for(int i = 0; i<newExpressions.length; i++){
						if(newExpressions[i] != null){
							newExpressions[i].initAttributePositions(outputSchema);
						}
					}
					newFunction.setExpressions(newExpressions);
					
					int[][] vars = new int[newExpressions.length][];
					for(int u = 0; u<newExpressions.length; u++){
						SDFExpression expression = newExpressions[u];
						if(expression != null)
							vars[u] = expression.getAttributePositions();
					}
					newFunction.setVariables(vars);
					
					newPredictionFunctions.put(newPredicate, newFunction);
				}
			}			
			recalcOutputSchemata = false;
			outputSchema.setMetadata(SDFAttributeListMetadataTypes.PREDICTION_FUNCTIONS, newPredictionFunctions);
		}
		return outputSchema;
		
	}
	
	/**
	 * The input schema for this operator has been set with
	 * the subscription to its source operator. After this has
	 * been done, we still have to transform the predicate of
	 * this operator into a range predicate according to the
	 * prediction functions of the serveral attributes. This
	 * has to be done for each combination of prediction
	 * functions from left and right
	 */
	public void init(IAttributeResolver attributeResolver){
		this.initRestrictList();
		
		// there can only be two input schemas
		SDFAttributeListExtended leftInputSchema = (SDFAttributeListExtended)getSubscribedToSource(0).getSchema();
		SDFAttributeListExtended rightInputSchema = (SDFAttributeListExtended)getSubscribedToSource(1).getSchema();
		
		Map<IPredicate, IPredictionFunction> leftFcts = (Map<IPredicate, IPredictionFunction>) leftInputSchema.getMetadata(SDFAttributeListMetadataTypes.PREDICTION_FUNCTIONS);
		Map<IPredicate, IPredictionFunction> rightFcts = (Map<IPredicate, IPredictionFunction>) rightInputSchema.getMetadata(SDFAttributeListMetadataTypes.PREDICTION_FUNCTIONS);		
		
		this.rangePredicates = new HashMap<IPredicate, IRangePredicate>();
		for(Entry<IPredicate, IPredictionFunction> leftEntry : leftFcts.entrySet()){
			for(Entry<IPredicate, IPredictionFunction> rightEntry: rightFcts.entrySet()){
				AndPredicate newPredicate = new AndPredicate(leftEntry.getKey(), rightEntry.getKey());
				
				IRangePredicate rangePredicate = this.generateRangePredicate(
						this.getPredicate(),
						leftEntry.getValue().getExpressions(), 
						rightEntry.getValue().getExpressions(), 
						attributeResolver);
				this.rangePredicates.put(newPredicate, rangePredicate);
			}
		}
	}
	
	private IRangePredicate generateRangePredicate(IPredicate joinPredicate, SDFExpression[] leftPredFct, SDFExpression[] rightPredFct, IAttributeResolver attributeResolver){
		if(joinPredicate instanceof AndPredicate){
			return new AndRangePredicate(
					generateRangePredicate(((AndPredicate)joinPredicate).getLeft(), leftPredFct, rightPredFct, attributeResolver), 
					generateRangePredicate(((AndPredicate)joinPredicate).getRight(), leftPredFct, rightPredFct, attributeResolver));
		}else if(joinPredicate instanceof OrPredicate){
			return new OrRangePredicate(
					generateRangePredicate(((OrPredicate)joinPredicate).getLeft(), leftPredFct, rightPredFct, attributeResolver), 
					generateRangePredicate(((OrPredicate)joinPredicate).getRight(), leftPredFct, rightPredFct, attributeResolver));
		}else if(joinPredicate instanceof RelationalPredicate){
			SDFAttributeListExtended leftSchema = (SDFAttributeListExtended)getSubscribedToSource(0).getSchema();
			SDFAttributeListExtended rightSchema = (SDFAttributeListExtended)getSubscribedToSource(1).getSchema();
			
			List<SDFAttribute> neededAttributes = ((RelationalPredicate)joinPredicate).getAttributes();
			String predicateString = joinPredicate.toString();
			
			StringTokenizer tokens = new StringTokenizer(predicateString,  IRangePredicate.tokenizerDelimiters, true);
			
			// for each occurence of an attribute in the expression, we have to substitute this occurence by
			// the corresponding prediction function of that attribute
			String rangePredicateExpression = "";
			while(tokens.hasMoreTokens()){
				String token = tokens.nextToken();
				boolean found = false;
				for(SDFAttribute curAttr: neededAttributes){
					if(token.equals(curAttr.toPointString())){
						int attrPos = leftSchema.indexOf(curAttr);
						boolean fromRightChannel = false;
						
						// the attribute can also be in the right schema
						if(attrPos == -1){
							attrPos = rightSchema.indexOf(curAttr);
							fromRightChannel = true;
						}
						
						if(!fromRightChannel){
							if(leftPredFct[attrPos] == null){
								rangePredicateExpression += token;
							}else{
								rangePredicateExpression += "(" + leftPredFct[attrPos].toString() +  ")";
							}
						}
						else{
							if(rightPredFct[attrPos] == null){
								rangePredicateExpression += token;
							}else{
								rangePredicateExpression += "(" + rightPredFct[attrPos].toString() +  ")";
							}
						}
						
						found = true;
						break;
					}
				}
				if(!found){
					rangePredicateExpression += token;
				}
				
			}
			
			// the RangePredicateExpression must be solved for t by Maple
			Map<IPredicate, ISolution> solutions = MapleFacade.getInstance().solveInequality(rangePredicateExpression, attributeResolver);
		
 			RelationalRangePredicate rangePredicate = new RelationalRangePredicate(solutions);
 			rangePredicate.init(leftSchema, rightSchema);
 			
 			return rangePredicate;	
			
		}else if(joinPredicate instanceof NotPredicate){
			// exchange the compare operator
			// < to >=
			// <= to >
			// >= to <
			// > to <=
			// it is more complex, since
			// !(a AND b) is allowed, what is
			// the same as (!a or !b)
			throw new UnsupportedOperationException();
		}
		return null;
	}
	
	
	private void initRestrictList(){
		ArrayList<Integer> tempList = new ArrayList<Integer>();
		ArrayList<Boolean> fromRightChannelTmp = new ArrayList<Boolean>();
		
		SDFAttributeList leftSchema = this.getSubscribedToSource(0).getSchema();
		SDFAttributeList rightSchema = this.getSubscribedToSource(1).getSchema();
		// first add the positions of the left schema
		for(int i = 0; i<leftSchema.getAttributeCount(); i++){
			if(SDFDatatypes.isMeasurementValue(leftSchema.getAttribute(i).getDatatype())){
				tempList.add(i);
				fromRightChannelTmp.add(false);
			}
		}
		
		// then add the positions of the right schema
		for(int i = 0; i<rightSchema.getAttributeCount(); i++){
			if(SDFDatatypes.isMeasurementValue(rightSchema.getAttribute(i).getDatatype())){
				tempList.add(i);
				fromRightChannelTmp.add(true);
			}
		}
		
		// put the positions into an array
		this.restrictList = new int[tempList.size()];
		for(int i = 0; i<tempList.size(); i++){
			this.restrictList[i] = tempList.get(i);
			this.fromRightChannel[i] = fromRightChannelTmp.get(i);
		}
		

	}

	@Override
	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return this.initialized;
	}

	@Override
	public void setInitialized(boolean b) {
		this.initialized = b;
		
	}

}

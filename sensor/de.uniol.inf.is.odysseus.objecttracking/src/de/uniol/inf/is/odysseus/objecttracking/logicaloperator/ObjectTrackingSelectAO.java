package de.uniol.inf.is.odysseus.objecttracking.logicaloperator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.base.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.base.predicate.OrPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.base.SelectAO;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.AndRangePredicate;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.IRangePredicate;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.ISolution;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.OrRangePredicate;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.RelationalRangePredicate;
import de.uniol.inf.is.odysseus.objecttracking.util.MapleFacade;
import de.uniol.inf.is.odysseus.objecttracking.util.MapleHack;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeListMetadataTypes;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

public class ObjectTrackingSelectAO extends SelectAO implements IHasRangePredicates{


	private boolean initialized;
	
	/**
	 * window size is use for sorting the conditions
	 * in a range predicate. if the second condition
	 * in a range predicate is has been true most often
	 * for the last window size evaluations, then
	 * this condition will be evaluated first the next
	 * time.
	 */
	private int windowSize;
	
	private Logger logger;
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
	
	public int getWindowSize() {
		return windowSize;
	}

	public void setWindowSize(int windowSize) {
		this.windowSize = windowSize;
	}

	/**
	 * The default range predicate will be used, if the default prediction
	 * function has been used by a tuple.
	 */
	IRangePredicate defaultRangePredicate;
	
	
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
	
	public ObjectTrackingSelectAO(){
		super();
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.initialized = false;
	}

	public ObjectTrackingSelectAO(ObjectTrackingSelectAO po) {
		super(po);
		this.rangePredicates = new HashMap<IPredicate, IRangePredicate>();
		for(Entry<IPredicate, IRangePredicate> entry: ((Map<IPredicate, IRangePredicate>)po.rangePredicates).entrySet()){
			this.rangePredicates.put(entry.getKey().clone(), entry.getValue().clone());
		}
		this.defaultRangePredicate = po.defaultRangePredicate != null ? po.defaultRangePredicate.clone() : null;
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.initialized = po.initialized;
	}

	public ObjectTrackingSelectAO(IPredicate predicate) {
		setPredicate(predicate);
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.initialized = false;
	}

	@Override
	public ObjectTrackingSelectAO clone() {
		return new ObjectTrackingSelectAO(this);
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema();
	}
	
	/**
	 * The input schema for this operator has been set with
	 * the subscription to its source operator. After this has
	 * been done, we still have to transform the predicate of
	 * this operator into a range predicate according to the
	 * prediction functions of the serveral attributes.
	 */
	public void init(IAttributeResolver attributeResolver){
		this.initRestrictList();
		
		if(this.getInputSchema() instanceof SDFAttributeListExtended){
			SDFAttributeListExtended inputSchema = (SDFAttributeListExtended)this.getInputSchema();
			
			Map<IPredicate, IPredictionFunction> predictionFunctions = (Map<IPredicate, IPredictionFunction>)inputSchema.getMetadata(SDFAttributeListMetadataTypes.PREDICTION_FUNCTIONS);
			
			this.rangePredicates = new HashMap<IPredicate, IRangePredicate>();
			for(Entry<IPredicate, IPredictionFunction> entry : predictionFunctions.entrySet()){
				IRangePredicate rangePredicate = this.generateRangePredicate(this.getPredicate(), entry.getValue().getExpressions(), attributeResolver);
				this.rangePredicates.put(entry.getKey(), rangePredicate);
			}
		}
	}
	
	/**
	 * 
	 * @param selectionPredicate The predicate of the selection operator
	 * @param predictionFunction The prediction function expressions to be used for the serveral attributes
	 * @param attributeResolver The attribute resolver to know, where the predicate is from
	 * @return
	 */
	private IRangePredicate generateRangePredicate(IPredicate selectionPredicate, SDFExpression[] predictionFunction, IAttributeResolver attributeResolver){
		if(selectionPredicate instanceof AndPredicate<?>){
			return new AndRangePredicate(
					generateRangePredicate(((AndPredicate)selectionPredicate).getLeft(), predictionFunction, attributeResolver), 
					generateRangePredicate(((AndPredicate)selectionPredicate).getRight(), predictionFunction, attributeResolver));
		}else if(selectionPredicate instanceof OrPredicate){
			return new OrRangePredicate(
					generateRangePredicate(((OrPredicate)selectionPredicate).getLeft(), predictionFunction, attributeResolver), 
					generateRangePredicate(((OrPredicate)selectionPredicate).getRight(), predictionFunction, attributeResolver));
		}else if(selectionPredicate instanceof RelationalPredicate){
			List<SDFAttribute> neededAttributes = ((RelationalPredicate)selectionPredicate).getAttributes();
			String predicateString = selectionPredicate.toString();
			
			StringTokenizer tokens = new StringTokenizer(predicateString,  IRangePredicate.tokenizerDelimiters, true);
			
			// for each occurence of an attribute in the expression, we have to substitute this occurence by
			// the corresponding prediction function of that attribute
			String rangePredicateExpression = "";
			while(tokens.hasMoreTokens()){
				String token = tokens.nextToken();
				boolean found = false;
				for(SDFAttribute curAttr: neededAttributes){
					if(token.equals(curAttr.toPointString())){
						int attrPos = this.getInputSchema().indexOf(curAttr);
						
						// attrPos can be -1 if curAttr is t (for time)
						// that is not in the schema
						// in this case we simply have to add the current token
						// to the resulting expression
						if(attrPos >= 0){
							if(predictionFunction[attrPos] == null){
								rangePredicateExpression += token;
							}else{
								rangePredicateExpression += "(" + predictionFunction[attrPos].toString() +  ")";
							}
						}
						else{
							rangePredicateExpression += token;
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
			this.logger.debug("Predicate '" + predicateString + "' transformed to rangePredicate '" + rangePredicateExpression + "'.");
			Map<IPredicate, ISolution> solutions = MapleFacade.getInstance().solveInequality(rangePredicateExpression, attributeResolver);
 			
			// ********************************************************
			// Hack for the missing solutions that come from maple
			// this is only necessary, if the the predicate contains the variable
			// t and must be transformed. Join predicates not containing t will
			// not be transformed and therefore no missing solution will exist
			
			if(!rangePredicateExpression.equals(predicateString)){
			
				Entry<IPredicate, ISolution> entry = solutions.entrySet().iterator().next();
				String conditionString = entry.getKey().toString();
				
				// there always a < compare opeartor and never a > compare operator
				int indexOfCompareOperator = conditionString.indexOf("<");		
				String denominator = null;
				
				if(conditionString.substring(0, indexOfCompareOperator).trim().equals("0") ||
						conditionString.substring(0, indexOfCompareOperator).trim().equals("(0)")){
					denominator = conditionString.substring(indexOfCompareOperator + 1).trim();
				}
				else if(conditionString.substring(indexOfCompareOperator + 1).trim().equals("0") ||
						conditionString.substring(indexOfCompareOperator + 1).trim().equals("(0)")){
					denominator = conditionString.substring(0, indexOfCompareOperator).trim();
				}
				
				String solution = entry.getValue().getSolution().toString();
				
				// Add the missing solutions, that Maple does not generate
				Map<IPredicate, ISolution> missingSolutions = MapleHack.getMissingJoinSolutions(rangePredicateExpression, denominator, solution, attributeResolver);
				if(missingSolutions != null){
					for(Entry<IPredicate, ISolution> missing: missingSolutions.entrySet()){
						solutions.put(missing.getKey(), missing.getValue());
					}
				}
				
				// *********************************************************
				// ONLY FOR EVALUATION
				List<SDFAttribute> attributes = new SDFExpression(null, rangePredicateExpression, attributeResolver).getAllAttributes();
				// remove attribute t
				for(int a = 0;a<attributes.size(); a++){
					if(attributes.get(a).toPointString().equals("t")){
						attributes.remove(a);
						break;
					}
				}
				Map<IPredicate, ISolution> additionalFalseSolutions = MapleHack.getAdditionalFalsePredrdicates(Math.min(1, attributes.size() * attributes.size()), attributes, attributeResolver);
				for(Entry<IPredicate, ISolution> additional: additionalFalseSolutions.entrySet()){
					solutions.put(additional.getKey(), additional.getValue());
				}
				// *********************************************************
				
				
			}
			// *********************************************************
			
			
			
			RelationalRangePredicate rangePredicate = new RelationalRangePredicate(solutions, this.windowSize);
 			rangePredicate.init(this.getInputSchema(), null);
 			
 			return rangePredicate;
	 			
			
			
		}else if(selectionPredicate instanceof NotPredicate){
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
		this.restrictList = ((SDFAttributeListExtended)this.getInputSchema(0)).getMeasurementAttributePositions();
	}

	@Override
	public Map<IPredicate, IRangePredicate> getRangePredicates() {
		// TODO Auto-generated method stub
		return this.rangePredicates;
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

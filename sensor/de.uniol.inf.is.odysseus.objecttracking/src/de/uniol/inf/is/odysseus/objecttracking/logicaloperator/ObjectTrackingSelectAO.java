package de.uniol.inf.is.odysseus.objecttracking.logicaloperator;

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
import de.uniol.inf.is.odysseus.logicaloperator.base.SelectAO;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunction;
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

public class ObjectTrackingSelectAO<T> extends SelectAO{


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
	}

	public ObjectTrackingSelectAO(ObjectTrackingSelectAO po) {
		super(po);
		this.rangePredicates = new HashMap<IPredicate, IRangePredicate>();
		for(Entry<IPredicate, IRangePredicate> entry: ((Map<IPredicate, IRangePredicate>)po.rangePredicates).entrySet()){
			this.rangePredicates.put(entry.getKey().clone(), entry.getValue().clone());
		}
		this.defaultRangePredicate = po.defaultRangePredicate.clone();
	}

	public ObjectTrackingSelectAO(IPredicate<?> predicate) {
		setPredicate(predicate);
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
		if(selectionPredicate instanceof AndPredicate){
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
			
			StringTokenizer tokens = new StringTokenizer(predicateString,  " \t\n\r\f + - * / < > '<=' '>=' ^", true);
			
			// for each occurence of an attribute in the expression, we have to substitute this occurence by
			// the corresponding prediction function of that attribute
			String rangePredicateExpression = "";
			while(tokens.hasMoreTokens()){
				String token = tokens.nextToken();
				boolean found = false;
				for(SDFAttribute curAttr: neededAttributes){
					if(token.equals(curAttr.toString())){
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
			Map<IPredicate, ISolution> solutions = MapleFacade.getInstance().solveInequality(rangePredicateExpression, attributeResolver);
 			RelationalRangePredicate rangePredicate = new RelationalRangePredicate(solutions);
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
}

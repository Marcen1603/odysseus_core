package de.uniol.inf.is.odysseus.objecttracking.logicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;
@SuppressWarnings("unchecked")
public class PredictionAO<T> extends UnaryLogicalOp{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1917550706455197576L;

	private Map<IPredicate<? super T>, SDFExpression[]> predictionFunctions;
	
	private Map<IPredicate<? super T>, int[][]> variables;
	
	private SDFExpression[] defaultPredictionFunction;
	
	/** 
	 * The first measurement attribute has not necessarily to be the
	 * first attribute in the schema and so on. So, we have to find the measurement
	 * attribute positions.
	 * 
	 * This array contains the positions of the measurement values.
	 * restrictList[0] contains the position of the first measurement attribute in the
	 * schema, restrictList[1] contains the position of the second measurement attribute
	 * in the schema and so on.
	 */
	private int[] restrictList;
	
	public SDFExpression[] getDefaultPredictionFunction() {
		return defaultPredictionFunction;
	}

	public void setDefaultPredictionFunction(SDFExpression[] defaultPredictionFunction) {
		this.defaultPredictionFunction = defaultPredictionFunction;
	}

	public PredictionAO() {
		super();
		this.predictionFunctions = new HashMap<IPredicate<? super T>, SDFExpression[]>();
		this.defaultPredictionFunction = null;
		this.variables = new HashMap<IPredicate<? super T>, int[][]>();
	}
	
	public PredictionAO(PredictionAO predictionAO) {
		super(predictionAO);
		this.predictionFunctions = new HashMap<IPredicate<? super T>, SDFExpression[]>();
		Set<Entry<IPredicate<? super T>, SDFExpression[]>> entries = predictionAO.predictionFunctions.entrySet();
		for(Map.Entry<IPredicate<? super T>, SDFExpression[]> curEntry: entries){
			this.predictionFunctions.put(curEntry.getKey(), curEntry.getValue());
		}
		
		this.variables = new HashMap<IPredicate<? super T>, int[][]>();
		Set<Entry<IPredicate<? super T>, int[][]>> variablesEntries = predictionAO.variables.entrySet();
		for(Map.Entry<IPredicate<? super T>, int[][]> curEntry: variablesEntries){
			this.variables.put(curEntry.getKey(), curEntry.getValue());
		}
	}

	public Map<IPredicate<? super T>, SDFExpression[]> getPredictionFunctions() {
		return predictionFunctions;
	}

	public void setPredictionFunctions(Map<IPredicate<? super T>, SDFExpression[]> predictionFunctions) {
		this.predictionFunctions = predictionFunctions;
		
		Set<Entry<IPredicate<? super T>, SDFExpression[]>> entries = predictionFunctions.entrySet();
		for(Map.Entry<IPredicate<? super T>, SDFExpression[]> curEntry: entries){
			SDFExpression[] expressions = curEntry.getValue();
			IPredicate<? super T> predicate = curEntry.getKey();
			
			int[][] vars = new int[expressions.length][];
			for(int u = 0; u<expressions.length; u++){
				SDFExpression expression = expressions[u];
				if(expression != null)
					vars[u] = expression.getAttributePositions();
			}
		
			this.variables.put(predicate, vars);
		}
	}
	
	public void setPredictionFunction(SDFExpression[] expressions, IPredicate<? super T> predicate) {
		if (this.predictionFunctions.containsKey(expressions)) {
			throw new IllegalArgumentException("predictionFunction already exists: " + expressions);
		}
		this.predictionFunctions.put(predicate, expressions);
		
		int[][] vars = new int[expressions.length][];
		for(int u = 0; u<expressions.length; u++){
			SDFExpression expression = expressions[u];
			if(expression != null)
				vars[u] = expression.getAttributePositions();
		}
		
		this.variables.put(predicate, vars);
	}
	
	public Map<IPredicate<? super T>, int[][]> getVariables(){
		return variables;
	}

	@Override
	public PredictionAO clone() {
		return new PredictionAO(this);
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema();
	}
	
	public void initRestrictList(){
		ArrayList<Integer> tempList = new ArrayList<Integer>();
		
		for(int i = 0; i<this.getInputSchema().getAttributeCount(); i++){
			if(SDFDatatypes.isMeasurementValue(this.getInputSchema().getAttribute(i).getDatatype())){
				tempList.add(i);
			}
		}
		
		// put the positions into an array
		this.restrictList = new int[tempList.size()];
		for(int i = 0; i<tempList.size(); i++){
			this.restrictList[i] = tempList.get(i);
		}
	}
	
	public int[] getRestrictList() {
		return restrictList;
	}
}

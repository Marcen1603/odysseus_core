package mg.dynaquest.sourceselection.action.compensate.compareoperatorcompensationmethods;

import java.util.ArrayList;
import java.util.Iterator;

import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperator;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperatorFactory;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperatorList;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFIntervalPredicate;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicate;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicateFactory;
import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFDatatypeConstraint;
import mg.dynaquest.sourcedescription.sdf.schema.SDFGranularityConstraint;
import mg.dynaquest.sourcedescription.sdf.schema.SDFIntervall;
import mg.dynaquest.sourcedescription.sdf.schema.SDFNumberConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFRangeConstraint;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypeConstraints;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates;
import mg.dynaquest.sourceselection.AnnotatedPlan;
/**
 * Diese Klasse ist eine Fabrik für CompareOpCompMethod.
 * @author Benjamin
 *
 */
public class CompareOpCompMethodFactory {
	
	/**
	 * Gibt an, wie oft eine Quelle Maximal angefragt wird
	 */
	static final double MAX_MULTI_QUERY = 5;
	
	/**
	 * 
	 * @param toCompensate Der zu kompensierende AnnotatedPlan
	 * @param apCompareOperators Liste von Vergleichsoperatoren, die der Plan
	 * 							 verarbeiten kann
	 * @param predicate Das zu kompensierende Prädikat
	 * @param query Die Anfrage des Nutzers
	 * @return Liste mit Kompensationsmethoden
	 */
	public static ArrayList <CompareOpCompMethod> createMethods(AnnotatedPlan toCompensate, 
											   SDFCompareOperatorList apCompareOperators, 
											   SDFSimplePredicate predicate, SDFQuery query){
		
		ArrayList <CompareOpCompMethod> methods = new ArrayList<CompareOpCompMethod>();
		SDFCompareOperator opToCompensate = predicate.getCompareOp();
		//System.out.println("Operator: "+opToCompensate.toString());
		SDFAttribute attribute = predicate.getAttribute();
		
		SDFConstant value = new SDFNumberConstant(null,null);
		if(!predicate.getURI(true).equals(SDFPredicates.IntervallPredicate)){
			value = predicate.getValue();
		}
		
		//Wertebereich des Attributs bestimmten
		Double leftAttrLimit = -1.0;
		Double rightAttrLimit = 1000000000.0;
		Iterator<SDFDatatypeConstraint> constrIterator = attribute.getDtConstraints().iterator();
		boolean isInt = true;
		
		while(constrIterator.hasNext()){
			SDFDatatypeConstraint current = constrIterator.next();
			if(current.getCType().equals(SDFDatatypeConstraints.hasRange)){
				SDFRangeConstraint rangeConstr = (SDFRangeConstraint)current;
				SDFIntervall rangeIntervall = rangeConstr.getRange();
				leftAttrLimit = rangeIntervall.getLeftBorder();
				rightAttrLimit = rangeIntervall.getRightBorder();
				break;
			}
			if(current.getURI(false).equals(SDFDatatypeConstraints.hasGranularity)){
				SDFGranularityConstraint granConstr = (SDFGranularityConstraint)current;
				isInt=granConstr.isInt();
			}
		}
		
		
		
		/////////////////////////////////////Anfrage nutzt = Operator//////////////////////////
		if(opToCompensate.equals(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.Equal))){
			// Es gibt SDFPredicates.Intervall noch nicht, deshalb ist diese
			//Möglichkeit überall auskommentiert
			/*if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.Intervall))){
			SDFIntervall intervall = new SDFIntervall(value.getDouble(), true, value.getDouble(),true);
			SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
					Long.toString(System.currentTimeMillis()), 
					SDFPredicates.IntervallPredicate, 
					attribute, 
					SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.Intervall), 
					null, 
					null, 
					null, 
					intervall);
			CompareOpCompMethod newCompensateMethod = new PreProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
			methods.add(newCompensateMethod);
			}*/
			if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.GreaterOrEqualThan))){
				SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
						Long.toString(System.currentTimeMillis()), 
						SDFPredicates.NumberPredicate, 
						attribute, 
						SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.GreaterOrEqualThan), 
						Double.toString(value.getDouble()), 
						null, 
						null, 
						null);
				CompareOpCompMethod newCompensateMethod = new PrePostProcessingCompMethod(toCompensate, predicate, query, preProcessPredicate);
				methods.add(newCompensateMethod);
			}
			if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.LowerOrEqualThan))){
				SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
						Long.toString(System.currentTimeMillis()), 
						SDFPredicates.NumberPredicate, 
						attribute, 
						SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.LowerOrEqualThan), 
						Double.toString(value.getDouble()), 
						null, 
						null, 
						null);
				CompareOpCompMethod newCompensateMethod = new PrePostProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
				methods.add(newCompensateMethod);
			}
			if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.GreaterThan)) && value.getDouble()-1 >= leftAttrLimit){
				SDFConstant newValue = new SDFNumberConstant(Long.toString(System.currentTimeMillis()),value.getDouble()-1);
				SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
						Long.toString(System.currentTimeMillis()), 
						SDFPredicates.NumberPredicate, 
						attribute, 
						SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.GreaterThan), 
						Double.toString(newValue.getDouble()), 
						null, 
						null, 
						null);
				CompareOpCompMethod newCompensateMethod = new PrePostProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
				methods.add(newCompensateMethod);
			}
			if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.LowerThan)) && value.getDouble()+1 <= rightAttrLimit){
				SDFConstant newValue = new SDFNumberConstant(Long.toString(System.currentTimeMillis()),value.getDouble()+1);
				SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
						Long.toString(System.currentTimeMillis()), 
						SDFPredicates.NumberPredicate, 
						attribute, 
						SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.LowerThan), 
						Double.toString(newValue.getDouble()), 
						null, 
						null, 
						null);
				CompareOpCompMethod newCompensateMethod = new PrePostProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
				methods.add(newCompensateMethod);
			}
			
			return methods;
			
		}
		///////////////////////////////////////Anfrage nutzt >= Operator//////////////////////////////////////////////
		if(opToCompensate.equals(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.GreaterOrEqualThan))){
			/*if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.Intervall))){
			SDFIntervall intervall = new SDFIntervall(value.getDouble(), true, rightAttributeLimit,true);
			SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
					Long.toString(System.currentTimeMillis()), 
					SDFPredicates.IntervallPredicate, 
					attribute, 
					SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.Intervall), 
					null, 
					null, 
					null, 
					intervall);
			CompareOpCompMethod newCompensateMethod = new PreProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
			methods.add(newCompensateMethod);
			}*/
			if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.GreaterThan)) && value.getDouble()-1 >= leftAttrLimit){
				SDFConstant newValue = new SDFNumberConstant(Long.toString(System.currentTimeMillis()),value.getDouble()-1);
				SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
						Long.toString(System.currentTimeMillis()), 
						SDFPredicates.NumberPredicate, 
						attribute, 
						SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.GreaterThan), 
						Double.toString(newValue.getDouble()), 
						null, 
						null, 
						null);
				if(!isInt){
					CompareOpCompMethod newCompensateMethod = new PrePostProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
					methods.add(newCompensateMethod);
				}else{
					CompareOpCompMethod newCompensateMethod = new PreProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
					methods.add(newCompensateMethod);
				}
			}
			if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.LowerOrEqualThan))){
				SDFConstant newValue = new SDFNumberConstant(Long.toString(System.currentTimeMillis()),rightAttrLimit);
				SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
						Long.toString(System.currentTimeMillis()), 
						SDFPredicates.NumberPredicate, 
						attribute, 
						SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.LowerOrEqualThan), 
						Double.toString(newValue.getDouble()), 
						null, 
						null, 
						null);
				CompareOpCompMethod newCompensateMethod = new PrePostProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
				methods.add(newCompensateMethod);
			}
			if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.Equal))){
				if(isInt && (rightAttrLimit - value.getDouble())<=MAX_MULTI_QUERY){
					int anzahl = new Double(rightAttrLimit - value.getDouble()).intValue();
					ArrayList<Double> queryValues = new ArrayList<Double>();
					for(int i=0; i<anzahl+1; i++){
						queryValues.add(value.getDouble()+i);
					}
					CompareOpCompMethod newCompensateMethod = new MultiQueryMethod(toCompensate, predicate, queryValues,true);
					methods.add(newCompensateMethod);
					CompareOpCompMethod newAlternativCompensateMethod = new MultiQueryMethod(toCompensate, predicate, queryValues,false);
					methods.add(newAlternativCompensateMethod);
				}
			}
			if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.LowerThan))){
				SDFConstant newValue = new SDFNumberConstant(Long.toString(System.currentTimeMillis()),rightAttrLimit);
				SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
						Long.toString(System.currentTimeMillis()), 
						SDFPredicates.NumberPredicate, 
						attribute, 
						SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.LowerThan), 
						Double.toString(newValue.getDouble()), 
						null, 
						null, 
						null);
				CompareOpCompMethod newCompensateMethod = new PrePostProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
				methods.add(newCompensateMethod);
			}
			return methods;
		}
		
		/////////////////////////////////////Anfrage nutzt > Operator//////////////////////////////////////
		if(opToCompensate.equals(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.GreaterThan))){
			/*if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.Intervall))){
				SDFIntervall intervall = new SDFIntervall(value.getDouble(), false,rightAttrLimit ,true);
				SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
						Long.toString(System.currentTimeMillis()), 
						SDFPredicates.IntervallPredicate, 
						attribute, 
						SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.Intervall), 
						null, 
						null, 
						null, 
						intervall);
				CompareOpCompMethod newCompensateMethod = new PreProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
				methods.add(newCompensateMethod);
			}*/
			if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.GreaterOrEqualThan))){
				SDFConstant newValue = new SDFNumberConstant(Long.toString(System.currentTimeMillis()), value.getDouble()+1); 
				if(isInt){
					SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
						Long.toString(System.currentTimeMillis()), 
						SDFPredicates.NumberPredicate, 
						attribute, 
						SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.GreaterOrEqualThan), 
						Double.toString(newValue.getDouble()), 
						null, 
						null, 
						null);
					CompareOpCompMethod newCompensateMethod = new PreProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
					methods.add(newCompensateMethod);
				}else{
					SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
							Long.toString(System.currentTimeMillis()), 
							SDFPredicates.NumberPredicate, 
							attribute, 
							SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.GreaterOrEqualThan), 
							Double.toString(value.getDouble()), 
							null, 
							null, 
							null);
					CompareOpCompMethod newCompensateMethod = new PrePostProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
					methods.add(newCompensateMethod);
				}
			}
			if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.LowerOrEqualThan))){
				SDFConstant newValue = new SDFNumberConstant(Long.toString(System.currentTimeMillis()), rightAttrLimit);
				SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
						Long.toString(System.currentTimeMillis()), 
						SDFPredicates.NumberPredicate, 
						attribute, 
						SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.LowerOrEqualThan), 
						Double.toString(newValue.getDouble()), 
						null, 
						null, 
						null);
					CompareOpCompMethod newCompensateMethod = new PrePostProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
					methods.add(newCompensateMethod);
			}
			if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.LowerThan))){
				SDFConstant newValue = new SDFNumberConstant(Long.toString(System.currentTimeMillis()), rightAttrLimit);
				SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
						Long.toString(System.currentTimeMillis()), 
						SDFPredicates.NumberPredicate, 
						attribute, 
						SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.LowerThan), 
						Double.toString(newValue.getDouble()), 
						null, 
						null, 
						null);
					CompareOpCompMethod newCompensateMethod = new PrePostProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
					methods.add(newCompensateMethod);
			}
			if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.Equal))){
				if(isInt && (rightAttrLimit - (value.getDouble() + 1.0))<=MAX_MULTI_QUERY){
					int anzahl = new Double(rightAttrLimit - value.getDouble()).intValue();
					ArrayList<Double> queryValues = new ArrayList<Double>();
					for(int i=1; i<=anzahl; i++){
						queryValues.add(value.getDouble()+i);
					}
					CompareOpCompMethod newCompensateMethod = new MultiQueryMethod(toCompensate, predicate, queryValues,true);
					methods.add(newCompensateMethod);
					CompareOpCompMethod newAlternativCompensateMethod = new MultiQueryMethod(toCompensate, predicate, queryValues,false);
					methods.add(newAlternativCompensateMethod);
				}
			}
			return methods;
		}
		///////////////////////////////////Anfrage nutzt <= Operator////////////////////////////////		
		if(opToCompensate.equals(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.LowerOrEqualThan))){
			/*if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.Intervall))){
			SDFIntervall intervall = new SDFIntervall(leftAttrLimit, true, value.getDouble(),true);
			SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
					Long.toString(System.currentTimeMillis()), 
					SDFPredicates.IntervallPredicate, 
					attribute, 
					SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.Intervall), 
					null, 
					null, 
					null, 
					intervall);
			CompareOpCompMethod newCompensateMethod = new PreProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
			methods.add(newCompensateMethod);
			}*/
			if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.LowerThan)) && value.getDouble()+1 <= rightAttrLimit){
				SDFConstant newValue = new SDFNumberConstant(Long.toString(System.currentTimeMillis()),value.getDouble()+1);
				SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
						Long.toString(System.currentTimeMillis()), 
						SDFPredicates.NumberPredicate, 
						attribute, 
						SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.LowerThan), 
						Double.toString(newValue.getDouble()), 
						null, 
						null, 
						null);
				if(!isInt){
					CompareOpCompMethod newCompensateMethod = new PrePostProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
					methods.add(newCompensateMethod);
				}else{
					CompareOpCompMethod newCompensateMethod = new PreProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
					methods.add(newCompensateMethod);
				}
			}
			if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.GreaterOrEqualThan))){
				SDFConstant newValue = new SDFNumberConstant(Long.toString(System.currentTimeMillis()),leftAttrLimit);
				SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
						Long.toString(System.currentTimeMillis()), 
						SDFPredicates.NumberPredicate, 
						attribute, 
						SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.GreaterOrEqualThan), 
						Double.toString(newValue.getDouble()), 
						null, 
						null, 
						null);
				CompareOpCompMethod newCompensateMethod = new PrePostProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
				methods.add(newCompensateMethod);
			}
			if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.Equal))){
				if(isInt && (value.getDouble()-leftAttrLimit)<=MAX_MULTI_QUERY){
					int anzahl = new Double(value.getDouble() - leftAttrLimit ).intValue();
					ArrayList<Double> queryValues = new ArrayList<Double>();
					for(int i=0; i<anzahl+1; i++){
						queryValues.add(value.getDouble()+i);
					}
					CompareOpCompMethod newCompensateMethod = new MultiQueryMethod(toCompensate, predicate, queryValues,true);
					methods.add(newCompensateMethod);
					CompareOpCompMethod newAlternativCompensateMethod = new MultiQueryMethod(toCompensate, predicate, queryValues,false);
					methods.add(newAlternativCompensateMethod);
				}
			}
			if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.GreaterThan))){
				SDFConstant newValue = new SDFNumberConstant(Long.toString(System.currentTimeMillis()),leftAttrLimit);
				SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
						Long.toString(System.currentTimeMillis()), 
						SDFPredicates.NumberPredicate, 
						attribute, 
						SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.GreaterThan), 
						Double.toString(newValue.getDouble()), 
						null, 
						null, 
						null);
				CompareOpCompMethod newCompensateMethod = new PrePostProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
				methods.add(newCompensateMethod);
			}
			return methods;
		}
		///////////////////////////////////Anfrage nutzt < Operator////////////////////////////////
		if(opToCompensate.equals(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.LowerThan))){
			/*if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.Intervall))){
				SDFIntervall intervall = new SDFIntervall(leftAttrLimit, true, value.getDouble(),false);
				SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
						Long.toString(System.currentTimeMillis()), 
						SDFPredicates.IntervallPredicate, 
						attribute, 
						SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.Intervall), 
						null, 
						null, 
						null, 
						intervall);
				CompareOpCompMethod newCompensateMethod = new PreProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
				methods.add(newCompensateMethod);
			}*/
			if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.LowerOrEqualThan))){
				SDFConstant newValue = new SDFNumberConstant(Long.toString(System.currentTimeMillis()), value.getDouble()-1); 
				if(isInt){
					SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
						Long.toString(System.currentTimeMillis()), 
						SDFPredicates.NumberPredicate, 
						attribute, 
						SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.LowerOrEqualThan), 
						Double.toString(newValue.getDouble()), 
						null, 
						null, 
						null);
					CompareOpCompMethod newCompensateMethod = new PreProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
					methods.add(newCompensateMethod);
				}else{
					SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
							Long.toString(System.currentTimeMillis()), 
							SDFPredicates.NumberPredicate, 
							attribute, 
							SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.LowerOrEqualThan), 
							Double.toString(value.getDouble()), 
							null, 
							null, 
							null);
					CompareOpCompMethod newCompensateMethod = new PrePostProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
					methods.add(newCompensateMethod);
				}
			}
			if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.GreaterOrEqualThan))){
				SDFConstant newValue = new SDFNumberConstant(Long.toString(System.currentTimeMillis()), leftAttrLimit);
				SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
						Long.toString(System.currentTimeMillis()), 
						SDFPredicates.NumberPredicate, 
						attribute, 
						SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.GreaterOrEqualThan), 
						Double.toString(newValue.getDouble()), 
						null, 
						null, 
						null);
					CompareOpCompMethod newCompensateMethod = new PrePostProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
					methods.add(newCompensateMethod);
			}
			if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.GreaterThan))){
				SDFConstant newValue = new SDFNumberConstant(Long.toString(System.currentTimeMillis()), leftAttrLimit);
				SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
						Long.toString(System.currentTimeMillis()), 
						SDFPredicates.NumberPredicate, 
						attribute, 
						SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.GreaterThan), 
						Double.toString(newValue.getDouble()), 
						null, 
						null, 
						null);
					CompareOpCompMethod newCompensateMethod = new PrePostProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
					methods.add(newCompensateMethod);
			}
			if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.Equal))){
				if(isInt && ((value.getDouble() - 1.0) - leftAttrLimit)<=MAX_MULTI_QUERY){
					int anzahl = new Double(value.getDouble() - leftAttrLimit).intValue();
					ArrayList<Double> queryValues = new ArrayList<Double>();
					for(int i=1; i<=anzahl; i++){
						queryValues.add(value.getDouble()-i);
					}
					CompareOpCompMethod newCompensateMethod = new MultiQueryMethod(toCompensate, predicate, queryValues,true);
					methods.add(newCompensateMethod);
					CompareOpCompMethod newAlternativCompensateMethod = new MultiQueryMethod(toCompensate, predicate, queryValues,false);
					methods.add(newAlternativCompensateMethod);
				}
			}
			return methods;
		}
		/////////////////////////////////Anfrage nutzt Intervall Operator////////////////////////////////
		if(predicate.getURI(false).equals(SDFPredicates.IntervallPredicate)){
			if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.LowerOrEqualThan))){
				SDFConstant newValue = new SDFNumberConstant(Long.toString(System.currentTimeMillis()), ((SDFIntervalPredicate)predicate).getRightValue()); 
				SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
						Long.toString(System.currentTimeMillis()), 
						SDFPredicates.NumberPredicate, 
						attribute, 
						SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.LowerOrEqualThan), 
						Double.toString(newValue.getDouble()), 
						null, 
						null, 
						null);
					CompareOpCompMethod newCompensateMethod = new PrePostProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
					methods.add(newCompensateMethod);
			}
			if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.GreaterOrEqualThan))){
				SDFConstant newValue = new SDFNumberConstant(Long.toString(System.currentTimeMillis()), ((SDFIntervalPredicate)predicate).getLeftValue());
				SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
						Long.toString(System.currentTimeMillis()), 
						SDFPredicates.NumberPredicate, 
						attribute, 
						SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.GreaterOrEqualThan), 
						Double.toString(newValue.getDouble()), 
						null, 
						null, 
						null);
					CompareOpCompMethod newCompensateMethod = new PrePostProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
					methods.add(newCompensateMethod);
			}
			if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.GreaterThan))
					&&((SDFIntervalPredicate)predicate).getLeftValue()-1 >= leftAttrLimit){
				SDFConstant newValue = new SDFNumberConstant(Long.toString(System.currentTimeMillis()), ((SDFIntervalPredicate)predicate).getLeftValue()-1);
				SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
						Long.toString(System.currentTimeMillis()), 
						SDFPredicates.NumberPredicate, 
						attribute, 
						SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.GreaterThan), 
						Double.toString(newValue.getDouble()), 
						null, 
						null, 
						null);
					CompareOpCompMethod newCompensateMethod = new PrePostProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
					methods.add(newCompensateMethod);
			}
			if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.LowerThan))
					&&((SDFIntervalPredicate)predicate).getRightValue()+1 <= rightAttrLimit){
				SDFConstant newValue = new SDFNumberConstant(Long.toString(System.currentTimeMillis()), ((SDFIntervalPredicate)predicate).getRightValue()+1);
				SDFSimplePredicate preProcessPredicate = SDFSimplePredicateFactory.createSimplePredicate(
						Long.toString(System.currentTimeMillis()), 
						SDFPredicates.NumberPredicate, 
						attribute, 
						SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.GreaterThan), 
						Double.toString(newValue.getDouble()), 
						null, 
						null, 
						null);
					CompareOpCompMethod newCompensateMethod = new PrePostProcessingCompMethod(toCompensate, predicate, query,preProcessPredicate);
					methods.add(newCompensateMethod);
			}
			if(apCompareOperators.contains(SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.Equal))){
				if(isInt && (((SDFIntervalPredicate)predicate).getRightValue() - ((SDFIntervalPredicate)predicate).getLeftValue())<=MAX_MULTI_QUERY){
					int anzahl = new Double(value.getDouble() - leftAttrLimit).intValue();
					ArrayList<Double> queryValues = new ArrayList<Double>();
					for(int i=1; i<=anzahl; i++){
						queryValues.add(value.getDouble()-i);
					}
					CompareOpCompMethod newCompensateMethod = new MultiQueryMethod(toCompensate, predicate, queryValues,true);
					methods.add(newCompensateMethod);
					CompareOpCompMethod newAlternativCompensateMethod = new MultiQueryMethod(toCompensate, predicate, queryValues,false);
					methods.add(newAlternativCompensateMethod);
				}
			}
			return methods;
		}
		
		return null;
	}
	
	public static void main(String[] args){
	}

}

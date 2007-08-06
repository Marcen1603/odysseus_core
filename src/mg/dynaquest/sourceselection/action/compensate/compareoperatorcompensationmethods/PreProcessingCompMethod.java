package mg.dynaquest.sourceselection.action.compensate.compareoperatorcompensationmethods;

import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicate;
import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourcedescription.sdf.query.WSimplePredicateSet;
import mg.dynaquest.sourcedescription.sdf.query.WeightedSimplePredicate;
import mg.dynaquest.sourceselection.AnnotatedPlan;

/**
 * Die Klasse dient zur Kompensation von Vergleichsoperatoren mittels
 * Pre-Processing
 * @author Benjamin
 *
 */
public class PreProcessingCompMethod extends SingleQueryMethod {
	
	/** Es wird der lediglich der Konstruktor der Oberklasse aufgerufen
	  * Dieser Konstruktor sollte nur von der OperatorCompMethodFactory 
	  * aufgerufen werden, da sie sicherstellt, das die Kompensation
	  * durchgef�hrt werden kann.
	  * @param toCompensate Der zu kompensierende AnnotatedPlan
	  * @param predicate Das zu kompensierende Attribut
	  * @param query Die Nutzeranfrage
	  * @param preProcessPredicate Das zu verwendende Post-Processing-Pr�dikat
	  */
	public PreProcessingCompMethod(AnnotatedPlan toCompensate,
			SDFSimplePredicate predicate, SDFQuery query, 
			SDFSimplePredicate preProcessPredicate) {
		super(toCompensate, predicate, query, preProcessPredicate);
	}
	
	/**
	 * F�hrt die Kompensation mit dem Pre-Process-Pr�dikat durch und liefert, 
	 * den kompensierten Plan. 
	 * @return Der kompensierte Plan
	 */
	public AnnotatedPlan compensate(){
		//neuen Plan
		//und neue Query anlegen
		AnnotatedPlan newPlan = new AnnotatedPlan(getInputPlan());
		SDFQuery newQuery = new SDFQuery(Long.toString(System.currentTimeMillis())); 
		
		//Alle Pr�dikate (au�er das zu kompensierende) aus der alten query in die 
		//neue �bertragen.
		//das neue Pr�dikat in die neue query einf�gen
		WSimplePredicateSet queryPredicates = this.getQuery().getQueryPredicates();
		for(int i=0; i<queryPredicates.getPredicateCount(); i++){
			if(!queryPredicates.getPredicate(i).getPredicate().equals(this.getPredicate())){
				newQuery.addWSimplePredicate(queryPredicates.getPredicate(i));
			}else{
				WeightedSimplePredicate newWSP = new WeightedSimplePredicate(this.getPreProcessPredicate(),queryPredicates.getPredicate(i).getWeighting());
				newQuery.addWSimplePredicate(newWSP);
			}
		}
	    //Die InputValues des neuen Plans anhand der neuen Query setzen	
		newPlan.setInputValuesOnAccessPOs(newQuery); 
		//ACHTUNG: Sollten irgendwann mehrere Vergl.Operatoren f�r ein Attribut vom
		//         AnnotatedPlan angeboten werden, muss hier auch noch
		//		   der zu nutzende Vergl.Operator angegeben werden
		
		return newPlan;
	}


	
	

}

package mg.dynaquest.sourceselection.action.compensate.compareoperatorcompensationmethods;

import mg.dynaquest.queryexecution.po.algebra.SelectPO;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicate;
import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourcedescription.sdf.query.WSimplePredicateSet;
import mg.dynaquest.sourcedescription.sdf.query.WeightedSimplePredicate;
import mg.dynaquest.sourceselection.AnnotatedPlan;
/**
 * Die Klasse dient zur Kompensation von Vergleichsoperatoren mittels
 * Pre-Processing und anschließendem Post-Processing
 * @author Benjamin
 *
 */
public class PrePostProcessingCompMethod extends SingleQueryMethod {

	/** Es wird der lediglich der Konstruktor der Oberklasse aufgerufen
	  * Dieser Konstruktor sollte nur von der OperatorCompMethodFactory 
	  * aufgerufen werden, da sie sicherstellt, das die Kompensation
	  * durchgeführt werden kann.
	  * @param toCompensate Der zu kompensierende AnnotatedPlan
	  * @param predicate Das zu kompensierende Attribut
	  * @param query Die Nutzeranfrage
	  * @param preProcessPredicate Das zu verwendende Post-Processing-Prädikat
	  */
	public PrePostProcessingCompMethod(AnnotatedPlan toCompensate,
			SDFSimplePredicate predicate, SDFQuery query, 
			SDFSimplePredicate preProcessPredicate) {
		super(toCompensate, predicate, query, preProcessPredicate);
	}
	
	/**
	 * Führt die Kompensation mit dem Pre-Processing Prädikat 
	 * und anschließendem Post-Processing durch und liefert, den kompensierten Plan.
	 */
	public AnnotatedPlan compensate(){
		AnnotatedPlan newPlan = new AnnotatedPlan(getInputPlan());
		//Neue Query anlegen
		SDFQuery newQuery = new SDFQuery(Long.toString(System.currentTimeMillis())); 
		
		//Alle Prädikate (außer das zu kompensierende) aus der alten query in die 
		//neue übertragen.
		//das neue Prädikat in die neue query einfügen
		WSimplePredicateSet queryPredicates = this.getQuery().getQueryPredicates();
		for(int i=0; i<queryPredicates.getPredicateCount(); i++){
			if(!queryPredicates.getPredicate(i).getPredicate().equals(this.getPredicate())){
				newQuery.addWSimplePredicate(queryPredicates.getPredicate(i));
			}else{
				WeightedSimplePredicate newWSP = new WeightedSimplePredicate(this.getPreProcessPredicate(),queryPredicates.getPredicate(i).getWeighting());
				newQuery.addWSimplePredicate(newWSP);
			}
		}
	    	
		newPlan.setInputValuesOnAccessPOs(newQuery); 
		//ACHTUNG: Sollten irgendwann mehrere Vergl.Operatoren für ein Attribut vom
		//         AnnotatedPlan angeboten werden, muss hier auch noch
		//		   der zu nutzende Vergl.Operator angegeben werden
        
		//Jetzt das PostProcessing mit Hilfe eines SelektorPOs und dem
		//Original-Prädikat der Anfrage
        SelectPO selectPO = new SelectPO();
		selectPO.setPredicate(this.getPredicate());
        selectPO.setInputPO(newPlan.getAccessPlan());
        
        //SelektorPO im neuen Plan nach oben setzen
        newPlan.setAccessPlan(selectPO);
 
		
		return newPlan;
	}
	

}

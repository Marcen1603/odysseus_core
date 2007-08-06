package mg.dynaquest.sourceselection.action.compensate.compareoperatorcompensationmethods;

import java.util.ArrayList;

import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.algebra.CollectorPO;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicate;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstantList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFNumberConstant;
import mg.dynaquest.sourceselection.AnnotatedPlan;
/**
 * Die Klasse dient dazu einen AnnotatedPlan zu kompensieren indem
 * eine Quelle mehrmals mit jeweils einem konkreten Wert angefragt wird.
 * @author Benjamin
 *
 */
public class MultiQueryMethod implements CompareOpCompMethod {
	
	private AnnotatedPlan inputPlan;
	private SDFSimplePredicate predicate;
	private ArrayList <Double> queryValues;
	private boolean doParalell;
	
	/**
	 * Erstellt eine Instanz mit den entsprechenden Werten
	 * @param inputPlan Der zu kompensierende Plan
	 * @param predicate Das zu kompensierende Prädikat
	 * @param queryValues Die Werte mit denen die Quelle jeweils angefragt werden soll
	 * @param doParallel True gibt ab, das die Quelle mehrfach paralell angefragt wird
	 * 					 false, besagt, dass die Quelle mehrmals nacheinander angefragt wird 
	 */
	public MultiQueryMethod (AnnotatedPlan inputPlan, SDFSimplePredicate predicate, ArrayList <Double> queryValues, boolean doParallel){
		this.inputPlan=inputPlan;
		this.predicate=predicate;
		this.queryValues=queryValues;
		this.doParalell=doParallel;
	}
	/**
	 * Führt die Kompensation durch und liefert den kompensierten Plan
	 */
	public AnnotatedPlan compensate() {
		//neuen Plan Anlegen, der auf dem Original basiert
		AnnotatedPlan newPlan = new AnnotatedPlan(this.inputPlan);
		
		SDFAttribute toReplace = predicate.getAttribute();
		//Falls paralell angefragt werden soll
		if(this.doParalell){	 		    
		    
			//Liste in der die einzelnen Pläne eingetragen werden
		    ArrayList <AnnotatedPlan> accessPOList = new ArrayList <AnnotatedPlan>();
		    
		    //Für jede Belegung ein AnnotatedPlan erzeugen, in welchem ein PhysicalAccessPO lediglich 
		    //eine Belegung des Attributes testet.
	    	for(int i = 0; i<queryValues.size(); i++)
	    	{
	    		//Plan basiert auf dem Original
	    		AnnotatedPlan nextPlan = new AnnotatedPlan(this.inputPlan);    	    		
	    		SDFConstant newConstant = new SDFNumberConstant("",queryValues.get(i));
	    		//entsprechendes Attribut im Plan durch eine Konstante ersetzen
	    		nextPlan.replaceGlobalAttributeByConstant(toReplace, newConstant);
//	    		ACHTUNG: Falls irgendwann mehrere Vergl.Operatoren von einem Plan
	    		//       angeboten werden, muss hier noch festgelegt werden, welcher genutzt werden soll.
	    		
	    		//Plan zur Liste hinzufügen
	    		accessPOList.add(nextPlan);
	    	}
	
		    // Pläne sind da, nun alle an einen CollectorPO übergeben
	    	CollectorPO collectorPO = new CollectorPO();
		    collectorPO.setNoOfInputs(accessPOList.size());
		    collectorPO.setOutElements(newPlan.getGlobalOutputPattern().getAllAttributes());
		    //Jeden Plan aus der Liste in den CollectorPO leiten
		    //und als BasePlan in den neuen Plan einbetten
		    for(int i = 0; i<accessPOList.size(); i++)
		    {
		    	AnnotatedPlan nextPlan = accessPOList.get(i);
		    	AlgebraPO inPlan = nextPlan.getAccessPlan();
		    	collectorPO.setInputPO(i,inPlan);
		    	newPlan.addBasePlan(nextPlan);	    	
		    }
		    //den CollectorPO als obersten Plan
		    //im neuen Plan
		    newPlan.setAccessPlan(collectorPO);
		    
		    // Kompensiertes Attribut aus InputPattern entfernen
		    newPlan.removeAttributeFromGlobalInputPattern(toReplace);
			    
		}else{
			//neue KonstantenListe anlegen
    		SDFConstantList newConstantSet = new SDFConstantList();
    		//alle Werte in die Liste eintragen
    		for(int i=0; i<queryValues.size(); i++){
    			SDFConstant newConstant = new SDFNumberConstant("",queryValues.get(i));
    			newConstantSet.add(newConstant);
    		}
    		//Attribut durch Konstantenliste erstzten 
    		//=> Die Quelle wir nacheinander für jeden Wert angefragt
    		newPlan.replaceGlobalAttributeByConstantList(toReplace, newConstantSet);
    		//ACHTUNG: Falls irgendwann mehrere Vergl.Operatoren von einem Plan
    		//         angeboten werden, muss hier noch festgelegt werden, welcher genutzt werden soll.
		    
		    // Kompensiertes Attribut aus InputPattern entfernen
    		// Nicht mehr notwendig. Macht replaceGlobaleAttribute*
		    // newPlan.removeAttributeFromGlobalInputPattern(toReplace);
		}
	    return newPlan;
	}

}

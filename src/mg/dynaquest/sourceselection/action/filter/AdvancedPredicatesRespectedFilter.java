/*
 * Created on 17.11.2004
 *
 */
package mg.dynaquest.sourceselection.action.filter;

import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicate;
import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourcedescription.sdf.query.WSimplePredicateSet;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourceselection.AnnotatedPlan;

/**
 * @author  Jurij Henne  Diese FilterAction unterscheidet sich von der PredicatesRespectedFilter,  indem sie nicht nur danach schaut, ob alle Prädikatattribute im GIP der Quelleliegen,  sondern gleichzeitig die Operatorenkompatibilität überprüft.  Dies ist notwendig, da die vierte Stufe des Quellenwahlprozesses noch nicht implementiert wurde   und die Kompensation der inkompatiblen Predikatenoperatoren daher nach Möglichkeit   in der CompensateAction der dritten Stufe vorgenommen werden muss.
 */
public class AdvancedPredicatesRespectedFilter extends AbstractFilterAction{

	public AdvancedPredicatesRespectedFilter() {}
	
    /**
     * Instanziiert die FilterAction mit der Nutzeranfrage
     * @param query
     */
    public AdvancedPredicatesRespectedFilter(SDFQuery query) {
        super(query);
     }

    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.action.FilterAction#positive(java.lang.Object)
     */
    public boolean positive(Object obj) 
    {
        AnnotatedPlan ap = (AnnotatedPlan) obj;
        // Testen, ob alle in den Prädikaten enthaltenen Attribute
        // auch von der Quelle entgegen genommen werden können
        SDFAttributeList attrSet = getQuery().getPredicateAttributes();
        boolean allPredicatesIn =  ap.containsInAttributes(attrSet);

        // Hier müssen die Operatoren der Prädikate der Anfrage und die der
        // Quelle verglichen werden. Alle Prädikate der Anfrage durchgehen und schauen, 
        // ob dies von der Quelle ausführbar ist
        WSimplePredicateSet predSet = getQuery().getQueryPredicates();
        boolean allOperatorsCompatible = true;
        for (int i=0;i<predSet.getPredicateCount();i++)
        {
        	SDFSimplePredicate pred =  predSet.getPredicate(i).getPredicate();
        	if (!ap.canProcess(pred))
            {
        		allOperatorsCompatible = false;
        		break;
            }
        }
        return (allPredicatesIn && allOperatorsCompatible);
	}

}

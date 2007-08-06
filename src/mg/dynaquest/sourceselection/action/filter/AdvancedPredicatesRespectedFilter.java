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
 * @author  Jurij Henne  Diese FilterAction unterscheidet sich von der PredicatesRespectedFilter,  indem sie nicht nur danach schaut, ob alle Pr�dikatattribute im GIP der Quelleliegen,  sondern gleichzeitig die Operatorenkompatibilit�t �berpr�ft.  Dies ist notwendig, da die vierte Stufe des Quellenwahlprozesses noch nicht implementiert wurde   und die Kompensation der inkompatiblen Predikatenoperatoren daher nach M�glichkeit   in der CompensateAction der dritten Stufe vorgenommen werden muss.
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
        // Testen, ob alle in den Pr�dikaten enthaltenen Attribute
        // auch von der Quelle entgegen genommen werden k�nnen
        SDFAttributeList attrSet = getQuery().getPredicateAttributes();
        boolean allPredicatesIn =  ap.containsInAttributes(attrSet);

        // Hier m�ssen die Operatoren der Pr�dikate der Anfrage und die der
        // Quelle verglichen werden. Alle Pr�dikate der Anfrage durchgehen und schauen, 
        // ob dies von der Quelle ausf�hrbar ist
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

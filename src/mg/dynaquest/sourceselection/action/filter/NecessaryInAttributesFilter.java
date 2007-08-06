/*
 * Created on 17.11.2004
 *
 */
package mg.dynaquest.sourceselection.action.filter;

import mg.dynaquest.sourcedescription.sdf.description.SDFAttributeAttributeBindingPair;
import mg.dynaquest.sourcedescription.sdf.description.SDFAttributeAttributeBindingPairList;
import mg.dynaquest.sourcedescription.sdf.description.SDFInputAttributeBinding;
import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstantList;
import mg.dynaquest.sourceselection.AnnotatedPlan;

/**
 * @author  Marco Grawunder
 */
public class NecessaryInAttributesFilter extends AbstractFilterAction {

    
    
    public NecessaryInAttributesFilter() {}
    public NecessaryInAttributesFilter(SDFQuery query) {
        super(query);                
    }

    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.action.FilterAction#positive(java.lang.Object)
     */
    public boolean positive(Object obj) {
        AnnotatedPlan plan = (AnnotatedPlan) obj;
        SDFAttributeAttributeBindingPairList reqInAttrAttributeBinding = plan.getRequiredInAttrAttributeBindingPairs();
        for (int i=0;i<reqInAttrAttributeBinding.getAttributeAttributeBindingPairCount();i++){
            SDFAttributeAttributeBindingPair aap = reqInAttrAttributeBinding.getAttributeAttributeBindingPair(i);
            SDFAttribute attrib = aap.getAttribute();
            SDFInputAttributeBinding inAttributeBinding = (SDFInputAttributeBinding) aap.getAttributeBinding();
//          Zunächst testen, ob das Attribut in der Anfrage enthalten ist
            if (!getQuery().hasPredicateWithAttribute(attrib)){
                //System.out.println("Attribut nicht den Anfrageprädikaten enthalten:"+attrib.getQualName());
                return false;
            }
            // Dann testen, ob die Belegung des Attributs so von der 
            // Quelle akzeptiert wird
            SDFConstantList constSet = inAttributeBinding.getConstantSet(); 
            if (constSet != null && constSet.size() > 0){
                if (!getQuery().predicateHasValueFromSet(attrib, constSet)){
                    //System.out.println("Konstante nicht im Anfragemuster enthalten");
                    return false;
                }
            }
        }

        return true;
    }

}

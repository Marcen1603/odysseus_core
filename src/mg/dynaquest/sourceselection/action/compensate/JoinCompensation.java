package mg.dynaquest.sourceselection.action.compensate;

import java.util.ArrayList;

import mg.dynaquest.sourceselection.AnnotatedPlan;

import org.apache.log4j.Logger;
import org.w3c.dom.NodeList;


/**
 * @autor  Jurij Henne  Spezielle Auspr�gung der CompensateAction, ausgelegt auf die Kompensation der   fehlenden Ausgabeattribute mit Hilfe einer Join-Verkn�pfung der Eingabezugriffspl�ne
 */

abstract public class JoinCompensation extends AbstractCompensateAction
{
	/**
	 * Kompensationseigener Debuger
	 * @uml.property  name="logger"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	protected Logger logger = Logger.getLogger(this.getClass()); 

	/**
	 * True, wenn auch Pl�ne generiert werden sollen, die nicht alle gew�nschten Attribute liefern Nur f�r JOIN-Kompensation evtl interessant, f�r SelectJoin gar sinnlos. Siehe DA.
	 * @uml.property  name="generatePartialPlans"
	 */
	protected boolean generatePartialPlans = false;
   

	/*
	 *  (non-Javadoc)
	 * @see mg.dynaquest.sourceselection.action.CompensateAction#compensate(java.util.ArrayList)
	 */
	abstract public ArrayList<AnnotatedPlan> compensate(ArrayList <AnnotatedPlan> toCompensate);
    
	/*
	 *  (non-Javadoc)
	 * @see mg.dynaquest.sourceselection.action.CompensateAction#getInternalXMLRepresentation(java.lang.String, java.lang.String, java.lang.StringBuffer)
	 */
    abstract public  void getInternalXMLRepresentation(String baseIndent, String indent, StringBuffer xmlRetValue) ;
    
	/*
	 *  (non-Javadoc)
	 * @see mg.dynaquest.sourceselection.action.CompensateAction#initInternalBaseValues(org.w3c.dom.NodeList)
	 */
    abstract public void initInternalBaseValues(NodeList children);
}

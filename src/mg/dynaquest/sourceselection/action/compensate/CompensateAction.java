/*
 * Created on 17.11.2004
 *
 */
package mg.dynaquest.sourceselection.action.compensate;

import java.util.ArrayList;
import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourceselection.AnnotatedPlan;
import org.w3c.dom.NodeList;

/**
Author: $Author: J. Henne $
Date: $Date: 2005/15/12 11:00:00 $ 
Version: $Revision: 1.04 $
 */

/**
 * Author: $Author: J. Henne $ Date: $Date: 2005/15/12 11:00:00 $  Version: $Revision: 1.04 $
 */

public interface CompensateAction {
  	
	/** Bekommt eine Menge mit Elementen aus AnnotatedPlan �bergeben und
     *  liefert eine Menge von kompensierten Pl�nen
     * @param toCompensate Elemente vom Typ AnnotatedPlan, die zu kompensieren sind
     * @return Menge von kompensierten Pl�nen, evtl. auch leer
     */
    public ArrayList <AnnotatedPlan> compensate(ArrayList <AnnotatedPlan> toCompensate);
    
    public void setQuery(SDFQuery query);
    
    
    /**
     * Schreibt den Zustand ausgew�hlter Attribute in ein XML-String rein
     * @param baseIndent
     * @param indent
     * @param xmlRetValue XML-String zum Schreiben
     */
     public void getInternalXMLRepresentation(String baseIndent, String indent, StringBuffer xmlRetValue);

     /**
      * Initialisiert die Action mit XML-Daten 
      * @param children XML-Daten
      */
 	 public  void initInternalBaseValues(NodeList children);
 	 
 	 /**
 	  * Liefert eine Menge an Pl�nen zur�ck, die in der 
 	  * Verarbeitung garantiert nicht verwendet werden konnten 
 	  * TODO: Welche Pl�ne k�nnen �berhaupt dazu gez�hlt werden? 
 	  * Z.B. Beim Join ist noch lange nicht bekannt, ob der neue Plan ein kompensierter Plan ist.
 	  * @return Liste der Pl�ne
 	  */
 	 public ArrayList <AnnotatedPlan> getErrorPlans();
}

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
  	
	/** Bekommt eine Menge mit Elementen aus AnnotatedPlan übergeben und
     *  liefert eine Menge von kompensierten Plänen
     * @param toCompensate Elemente vom Typ AnnotatedPlan, die zu kompensieren sind
     * @return Menge von kompensierten Plänen, evtl. auch leer
     */
    public ArrayList <AnnotatedPlan> compensate(ArrayList <AnnotatedPlan> toCompensate);
    
    public void setQuery(SDFQuery query);
    
    
    /**
     * Schreibt den Zustand ausgewählter Attribute in ein XML-String rein
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
 	  * Liefert eine Menge an Plänen zurück, die in der 
 	  * Verarbeitung garantiert nicht verwendet werden konnten 
 	  * TODO: Welche Pläne können überhaupt dazu gezählt werden? 
 	  * Z.B. Beim Join ist noch lange nicht bekannt, ob der neue Plan ein kompensierter Plan ist.
 	  * @return Liste der Pläne
 	  */
 	 public ArrayList <AnnotatedPlan> getErrorPlans();
}

package mg.dynaquest.queryexecution.po.base;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:12 $ 
 Version: $Revision: 1.4 $
 Log: $Log: FilterPO.java,v $
 Log: Revision 1.4  2004/09/16 08:57:12  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.3  2004/05/14 09:41:26  grawund
 Log: Umstellung: process_next liefert nun das Objekt, putToBuffer ist private
 Log:
 Log: Revision 1.2  2004/03/26 13:57:58  grawund
 Log: no message
 Log:
 Log: Revision 1.1  2003/08/20 13:44:54  grawund
 Log: Planoperator der einen Strom in zwei Ströme splittet mit Hilfe einer Filterbedingung
 Log:
 */

import java.util.concurrent.TimeoutException;
import mg.dynaquest.queryexecution.action.FilterAction;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.factory.POFactory;
import mg.dynaquest.queryexecution.object.POElementBuffer;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourceselection.AnnotatedPlan;
import mg.dynaquest.sourceselection.HasQuery;

import org.apache.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author  Marco Grawunder
 */
public class FilterPO extends UnaryPlanOperator implements WritesErrorElements, HasQuery {
	
    protected static Logger logger = Logger.getLogger(FilterPO.class.getName());

	/**
	 * Die FilterAction bestimmt das Verhalten des POs
	 * @uml.property  name="filterAction"
	 * @uml.associationEnd  
	 */
	private FilterAction filterAction;
	/**
	 * Legt fest, was mit den herausgefilterten Elementen passieren soll
	 * @uml.property  name="doErrorElementWriting"
	 */
	private boolean doErrorElementWriting = false;
	
	//private String poName = "Unnamed_FilterPO";

	/**
	 * @uml.property  name="errorElementBuffer"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	POElementBuffer errorElementBuffer = null;

	public FilterPO(FilterAction filterAction) {
		int maxElementsInErrorBuffer = 100;
		errorElementBuffer = new POElementBuffer(maxElementsInErrorBuffer);
		setFilterAction(filterAction);
	}
	
	public FilterPO() {
		int maxElementsInErrorBuffer = 100;
		errorElementBuffer = new POElementBuffer(maxElementsInErrorBuffer);
	}

	/**
	 * @param filterAction  the filterAction to set
	 * @uml.property  name="filterAction"
	 */
	public void setFilterAction(FilterAction filterAction) {
		this.filterAction = filterAction;
	}

	/**
	 * @return  the doErrorElementWriting
	 * @uml.property  name="doErrorElementWriting"
	 */
	public boolean isDoErrorElementWriting() {
		return doErrorElementWriting;
	}

	/**
	 * @param doErrorElementWriting  the doErrorElementWriting to set
	 * @uml.property  name="doErrorElementWriting"
	 */
	public void setDoErrorElementWriting(boolean state) {
		this.doErrorElementWriting = state;
	}

	protected void putToErrorBuffer(Object element) {
		errorElementBuffer.put(element);
	}

	protected Object getFromErrorBuffer() throws TimeoutException {
		return errorElementBuffer.get(-1);
	}

	public Object nextErrorElement() throws POException, TimeoutException {
		Object obj = getFromErrorBuffer();
		return obj;
	}
	protected boolean process_open() throws POException 
	{
		// TODO: Implement this
		// mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator abstract method
	    //System.out.println(this.getPOName()+" process_open");
		return true;
	}
	protected Object process_next() throws POException, TimeoutException {
	    //System.out.println(this.getPOName()+" process_next");
		// Muss solange laufen, bis entweder ein positives Objekt
		// produziert wird, oder die Verarbeitung terminiert
		Object obj = null;
		//System.out.println(this.getInternalPOName());
		while ((obj = this.getInputNext(this, -1)) != null) {
			if (obj != null) {
				
				// Objekt erfüllt die Filterbedingung
				if (filterAction.positive(obj)) {
					// Verlassen der Schleife
					logger.debug(this.getPOName()+"> Plan erfüllt Bedingung ["+((AnnotatedPlan)obj).getIndex()+"] / GIP: "+((AnnotatedPlan)obj).getGlobalInputPattern()+" / GOP: "+((AnnotatedPlan)obj).getGlobalOutputPattern());					
					break;
				} else {
				    
					if (doErrorElementWriting) {
						logger.debug(this.getPOName()+"> Schreibe in den Fehlerstrom: ["+((AnnotatedPlan)obj).getIndex()+"] / GIP: "+((AnnotatedPlan)obj).getGlobalInputPattern()+" / GOP: "+((AnnotatedPlan)obj).getGlobalOutputPattern());
						this.putToErrorBuffer(obj);
					}else{
						logger.debug(this.getPOName()+"> Gefiltert und Verworfen ["+((AnnotatedPlan)obj).getIndex()+"] / GIP: "+((AnnotatedPlan)obj).getGlobalInputPattern()+" / GOP: "+((AnnotatedPlan)obj).getGlobalOutputPattern());
					}
				}
			}
		}
		if (obj == null){
			if (doErrorElementWriting) {
				this.putToErrorBuffer(null);
			}
		}
	    //System.out.println(this.getPOName()+" process_next done");
		return obj;
	}
	

	protected boolean process_close() throws POException {
		// TODO: Implement this
		// mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator abstract method
	    //System.out.println(this.getPOName()+" process_close");
		return true;
	}

	public String getInternalPOName() {
		// TODO: Implement this
		// mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator abstract method
		return "Unnamed_FilterPO";
	}

	protected void initInternalBaseValues(NodeList children) {
		for (int i = 0; i < children.getLength(); i++) 
		{
			Node cNode = children.item(i);
			if (cNode.getNodeType() == Node.ELEMENT_NODE && cNode.getLocalName().equals("doErrorElementWriting")) 
			{
				this.doErrorElementWriting  = (Integer.parseInt(cNode.getFirstChild().getNodeValue()) != 0);
			}
			if (cNode.getNodeType() == Node.ELEMENT_NODE && cNode.getLocalName().equals("filterAction")) 
			{
				NamedNodeMap attribs = cNode.getAttributes();
//				String id = attribs.getNamedItem("id").getNodeValue();
//				String name = attribs.getNamedItem("name").getNodeValue();
				String c_class = attribs.getNamedItem("class").getNodeValue();
				
				this.filterAction = POFactory.createFilterAction(c_class);
			}	
		}
	}

	protected void getInternalXMLRepresentation(String baseIndent, String indent, StringBuffer retBuffer) 
	{
		retBuffer.append(baseIndent + "<doErrorElementWriting>");
		retBuffer.append((this.doErrorElementWriting)?1:0);
		retBuffer.append("</doErrorElementWriting>\n");	
		
		retBuffer.append(baseIndent + "<filterAction  name='"+this.filterAction.getActionName() + "' id='"
				+ this.filterAction.hashCode() + "' class='"+this.filterAction.getClass().getName()+"'>\n");
		retBuffer.append(baseIndent + "</filterAction>\n");
	}

	public void setQuery(SDFQuery query)
	{
		this.filterAction.setQuery(query);
	}

	public SupportsCloneMe cloneMe() {
		// Benötigt zunächst kein Clonen
		throw new NotImplementedException();
	}


}
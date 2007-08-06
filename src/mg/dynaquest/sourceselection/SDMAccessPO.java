package mg.dynaquest.sourceselection;

import java.util.Collection;
import java.util.Iterator;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.NAryPlanOperator;
import mg.dynaquest.sourcedescription.sdf.description.SDFSourceDescription;
import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdm.SourceDescriptionManager;
import org.apache.log4j.Logger;
import org.w3c.dom.NodeList;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


/**
 * @author  Marco Grawunder
 */
public class SDMAccessPO extends NAryPlanOperator {

    /**
	 * @uml.property  name="logger"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private Logger logger = Logger.getLogger(this.getClass().getName()); 
    
    /**
	 * @uml.property  name="query"
	 * @uml.associationEnd  
	 */
    SDFQuery query = null;

	/**
	 * @uml.property  name="sdm"
	 * @uml.associationEnd  
	 */
	SourceDescriptionManager sdm = null;
	Collection<String> sources = null;
	Iterator<String> sourceIter = null;
	

	public SDMAccessPO() {

	}
	

	public SDMAccessPO(SDFQuery query, SourceDescriptionManager sdm) {
		this.query = query;
		this.sdm = sdm;

	}

	protected synchronized boolean process_open() throws POException {
		boolean ret = true;
		try {
			SDFAttributeList attributes = query.getAttributes();
			sources = sdm.getSourcesWithAttributeFromList(attributes);
			sourceIter = sources.iterator();
			logger.debug("SDM-Sourcen-Anzahl: "+sources.size());
		} catch (Exception e) {
			e.printStackTrace();
			ret = false;
		}
		return ret;
	}

	protected synchronized Object process_next() throws POException {
		SDFSourceDescription source = null;
		try {
			if (sourceIter.hasNext()) {
				String sourceURI = sourceIter.next();
				source = sdm.getSourceDescription(sourceURI);
				logger.debug("SMD-Aktuelle Quelle: "+source.toString()); 				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return source;
	}

	protected synchronized boolean process_close() throws POException {
		return true;
	}

	public String getInternalPOName() {
		return "SDMAccessPO";
	}

	protected void getInternalXMLRepresentation(String baseIndent,
			String indent, StringBuffer xmlRetValue) {
		// TODO: Implement this
		// mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator abstract method
	}

    protected void initInternalBaseValues(NodeList childNodes) {
		// TODO: Implement this
		// mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator abstract method
	}


	public SupportsCloneMe cloneMe() {
		// Quellenwahloperatoren müssen die Methode nicht implementieren
		throw new NotImplementedException();
	}

}
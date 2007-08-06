package mg.dynaquest.queryexecution.po.streaming.oo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.ProjectPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.streaming.object.StreamExchangeElement;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;

import org.w3c.dom.NodeList;

/**
 * OO Projektion
 * Darf i.Allg. nur als oberster PO in einem Plan auftreten, da
 * die projizirten Werte direkt als Objekte innerhalb der Map<String, Object>
 * des StreamExchangeElements gespeichert werden und als Key den kompletten Pfad
 * des Attributs besitzen.   
 * @author Jonas Jacobi
 */
public class OOStreamingProjectPO extends OOStreamingBasePO {

	public OOStreamingProjectPO() {
	}

	public OOStreamingProjectPO(ProjectPO algebraPO) {
		super(algebraPO);
	}

	public OOStreamingProjectPO(OOStreamingProjectPO operator) {
		super(operator);
	}

	@Override
	protected void getInternalXMLRepresentation(String baseIndent,
			String indent, StringBuffer xmlRetValue) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initInternalBaseValues(NodeList childNodes) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean process_close() throws POException {
		return true;
	}

	@Override
	protected Object process_next() throws POException, TimeoutException {
		StreamExchangeElement<Map<String, Object>> curVal = getInputNext(this, -1);
		Map<String, Object> values = new HashMap<String, Object>();
		SDFAttributeList attributes = ((ProjectPO)getAlgebraPO()).getProjectAttributes();
		Iterator<SDFAttribute> i = attributes.iterator();
		while(i.hasNext()) {
			SDFAttribute attribute = i.next();
			try {
				Object value = getAttributeValue(attribute, curVal.getCargo());
				// TODO den tatsaechlichen feldnamen eintragen, wenn
				// vernuenftige repraesentation fuer oo konzepte in sdf gefunden
				// wurde
				values.put(attribute.getURI(false), value);			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		
		return new StreamExchangeElement<Map<String,Object>>(values, curVal.getValidity());
	}

	@Override
	protected boolean process_open() throws POException {
		return true;
	}

	public SupportsCloneMe cloneMe() {
		return new OOStreamingProjectPO(this);
	}

}

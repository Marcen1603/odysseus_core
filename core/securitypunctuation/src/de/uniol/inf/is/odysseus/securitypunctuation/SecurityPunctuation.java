package de.uniol.inf.is.odysseus.securitypunctuation;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;

public class SecurityPunctuation extends AbstractSecurityPunctuation {

	private static final long serialVersionUID = 8534064040716648960L;
	
	private ArrayList<Integer> evaluateAttributesCache = new ArrayList<Integer>();
	private SDFSchema schemaCache;
	
	public SecurityPunctuation(Object[] objects) {
		String[] ddpStream = ((String) objects[0]).split(",");
		for(int i = 0; i < ddpStream.length; i++) {
			ddpStream[i] = ddpStream[i].trim();
		}
		attributes.put("ddpStream", ddpStream);
		attributes.put("ddpStarttuple", (Long) objects[1]);
		attributes.put("ddpEndtuple", (Long) objects[2]);
		String[] ddpName = ((String) objects[3]).split(",");
		for(int i = 0; i < ddpName.length; i++) {
			ddpName[i] = ddpName[i].trim();
		}
		attributes.put("ddpName", ddpName);
		String[] srpRole = ((String) objects[4]).split(",");
		for(int i = 0; i < srpRole.length; i++) {
			srpRole[i] = srpRole[i].trim();
		}
		attributes.put("srpRole", srpRole);
		attributes.put("sign", (Integer) objects[5]);
		attributes.put("immutable", (Integer) objects[6]);
		attributes.put("ts", (Long) objects[7]);
	}
	
	public SecurityPunctuation(SecurityPunctuation sp) {
		attributes.put("ddpStream", sp.getAttribute("ddpStream"));
		attributes.put("ddpStarttuple", sp.getAttribute("ddpStarttuple"));
		attributes.put("ddpEndtuple", sp.getAttribute("ddpEndtuple"));
		attributes.put("ddpName", sp.getAttribute("ddpName"));
		attributes.put("srpRole", sp.getAttribute("srpRole"));
		attributes.put("sign", sp.getAttribute("sign"));
		attributes.put("immutable", sp.getAttribute("immutable"));
		attributes.put("ts", sp.getAttribute("ts"));
	}	
	
	public Boolean evaluateTS(Long ts) {
		if((this.getLongAttribute("ddpStarttuple") == -1 && (this.getLongAttribute("ddpEndtuple")) == -1) ||
				(ts > (this.getLongAttribute("ddpStarttuple"))) && (ts <= this.getLongAttribute("ddpEndtuple")) || this.getLongAttribute("ddpEndtuple") == -1) {
			return true;
		}
		return false;
	}

	public Boolean evaluateRoles(List<String> userRoles) {
		if(this.getAttribute("srpRole").equals("")) {
			return true;
		}
		for(String role:((String[])this.getAttribute("srpRole"))) {
			if(userRoles.contains(role)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Setzt alle Attribute, auf die der Zugriff nicht erlaubt ist, auf null
	 * Namen werden bei ACCESS bestimmt --> nicht sicher...???
	 * 
	 * @param oldTuple
	 * @param sdfSchema 
	 * @return
	 */
	public Boolean evaluateAttributes(Tuple<?> tuple, SDFSchema schema) {
		if(!schema.equals(schemaCache) || evaluateAttributesCache.isEmpty()) {
			evaluateAttributesCache.clear();
			if(this.getAttribute("ddpName") == null) {
				return false;
			}
			if(((String[])this.getAttribute("ddpName")).length <= 1 && ((String[])this.getAttribute("ddpName"))[0].equals("")) {
				return true;
			}
			
			for(int i = 0; i < schema.size(); i++) {
				Boolean setToNull = true;
				String schemaAttribute = schema.getAttribute(i).getAttributeName();
				for(String ddpName:(String[])this.getAttribute("ddpName")) {
					if(schemaAttribute.equals(ddpName)) {
						setToNull = false;
						break;
					}
				}
				if(setToNull) {
					//Timestamp wird evtl. nochmal benötigt, wenn SecShield variabel in die Anfrage eingebaut wird.
					if(!schemaAttribute.equals("ts")) {
						tuple.setAttribute(i, (Object) null);
						evaluateAttributesCache.add(i);
					}
				}
			}	
			return true;
		}
		for(Integer i:evaluateAttributesCache) {
			tuple.setAttribute(i, (Object) null);
		}
		return true;
	}	
	
	public Boolean evaluateStreamName(SDFSchema schema) {
		//Was passiert hier bei Join???
		if(((String[])this.getAttribute("ddpStream"))[0].equals("")) {
			return true;
		}
		for(String stream:(String[])this.getAttribute("ddpStream")) {
			if(schema.getURI().equals(stream)) {
				return true;
			}
		}
		return false;
	}
	
	public Boolean evaluateAll(Long tupleTS, List<String> userRoles, Tuple<?> tuple, SDFSchema schema) {
		if( (Integer)this.getAttribute("sign") == 1 
			&& evaluateRoles(userRoles)
			&& evaluateTS(tupleTS)
			&& evaluateAttributes(tuple, schema)
			&& evaluateStreamName(schema)
			) {
			return true;
		}
		return false;
	}

	@Override
	public ISecurityPunctuation union(ISecurityPunctuation sp2) {
		return null;
	}
}
package de.uniol.inf.is.odysseus.core.collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

//public class SecurityPunctuation extends AbstractSecurityPunctuation {
public class SecurityPunctuation implements Serializable {

	private static final long serialVersionUID = 8534064040716648960L;
	
	private String[] ddpStream;
	private Integer ddpStarttuple;
	private Integer ddpEndtuple;
	private String[] ddpName;
	private String[] srpRole;
	private Integer sign;
	private Long ts;
	
	private ArrayList<Integer> evaluateAttributesCache = new ArrayList<Integer>();
	
	
	
	
//	public SecurityPunctuation(Object[] objects) {
//		String[] ddpStream = ((String) objects[0]).split(",");
//		for(int i = 0; i < ddpStream.length; i++) {
//			ddpStream[i] = ddpStream[i].trim();
//		}
//		attributes.put("ddpStream", ddpStream);
//		attributes.put("ddpStarttuple", (Integer) objects[1]);
//		attributes.put("ddpEndtuple", (Integer) objects[2]);
//		String[] ddpName = ((String) objects[3]).split(",");
//		for(int i = 0; i < ddpName.length; i++) {
//			ddpName[i] = ddpName[i].trim();
//		}
//		attributes.put("ddpName", ddpName);
//		String[] srpRole = ((String) objects[4]).split(",");
//		for(int i = 0; i < srpRole.length; i++) {
//			srpRole[i] = srpRole[i].trim();
//		}
//		attributes.put("srpRole", srpRole);
//		attributes.put("sign", (Integer) objects[5]);
//		attributes.put("ts", (Integer) objects[6]);
//	}
//	
//	public SecurityPunctuation(SecurityPunctuation sp) {
//		attributes.put("ddpStream", sp.getAttribute("ddpStream"));
//		attributes.put("ddpStarttuple", sp.getAttribute("ddpStarttuple"));
//		attributes.put("ddpEndtuple", sp.getAttribute("ddpEndtuple"));
//		attributes.put("ddpName", sp.getAttribute("ddpName"));
//		attributes.put("srpRole", sp.getAttribute("srpRole"));
//		attributes.put("sign", sp.getAttribute("sign"));
//		attributes.put("ts", sp.getAttribute("ts"));
//	}	
	
	
	
	public SecurityPunctuation(Object[] objects) {
		//Strings mit möglicher Mehrfach-Angabe zerschneiden:
		ddpStream = ((String) objects[0]).split(",");
		for(int i = 0; i < ddpStream.length; i++) {
			ddpStream[i] = ddpStream[i].trim();
		}
		ddpStarttuple = (Integer) objects[1];
		ddpEndtuple = (Integer) objects[2];
		ddpName = ((String) objects[3]).split(",");
		for(int i = 0; i < ddpName.length; i++) {
			ddpName[i] = ddpName[i].trim();
		}
		srpRole = ((String) objects[4]).split(",");
		for(int i = 0; i < srpRole.length; i++) {
			srpRole[i] = srpRole[i].trim();
		}
		sign = (Integer) objects[5];
		ts = (Long) objects[6];
	}
	
	public SecurityPunctuation(SecurityPunctuation sp) {
		this.ddpStream = sp.ddpStream;
		this.ddpStarttuple = sp.ddpStarttuple;
		this.ddpEndtuple = sp.ddpEndtuple;
		this.ddpName = sp.ddpName;
		this.srpRole = sp.srpRole;
		this.sign = sp.sign;
		this.ts = sp.ts;
	}	

	public String[] getDDPdpStream() {
		return this.ddpStream;
	}
	public void setDDPStream(String[] ddpStream) {
		this.ddpStream = ddpStream;
	}
	public void setDDPStream(String ddpStream) {
		this.ddpStream[0] = ddpStream;
	}
	
	public Integer getDDPStarttuple() {
		return this.ddpStarttuple;
	}
	public void setDDPStarttuple(Integer ddpStarttuple) {
		this.ddpStarttuple = ddpStarttuple;
	}
	
	public Integer getDDPEndtuple() {
		return this.ddpEndtuple;
	}
	public void setDDPEndtuple(Integer ddpEndtuple) {
		this.ddpEndtuple = ddpEndtuple;
	}
	
	public String[] getDDPName() {
		return this.ddpName;
	}
	public void setDDPName(String[] ddpName) {
		this.ddpName = ddpName;
	}
	public void setDDPName(String ddpName) {
		this.ddpName[0] = ddpName;
	}
	
	public String[] getSRPRole() {
		return this.srpRole;
	}
	public void setSRPRole(String[] srpRole) {
		this.srpRole = srpRole;
	}
	public void setSRPRole(String srpRole) {
		this.srpRole[0] = srpRole;
	}
	
	public Integer getSign() {
		return this.sign;
	}
	public void setSign(Integer sign) {
		this.sign = sign;
	}
	
	public Long getTS() {
		return this.ts;
	}
	public void setTS(Long ts) {
		this.ts = ts;
	}
	
	
	public Boolean evaluateTS(Long ts) {
		if((this.getDDPStarttuple() == -1 && this.getDDPEndtuple() == -1) ||
				(ts > this.getDDPStarttuple() && (ts <= this.getDDPEndtuple() || this.getDDPEndtuple() == -1))) {
			return true;
		}
		return false;
	}

	public Boolean evaluateRoles(List<String> userRoles) {
		if(this.getSRPRole().equals("")) {
			return true;
		}
		for(String role:this.getSRPRole()) {
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
		if(evaluateAttributesCache.isEmpty()) {
			if(this.getDDPName() == null) {
				return false;
			}
			if(this.getDDPName().length <= 1 && this.getDDPName()[0].equals("")) {
				return true;
			}
			
			for(int i = 0; i < schema.size(); i++) {
				Boolean setToNull = true;
				String schemaAttribute = schema.getAttribute(i).getAttributeName();
				for(String ddpName:getDDPName()) {
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
		} else {
			for(Integer i:evaluateAttributesCache) {
				tuple.setAttribute(i, (Object) null);
			}
		}
		return true;
	}	
	
	public Boolean evaluateStreamName(SDFSchema schema) {
		//Was passiert hier bei Join???
		if(ddpStream[0].equals("")) {
			return true;
		}
		for(String stream:ddpStream) {
			if(schema.getURI().equals(stream)) {
				return true;
			}
		}
		return false;
	}
	
	public Boolean evaluateAll(Long tupleTS, List<String> userRoles, Tuple<?> tuple, SDFSchema schema) {
		if(	getSign() == 1 
			&& evaluateRoles(userRoles)
			&& evaluateTS(tupleTS)
			&& evaluateAttributes(tuple, schema)
			&& evaluateStreamName(schema)
			) {
			return true;
		}
		return false;
	}
}

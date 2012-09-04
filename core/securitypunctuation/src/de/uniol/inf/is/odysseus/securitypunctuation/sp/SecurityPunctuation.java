package de.uniol.inf.is.odysseus.securitypunctuation.sp;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;

public class SecurityPunctuation extends AbstractSecurityPunctuation {

	private static final long serialVersionUID = 8534064040716648960L;
	
	private ArrayList<Integer> evaluateAttributesCache = new ArrayList<Integer>();
	
	public SecurityPunctuation(Object[] objects, SDFSchema schema) {
		setSchema(schema);
		String[] streamname = ((String) objects[0]).split(",");
		for(int i = 0; i < streamname.length; i++) {
			streamname[i] = streamname[i].trim();
		}
		setAttribute("streamname", streamname);
//		attributes.put("streamname", createPredicate("streamname", (String) objects[0]));
		setAttribute("tupleStartTS", (Long) objects[1]);
		setAttribute("tupleEndTS", (Long) objects[2]);
		String[] attributeNames = ((String) objects[3]).split(",");
		for(int i = 0; i < attributeNames.length; i++) {
			attributeNames[i] = attributeNames[i].trim();
		}
		setAttribute("attributeNames", attributeNames);
		String[] role = ((String) objects[4]).split(",");
		for(int i = 0; i < role.length; i++) {
			role[i] = role[i].trim();
		}
		setAttribute("role", role);
		setAttribute("sign", (Integer) objects[5]);
		setAttribute("immutable", (Integer) objects[6]);
		setAttribute("ts", (Long) objects[7]);
	}

	public SecurityPunctuation(SecurityPunctuation sp) {
		setSchema(sp.getSchema());
		setAttribute("streamname", sp.getAttribute("streamname"));
		setAttribute("tupleStartTS", sp.getAttribute("tupleStartTS"));
		setAttribute("tupleEndTS", sp.getAttribute("tupleEndTS"));
		setAttribute("attributeNames", sp.getAttribute("attributeNames"));
		setAttribute("role", sp.getAttribute("role"));
		setAttribute("sign", sp.getAttribute("sign"));
		setAttribute("immutable", sp.getAttribute("immutable"));
		setAttribute("ts", sp.getAttribute("ts"));
	}	

	public Boolean evaluateTS(Long ts) {
		if(this.getLongAttribute("tupleStartTS") != null) {
			if((this.getLongAttribute("tupleStartTS") == -1 && (this.getLongAttribute("tupleEndTS")) == -1) ||
					((ts > (this.getLongAttribute("tupleStartTS"))) && (ts <= this.getLongAttribute("tupleEndTS")) || this.getLongAttribute("tupleEndTS") == -1) ||
					(this.getLongAttribute("tupleStartTS") <= this.getLongAttribute("tupleEndTS") && (ts <= this.getLongAttribute("tupleEndTS")) || this.getLongAttribute("tupleEndTS") == -1)) {
				return true;
			}
		}
		return false;
	}

	public Boolean evaluateRoles(List<String> userRoles) {
		if(this.getStringArrayAttribute("role") == null) {
			return false;
		}			
		if(this.getStringArrayAttribute("role").equals("")) {
			return true;
		}
		for(String role:(this.getStringArrayAttribute("role"))) {
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
		if(!schema.equals(getSchema()) || evaluateAttributesCache.isEmpty()) {
			evaluateAttributesCache.clear();
			if(this.getStringArrayAttribute("attributeNames") == null) {
				return false;
			}
			if((this.getStringArrayAttribute("attributeNames")).length <= 1 && (this.getStringArrayAttribute("attributeNames"))[0].equals("")) {
				return true;
			}
			
			for(int i = 0; i < schema.size(); i++) {
				Boolean setToNull = true;
				String schemaAttribute = schema.getAttribute(i).getAttributeName();
				for(String attributeNames:this.getStringArrayAttribute("attributeNames")) {
					if(schemaAttribute.equals(attributeNames)) {
						setToNull = false;
						break;
					}
				}
				if(setToNull) {
					//Timestamp wird evtl. nochmal ben�tigt, wenn SecShield variabel in die Anfrage eingebaut wird.
					if(!schemaAttribute.equals("ts")) {
						tuple.setAttribute(i, (Object) null);
						evaluateAttributesCache.add(i);
					}
				}
			}	
			setSchema(schema);
			return true;
		}
		for(Integer i:evaluateAttributesCache) {
			tuple.setAttribute(i, (Object) null);
		}
		return true;
	}	
	
	public Boolean evaluateStreamName(SDFSchema schema) {
		//Was passiert hier bei Join???
		if(this.getStringArrayAttribute("streamname") == null) {
			return false;
		}
		if((this.getStringArrayAttribute("streamname")).length <= 1 && (this.getStringArrayAttribute("streamname"))[0].equals("")) {
			return true;
		}
		for(String stream:this.getStringArrayAttribute("streamname")) {
			if(schema.getURI().equals(stream)) {
				return true;
			}
		}
		return false;
	}
	
	public Boolean evaluateAll(Long tupleTS, List<String> userRoles, Tuple<?> tuple, SDFSchema schema) {
		if( (Integer)this.getIntegerAttribute("sign") == 1 
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
		if(sp2 instanceof SecurityPunctuation) {
			// Union gibt es nur, wenn SP mit gleichem Zeitstempel aus der gleichen Quelle kommen
			if(getLongAttribute("ts") == sp2.getLongAttribute("ts") &&
					getSchema().getURI().equals(((SecurityPunctuation) sp2).getSchema().getURI())) {

				Object[] attribute = new Object[sp2.getNumberofAttributes()];
				
				attribute[0] = mergeStringArrays(this.getStringArrayAttribute("streamname"), sp2.getStringArrayAttribute("streamname"));
				//kleineren w�hlen
				if(this.getLongAttribute("tupleStartTS") >= sp2.getLongAttribute("tupleStartTS")) {
					attribute[1] = sp2.getLongAttribute("tupleStartTS");
				} else {
					attribute[1] = this.getLongAttribute("tupleStartTS");
				}
				//gr��eren W�hlen
				if(this.getLongAttribute("tupleEndTS") >= sp2.getLongAttribute("tupleEndTS")) {
					attribute[2] = this.getLongAttribute("tupleStartTS");
				} else {
					attribute[2] = sp2.getLongAttribute("tupleStartTS");
				}
				attribute[3] = mergeStringArrays(this.getStringArrayAttribute("attributeNames"), sp2.getStringArrayAttribute("attributeNames"));
				attribute[4] = mergeStringArrays(this.getStringArrayAttribute("role"), sp2.getStringArrayAttribute("role"));
				
				//Was soll hier passieren?
				attribute[5] = getIntegerAttribute("sign");
				attribute[6] = getIntegerAttribute("immutable");
				attribute[7] = getLongAttribute("ts");
				
				SecurityPunctuation newSP = new SecurityPunctuation(attribute, getSchema());
				return newSP;
			}
		}
		return null;
	}
	
	private String[] mergeStringArrays(String[] array1, String[] array2) {
		ArrayList<String> temp = new ArrayList<String>();
		for(String streamname:array1) {
			temp.add(streamname);
		}
		for(String streamname:array2) {
			if(!temp.contains(streamname)) {
				temp.add(streamname);
			}
		}
		String[] stringarray = new String[temp.size()];
		for(int i = 0; i < temp.size(); i++){
			stringarray[i] = temp.get(i);
		}
		return stringarray;
	}
}
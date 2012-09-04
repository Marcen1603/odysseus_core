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
		ArrayList<String> streamnameArrayList = new ArrayList<String>();
		String[] streamname = ((String) objects[0]).split(",");
		for(int i = 0; i < streamname.length; i++) {
			streamnameArrayList.add(streamname[i].trim());
		}
		setAttribute("streamname", streamnameArrayList);

		setAttribute("tupleStartTS", (Long) objects[1]);
		setAttribute("tupleEndTS", (Long) objects[2]);
		
		ArrayList<String> attributeNamesArrayList = new ArrayList<String>();
		String[] attributeNames = ((String) objects[3]).split(",");
		for(int i = 0; i < attributeNames.length; i++) {
			attributeNamesArrayList.add(attributeNames[i].trim());
		}
		setAttribute("attributeNames", attributeNamesArrayList);

		ArrayList<String> roleArrayList = new ArrayList<String>();
		String[] role = ((String) objects[4]).split(",");
		for(int i = 0; i < role.length; i++) {
			roleArrayList.add(role[i].trim());
		}
		setAttribute("role", roleArrayList);
		
		this.sign = (Integer) objects[5];
		this.immutable = (Integer) objects[6];
		this.ts = (Long) objects[7];
	}

	public SecurityPunctuation(SecurityPunctuation sp) {
		setSchema(sp.getSchema());
		setAttribute("streamname", sp.getAttribute("streamname"));
		setAttribute("tupleStartTS", sp.getAttribute("tupleStartTS"));
		setAttribute("tupleEndTS", sp.getAttribute("tupleEndTS"));
		setAttribute("attributeNames", sp.getAttribute("attributeNames"));
		setAttribute("role", sp.getAttribute("role"));
		this.sign = sp.sign;
		this.immutable = sp.immutable;
		this.ts = sp.ts;
	}	
	
	@Override
	public SecurityPunctuation clone() {
		return new SecurityPunctuation(this);
	}
	
	public Boolean evaluate(Long tupleTS, List<String> userRoles, Tuple<?> tuple, SDFSchema schema) {
		if( this.sign == 1 
			&& evaluateRoles(userRoles)
			&& evaluateTS(tupleTS)
			&& evaluateAttributes(tuple, schema)
			&& evaluateStreamName(schema)
			) {
			return true;
		}
		return false;
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
		if(this.getStringArrayListAttribute("role") == null) {
			return false;
		}			
		if(this.getStringArrayListAttribute("role").isEmpty()) {
			return true;
		}
		for(String role:(this.getStringArrayListAttribute("role"))) {
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
			if(this.getStringArrayListAttribute("attributeNames") == null) {
				return false;
			}
			if((this.getStringArrayListAttribute("attributeNames")).isEmpty()) {
				return true;
			}
			
			for(int i = 0; i < schema.size(); i++) {
				Boolean setToNull = true;
				String schemaAttribute = schema.getAttribute(i).getAttributeName();
				for(String attributeNames:this.getStringArrayListAttribute("attributeNames")) {
					if(schemaAttribute.equals(attributeNames)) {
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
		if(this.getStringArrayListAttribute("streamname") == null) {
			return false;
		}
		if((this.getStringArrayListAttribute("streamname")).isEmpty()) {
			return true;
		}
		for(String stream:this.getStringArrayListAttribute("streamname")) {
			if(schema.getURI().equals(stream)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean union(ISecurityPunctuation sp2) {
		if(sp2 instanceof SecurityPunctuation) {
			// Union gibt es nur, wenn SP mit gleichem Zeitstempel aus der gleichen Quelle kommen und immutable sind
			if(this.getLongAttribute("ts") == sp2.getLongAttribute("ts") 
					&& getSchema().getURI().equals(((SecurityPunctuation) sp2).getSchema().getURI())
					&& this.immutable == 1
					&& ((SecurityPunctuation)sp2).immutable == 1
					) {
				mergeStringArrayList(this.getStringArrayListAttribute("streamname"), sp2.getStringArrayListAttribute("streamname"));
				
				// kleineren Wählen --> Bereich wird größer
				if(this.getLongAttribute("tupleStartTS") > sp2.getLongAttribute("tupleStartTS")) {
					setAttribute("tupleStartTS", sp2.getLongAttribute("tupleStartTS"));
				} 
				
				// größeren Wählen --> Bereich wird größer
				if(this.getLongAttribute("tupleEndTS") < sp2.getLongAttribute("tupleEndTS")) {
					setAttribute("tupleEndTS", sp2.getLongAttribute("tupleEndTS"));
				} 
				mergeStringArrayList(this.getStringArrayListAttribute("attributeNames"), sp2.getStringArrayListAttribute("attributeNames"));
				mergeStringArrayList(this.getStringArrayListAttribute("role"), sp2.getStringArrayListAttribute("role"));
				//Was soll hier passieren?
//				setAttribute("sign", ???);
//				setAttribute("immutable", ???);
//				setAttribute("ts", ???);
				return true;
			}
		}
		return false;
	}
	
	@Override 
	public boolean intersect(ISecurityPunctuation sp2) {
		if(sp2 instanceof SecurityPunctuation) {
			// Union gibt es nur, wenn SP mit gleichem Zeitstempel aus der gleichen Quelle kommen und immutable sind
			if(this.ts == ((SecurityPunctuation)sp2).ts 
					&& !this.getSchema().getURI().equals(((SecurityPunctuation) sp2).getSchema().getURI())
					&& this.immutable == 1
					&& ((SecurityPunctuation)sp2).immutable == 1
					) {


				intersectStringArrayList(this.getStringArrayListAttribute("streamname"), sp2.getStringArrayListAttribute("streamname"));
				
				// größeren Wählen --> Bereich wird kleiner
				if(this.getLongAttribute("tupleStartTS") < sp2.getLongAttribute("tupleStartTS")) {
					setAttribute("tupleStartTS", sp2.getLongAttribute("tupleStartTS"));
				} 
				
				// kleineren Wählen --> Bereich wird kleiner
				if(this.getLongAttribute("tupleEndTS") > sp2.getLongAttribute("tupleEndTS")) {
					setAttribute("tupleEndTS", sp2.getLongAttribute("tupleEndTS"));
				} 
				intersectStringArrayList(this.getStringArrayListAttribute("attributeNames"), sp2.getStringArrayListAttribute("attributeNames"));
				intersectStringArrayList(this.getStringArrayListAttribute("role"), sp2.getStringArrayListAttribute("role"));
				//Was soll hier passieren?
//				setAttribute("sign", ???);
//				setAttribute("immutable", ???);
//				setAttribute("ts", ???);
				return true;
			}
		}
		return false;
	}
	
	private void mergeStringArrayList(ArrayList<String> list, ArrayList<String> list2) {
		if(list.size() == 1 && list.get(0).equals("*")) {
			return;
		}
		if(list2.size() == 1 && list2.get(0).equals("*")) {
			list = list2;
			return;
		}
		for(String streamname:list2) {
			if(!list.contains(streamname)) {
				list.add(streamname);
			}
		}
	}
	
	private void intersectStringArrayList(ArrayList<String> list, ArrayList<String> list2) {
		if(list.size() == 1 && list.get(0).equals("*")) {
			list = list2;
			return;
		}
		if(list2.size() == 1 && list2.get(0).equals("*")) {
			return;
		}
		for(int i = list.size() - 1; i >= 0; i--) {
			if(!list2.contains(list.get(i))) {
				list.remove(i);
			}
		}
	}
}
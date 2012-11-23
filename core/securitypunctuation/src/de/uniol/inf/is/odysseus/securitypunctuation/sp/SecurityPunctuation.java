/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.securitypunctuation.sp;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;

public class SecurityPunctuation extends AbstractSecurityPunctuation {
	
    private static Logger LOG = LoggerFactory.getLogger(SecurityPunctuation.class);
	
	private static final long serialVersionUID = 8534064040716648960L;
	
	private ArrayList<Integer> evaluateAttributesCache = new ArrayList<Integer>();
	
	/**
	 * Gibt an, ob die Attribute der SP leer sind - die SP also keinerlei Zugriff gewährt.
	 */
	private Boolean isEmpty;
	
	public SecurityPunctuation(Object[] objects, SDFSchema schema) {
		super((Long) objects[1]);
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
		
		this.setAttribute("sign", (Integer) objects[5]);
		this.setAttribute("mutable", (Integer) objects[6]);
		this.setAttribute("ts", (Long) objects[7]);
	}

	public SecurityPunctuation(SecurityPunctuation sp) {
		super(sp);
		setSchema(sp.getSchema());
		setAttribute("streamname", sp.getAttribute("streamname"));
		setAttribute("tupleStartTS", sp.getAttribute("tupleStartTS"));
		setAttribute("tupleEndTS", sp.getAttribute("tupleEndTS"));
		setAttribute("attributeNames", sp.getAttribute("attributeNames"));
		setAttribute("role", sp.getAttribute("role"));
		this.setAttribute("sign", sp.getIntegerAttribute("sign"));
		this.setAttribute("mutable", sp.getIntegerAttribute("mutable"));
		this.setAttribute("ts", sp.getLongAttribute("ts"));
	}	
	
	/**
	 * Creates an empty security punctuation that allows no access to the stream
	 */
	public SecurityPunctuation(SDFSchema schema, Long ts) {
		super(ts);
		setSchema(schema);
		isEmpty = true;
		this.setAttribute("sign", 1);
		this.setAttribute("mutable", 1);
		this.setAttribute("ts", ts);
	}
	
	public Boolean evaluate(Long tupleTS, List<String> userRoles, Tuple<?> tuple, SDFSchema schema) {
		if( !this.isEmpty()
			&& this.getIntegerAttribute("sign") == 1 
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
			return false;
		}
		if(this.getStringArrayListAttribute("role").size() == 1 && this.getStringArrayListAttribute("role").get(0).equals("*")) {
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
				return false;
			}
			if((this.getStringArrayListAttribute("attributeNames")).size() == 1 && this.getStringArrayListAttribute("attributeNames").get(0).equals("*")) {
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
		if(this.getStringArrayListAttribute("streamname") == null) {
			return false;
		}
		if((this.getStringArrayListAttribute("streamname")).isEmpty()) {
			return false;
		}
		if((this.getStringArrayListAttribute("streamname")).size() == 1 && this.getStringArrayListAttribute("streamname").get(0).equals("*")) {
			return true;
		}		
		
		Boolean returnValue = true;
		for(String baseSourceName:schema.getBaseSourceNames()) {
			if(!this.getStringArrayListAttribute("streamname").contains(baseSourceName)) {
				returnValue = false;
			}
		}
		if(returnValue) {
			return true;
		}
		return false;
	}

	/**
	 * Logik zum entscheiden, ob auf Tupel union() oder intersect() angewendet werden soll.
	 * Falls beides nicht zutrifft wird null zurückgegeben.
	 */
	@Override
	public ISecurityPunctuation processSP(ISecurityPunctuation isp) {
		SecurityPunctuation sp2 = (SecurityPunctuation)isp;
		if(sp2.isEmpty()) {
			return null;
		}
		
		if(this.getSchema().getURI().equals(sp2.getSchema().getURI())) {
			if(this.getLongAttribute("ts") == sp2.getLongAttribute("ts")) {
				if(this.getIntegerAttribute("mutable") >= 1 && sp2.getIntegerAttribute("mutable") >= 1) {
					return this.union(sp2);
				} 
			} else {
				if(sp2.getIntegerAttribute("mutable") == 2 && this.getIntegerAttribute("mutable") >= 1) {
					return this.union(sp2);
				} 
			}
		} else {
			if(this.getIntegerAttribute("mutable") >= 1 && sp2.getIntegerAttribute("mutable") >= 2) {
				return this.intersect(sp2);
			}
		}
		return null;
	}
	
	/**
	 * Vereinigt zwei SP und gibt null zurück, falls die Bedingungen für eine Vereinigung nicht erfüllt sind
	 */
	@Override
	public ISecurityPunctuation union(ISecurityPunctuation sp2) {
		if(sp2 instanceof SecurityPunctuation) {			
			SecurityPunctuation newSP = new SecurityPunctuation(this);
			
			if(sp2.getIntegerAttribute("sign") == 1) {				
				
				newSP.mergeStringArrayList(newSP.getStringArrayListAttribute("streamname"), sp2.getStringArrayListAttribute("streamname"));
				
				// kleineren Wählen --> Bereich wird größer
				if(newSP.getLongAttribute("tupleStartTS") > sp2.getLongAttribute("tupleStartTS")) {
					newSP.setAttribute("tupleStartTS", sp2.getLongAttribute("tupleStartTS"));
				} 
				
				// größeren Wählen --> Bereich wird größer
				if(newSP.getLongAttribute("tupleEndTS") < sp2.getLongAttribute("tupleEndTS")) {
					newSP.setAttribute("tupleEndTS", sp2.getLongAttribute("tupleEndTS"));
				} 
				newSP.mergeStringArrayList(newSP.getStringArrayListAttribute("attributeNames"), sp2.getStringArrayListAttribute("attributeNames"));
				newSP.mergeStringArrayList(newSP.getStringArrayListAttribute("role"), sp2.getStringArrayListAttribute("role"));
				
				newSP.setAttribute("sign", 1); //Sign = 0 macht nur Sinn für Tuple, die noch unterwegs sind und vorrangegangene Tupel einschränken können
				newSP.setAttribute("mutable", 1); //Falls weitere Tupel ankommen die mutable = 2 haben, soll auch die Verarbeitung möglich sein.
				if(sp2.getLongAttribute("ts") > newSP.getLongAttribute("ts")) {
					newSP.setAttribute("ts", sp2.getLongAttribute("ts"));
				} 
				return newSP;
			} else if(sp2.getIntegerAttribute("sign") == 0){
				newSP.minusStringArrayList(newSP.getStringArrayListAttribute("streamname"), sp2.getStringArrayListAttribute("streamname"));

				//MUSS EIGENTLICH IRGENWIE ANDERS!!!!
				
				// kleineren Wählen --> Bereich wird größer
				if(newSP.getLongAttribute("tupleStartTS") > sp2.getLongAttribute("tupleStartTS")) {
					newSP.setAttribute("tupleStartTS", sp2.getLongAttribute("tupleStartTS"));
				} 
				
				// größeren Wählen --> Bereich wird größer
				if(newSP.getLongAttribute("tupleEndTS") < sp2.getLongAttribute("tupleEndTS")) {
					newSP.setAttribute("tupleEndTS", sp2.getLongAttribute("tupleEndTS"));
				} 
				
				newSP.minusStringArrayList(newSP.getStringArrayListAttribute("attributeNames"), sp2.getStringArrayListAttribute("attributeNames"));
				newSP.minusStringArrayList(newSP.getStringArrayListAttribute("role"), sp2.getStringArrayListAttribute("role"));

				newSP.setAttribute("sign", 1); //Sign = 0 macht nur Sinn für Tuple, die noch unterwegs sind und vorrangegangene Tupel einschränken können
				newSP.setAttribute("mutable", 1); //Falls weitere Tupel ankommen die mutable = 2 haben, soll auch die Verarbeitung möglich sein
				if(sp2.getLongAttribute("ts") > newSP.getLongAttribute("ts")) {
					newSP.setAttribute("ts", sp2.getLongAttribute("ts"));
				} 
				return newSP;
			}
		}
		return null;
	}
	
	/**
	 *
	 */
	@Override 
	public ISecurityPunctuation intersect(ISecurityPunctuation sp2) {		
		if(sp2 instanceof SecurityPunctuation) {

			SecurityPunctuation newSP = new SecurityPunctuation(this);

			newSP.intersectStringArrayList("streamname", sp2.getStringArrayListAttribute("streamname"));
				
			// größeren Wählen --> Bereich wird kleiner
			if(newSP.getLongAttribute("tupleStartTS") < sp2.getLongAttribute("tupleStartTS")) {
				newSP.setAttribute("tupleStartTS", sp2.getLongAttribute("tupleStartTS"));
			} 
				
			// kleineren Wählen --> Bereich wird kleiner
			if(newSP.getLongAttribute("tupleEndTS") > sp2.getLongAttribute("tupleEndTS")) {
				newSP.setAttribute("tupleEndTS", sp2.getLongAttribute("tupleEndTS"));
			} 
			newSP.intersectStringArrayList("attributeNames", sp2.getStringArrayListAttribute("attributeNames"));
			newSP.intersectStringArrayList("role", sp2.getStringArrayListAttribute("role"));

			newSP.setAttribute("sign", 1); //Sign = 0 macht nur Sinn für Tuple, die noch unterwegs sind und vorrangegangene Tupel einschränken können
			newSP.setAttribute("mutable", 0); //Da intersect z.B. beim Join ausgeführt wird und die SP danach nicht mehr geändert werden können sollen.
			if(sp2.getLongAttribute("ts") > newSP.getLongAttribute("ts")) {
				newSP.setAttribute("ts", sp2.getLongAttribute("ts"));
			} 
			return newSP;
		}
		return null;
	}	

	/**
	 * @param sp2
	 * @param newTS the timestamp for the new sp
	 * @return
	 */
	@Override 
	public ISecurityPunctuation intersect(ISecurityPunctuation sp2, Long newTS) {	
		SecurityPunctuation newSP = (SecurityPunctuation)intersect(sp2);
		newSP.setAttribute("ts", newTS);
		return newSP;
	}
	
	@SuppressWarnings("unchecked")
	private void mergeStringArrayList(ArrayList<String> list, ArrayList<String> list2) {
		if(list == null && list2 == null) {
			return;
		}
		if(list2 == null) {
			return;
		}
		if(list == null
				|| list2.size() == 1 && list2.get(0).equals("*")) {
			list = (ArrayList<String>) list2.clone();
			return;
		}
		if(list.size() == 1 && list.get(0).equals("*")) {
			return;
		}
		for(String streamname:list2) {
			if(!list.contains(streamname)) {
				list.add(streamname);
			}
		}
	}
	
	private void minusStringArrayList(ArrayList<String> list, ArrayList<String> list2) {
		if(list == null && list2 == null) {
			return;
		}
		if(list2.size() == 1 && list2.get(0).equals("*")) {
			list = new ArrayList<String>();
			return;
		}
		for(String streamname:list2) {
			if(list.contains(streamname)) {
				list.remove(streamname);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void intersectStringArrayList(String attribute, ArrayList<String> list2) {
		ArrayList<String> list = this.getStringArrayListAttribute(attribute);
		if(list.size() == 1 && list.get(0).equals("*")) {
			list = (ArrayList<String>) list2.clone();
		} else if(list2.size() == 1 && list2.get(0).equals("*")) {
			return;
		} else {
			for(int i = list.size() - 1; i >= 0; i--) {
				if(!list2.contains(list.get(i))) {
					list.remove(i);
				}
			}
		}
		this.setAttribute(attribute, list);
	}
	
	public Boolean isEmpty() {
		if(isEmpty == null) {
			isEmpty = this.getStringArrayListAttribute("attributeNames").isEmpty()
						&& this.getStringArrayListAttribute("streamname").isEmpty()
						&& this.getStringArrayListAttribute("role").isEmpty()
						&& this.getLongAttribute("tupleStartTS") == -1
						&& this.getLongAttribute("tupleEndTS") == -1;
		} 
		return isEmpty;
	}
	
	/**
	 * for debugging
	 */
	public void printSP() {
		LOG.debug("" + this);
		LOG.debug("" + this.getSchema());
		LOG.debug("" + this.getAttribute("streamname"));
		LOG.debug("" + this.getAttribute("tupleStartTS"));
		LOG.debug("" + this.getAttribute("tupleEndTS"));
		LOG.debug("" + this.getAttribute("attributeNames"));
		LOG.debug("" + this.getAttribute("role"));
		LOG.debug("" + this.getIntegerAttribute("sign"));
		LOG.debug("" + this.getIntegerAttribute("mutable"));
		LOG.debug("" + this.getLongAttribute("ts"));
	}
}
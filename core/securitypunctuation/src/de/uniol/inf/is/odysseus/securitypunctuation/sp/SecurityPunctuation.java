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
	
	//TODO Kann wieder raus...
	@Override
	public SecurityPunctuation clone() {
		return new SecurityPunctuation(this);
	}
	
	//TODO tupleTS ist doppelt...
	public Boolean evaluate(Long tupleTS, List<String> userRoles, Tuple<?> tuple, SDFSchema schema) {
		if( this.getIntegerAttribute("sign") == 1 
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
		//Checken, ob in schema mehrere Streamnamen "gemerged" sind (Diese sind dann mit Komma voneinander getrennt)
		String[] streamsInSchemaArray = null;
		if(schema.getURI().contains(",")) {
			streamsInSchemaArray = schema.getURI().split(",");
		}
		
		for(String stream:this.getStringArrayListAttribute("streamname")) {
			for(String streamInSchema:streamsInSchemaArray) {
				if(streamInSchema.equals(stream)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Logik zum entscheiden, ob auf Tupel union() oder intersect() angewendet werden soll.
	 * Falls beides nicht zutrifft wird null zurückgegeben.
	 */
	@Override
	public ISecurityPunctuation processSP(ISecurityPunctuation isp) {
		AbstractSecurityPunctuation sp2 = (AbstractSecurityPunctuation)isp;
		
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
			return this.intersect(sp2);
		}
		return null;
	}
	
	/**
	 * Vereinigt zwei SP und gibt null zurück, falls die Bedingungen für eine Vereinigung nicht erfüllt sind
	 */
	@Override
	public ISecurityPunctuation union(ISecurityPunctuation sp2) {
		
		LOG.debug("union in SP - TS: " + this.getLongAttribute("ts"));
		
		if(sp2 instanceof SecurityPunctuation) {
			
			// Union gibt es nur, wenn SP mit gleichem Zeitstempel aus der gleichen Quelle kommen und mutable sind
			// Logik, wann es ausgeführt werden soll/kann liegt eher in processSP()!!!!!
			
//			if(this.getLongAttribute("ts") == sp2.getLongAttribute("ts") 
//					&& getSchema().getURI().equals(((SecurityPunctuation) sp2).getSchema().getURI())
//					&& this.getIntegerAttribute("mutable") == 1
//					&& sp2.getIntegerAttribute("mutable") == 1
//					) {
			
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
				if(this.getLongAttribute("ts") == sp2.getLongAttribute("ts")) {
					newSP.setAttribute("ts", this.getLongAttribute("ts")); //Egal welcher genommen wird.
				} else {
					newSP.setAttribute("ts", sp2.getLongAttribute("ts")); //richtig so????
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
				newSP.setAttribute("mutable", 1); //Falls weitere Tupel ankommen die mutable = 2 haben, soll auch die Verarbeitung möglich sein.
				if(this.getLongAttribute("ts") == sp2.getLongAttribute("ts")) {
					newSP.setAttribute("ts", this.getLongAttribute("ts")); //Egal welcher genommen wird.
				} else {
					newSP.setAttribute("ts", sp2.getLongAttribute("ts")); //richtig so????
				}
			}
		}
		return null;
	}
	
	/**
	 *
	 */
	@Override 
	public ISecurityPunctuation intersect(ISecurityPunctuation sp2) {
		
		LOG.debug("intersect in SP - TS: " + this.getLongAttribute("ts"));
		
		if(sp2 instanceof SecurityPunctuation) {
			// intersect gibt es nur, wenn SP mit gleichem Zeitstempel aus der gleichen Quelle kommen und mutable sind
			// Logik, wann es ausgeführt werden soll/kann liegt eher in processSP()!!!!!
			
//			if(this.getLongAttribute("ts") == sp2.getLongAttribute("ts")
//					&& !this.getSchema().getURI().equals(((SecurityPunctuation) sp2).getSchema().getURI())
//					&& this.getIntegerAttribute("mutable") == 1
//					&& sp2.getIntegerAttribute("mutable") == 1
//					) {

				SecurityPunctuation newSP = new SecurityPunctuation(this);

				newSP.intersectStringArrayList(newSP.getStringArrayListAttribute("streamname"), sp2.getStringArrayListAttribute("streamname"));
				
				// größeren Wählen --> Bereich wird kleiner
				if(newSP.getLongAttribute("tupleStartTS") < sp2.getLongAttribute("tupleStartTS")) {
					newSP.setAttribute("tupleStartTS", sp2.getLongAttribute("tupleStartTS"));
				} 
				
				// kleineren Wählen --> Bereich wird kleiner
				if(newSP.getLongAttribute("tupleEndTS") > sp2.getLongAttribute("tupleEndTS")) {
					newSP.setAttribute("tupleEndTS", sp2.getLongAttribute("tupleEndTS"));
				} 
				newSP.intersectStringArrayList(newSP.getStringArrayListAttribute("attributeNames"), sp2.getStringArrayListAttribute("attributeNames"));
				newSP.intersectStringArrayList(newSP.getStringArrayListAttribute("role"), sp2.getStringArrayListAttribute("role"));

				newSP.setAttribute("sign", 1); //Sign = 0 macht nur Sinn für Tuple, die noch unterwegs sind und vorrangegangene Tupel einschränken können
				newSP.setAttribute("mutable", 1); //Falls weitere Tupel ankommen die mutable = 2 haben, soll auch die Verarbeitung möglich sein.
				if(this.getLongAttribute("ts") == sp2.getLongAttribute("ts")) {
					newSP.setAttribute("ts", this.getLongAttribute("ts")); //Egal welcher genommen wird.
				} else {
					newSP.setAttribute("ts", sp2.getLongAttribute("ts")); //richtig so????
				}
				return newSP;
//			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private void mergeStringArrayList(ArrayList<String> list, ArrayList<String> list2) {
		if(list == null && list2 == null) {
			return;
		}
		if(list2 == null
				|| list.size() == 1 && list.get(0).equals("*")) {
			return;
		}
		if(list == null
				|| list2.size() == 1 && list2.get(0).equals("*")) {
			list = (ArrayList<String>) list2.clone();
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
	
	public Boolean isEmpty() {
		if(isEmpty == null) {
			// isEmpty überprüfen!!!
		} 
//		return isEmpty;
		return false;
	}
}
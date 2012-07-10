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
//package de.uniol.inf.is.odysseus.core.server.sparql.physicalops.object;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//
//import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
//import com.hp.hpl.jena.graph.Node;
//
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.IClone;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.container.MetaAttribute;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.util.SPARQL_Util;
//import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFSchema;
//
///**
// * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>
// */
//public class NodeList<T extends IClone> extends MetaAttribute<T> implements Comparable<NodeList<T>>, Iterable<Node>{
//
//	private ArrayList<Node> elems = new ArrayList<Node>();		
//	
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 8805922431914228778L;
//
//	public NodeList(){
//		super();
//	}
//	
//	public NodeList(NodeList<T> toCopy){
//		elems = new ArrayList<Node>(toCopy.elems);
//		this.setMetadata((T)toCopy.getMetadata().clone());
//	}
//	
//	/**
//	 * Creates a new NodeList with a copy of the overgiven old nodes.
//	 * The new meta data will not be copied but directly inserted into
//	 * the new object
//	 * @param toCopy The old NodeList that will be copied before data will be inserted
//	 * @param newMetadata The new meta data that will be directly inserted into the new object.
//	 */
//	public NodeList(NodeList toCopy, T newMetadata){
//		elems = new ArrayList<Node>(toCopy.elems);
//		this.setMetadata(newMetadata);
//	}
//	
//	public NodeList(int size){
//		elems = new ArrayList<Node>(size);
//	}
//
//	public Node get(int arg0) {
//		return elems.get(arg0);
//	}
//
//	public int size() {
//		return elems.size();
//	}
//
//	public Node set(int arg0, Node arg1) {
//		return elems.set(arg0, arg1);
//	}
//
//	public boolean add(Node arg0) {
//		return elems.add(arg0);
//	}
//
//	public int compareTo(NodeList<T> c){
//		int min = c.size();
//		if (min > this.size()) {
//			min = this.size();
//		}
//		int compare = 0;
//		int i = 0;
//		for (i = 0; i < min && compare == 0; i++) {
//			compare = this.compare(this.get(i), c.get(i));
//		}
//		if (compare < 0) {
//			compare = (-1) * i;
//		}
//		if (compare > 0) {
//			compare = i;
//		}
//		return compare;
//	}
//	
//	/**
//	 * erzeugen eines neuen Objektes, in dem nur die Attribute betrachtet
//	 * werden, die in der attrList uebergeben werden, die Reihenfolge des neuen
//	 * Objektes wird durch die Reihenfolge der Attribute im Array festgelegt
//	 * Beispiel: attrList[1]=14,attrList[2]=12 erzeugt ein neues Objekt, welches
//	 * die Attribute 14 und 12 enthaelt.
//	 * 
//	 * Falls das aktuelle Tuple keine Schemainformationen enthaelt,
//	 * wird das neue auch ohne erzeugt. Ansonsten enthaelt das neue Tuple auch
//	 * die passenden Schemainformationen.
//	 * 
//	 * @param attrList
//	 *            erzeugt ein neues Objekt das die Attribute der Positionen aus
//	 *            attrList enthaelt
//	 */
//	public NodeList<T> restrict(int[] attrList, SDFSchema overwriteSchema) {
//		NodeList<T> newAttrList = new NodeList<T>(attrList.length);
//
//		for (int i = 0; i < attrList.length; i++) {
//			Node curAttribute = this.get(attrList[i]);
//			newAttrList.set(i, curAttribute);
//		}		
//		
//		return newAttrList;
//	}
//	
//	@Override
//	public NodeList<T> clone(){
//		return new NodeList<T>(this);
//	}
//	
//	/**
//	 * 
//	 * @return n1 < n2 -> -1, n1 == n2 -> 0, n1 > n2 -> 1
//	 */
//	private int compare(Node n1, Node n2){
//		int retval = 0;
//		
//		if(n1 == SPARQL_Util.UNBOUND_ATTRIBUTE_NODE &&
//				n2 != SPARQL_Util.UNBOUND_ATTRIBUTE_NODE){
//			return -1;
//		}
//		else if(n1.isBlank() && n2 == SPARQL_Util.UNBOUND_ATTRIBUTE_NODE ){
//			return 1;
//		}
//		else if(n1.isBlank() && n2 != SPARQL_Util.UNBOUND_ATTRIBUTE_NODE
//				&& !n2.isBlank()){
//			return -1;
//		}
//		else if(n1.isURI() && (n2 == SPARQL_Util.UNBOUND_ATTRIBUTE_NODE || 
//								n2.isBlank())){
//			return 1;
//		}
//		else if(n1.isURI() && n2 != SPARQL_Util.UNBOUND_ATTRIBUTE_NODE && 
//				!n2.isBlank() && 
//				!n2.isURI()){
//			return -1;
//		}
//		else if(n1.isURI() && n2.isURI()){
//			int compVal = n1.getURI().compareTo(n2.getURI());
//			if(compVal != 0){
//				return compVal;
//			}
//		}
//		else if(n1.isLiteral() && (n2 == SPARQL_Util.UNBOUND_ATTRIBUTE_NODE || 
//									n2.isBlank() || 
//									n2.isURI())){
//				return 1;
//		}
//		else if(n1.isLiteral() && n2.isLiteral()){
//			if(n1.getLiteralDatatype() != null &&
//					n2.getLiteralDatatype() != null){
//				if(n1.getLiteralDatatype().equals(XSDDatatype.XSDstring) &&
//					!n2.getLiteralDatatype().equals(XSDDatatype.XSDstring)){
//					return 1;
//				}
//				else if(!n1.getLiteralDatatype().equals(XSDDatatype.XSDstring) &&
//					n2.getLiteralDatatype().equals(XSDDatatype.XSDstring)){
//					return -1;
//				}
//				else{
//					int compVal = n1.getLiteralLexicalForm().compareTo(n2.getLiteralLexicalForm());
//					return compVal;
//				}
//			}
//			else{
//				int compVal =  n1.getLiteralLexicalForm().compareTo(n2.getLiteralLexicalForm());
//				return compVal;
//			}
//		}
//		// SPARQL_Util.EXTRA_ATTRIBUTE_NODEs are literals
//		// they are equal to each other and are set to
//		// the end of the list.
//		return retval;
//
//	}
//	
//	/**
//	 * @param n2 the nodelist to compare to
//	 * @return true if all nodes from this and n2 are equal, false otherwise
//	 */
//	public boolean equals(NodeList<T> n2){
//		if(this.size() != n2.size()){
//			return false;
//		}
//		else{
//			for(int i = 0; i<this.size(); i++){
//				if(!this.get(i).equals(n2.get(i))){
//					return false;
//				}
//			}
//		}
//		
//		return true;
//	}
//	
//	public int hashCode(){
//		int retval = 0;
//		for(int i = 0; i<this.size(); i++){
//			retval += 2^i * this.get(i).hashCode();
//		}
//		return retval;
//	}
//
//	public Iterator<Node> iterator() {
//		return elems.iterator();
//	}
//	
//	public String toString(){
//		StringBuffer sb = new StringBuffer();
//		for(Node n: this.elems){
//			sb.append(n != null ? n.toString() : n);
//			sb.append("   <|>   ");
//		}
//		sb.append(this.getMetadata() != null ? this.getMetadata().toString() : this.getMetadata());
//		return sb.toString();
//	}
//}

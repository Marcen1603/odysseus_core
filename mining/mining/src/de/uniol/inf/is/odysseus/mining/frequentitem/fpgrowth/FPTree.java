/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.mining.frequentitem.fpgrowth;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

/**
 * @author Dennis Geesen
 *
 */
public class FPTree<M extends IMetaAttribute> {

	private FPTreeNode<M> root = new FPTreeNode<M>(null);	
	private TreeMap<Tuple<M>, FPTreeNode<M>> headerTable = new TreeMap<Tuple<M>, FPTreeNode<M>>();
	private TreeMap<Tuple<M>, Integer> headerTableCount = new TreeMap<Tuple<M>, Integer>();
	private TreeMap<Tuple<M>, FPTreeNode<M>> headerTableLastNode = new TreeMap<Tuple<M>, FPTreeNode<M>>();
	
	
	public synchronized int getCount(Tuple<M> t){
		return this.headerTableCount.get(t);
	}
	
	
	public synchronized List<Tuple<M>> getDescendingHeaderList(){
		return new ArrayList<Tuple<M>>(this.headerTable.descendingKeySet());		
	}
	
	public synchronized void addToHeaderTable(FPTreeNode<M> node){
		if(this.headerTable.get(node.getItem())== null){
			this.headerTable.put(node.getItem(), node);			
			this.headerTableLastNode.put(node.getItem(), node);
		}else{
			this.headerTableLastNode.get(node.getItem()).setLink(node);			
			this.headerTableLastNode.put(node.getItem(), node);
		}
	}
	
	public boolean isEmpty(){
		if(this.root.getChilds().size()==0){
			return true;
		}
		return false;
	}
	
	public synchronized void removeWithoutMinSupport(int minsupport){
		Iterator<Entry<Tuple<M>, Integer>> iter = this.headerTableCount.entrySet().iterator();
		while(iter.hasNext()){
			Entry<Tuple<M>, Integer> e = iter.next();
			if(e.getValue()<minsupport){
				println("REMOVE: "+e.getKey());
				FPTreeNode<M> n = this.headerTable.get(e.getKey());
				while(n!=null){					
					removeNode(n);					
					n = n.getLink();						
				}				
				this.headerTable.remove(e.getKey());
				//this.headerTableCount.remove(e.getKey());
				iter.remove();
				this.headerTableLastNode.remove(e.getKey());			
			}
		}
		
	}
	
	private void removeNode(FPTreeNode<M> nodeToRemove){
		if(nodeToRemove==null){
			return;
		}
		nodeToRemove.getParent().getChilds().remove(nodeToRemove);
		for(FPTreeNode<M> child : nodeToRemove.getChilds()){
			nodeToRemove.getParent().addChild(child);
			child.setParent(nodeToRemove.getParent());
		}
	}
	
	public List<FPTreeNode<M>> getSinglePrefixPath(){
		List<FPTreeNode<M>> spp = new ArrayList<FPTreeNode<M>>();
		FPTreeNode<M> branchNode = root;
		do{		
			spp.add(branchNode);
			branchNode = branchNode.getChilds().get(0);
		}while(branchNode.getChilds().size()==1);
		return spp;
	}
	
	public FPTree<M> getMultiPathTree(){
		FPTree<M> tree = new FPTree<M>();
		for(FPTreeNode<M> child : this.getSinglePrefixBranchNode().getChilds()){
			tree.getRoot().addChild(child);			
			tree.addToHeaderTable(child);
			tree.increaseHeaderCount(child.getItem(), 1);
		}
		return tree;
	}
	
	
	public synchronized List<Pattern<M>> getPrefixPaths(Tuple<M> tuple){
		List<Pattern<M>> paths = new ArrayList<Pattern<M>>();
		FPTreeNode<M> node = this.headerTable.get(tuple);
		while(node!=null){
			Pattern<M> p = this.getPatternBottomUp(node);
			if(!p.isEmpty()){
				paths.add(p);
			}
			node = node.getLink();
		}
		return paths;		
	}
		
		
	private Pattern<M> getPatternBottomUp(FPTreeNode<M> node) {
		int count = node.getCount();
		Pattern<M> p = new Pattern<M>();
		while(node != null && node.getItem()!=null){
			p.add(node.getItem(), count);
			node = node.getParent();
		}
		// remove the node for this path
		p.removeFirst();
		// since it is bottom up, reverse it
		p.reverse();
		return p;
	}

	public FPTreeNode<M> getSinglePrefixBranchNode(){
		FPTreeNode<M> branchNode = root;
		while(branchNode.getChilds().size()==1){
			branchNode = branchNode.getChilds().get(0);
		}
		return branchNode;
	}
	
	public boolean hasSingePrefixPath(){
		return (!getSinglePrefixBranchNode().equals(root));
	}
	
	public void insertTree(List<Tuple<M>> transactionFList) {
		insertIntoTree(transactionFList, this.root, 1);
	}
	
	public void insertTree(Pattern<M> p){
		insertIntoTree(p.getPattern(), this.root, p.getSupport());
	}
	
	public void insertTree(List<Tuple<M>> transactionFList, FPTreeNode<M> root){
		insertIntoTree(transactionFList, root, 1);
	}
	
	private void insertIntoTree(List<Tuple<M>> transactionFList, FPTreeNode<M> root, int supportCount){
		Tuple<M> item = transactionFList.get(0);
		FPTreeNode<M> thechild = null;
		for (FPTreeNode<M> child : root.getChilds()) {
			if (child.getItem().equals(item)) {
				child.addCount(supportCount);				
				thechild = child;
				break;
			}
		}

		if (thechild == null) {
			thechild = new FPTreeNode<M>(item);
			root.addChild(thechild);
			thechild.setCount(supportCount);
			this.addToHeaderTable(thechild);
		}
		increaseHeaderCount(item, supportCount);
		transactionFList.remove(0);
		if (!transactionFList.isEmpty()) {
			insertIntoTree(transactionFList, thechild, supportCount);
		}
	}
	
	private synchronized void increaseHeaderCount(Tuple<M> item, int count){
		if(this.headerTableCount.containsKey(item)){
			int newcount = this.headerTableCount.get(item)+count;
			this.headerTableCount.put(item, newcount);
		}else{
			this.headerTableCount.put(item, count);
		}
	}
	
	/**
	 * @return the root
	 */
	public FPTreeNode<M> getRoot() {
		return root;
	}
	/**
	 * @param root the root to set
	 */
	public void setRoot(FPTreeNode<M> root) {
		this.root = root;
	}
	/**
	 * @return the headerTable
	 */
	public TreeMap<Tuple<M>, FPTreeNode<M>> getHeaderTable() {
		return headerTable;
	}
	/**
	 * @param headerTable the headerTable to set
	 */
	public void setHeaderTable(TreeMap<Tuple<M>, FPTreeNode<M>> headerTable) {
		this.headerTable = headerTable;
	}
	/**
	 * 
	 */
	public void printTree() {
		println(toString());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {	
		String s = ""; 
		s = this.root.printTree("", s);
		s = s+"Header Table: \n";
		for(Entry<Tuple<M>, FPTreeNode<M>> e : this.headerTable.entrySet()){
			FPTreeNode<M> node = e.getValue();				
			s = s+e.getKey()+" ("+(this.headerTableCount.get(e.getKey()))+"): "+node;			
			while(node.getLink()!=null){
				s = s+" -> "+node.getLink();
				node = node.getLink();
			}
			s = s+"\n";
		}
		return s;
	}
	
	
	private void println(String s){
		//System.out.println(s);
	}
	
		
	
}

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
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

/**
 * @author Dennis Geesen
 *
 */
public class FPTreeNode<M extends IMetaAttribute> {
	
	private int count;
	private Tuple<M> item;
	private FPTreeNode<M> link;
	private List<FPTreeNode<M>> childs = new ArrayList<FPTreeNode<M>>();
	private FPTreeNode<M> parent = null;
	private boolean isBranchingNode = false;
	
	public FPTreeNode(Tuple<M> item){
		this.item = item;
		count = 1;
		link = null;
	}
	
	public void addChild(FPTreeNode<M> child){
		this.childs.add(child);
		child.parent = this;
	}
	public List<FPTreeNode<M>> getChilds(){
		return this.childs;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the item
	 */
	public Tuple<M> getItem() {
		return item;
	}

	/**
	 * @param item the item to set
	 */
	public void setItem(Tuple<M> item) {
		this.item = item;
	}

	/**
	 * @return the link
	 */
	public FPTreeNode<M> getLink() {
		return link;
	}

	/**
	 * @param link the link to set
	 */
	public void setLink(FPTreeNode<M> link) {
		this.link = link;
	}

	/**
	 * 
	 */
	public void increaseCount() {
		this.count++;
	}
	
	public void addCount(int toAdd){
		this.count = this.count+toAdd;
	}
	
	public String printTree(String prefix, String appendTo){
		appendTo = appendTo+prefix+this.toString()+"\n";
		for(FPTreeNode<M> child : this.getChilds()){
			appendTo = child.printTree(prefix+"    ", appendTo);
		}
		return appendTo;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "["+this.item+"] ("+this.count+")";
	}

	/**
	 * @return the isBranchingNode
	 */
	public boolean isBranchingNode() {
		return isBranchingNode;
	}

	/**
	 * @param isBranchingNode the isBranchingNode to set
	 */
	public void setBranchingNode(boolean isBranchingNode) {
		this.isBranchingNode = isBranchingNode;
	}

	/**
	 * @return the parent
	 */
	public FPTreeNode<M> getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(FPTreeNode<M> parent) {
		this.parent = parent;
	}
	
	
}

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
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;

/**
 * @author Dennis Geesen
 * 
 */
public class Pattern<M extends ITimeInterval> implements IMetaAttributeContainer<M> {

	private static final long serialVersionUID = -2474068801651074450L;

	private ArrayList<Tuple<M>> pattern = new ArrayList<Tuple<M>>();
	private ArrayList<Integer> supports = new ArrayList<Integer>();
	private int support = Integer.MAX_VALUE;
	private M metadata;

	public Pattern() {

	}

	@SuppressWarnings("unchecked")
	public Pattern(Pattern<M> old) {
		for(int i=0;i<old.pattern.size();i++){
			this.pattern.add(old.pattern.get(i));
			this.supports.add(old.supports.get(i));
		}
		this.support = old.support;
		if(old.metadata!=null){
			this.metadata = (M) old.metadata.clone();
		}
	}
	

	/**
	 * @param t
	 * @param support2
	 */
	public Pattern(Tuple<M> t, int support) {
		this.add(t, support);
	}

	/**
	 * @param tuples
	 * @param support2
	 */
	public Pattern(List<Tuple<M>> tuples, int sup) {
		for(Tuple<M> t : tuples){
			this.add(t, sup);
		}
		this.support = sup;
	}

	public void add(Tuple<M> t, int supportCount) {
		this.pattern.add(t);
		this.supports.add(supportCount);
		@SuppressWarnings("unchecked")
		M clonedMD = (M) t.getMetadata().clone();
		this.metadata = clonedMD;
		if (supportCount <= this.support) {
			this.support = supportCount;
		}
	}

	public boolean contains(Tuple<M> t) {
		return this.pattern.contains(t);
	}

	public void reverse() {
		Collections.reverse(pattern);
	}

	public Pattern<M> substract(Pattern<M> p) {
		Pattern<M> newOne = new Pattern<M>();
		for (Tuple<M> item : this.pattern) {
			if (!p.contains(item)) {
				newOne.add(item, 1);
			}
		}
		return newOne;
	}

	public Pattern<M> combine(Pattern<M> p) {
		for (Tuple<M> t : p.getPattern()) {
			this.add(t, p.getSupport(t));
		}
		return this;
	}
	
	public boolean overlaps(Pattern<M> p) {
		for (Tuple<M> t : p.getPattern()) {
			if (this.contains(t)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean overlapsExceptForOne(Pattern<M> p) {
		int count = 0;
		if(p.length()!=this.length()){
			return false;
		}
		for (Tuple<M> t : p.getPattern()) {
			if (this.contains(t)) {
				count++;
			}
		}
		if(count==(this.length()-1)){
			return true;
		}
		return false;
	}	

	public List<Pattern<M>> splitIntoSinglePatterns(){
		List<Pattern<M>> liste = new ArrayList<Pattern<M>>();
		for(int i=0;i<this.pattern.size();i++){
			Pattern<M> p = new Pattern<M>(this.pattern.get(i), this.supports.get(i));
			liste.add(p);
		}
		return liste;
	}
	
	public List<Tuple<M>> getPattern() {
		return this.pattern;
	}

	public int getSupport() {
		return this.support;
	}
	
	public int getSupport(Tuple<M> t){
		return this.supports.get(this.pattern.indexOf(t));
	}

	/**
	 * @return
	 */
	public int length() {
		return this.pattern.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Pattern<M> clone() {
		return new Pattern<M>(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.support + ": " + this.pattern;
	}

	/**
	 * 
	 */
	public void removeFirst() {
		this.pattern.remove(0);
	}

	/**
	 * @return
	 */
	public boolean isEmpty() {
		return this.pattern.isEmpty();
	}

	/**
	 * @return
	 */
	public M getMetadata() {
		return this.metadata;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer#setMetadata
	 * (de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute)
	 */
	@Override
	public void setMetadata(M metadata) {
		this.metadata = metadata;

	}
	
	public void setSupport(int sup) {
		this.support = sup;
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {		
		return this.pattern.hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Pattern){
			Pattern<?> other = (Pattern<?>) obj;
			if(this.pattern.containsAll(other.getPattern())){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}		
	}

	/**
	 * @return
	 */
	public PointInTime getMinTime() {
		PointInTime min = PointInTime.getInfinityTime();
		for(Tuple<M> t : this.pattern){
			if(min.after(t.getMetadata().getStart())){
				min = t.getMetadata().getStart();
			}
		}
		return min;
	}

}

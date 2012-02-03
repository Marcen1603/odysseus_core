/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.objecttracking.util;

/**
 * This class is used in a prediction join and the corresponding SweepAreas.
 * In the SweepAreas elements have to be predicted first and then evaluated.
 * They must then be predicted again in the join if the query predicate is true.
 * So, in a prediction join we return both, the predicted element and the element
 * from the SweepArea, so that we do not have to predict them again in the join
 * for merging.
 * 
 * @author abolles
 *
 * @param <T1> The element carried by this class. Both, left and right, must be
 * of the same type.
 */
public class Pair<T1, T2>{

	T1 left;
	T2 right;
	
	public Pair(T1 left, T2 right){
		this.left = left;
		this.right = right;
	}
	
	public T1 getLeft(){
		return left;
	}
	
	public T2 getRight(){
		return right;
	}
	
	public void setLeft(T1 left){
		this.left = left;
	}
	
	public void setRight(T2 right){
		this.right = right;
	}
}

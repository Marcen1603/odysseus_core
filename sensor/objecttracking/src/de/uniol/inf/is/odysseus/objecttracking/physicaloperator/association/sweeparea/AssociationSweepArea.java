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
package de.uniol.inf.is.odysseus.objecttracking.physicaloperator.association.sweeparea;

import de.uniol.inf.is.odysseus.intervalapproach.JoinTISweepArea;
import de.uniol.inf.is.odysseus.objecttracking.IPredictionFunctionKeyTimeIntervalProbabilityApplicationTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;



/**
 * This is a sweep area, that uses prediction functions for evaluating predicates.
 * If two elements overlap, only their values at max(left.ts, right.ts) will
 * be used for evaluating p_query
 * 
 * @TODO If anyone has an idea, how to substitute the casts by generics, tell me!
 * 
 * @author abolles
 *
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class AssociationSweepArea<M extends IPredictionFunctionKeyTimeIntervalProbabilityApplicationTime, T extends MVRelationalTuple<M>> extends JoinTISweepArea<T>{
//
//	SDFAttributeList leftSchema;
//	SDFAttributeList rightSchema;
//	
//	Map<IPredicate, IRangePredicate> rangePredicates;
//	
//	public AssociationSweepArea(SDFAttributeList leftSchema, SDFAttributeList rightSchema){
//		super();
//		this.leftSchema = leftSchema;
//		this.rightSchema = rightSchema;
//	}
//	
//	public void setRangePredicates(Map<IPredicate, IRangePredicate> rangePredicates){
//		this.rangePredicates = rangePredicates;
//	}
//	
//	public Iterator<T> query(T element, Order order){
//		LinkedList<Pair<T>> result = new LinkedList<Pair<T>>();
//		synchronized(this.elements){
//			switch (order) {
//			case LeftRight:
//				for (T next : this.elements) {
//					if (TimeInterval.totallyBefore(next.getMetadata(), element
//							.getMetadata())) {
//						continue;
//					}
//					if (TimeInterval.totallyAfter(next.getMetadata(), element
//							.getMetadata())) {
//						break;
//					}
//					
//					if(element.getMetadata().getStart().after(next.getMetadata().getStart())){
//						long sp = System.currentTimeMillis();
//						
//						T next_predicted = (T)next.getMetadata().predictData(this.rightSchema, next, element.getMetadata().getStart());
//						M predictedMetaRight = (M)next.getMetadata().predictMetadata(this.rightSchema, next, element.getMetadata().getStart());
//						next_predicted.setMetadata(predictedMetaRight);
//						
//						long sp2 = System.currentTimeMillis();
//						if(getQueryPredicate().evaluate(element, next_predicted)){
//							result.add(new Pair<T>(element, next_predicted));
//						}
//						long ep2 = System.currentTimeMillis();
//						durationPredictionPredicate += (ep2 - sp2);
//						
//						long ep = System.currentTimeMillis();
//						durationPrediction += (ep - sp);
//					}
//					else if(element.getMetadata().getStart().equals(next.getMetadata().getStart())){
//						compareCounter++;
//						long startNormal = System.currentTimeMillis();
//						if(getQueryPredicate().evaluate(element, next)){
//							result.add(new Pair<T>(element, next));
//						}
//						long endNormal = System.currentTimeMillis();
//						durationNormal += (endNormal - startNormal);
//					}
//					else if(element.getMetadata().getStart().before(next.getMetadata().getStart())){
//						compareCounter++;
//						
//						long sp = System.currentTimeMillis();
//						
//						T element_predicted = (T)element.getMetadata().predictData(this.leftSchema, element, next.getMetadata().getStart());
//						M predictedMetaLeft = (M)element.getMetadata().predictMetadata(this.leftSchema, element, next.getMetadata().getStart());
//						element_predicted.setMetadata(predictedMetaLeft);
//						
//						long sp2 = System.currentTimeMillis();
//						if (getQueryPredicate().evaluate(element_predicted, next)) {
//							result.add(new Pair<T>(element_predicted, next));
//						}
//						long ep2 = System.currentTimeMillis();
//						durationPredictionPredicate += (ep2 - sp2);
//						
//						long ep = System.currentTimeMillis();
//						durationPrediction += (ep - sp);
//					}
//				}
//				break;
//			case RightLeft:
//				for (T next : this.elements) {
//					if (TimeInterval.totallyBefore(next.getMetadata(), element
//							.getMetadata())) {
//						continue;
//					}
//					if (TimeInterval.totallyAfter(next.getMetadata(), element
//							.getMetadata())) {
//						break;
//					}
//					
//					
//					if(element.getMetadata().getStart().after(next.getMetadata().getStart())){
//						compareCounter++;
//						
//						long sp = System.currentTimeMillis();
//						
//						T next_predicted = (T)next.getMetadata().predictData(this.leftSchema, next, element.getMetadata().getStart());
//						M predictedMetaLeft = (M)next.getMetadata().predictMetadata(this.leftSchema, next, element.getMetadata().getStart());
//						next_predicted.setMetadata(predictedMetaLeft);
//						
//						long sp2 = System.currentTimeMillis();
//						
//						if(getQueryPredicate().evaluate(next_predicted, element)){
//							result.add(new Pair<T>(next_predicted, element));
//						}
//						
//						long ep2 = System.currentTimeMillis();
//						durationPredictionPredicate += (ep2 - sp2);
//						
//						long ep = System.currentTimeMillis();
//						durationPrediction += (ep - sp);
//					}
//					else if(element.getMetadata().getStart().equals(next.getMetadata().getStart())){
//						compareCounter++;
//						
//						long startNormal = System.currentTimeMillis();
//						if(getQueryPredicate().evaluate(element, next)){
//							result.add(new Pair<T>(next, element));
//						}
//						long endNormal = System.currentTimeMillis();
//						durationNormal += (endNormal - startNormal);
//					}
//					else if(element.getMetadata().getStart().before(next.getMetadata().getStart())){
//						compareCounter++;
//						long sp = System.currentTimeMillis();
//						
//						T element_predicted = (T)element.getMetadata().predictData(this.rightSchema, element, next.getMetadata().getStart());
//						M predictedMetaRight = (M)element.getMetadata().predictMetadata(this.rightSchema, element, next.getMetadata().getStart());
//						element_predicted.setMetadata(predictedMetaRight);
//						
//						long sp2 = System.currentTimeMillis();
//						
//						if (getQueryPredicate().evaluate(next, element_predicted)) {
//							result.add(new Pair<T>(next, element_predicted));
//						}
//						
//						long ep2 = System.currentTimeMillis();
//						durationPredictionPredicate += (ep2 - sp2);
//						
//						long ep = System.currentTimeMillis();
//						durationPrediction += (ep -sp);
//					}
//				}
//				break;
//			}
//		}
//		
//		return result.iterator();
//	}
//	
//	public Iterator<Pair<T>> queryCopyPrediction(T element, Order order) {
//		counter++;
//		long startDuration = System.currentTimeMillis();
//		LinkedList<Pair<T>> result = new LinkedList<Pair<T>>();
//		synchronized(this.elements){
//			switch (order) {
//			case LeftRight:
//				for (T next : this.elements) {
//					if (TimeInterval.totallyBefore(next.getMetadata(), element
//							.getMetadata())) {
//						continue;
//					}
//					if (TimeInterval.totallyAfter(next.getMetadata(), element
//							.getMetadata())) {
//						break;
//					}
//					
//					if(element.getMetadata().getStart().after(next.getMetadata().getStart())){
//						compareCounter++;
//						long sp = System.currentTimeMillis();
//						
//						T next_predicted = (T)next.getMetadata().predictData(this.rightSchema, next, element.getMetadata().getStart());
//						M predictedMetaRight = (M)next.getMetadata().predictMetadata(this.rightSchema, next, element.getMetadata().getStart());
//						next_predicted.setMetadata(predictedMetaRight);
//						
//						long sp2 = System.currentTimeMillis();
//						if(getQueryPredicate().evaluate(element, next_predicted)){
//							result.add(new Pair<T>(element, next_predicted));
//						}
//						long ep2 = System.currentTimeMillis();
//						durationPredictionPredicate += (ep2 - sp2);
//						
//						long ep = System.currentTimeMillis();
//						durationPrediction += (ep - sp);
//					}
//					else if(element.getMetadata().getStart().equals(next.getMetadata().getStart())){
//						compareCounter++;
//						long startNormal = System.currentTimeMillis();
//						if(getQueryPredicate().evaluate(element, next)){
//							result.add(new Pair<T>(element, next));
//						}
//						long endNormal = System.currentTimeMillis();
//						durationNormal += (endNormal - startNormal);
//					}
//					else if(element.getMetadata().getStart().before(next.getMetadata().getStart())){
//						compareCounter++;
//						
//						long sp = System.currentTimeMillis();
//						
//						T element_predicted = (T)element.getMetadata().predictData(this.leftSchema, element, next.getMetadata().getStart());
//						M predictedMetaLeft = (M)element.getMetadata().predictMetadata(this.leftSchema, element, next.getMetadata().getStart());
//						element_predicted.setMetadata(predictedMetaLeft);
//						
//						long sp2 = System.currentTimeMillis();
//						if (getQueryPredicate().evaluate(element_predicted, next)) {
//							result.add(new Pair<T>(element_predicted, next));
//						}
//						long ep2 = System.currentTimeMillis();
//						durationPredictionPredicate += (ep2 - sp2);
//						
//						long ep = System.currentTimeMillis();
//						durationPrediction += (ep - sp);
//					}
//				}
//				break;
//			case RightLeft:
//				for (T next : this.elements) {
//					if (TimeInterval.totallyBefore(next.getMetadata(), element
//							.getMetadata())) {
//						continue;
//					}
//					if (TimeInterval.totallyAfter(next.getMetadata(), element
//							.getMetadata())) {
//						break;
//					}
//					
//					
//					if(element.getMetadata().getStart().after(next.getMetadata().getStart())){
//						compareCounter++;
//						
//						long sp = System.currentTimeMillis();
//						
//						T next_predicted = (T)next.getMetadata().predictData(this.leftSchema, next, element.getMetadata().getStart());
//						M predictedMetaLeft = (M)next.getMetadata().predictMetadata(this.leftSchema, next, element.getMetadata().getStart());
//						next_predicted.setMetadata(predictedMetaLeft);
//						
//						long sp2 = System.currentTimeMillis();
//						
//						if(getQueryPredicate().evaluate(next_predicted, element)){
//							result.add(new Pair<T>(next_predicted, element));
//						}
//						
//						long ep2 = System.currentTimeMillis();
//						durationPredictionPredicate += (ep2 - sp2);
//						
//						long ep = System.currentTimeMillis();
//						durationPrediction += (ep - sp);
//					}
//					else if(element.getMetadata().getStart().equals(next.getMetadata().getStart())){
//						compareCounter++;
//						
//						long startNormal = System.currentTimeMillis();
//						if(getQueryPredicate().evaluate(element, next)){
//							result.add(new Pair<T>(next, element));
//						}
//						long endNormal = System.currentTimeMillis();
//						durationNormal += (endNormal - startNormal);
//					}
//					else if(element.getMetadata().getStart().before(next.getMetadata().getStart())){
//						compareCounter++;
//						long sp = System.currentTimeMillis();
//						
//						T element_predicted = (T)element.getMetadata().predictData(this.rightSchema, element, next.getMetadata().getStart());
//						M predictedMetaRight = (M)element.getMetadata().predictMetadata(this.rightSchema, element, next.getMetadata().getStart());
//						element_predicted.setMetadata(predictedMetaRight);
//						
//						long sp2 = System.currentTimeMillis();
//						
//						if (getQueryPredicate().evaluate(next, element_predicted)) {
//							result.add(new Pair<T>(next, element_predicted));
//						}
//						
//						long ep2 = System.currentTimeMillis();
//						durationPredictionPredicate += (ep2 - sp2);
//						
//						long ep = System.currentTimeMillis();
//						durationPrediction += (ep -sp);
//					}
//				}
//				break;
//			}
//		}
//		
//		long endDuration = System.currentTimeMillis();
//		duration += (endDuration - startDuration);
//		
//		return result.iterator();
//	}
}

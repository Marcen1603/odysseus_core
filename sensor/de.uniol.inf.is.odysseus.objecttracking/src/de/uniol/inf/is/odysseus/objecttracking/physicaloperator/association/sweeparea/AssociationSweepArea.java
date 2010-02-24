package de.uniol.inf.is.odysseus.objecttracking.physicaloperator.association.sweeparea;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTISweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IApplicationTime;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.IRangePredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;



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
@SuppressWarnings("unchecked")
public class AssociationSweepArea<M extends IPredictionFunctionKey & ITimeInterval & IProbability & IApplicationTime, T extends MVRelationalTuple<M>> extends JoinTISweepArea<T>{
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

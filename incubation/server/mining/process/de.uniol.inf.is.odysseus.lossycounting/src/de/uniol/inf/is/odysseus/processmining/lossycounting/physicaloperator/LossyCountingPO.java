package de.uniol.inf.is.odysseus.processmining.lossycounting.physicaloperator;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class LossyCountingPO <T extends IMetaAttribute> extends AbstractPipe<Tuple<T>, Tuple<T>> {

		private final int TUPLE_FREQ_POS = 0;
		private final int TUPLE_BUCKET_POS = 1;
		private final int TUPLE_NAME_POS = 2;
		private int error = 5; // corresponds to w
		private HashMap<Object,Tuple<T>> activities = Maps.newHashMap();
		private HashMap<Object,Tuple<T>> cases = Maps.newHashMap();
		private HashMap<Object,Tuple<T>> relations = Maps.newHashMap();
		private int iterations = 0; // corresponds to N
		private int currentBucket = 1;
		private Integer caseId;
		private String activityName;
		
		
		public LossyCountingPO() {
			super();
		}
		
		public LossyCountingPO(LossyCountingPO<T> lossyCountinPO) {
			super(lossyCountinPO);
			this.error = lossyCountinPO.error;
			this.activities = lossyCountinPO.activities;
			this.iterations = lossyCountinPO.iterations;
			System.out.print("ERROR PO: "+this.error +"\n");
			System.err
			.println("The use of a copy constructor is only parially "
					+ "supported in DBEnrichPO. The cacheManager will not be copied.");
		}
		
		@Override
		public OutputMode getOutputMode() {
			return OutputMode.INPUT;
		}
	
		@Override
		public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
			if (this == ipo)
				return true;
			if (!super.equals(ipo))
				return false;
			if (getClass() != ipo.getClass())
				return false;
			return true;
		}

		@Override
		protected void process_next(Tuple<T> object, int port) {
			if(object instanceof Tuple){
				System.out.println("--------- CurrentBucket: "+currentBucket+" ---------");
				Object[] attributes = object.getAttributes();
				caseId = (Integer) attributes[0];
				activityName = (String)attributes[1];
				
				updateData(activityName,activities); // Updates the activity map
				printInfo(activityName);
				updateCases(caseId, activityName);

				dataCleansing();
				iterations++;
				
				if(iterations%20 == 0){
					Tuple mapTransfer = new Tuple<>(1,true);
					mapTransfer.setAttribute(0, this.relations);
					transfer(mapTransfer);
				}
			}
		}
		
		
		private void updateData(Object attribute,HashMap<Object,Tuple<T>> map ){
			if(map.containsKey(attribute)){
				incrementFrequenz(attribute,map);
			} else {
				addTupleToMap(attribute,map);
			}
		}
		
		private void updateCases(Integer caseId, String newActivityName){
			if(cases.containsKey(caseId)){
				String relationKeyName = (String)cases.get(caseId).getAttribute(TUPLE_NAME_POS)+ "->" +newActivityName;
				incrementAndReplaceCaseData(caseId,newActivityName,cases);
				//updates the frequenz counter of the relations Tuples
				updateData(relationKeyName, relations);
				
				
				
				//Test Output
				System.out.println("++++++++++++++ Relationen ++++++++++++++");
				for(Map.Entry<Object,Tuple<T>> entry : relations.entrySet()){
					System.out.println(entry.getKey() + "; Anzahl: " + entry.getValue().getAttribute(0) +" Bucket: "+ entry.getValue().getAttribute(1));
				}
				System.out.println("++++++++++++++ Relationen ++++++++++++++");
			} else {
				addCaseToMap(caseId,newActivityName,cases);
			}
		}
		
		/**
		 * 
		 * @param key to get the needed Tuple from map
		 * @param map  which contains the needed Tuple
		 */
		private void incrementFrequenz(Object key, HashMap<Object, Tuple<T>> map){
			int freq = ((Integer)(map.get(key).getAttribute(0))).intValue();
			Tuple<T> tuple = new Tuple<T>(2,true);
			map.remove(key);
			tuple.setAttribute(TUPLE_FREQ_POS,Integer.valueOf(freq+1));
			tuple.setAttribute(TUPLE_BUCKET_POS,Integer.valueOf(currentBucket-1));
			map.put(key, tuple);
		}
		
		private void addTupleToMap(Object key,HashMap<Object,Tuple<T>> map){
			Tuple<T> tuple = new Tuple<T>(2,true);
			tuple.setAttribute(TUPLE_FREQ_POS,Integer.valueOf(1));
			tuple.setAttribute(TUPLE_BUCKET_POS,Integer.valueOf(currentBucket-1));
			map.put(key, tuple);
		}
		
		private void incrementAndReplaceCaseData(Object key, String activityName, HashMap<Object, Tuple<T>> map){
			int freq = ((Integer)(map.get(key).getAttribute(1))).intValue();
			map.remove(key);
			Tuple<T> tuple = new Tuple<T>(3,true);
			tuple.setAttribute(TUPLE_FREQ_POS,Integer.valueOf(freq+1));
			tuple.setAttribute(TUPLE_BUCKET_POS,Integer.valueOf(currentBucket-1));
			tuple.setAttribute(TUPLE_NAME_POS,activityName);
			map.put(key, tuple);
		}
		
		private void addCaseToMap(Object key, String activityName,HashMap<Object,Tuple<T>> map){
			Tuple<T> tuple = new Tuple<T>(3,true);
			
			tuple.setAttribute(TUPLE_FREQ_POS,Integer.valueOf(1));
			tuple.setAttribute(TUPLE_BUCKET_POS,Integer.valueOf(currentBucket-1));
			tuple.setAttribute(TUPLE_NAME_POS, activityName);
			map.put(key, tuple);
			
		}
		
		private void dataCleansing(){
			if((iterations%error) == 0){
				System.out.println("++++++++++++++ CLEAN START++++++++++++++");
				activities = cleanMap(activities);
				cases = cleanMap(cases);
				relations = cleanMap(relations);
				currentBucket++;
				System.out.println("++++++++++++++ CLEAN DONE ++++++++++++++");
			}
		}
		
		private HashMap<Object,Tuple<T>> cleanMap(HashMap<Object,Tuple<T>> map){
			HashMap<Object,Tuple<T>> newMap = Maps.newHashMap();
			for(Map.Entry<Object,Tuple<T>> entry : map.entrySet()){
				int actualFrequenz = ((Integer)entry.getValue().getAttribute(TUPLE_FREQ_POS)).intValue();
				if(!(actualFrequenz+currentBucket <= currentBucket)){
					newMap.put(entry.getKey(), entry.getValue());
				} else {
					System.out.println("DELETED: " + entry.getKey() +";Anzahl: "+entry.getValue().getAttribute(TUPLE_FREQ_POS)+"<="+currentBucket );
				}
			}
			return newMap;
		}
		
		private void printInfo(String name){
			System.out.println("++++++++++++++ Activities ++++++++++++++");
			System.out.println("Actual Activity: "+ name);
			System.out.println("Map after Update: ");
			for(Map.Entry<Object,Tuple<T>> entry : activities.entrySet()){
				System.out.println(entry.getKey() + "; Anzahl: " + entry.getValue().getAttribute(0) +" Bucket: "+ entry.getValue().getAttribute(1));
			}
			System.out.println("++++++++++++++ Activities ++++++++++++++");
		}
}
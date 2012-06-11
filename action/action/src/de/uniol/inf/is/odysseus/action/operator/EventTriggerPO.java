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
package de.uniol.inf.is.odysseus.action.operator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.action.benchmark.BenchmarkData;
import de.uniol.inf.is.odysseus.action.benchmark.IActuatorBenchmark;
import de.uniol.inf.is.odysseus.action.output.Action;
import de.uniol.inf.is.odysseus.action.output.IActionParameter;
import de.uniol.inf.is.odysseus.action.output.IActionParameter.ParameterType;
import de.uniol.inf.is.odysseus.action.services.dataExtraction.IDataExtractor;
import de.uniol.inf.is.odysseus.action.services.exception.ActuatorException;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;

/**
 * Physical Operator calling a list of Actions when an Object is processed
 * @author Simon Flandergan
 *
 * @param <T>
 */
public class EventTriggerPO<T> extends AbstractSink<T>{
	private Map<Action, List<IActionParameter>> actions;
	private String type;
	private Logger logger;
	private SDFSchema schema;
	private static IDataExtractor dataExtractor;
	private static IActuatorBenchmark benchmark;

	/**
	 * Public Constructor used by OSGI
	 */
	public EventTriggerPO(){
		
	}
	
	/**
	 * Public Constructor for Transformation rule
	 * @param actions
	 * @param type
	 */
	public EventTriggerPO(Map<Action, List<IActionParameter>> actions, String type, SDFSchema schema) {
		super();
		this.actions = actions;
		this.type = type;
		this.logger = LoggerFactory.getLogger(EventTriggerPO.class);
		this.schema = schema;
	}
	
	/**
	 * Copy constructor
	 * @param po
	 */
	public EventTriggerPO(EventTriggerPO<T> po){
		super();
		this.actions = new HashMap<Action, List<IActionParameter>>(po.actions);
		this.type = po.type;
		this.logger = po.logger;
		this.schema = po.schema;
	}
	
	public void bindBenchmark(IActuatorBenchmark bm){
		EventTriggerPO.benchmark = bm;
	}
	
	/**
	 * Method called by OSGi to fulfill binding
	 * @param extractor
	 */
	public void bindDataExtractor(IDataExtractor extractor){
		dataExtractor = extractor;
	}


	@Override
	public EventTriggerPO<T> clone() {
		return new EventTriggerPO<T>(this);
	}
	
	public Map<Action, List<IActionParameter>> getActions() {
		return actions;
	}
	
	@SuppressWarnings({"rawtypes"})
	@Override
	protected void process_next(T object, int port) {
		//extract parameters
		BenchmarkData bmData = null;
		if (benchmark != null){
			try {
				//start timestamp = tupleTimestamp
				bmData = new BenchmarkData( 
						((Number)(dataExtractor.extractAttribute(object, "timestamp", type, this.schema))).longValue());

				//dataextraction until metadata was created
				ITimeInterval metaData = (ITimeInterval)((IMetaAttributeContainer)object).getMetadata();
				bmData.addOutputTime(IActuatorBenchmark.Operation.DATAEXTRACTION.name(), 
					metaData.getStart().getMainPoint());
				
				//current time is time for queryprocessing
				bmData.addOutputTime(IActuatorBenchmark.Operation.QUERYPROCESSING.name());
			}catch(Exception e){
				//ignore benchmarking
				bmData = null;
			}
		}
		for (Entry<Action, List<IActionParameter>> entry : actions.entrySet()){
			List<IActionParameter> parameterList = entry.getValue();
			Object[] parameters = new Object[parameterList.size()];
			int index = 0;
			for (IActionParameter param : parameterList){
				if (param.getType().equals(ParameterType.Value)){
					//static value
					parameters[index] = param.getValue();
				}else {
					//value must be extracted from attribute
					Object identifier = param.getValue();
					try {
						parameters[index] = dataExtractor.extractAttribute(object, identifier, this.type, this.schema);
					} catch (Exception e) {
						this.logger.error("Attribute: "+identifier+" wasn't extracted. \n" +
								"Reason: "+ e.getMessage());
						return;
					} 
				}
				index++;
			}
			try {
				Action action = entry.getKey();
				action.executeMethod(parameters);
				
				//add benchmarking information
				if (bmData != null){
					bmData.addOutputTime(IActuatorBenchmark.Operation.ACTIONEXECTION.name()+
							action.getActuator().getClass().getName()+"."+
							action.getMethod().getName());
				}
			} catch (ActuatorException e) {
				//Shouldnt happen, because method & parameters are checked during creation of action
				this.logger.error("Method execution failed: "+e.getMessage());
			}
		}
		if (bmData != null){
			benchmark.addBenchmarkData(bmData);
		}
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// Do nothing. 
	}
}

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
package de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.physicaloperator.event.POPortEvent;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.ElementType;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Andre Bolles, Jonas Jacobi
 */
public abstract class AbstractPNPipe<R extends IMetaAttributeContainer<? extends IPosNeg>, W extends IMetaAttributeContainer<? extends IPosNeg>> extends AbstractPipe<R, W>{
		
	protected POEvent[] processInitNegEvent = null;
	protected POEvent[] processDoneNegEvent = null;
	protected POEvent pushInitNegEvent = new POEvent(this,
			POEventType.PushInitNeg);
	protected POEvent pushDoneNegEvent = new POEvent(this,
			POEventType.PushDoneNeg);
	
	public AbstractPNPipe(){};
	
	public AbstractPNPipe(AbstractPNPipe<R,W> pipe){
		super(pipe);
		initNegPOEvents(pipe.getInputPortCount());
	}
	
	
	@Override
	public void process(R object, int port, boolean exclusive) {
		// if (!isOpen()) System.err.println(this+" PROCESS BEFORE OPEN!!!");
		// evtl. spaeter wieder einbauen? Exception?
		if(object.getMetadata().getElementType() == ElementType.POSITIVE ){
			super.process(object, port, exclusive);
		}
		else{
			fire(processInitNegEvent[port]);
			process_next(object, port);
			fire(processDoneNegEvent[port]);
		}
	}
	
	
	@Override
	public void subscribeToSource(ISource<? extends R> source, int sinkInPort, int sourceOutPort, SDFAttributeList schema) {
		super.subscribeToSource(source, sinkInPort, sourceOutPort, schema);
		int portCount = delegateSink.getInputPortCount();
		initNegPOEvents(portCount);
	}

	private void initNegPOEvents(int portCount) {
		processInitNegEvent = new POEvent[portCount];
		processDoneNegEvent = new POEvent[portCount];
		for (int i = 0; i < portCount; ++i) {
			processInitNegEvent[i] = new POPortEvent(this,
					POEventType.ProcessInitNeg, i);
			processDoneNegEvent[i] = new POPortEvent(this,
					POEventType.ProcessDoneNeg, i);
		}
	}
	
	@Override
	final public void transfer(W object) {
		if(object.getMetadata().getElementType() == ElementType.POSITIVE ){
			super.transfer(object);
		}
		else{
			fire(this.pushInitNegEvent);
			//process_transfer(object);
			transfer(object);
			fire(this.pushDoneNegEvent);
		}
	}
}

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
/**
 * @author Marco Grawunder
 */
package de.uniol.inf.is.odysseus.core.physicaloperator;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.Subscription;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class PhysicalSubscription<K> extends Subscription<K> {

	private static final long serialVersionUID = -6266008340674321020L;
	private boolean done;
	private int suspendCalls = 0;
	private int openCalls = 0;
	@SuppressWarnings("rawtypes")
	final private List<IStreamObject> suspendBuffer = new LinkedList<>();
	private int sheddingFactor = 0;
	private int readObjects;
	
	public PhysicalSubscription(K target, int sinkInPort, int sourceOutPort,
			SDFSchema schema) {
		super(target, sinkInPort, sourceOutPort, schema);
		done = false;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public boolean isDone() {
		return done;
	}

	public boolean isSuspended() {
		return openCalls == suspendCalls;
	}
	
	public int getBufferSize(){
		return suspendBuffer.size();
	}

	public synchronized void incOpenCalls() {
		openCalls++;
	}

	public synchronized void decOpenCalls() {
		openCalls--;
	}

	public synchronized int getOpenCalls() {
		return openCalls;
	}

	@Override
	public String toString() {
		return super.toString() + " openCalls=" + openCalls + " suspendCalls "
				+ suspendCalls + " " + suspendBuffer;
	}

	public void setOpenCalls(int openCalls) {
		this.openCalls = openCalls;
	}
	
	public void setSheddingFactor(int sheddingFactor) {
		this.sheddingFactor = sheddingFactor;
	}

	public int getSheddingFactor() {
		return sheddingFactor;
	}
	
	public boolean isShedding(){
		return sheddingFactor > 0;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void process(IStreamObject o) {
		if (sheddingFactor > 0){
			readObjects++;
			if (readObjects == sheddingFactor){
				readObjects = 0;
				return;
			}
		}
		if (openCalls > 0 && openCalls == suspendCalls) {
			suspendBuffer.add(o);
		} else {
			clearSuspendBuffer();
			((ISink) getTarget()).process(o, getSinkInPort());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void clearSuspendBuffer() {
		if (suspendBuffer.isEmpty())
			return;

		for (IStreamObject o : suspendBuffer) {
			((ISink) getTarget()).process(o, getSinkInPort());
		}
		suspendBuffer.clear();
	}

	public void suspend() {
		suspendCalls++;
	}

	public void resume() {
		suspendCalls--;
	}

}
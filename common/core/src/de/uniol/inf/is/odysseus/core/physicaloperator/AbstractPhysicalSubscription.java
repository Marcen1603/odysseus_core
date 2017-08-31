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

import de.uniol.inf.is.odysseus.core.Subscription;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

abstract public class AbstractPhysicalSubscription<I extends ISource<IStreamObject<?>>,O extends ISink<IStreamObject<?>>>
		extends Subscription<I, O> {

	private static final long serialVersionUID = -6266008340674321020L;
	private boolean done;
	private boolean needsClone;

	private int openCalls = 0;

	public AbstractPhysicalSubscription(I source, O sink, int sinkInPort,
			int sourceOutPort, SDFSchema schema) {
		super(source, sink, sinkInPort, sourceOutPort, schema);
		done = false;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public boolean isDone() {
		return done;
	}

	public void setNeedsClone(boolean needsClone) {
		this.needsClone = needsClone;
	}

	public boolean isNeedsClone() {
		return needsClone;
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
		return super.toString() + " openCalls=" + openCalls;
	}

	public void setOpenCalls(int openCalls) {
		this.openCalls = openCalls;
	}

	@SuppressWarnings({ "rawtypes" })
	final public void process(IStreamObject o) {
		IStreamObject toProcess = needsClone ? (IStreamObject) o.clone() : o;
		do_process(toProcess);
	}

	@SuppressWarnings("rawtypes")
	protected void do_process(IStreamObject o) {
		sendObject(o);
	}

	@SuppressWarnings({ "rawtypes" })
	final protected void sendObject(IStreamObject o) {
		getSink().process(o, getSinkInPort());
	}

}
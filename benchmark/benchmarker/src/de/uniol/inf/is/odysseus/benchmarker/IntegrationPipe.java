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
package de.uniol.inf.is.odysseus.benchmarker;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class IntegrationPipe extends AbstractPipe {

	private int curPort = 0;

	@Override
	public IntegrationPipe clone() {
		return new IntegrationPipe();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(Object object, int port) {
		transfer(object);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}

	@Override
	public void subscribeToSource(ISource source, int sinkInPort,
			int sourceOutPort, SDFSchema schema) {
		PhysicalSubscription<ISource> sub = new PhysicalSubscription<ISource>(
				source, curPort - 1, sourceOutPort, schema);
		if (!getSubscribedToSource().contains(sub)) {
			super.subscribeToSource(source, curPort++, sourceOutPort, schema);
		}
	}
	
	@Override
	protected void process_done(int port) {
		super.process_done(port);
	}
	
}

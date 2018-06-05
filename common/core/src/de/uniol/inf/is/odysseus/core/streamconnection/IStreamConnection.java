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
package de.uniol.inf.is.odysseus.core.streamconnection;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public interface IStreamConnection<In> {

	public void connect();
	public void disconnect();
	public boolean isConnected();
	
	public void disable();
	public void enable();
	public boolean isEnabled();
	
	public void addStreamElementListener( IStreamElementListener<In> listener );
	public void addStreamElementListener( IStreamElementListener<In> listener, String sinkName);
	public void removeStreamElementListener( IStreamElementListener<In> listener );
	public void removeStreamElementListener( IStreamElementListener<In> listener, String sinkName);
	
	public SDFSchema getOutputSchema();
	public ImmutableList<ISubscription<ISource<IStreamObject<?>>,?>> getSubscriptions();
	
	public ImmutableList<IPhysicalOperator> getConnectedOperators();
	public int getPortOfOperator( IPhysicalOperator operator );
	public IPhysicalOperator getOperatorOfPort( int port );
}

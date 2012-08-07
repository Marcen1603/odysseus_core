/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.connection;

import java.io.IOException;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * 
 * @deprecated Use {@link ITransportHandler}
 *
 * @param <T>
 */
@Deprecated
public interface IAccessConnectionHandler<T> extends IClone {

	@Override
	IAccessConnectionHandler<T> clone();
	
	public void open(IAccessConnectionListener<T> caller) throws OpenFailedException;
	public void close(IAccessConnectionListener<T> caller) throws IOException;
	public void reconnect();
	
	public String getUser();
	public String getPassword();
	
	public String getName();
	public IAccessConnectionHandler<T> getInstance(Map<String,String> options);
	
}

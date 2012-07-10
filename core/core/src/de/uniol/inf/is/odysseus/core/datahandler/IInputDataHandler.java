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
package de.uniol.inf.is.odysseus.core.datahandler;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionHandler;
import de.uniol.inf.is.odysseus.core.objecthandler.IObjectHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;


public interface IInputDataHandler<R,W> {

	void init();
	void done();
	void process(R input, IObjectHandler<W> objectHandler, IAccessConnectionHandler<R> accessHandler, ITransferHandler<W> transferHandler) throws ClassNotFoundException;
	IInputDataHandler<R,W> clone();
	
	String getName();
	IInputDataHandler<R, W> getInstance(Map<String, String> option);
}

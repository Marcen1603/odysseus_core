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
/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;

/**
 * 
 * @author Dennis Geesen
 * Created at: 15.08.2011
 */
public class StoredProcedureListResponse extends Response {

	private List<StoredProcedure> responseValue = new ArrayList<StoredProcedure>();

	public StoredProcedureListResponse() {
		super();
	}

	public StoredProcedureListResponse(boolean success) {
		super(success);		
	}
	
	public StoredProcedureListResponse(List<StoredProcedure> response, boolean success){
		super(success);
		this.responseValue = response;
	}
	
	public StoredProcedureListResponse(Collection<StoredProcedure> response, boolean success){
		super(success);
		this.setResponseValue(response.toArray(new StoredProcedure[0]));
	}

	public StoredProcedure[] getResponseValue() {
		return responseValue.toArray(new StoredProcedure[0]);
	}

	public void setResponseValue(StoredProcedure[] responseValue) {
		this.responseValue.clear();
		for(StoredProcedure s : responseValue){
			this.responseValue.add(s);
		}
	}
	
	public void addResponseValue(StoredProcedure s){
		this.responseValue.add(s);
	}
	
	public void removeResponseValue(StoredProcedure s){
		this.responseValue.remove(s);
	}
}

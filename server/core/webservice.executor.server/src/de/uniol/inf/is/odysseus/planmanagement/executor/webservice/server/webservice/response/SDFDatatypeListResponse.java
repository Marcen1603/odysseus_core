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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * 
 * @author Dennis Geesen
 * Created at: 15.08.2011
 */
public class SDFDatatypeListResponse extends Response {

	private List<SDFDatatype> responseValue = new ArrayList<SDFDatatype>();

	public SDFDatatypeListResponse() {
		super();
	}

	public SDFDatatypeListResponse(boolean success) {
		super(success);		
	}
	
	public SDFDatatypeListResponse(List<SDFDatatype> response, boolean success){
		super(success);
		this.responseValue = response;
	}
	
	public SDFDatatypeListResponse(Collection<SDFDatatype> response, boolean success){
		super(success);
		this.setResponseValue(response.toArray(new SDFDatatype[0]));
	}

	public SDFDatatype[] getResponseValue() {
		return responseValue.toArray(new SDFDatatype[0]);
	}

	public void setResponseValue(SDFDatatype[] responseValue) {
		this.responseValue.clear();
		for(SDFDatatype s : responseValue){
			this.responseValue.add(s);
		}
	}
	
	public void addResponseValue(SDFDatatype s){
		this.responseValue.add(s);
	}
	
	public void removeResponseValue(SDFDatatype s){
		this.responseValue.remove(s);
	}
}

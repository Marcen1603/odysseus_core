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

package de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 
 * @author Dennis Geesen
 * Created at: 15.08.2011
 */
public class StringListResponse extends Response {

	private List<String> responseValue = new ArrayList<String>();

	public StringListResponse() {
		super();
	}

	public StringListResponse(boolean success) {
		super(success);		
	}
	
	public StringListResponse(List<String> response, boolean success){
		super(success);
		this.responseValue = response;
	}
	
	public StringListResponse(Collection<String> response, boolean success){
		super(success);
		this.setResponseValue(response.toArray(new String[0]));
	}

	public String[] getResponseValue() {
		return responseValue.toArray(new String[0]);
	}

	public void setResponseValue(String[] responseValue) {
		this.responseValue.clear();
		for(String s : responseValue){
			this.responseValue.add(s);
		}
	}
	
	public void addResponseValue(String s){
		this.responseValue.add(s);
	}
	
	public void removeResponseValue(String s){
		this.responseValue.remove(s);
	}
}

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
/**
 * 
 * @author Dennis Geesen
 * Created at: 15.08.2011
 */
public class BooleanResponse extends Response {

	private boolean responseValue;

	public BooleanResponse() {
		super();
	}

	public BooleanResponse(boolean responseValue, boolean success) {
		super(success);
		this.responseValue = responseValue;
	}

	public boolean getResponseValue() {
		return responseValue;
	}

	public void setResponseValue(boolean responseValue) {
		this.responseValue = responseValue;
	}
}

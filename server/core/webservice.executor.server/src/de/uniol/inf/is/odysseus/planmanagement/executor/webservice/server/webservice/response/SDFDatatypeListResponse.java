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
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.SDFDatatypeInformation;

/**
 *
 * @author Dennis Geesen Created at: 15.08.2011
 */
public class SDFDatatypeListResponse extends Response {

	private List<SDFDatatypeInformation> responseValue = new ArrayList<>();

	public SDFDatatypeListResponse() {
		super();
	}

	public SDFDatatypeListResponse(boolean success) {
		super(success);
	}

	public SDFDatatypeListResponse(List<SDFDatatypeInformation> responseValue, boolean success) {
		super(success);
		this.responseValue = responseValue;
	}

	public void addResonseValue(SDFDatatypeInformation dti) {
		responseValue.add(dti);
	}

	public List<SDFDatatypeInformation> getResponseValue() {
		return this.responseValue;
	}

	public void setResponseValue(List<SDFDatatypeInformation> value) {
		this.responseValue = value;
	}
}

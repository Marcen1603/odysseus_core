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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.List;
import java.util.Objects;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AccessAOSourceParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;

abstract public class AbstractAccessAO extends AbstractSchemaBasedAO implements IAccessAO {

	private static final long serialVersionUID = -5423444612698319659L;

	private Resource accessAOResource;

	private long maxTimeToWaitForNewEventMS;
	
	// TODO: magic default values
	private long realTimeDelay = 1000;
	private long applicationTimeDelay = 900;

	public AbstractAccessAO() {
		super();
	}

	public AbstractAccessAO(AbstractAccessAO ao) {
		super(ao);
		accessAOResource = ao.accessAOResource;
		this.maxTimeToWaitForNewEventMS = ao.maxTimeToWaitForNewEventMS;
		this.realTimeDelay = ao.realTimeDelay;
		this.applicationTimeDelay = ao.applicationTimeDelay;
	}

	@Parameter(type = AccessAOSourceParameter.class, name = "source", optional = false, doc = "The name of the sourcetype to create.")
	public void setAccessAOName(Resource name) {
		super.setName(name.getResourceName());
		this.accessAOResource = name;
	}

	@Override
	public Resource getAccessAOName() {
		return accessAOResource;
	}



	@Parameter(type = LongParameter.class, name = "MaxTimeToWaitForNewEventMS", optional = true, isList = false, doc = "For access. Max time to wait for a new element before calling done. Typically used when the input stream has an end")
	public void setMaxTimeToWaitForNewEventMS(long maxTimeToWaitForNewEventMS) {
		this.maxTimeToWaitForNewEventMS = maxTimeToWaitForNewEventMS;
	}

	public long getMaxTimeToWaitForNewEventMS() {
		return maxTimeToWaitForNewEventMS;
	}




	public long getRealTimeDelay() {
		return realTimeDelay;
	}
	
	@Parameter(type = LongParameter.class, optional = true, isList = false, doc = "For out of order. How long should be waited for new elements.")
	public void setRealTimeDelay(long realTimeDelay) {
		this.realTimeDelay = realTimeDelay;
	}

	public long getApplicationTimeDelay() {
		return applicationTimeDelay;
	}

	@Parameter(type = LongParameter.class, optional = true, isList = false, doc = "For out of order. After waiting some realTimeDelay, what time should be added to application time.")
	public void setApplicationTimeDelay(long applicationTimeDelay) {
		this.applicationTimeDelay = applicationTimeDelay;
	}
	
	@Override
	public boolean isSemanticallyEqual(IAccessAO operator) {
		if (!(operator instanceof AbstractAccessAO)) {
			return false;
		}
		AbstractAccessAO other = (AbstractAccessAO) operator;

		if (!Objects.equals(this.accessAOResource, other.accessAOResource)) {
			return false;
		}

		if (!Objects.equals(this.getWrapper(), other.getWrapper())) {
			return false;
		}
		if (!Objects.equals(this.getDataHandler(), other.getDataHandler())) {
			return false;
		}
		if (!Objects.equals(this.getProtocolHandler(), other.getProtocolHandler())) {
			return false;
		}
		if (!Objects.equals(this.getTransportHandler(), other.getTransportHandler())) {
			return false;
		}
		if (!Objects.equals(this.getOptionsMap(), other.getOptionsMap())) {
			return false;
		}

		if (!Objects.equals(this.getOptions(), other.getOptions())) {
			return false;
		}

		if (!Objects.equals(this.getDateFormat(), other.getDateFormat())) {
			return false;
		}

		if (!Objects.equals(this.getOutputSchema(), other.getOutputSchema())) {
			return false;
		}
		if (!Objects.equals(this.getInputSchema(), other.getInputSchema())) {
			return false;
		}

		if (!Objects.equals(this.maxTimeToWaitForNewEventMS, other.maxTimeToWaitForNewEventMS)) {
			return false;
		}

		if (!Objects.equals(this.getLocalMetaAttribute(), other.getLocalMetaAttribute())) {
			return false;
		}
		if (!Objects.equals(this.readMetaData(), other.readMetaData())) {
			return false;
		}

		if (!Objects.equals(this.isOverWriteSchemaSourceName(), other.isOverWriteSchemaSourceName())) {
			return false;
		}
		if (!Objects.equals(this.getOverWrittenSchema(), other.getOverWrittenSchema())) {
			return false;
		}		
		if (!Objects.equals(this.realTimeDelay, other.realTimeDelay)) {
			return false;
		}
		if (!Objects.equals(this.applicationTimeDelay, other.applicationTimeDelay)) {
			return false;
		}
		return true;
	}

	// needed for PQL-Generator
	public boolean getReadMetaData() {
		return readMetaData();
	}



	@Override
	public boolean isAllPhysicalInputSet() {
		return true;
	}
	
	@Override
	public boolean isValid() {
//		if (accessAOResource.isMarked() && this.outputSchema.size() > 0) {
//			addError("Source " + accessAOResource + " already defined!");
//			return false;
//		}
		return super.isValid();
	}


	protected String buildString(List<?> elements, String sep) {
		StringBuffer v = new StringBuffer();
		for (Object s : elements) {
			v.append(s).append(sep);
		}
		return v.substring(0, v.length() - 1);
	}

	@Override
	public boolean isSinkOperator() {
		return false;
	}


}

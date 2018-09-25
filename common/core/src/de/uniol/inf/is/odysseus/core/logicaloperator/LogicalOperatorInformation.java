/*******************************************************************************
 * Copyright 2013 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.logicaloperator;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author DGeesen
 *
 */
public class LogicalOperatorInformation {
	
	private String operatorName;
	private String doc;
    private String url;
	private Map<String, LogicalParameterInformation> parameters = new HashMap<>();
	private int maxPorts;
	private int minPorts;
	private String[] categories;
	private boolean hidden;
	private boolean deprecated;

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getDoc() {
		return doc;
	}

	public void setDoc(String doc) {
		this.doc = doc;
	}

    /**
     * @return the url
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

	public Collection<LogicalParameterInformation> getParameters() {
		return Collections.unmodifiableCollection(parameters.values());
	}

	public void addParameter(LogicalParameterInformation parameter) {
		this.parameters.put(parameter.getName(), parameter);
	}

	public void setParameters(List<LogicalParameterInformation> parameters) {
		for (LogicalParameterInformation p:parameters){
			this.parameters.put(p.getName(), p);
		}
	}

	public int getMinPorts() {
		return minPorts;
	}

	public void setMinPorts(int minPorts) {
		this.minPorts = minPorts;
	}

	public int getMaxPorts() {
		return maxPorts;
	}

	public void setMaxPorts(int maxPorts) {
		this.maxPorts = maxPorts;
	}

	public String[] getCategories() {
		return categories;
	}

	public void setCategories(String[] categories) {
		this.categories = categories;
	}

	public LogicalParameterInformation getParameter(String name) {
		return parameters.get(name);
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}	
	
	public boolean isDeprecated() {
		return deprecated;
	}
	
	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}

}

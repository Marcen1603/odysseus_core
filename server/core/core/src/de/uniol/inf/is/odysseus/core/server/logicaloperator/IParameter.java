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
/**
 *
 */
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.io.Serializable;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * @author Jonas Jacobi
 */
public interface IParameter<T> extends Serializable {
	static enum REQUIREMENT {
		MANDATORY, OPTIONAL
	}

	static enum USAGE {
		RECENT, DEPRECATED
	}

	void setName(String name);

	String getName();

	void setAliasName(String aliasname);

	String getAliasName();

	void setDoc(String doc);

	String getDoc();

	IParameter.REQUIREMENT getRequirement();

	void setRequirement(REQUIREMENT requirement);

	void setUsage(USAGE usage);

	boolean isDeprecated();

	void setInputValue(Object object);

	boolean validate();

	List<String> getErrors();

	T getValue();

	boolean hasValue();

	boolean isMandatory();

	void setAttributeResolver(DirectAttributeResolver resolver);

	DirectAttributeResolver getAttributeResolver();

	void setDataDictionary(IDataDictionary dd);

	IDataDictionary getDataDictionary();

	void setServerExecutor(IServerExecutor serverExecutor);

	IServerExecutor getServerExecutor();

	void setContext(Context context);

	Context getContext();

	void setCaller(ISession caller);

	ISession getCaller();

	void clear();

	String getPQLString();

	String getPossibleValueMethod();

	void setPossibleValueMethod(String possibleValueMethod);

	boolean arePossibleValuesDynamic();

	void setPossibleValuesAreDynamic(boolean possibleValuesAreDynamic);


}
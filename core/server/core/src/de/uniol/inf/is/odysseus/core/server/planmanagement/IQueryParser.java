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
package de.uniol.inf.is.odysseus.core.server.planmanagement;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public interface IQueryParser {
	/**
	 * Name of the language. Needs to be unique
	 * @return
	 */
	public String getLanguage();
	
	/**
	 * Method used to parse a query text
	 * @param query The query text that should be translated
	 * @param user The user who is posing the query
	 * @param dd A read only reference to the data dictionary. Remark: Do you change the data dictionary from a query!
	 * @param context Some context information 
	 * @param metaAttribute The current meta attributes used
	 * @param executor A reference to the current executor. Use only for reading. A query parser should not change state!
	 * @return A list of IExecutorCommands
	 * @throws QueryParseException 
	 */
	public List<IExecutorCommand> parse(String query, ISession user,
			IDataDictionary dd, Context context, IMetaAttribute metaAttribute,
			IServerExecutor executor) throws QueryParseException;

	/**
	 * This method could deliver all language tokens (used by editors) 
	 * @param user
	 * @return
	 */
	public Map<String, List<String>> getTokens(ISession user);

	/**
	 * 
	 * @param hint
	 * @param user
	 * @return
	 */
	public List<String> getSuggestions(String hint, ISession user);
}


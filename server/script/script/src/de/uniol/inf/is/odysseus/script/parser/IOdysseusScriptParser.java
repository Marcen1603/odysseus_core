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
package de.uniol.inf.is.odysseus.script.parser;

import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public interface IOdysseusScriptParser {
	
	public List<?> parseAndExecute(String completeText, ISession caller, ISink<?> defaultSink, Context context, IServerExecutor executor) throws OdysseusScriptException;

	public List<?> parseAndExecute(String[] textlines, ISession caller, ISink<?> defaultSink, Context context, IServerExecutor executor) throws OdysseusScriptException;

	public List<?> execute(List<PreParserStatement> statements, ISession caller, ISink<?> defaultSink, IServerExecutor executor) throws OdysseusScriptException;

	public void validate(String[] textlines, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException;
	
	public List<PreParserStatement> parseScript(String completeText, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException;

	public List<PreParserStatement> parseScript(String[] textToParse, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException;

	public String getParameterKey();

	public String getReplacementStartKey();

	public String getReplacementEndKey();

	public String getSingleLineCommentKey();

	public Set<String> getKeywordNames();

	public Set<String> getStaticWords();
	
	public PreParserKeywordRegistry getPreParserKeywordRegistry();

	public void setReplacement(String key, String value);

}

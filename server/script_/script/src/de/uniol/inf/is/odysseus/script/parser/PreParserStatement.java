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
package de.uniol.inf.is.odysseus.script.parser;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;


public final class PreParserStatement {

	private static final Logger LOG = LoggerFactory.getLogger(PreParserStatement.class);
	
	private IPreParserKeyword keyword;
	private String keywordText;
	private String parameter;
	private int line;
	
	public PreParserStatement( String keywordText, IPreParserKeyword keyword, String parameter, int line ) {
		this.keyword = keyword;
		this.keywordText = keywordText;
		this.parameter = parameter;
		this.line = line;
	}
	
	void validate( Map<String, Object> variables, ISession caller, IOdysseusScriptParser parser, Context context ) throws OdysseusScriptException {
		keyword.setParser(parser);
		
		keyword.validate(variables, parameter, caller, context);
	}
	
	Optional<?> execute( Map<String, Object> variables, ISession caller, IOdysseusScriptParser parser, Context context ) throws OdysseusScriptException {
		if( keyword.isDeprecated() ) {
			logDeprecation();
		}
		
		keyword.setParser(parser);
		Object result = keyword.execute(variables, parameter, caller, context);
		return result == null ? Optional.absent() : Optional.of(result);
	}

	private void logDeprecation() {
		if( !Strings.isNullOrEmpty(keyword.getDeprecationInfo())) {
			LOG.error("PreParserKeyword {} is deprecated: {}", keywordText, keyword.getDeprecationInfo());
		} else {
			LOG.error("PreParserKeyword {} is deprecated", keywordText);
		}
	}
	
	public String getParameter() {
		return parameter;
	}
	
	public String getKeywordText() {
		return keywordText;
	}
	
	public IPreParserKeyword getKeyword(){
		return keyword;
	}
	
	@Override
	public String toString() {
		return keyword+" "+keywordText;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}
}

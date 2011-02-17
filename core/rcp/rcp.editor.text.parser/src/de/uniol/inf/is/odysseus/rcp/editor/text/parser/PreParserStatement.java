/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.rcp.editor.text.parser;

import java.util.Map;


public class PreParserStatement {

	private IPreParserKeyword keyword;
	private String keywordText;
	private String parameter;
	
	public PreParserStatement( String keywordText, IPreParserKeyword keyword, String parameter ) {
		this.keyword = keyword;
		this.keywordText = keywordText;
		this.parameter = parameter;
	}
	
	public void validate( Map<String, Object> variables ) throws QueryTextParseException {
		keyword.validate(variables, parameter);
	}
	
	public Object execute( Map<String, Object> variables ) throws QueryTextParseException {
		return keyword.execute(variables, parameter);
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
}

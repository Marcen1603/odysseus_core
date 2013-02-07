/********************************************************************************** 
  * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.rcp.editor.text.editors.formatting;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.rcp.editor.text.editors.DocumentUtils;

/**
 * @author Dennis Geesen
 *
 */
public class QueryFormattingStrategy extends DefaultFormattingStrategy{

	private Map<String, IOdysseusScriptFormattingStrategy> formattingStrategies = new HashMap<>();
	private String initialIndentation;

	/**
	 * @param queryStrategies
	 */
	public QueryFormattingStrategy(Map<String, IOdysseusScriptFormattingStrategy> queryStrategies) {
		this.formattingStrategies  = queryStrategies;
	}

	@Override
	public void formatterStarts(String initialIndentation) {
		this.initialIndentation = initialIndentation;
	}

	@Override
	public String format(String content, boolean isLineStart, String indentation, int[] positions) {	
		
		String parser = DocumentUtils.findValidParserAtPosition(document, region.getOffset());
		if(!parser.isEmpty()){
			IOdysseusScriptFormattingStrategy parserFormatter =  this.formattingStrategies.get(parser);
			if(parserFormatter!=null){				
				parserFormatter.setDocument(document);
				parserFormatter.setRegion(region);
				parserFormatter.formatterStarts(this.initialIndentation);
				return parserFormatter.format(content, isLineStart, indentation, positions);
			}		
		}
		return null;
	}

	@Override
	public void formatterStops() {	
		
	}

}

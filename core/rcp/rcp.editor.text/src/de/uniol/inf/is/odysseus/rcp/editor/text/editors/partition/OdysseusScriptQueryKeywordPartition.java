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
package de.uniol.inf.is.odysseus.rcp.editor.text.editors.partition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.completion.IEditorLanguagePropertiesProvider;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.formatting.IOdysseusScriptFormattingStrategy;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.formatting.QueryFormattingStrategy;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptParser;

/**
 * @author Dennis Geesen
 * 
 */
public class OdysseusScriptQueryKeywordPartition extends OdysseusScriptKeywordPartition {

	public OdysseusScriptQueryKeywordPartition() {
		super("__odysseus_script_query");
	}

	@Override
	public Color getDefaultColor() {
		// normal color here is dark blue
		return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
	}

	@Override
	public List<IRule> getColoringRules() {
		List<IRule> rules = super.getColoringRules();

		// additionally, we add rules for the current language
		ParserDependentWordRule pd = new ParserDependentWordRule(getWordDetector(), getColoringToken(), true);
		IToken wordToken = createToken(SWT.COLOR_DARK_BLUE, true);
		for(IEditorLanguagePropertiesProvider ecp : OdysseusRCPEditorTextPlugIn.getEditorCompletionProviders()){
			for (String word : ecp.getTerminals()) {
				pd.addWord(word, wordToken, ecp.supportsParser());
			}
			
		}
		
		rules.add(pd);
		return rules;
	}

	@Override
	public List<IPredicateRule> getPartitioningRules() {
		List<IPredicateRule> rules = new ArrayList<>();
		List<String> end = new ArrayList<>();		
		end.add(OdysseusScriptParser.PARAMETER_KEY);			
		List<String> ignore = new ArrayList<>();
		ignore.add("#IFDEF");
		ignore.add("#ELSE");
		ignore.add("#ENDIF");
		
		rules.add(new SimpleMultiLineRule(OdysseusScriptParser.PARAMETER_KEY + "QUERY", end, ignore, getPartitioningToken(), true, true));
		rules.add(new SimpleMultiLineRule(OdysseusScriptParser.PARAMETER_KEY + "ADDQUERY", end, ignore, getPartitioningToken(), true, true));
		rules.add(new SimpleMultiLineRule(OdysseusScriptParser.PARAMETER_KEY + "RUNQUERY", end, ignore, getPartitioningToken(), true, true));
		return rules;
	}
	
	@Override
	public IFormattingStrategy getFormattingStrategy() {
		Map<String, IOdysseusScriptFormattingStrategy> queryStrategies = new HashMap<>();
		for(IEditorLanguagePropertiesProvider ecp : OdysseusRCPEditorTextPlugIn.getEditorCompletionProviders()){			
			queryStrategies.put(ecp.supportsParser(), ecp.getFormattingStrategy());
		}
		return new QueryFormattingStrategy(queryStrategies);
	}

}

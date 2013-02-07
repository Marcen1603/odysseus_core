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
package de.uniol.inf.is.odysseus.rcp.editor.text.editors.partition;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptParser;

/**
 * @author Dennis Geesen
 * 
 */
public class OdysseusScriptSimpleCommentPartition extends AbstractOdysseusScriptParition {

	/**
	 * @param partitioningTokenName
	 */
	public OdysseusScriptSimpleCommentPartition() {
		super("__odysseus_script_comment");
	}

	@Override
	public Color getDefaultColor() {
		return convertColor(SWT.COLOR_DARK_GREEN);
	}

	@Override
	public List<IRule> getColoringRules() {
		return new ArrayList<>();
	}

	@Override
	public List<IPredicateRule> getPartitioningRules() {
		List<IPredicateRule> rules = new ArrayList<>();
		rules.add(new EndOfLineRule(OdysseusScriptParser.SINGLE_LINE_COMMENT_KEY, getPartitioningToken()));
		return rules;
	}	

}

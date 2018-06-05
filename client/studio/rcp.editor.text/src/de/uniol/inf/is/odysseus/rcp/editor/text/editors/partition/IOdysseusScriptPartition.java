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

import java.util.List;

import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.swt.graphics.Color;

/**
 * @author Dennis Geesen
 *
 */
public interface IOdysseusScriptPartition {
	
	/**
	 * the default color that is used if no coloring rule matches
	 */
	public Color getDefaultColor();
	
	/**
	 * A list of rules that tries to match parts (words, tokens...) and 
	 * colors them according to the rule 
	 */
	public List<IRule> getColoringRules();
	
	public IToken getColoringToken();
	
	/**
	 * This Rules are used to cut the whole document in certain partitions (e.g. comments, strings etc.) 
	 */
	public List<IPredicateRule> getPartitioningRules();
	
	
	public IToken getPartitioningToken();
	
	public String getPartitionTokenName();
	
	public ITokenScanner getReconcilerScanner();	
	
	public IFormattingStrategy getFormattingStrategy();

}

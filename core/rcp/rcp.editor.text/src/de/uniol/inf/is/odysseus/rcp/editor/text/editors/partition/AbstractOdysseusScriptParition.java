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

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.rcp.editor.text.editors.formatting.DefaultFormattingStrategy;

/**
 * @author Dennis Geesen
 * 
 */
public abstract class AbstractOdysseusScriptParition implements IOdysseusScriptPartition {

	private IToken partitionToken;
	private IToken coloringToken;
	private String partitioningTokenName;
	

	public AbstractOdysseusScriptParition(String partitioningTokenName) {
		this(partitioningTokenName, false);
	}

	public AbstractOdysseusScriptParition(String partitioningTokenName, boolean bold) {
		this.partitionToken = new Token(partitioningTokenName);
		this.partitioningTokenName = partitioningTokenName;
		this.coloringToken = createToken(getDefaultColor(), bold);
	}

	@Override
	public IToken getPartitioningToken() {
		return partitionToken;
	}

	@Override
	public IToken getColoringToken() {
		return coloringToken;
	}

	@Override
	public String getPartitionTokenName() {
		return partitioningTokenName;
	}

	@Override
	public OdysseusScriptRuleBasedScanner getReconcilerScanner() {
		OdysseusScriptRuleBasedScanner rbs = new OdysseusScriptRuleBasedScanner();
		rbs.setDefaultReturnToken(createToken(getDefaultColor(), false));
		rbs.setRules(getColoringRules().toArray(new IRule[0]));
		return rbs;
	}

	protected IToken createToken(int swtColor) {
		return createToken(convertColor(swtColor), false);
	}
	
	protected IToken createToken(int swtColor, boolean bold) {
		return createToken(convertColor(swtColor), bold);
	}

	protected IToken createToken(Color color, boolean bold) {
		if (bold) {
			return new Token(new TextAttribute(color, null, SWT.BOLD));
		} else {
			return new Token(new TextAttribute(color));
		}
	}
		
	
	protected Color convertColor(int swtColor){
		return Display.getCurrent().getSystemColor(swtColor);
	}
	
	@Override
	public IFormattingStrategy getFormattingStrategy() {	
		return new DefaultFormattingStrategy();
	}

}

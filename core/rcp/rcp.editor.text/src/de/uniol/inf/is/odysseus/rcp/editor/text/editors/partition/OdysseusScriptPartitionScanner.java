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
import java.util.List;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;

/**
 * @author Dennis Geesen
 * 
 */
public class OdysseusScriptPartitionScanner extends RuleBasedPartitionScanner {

	int counter = 0;
	
	public OdysseusScriptPartitionScanner() {
		counter = 0;
		List<IPredicateRule> rules = new ArrayList<>();
		for (IOdysseusScriptPartition osp : OdysseusScriptPartitionRegsitry.getParitions()) {
			rules.addAll(osp.getPartitioningRules());
		}
		setPredicateRules(rules.toArray(new IPredicateRule[0]));
	}

	public String[] getLegalParitions() {
		return OdysseusScriptPartitionRegsitry.getAllPartionNames();
	}
	
//	@Override
//	public void unread() {				
//		super.unread();
//		counter--;
//		System.out.println("unread "+counter);
//	}
//	
//	@Override
//	public int read() {
//		int c = super.read();
//		counter++;
//		System.out.println("read + "+counter+" - "+((char)c));
//		return c;
//	}
	
}

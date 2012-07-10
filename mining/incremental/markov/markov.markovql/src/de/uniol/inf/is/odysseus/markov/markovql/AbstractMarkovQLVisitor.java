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
package de.uniol.inf.is.odysseus.markov.markovql;

import de.uniol.inf.is.odysseus.markov.markovql.parser.ASTHiddenMarkovModel;
import de.uniol.inf.is.odysseus.markov.markovql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.markov.markovql.parser.ASTProbability;
import de.uniol.inf.is.odysseus.markov.markovql.parser.ASTProbabilityTransition;
import de.uniol.inf.is.odysseus.markov.markovql.parser.ASTProbabilityTransitionList;
import de.uniol.inf.is.odysseus.markov.markovql.parser.ASTProbabiltyList;
import de.uniol.inf.is.odysseus.markov.markovql.parser.ASTQuery;
import de.uniol.inf.is.odysseus.markov.markovql.parser.ASTQuotedIdentifier;
import de.uniol.inf.is.odysseus.markov.markovql.parser.ASTStateList;
import de.uniol.inf.is.odysseus.markov.markovql.parser.ASTStates;
import de.uniol.inf.is.odysseus.markov.markovql.parser.MarkovQLParserVisitor;
import de.uniol.inf.is.odysseus.markov.markovql.parser.SimpleNode;

public abstract class AbstractMarkovQLVisitor implements MarkovQLParserVisitor {

	@Override
	public Object visit(SimpleNode node, Object data) {		
		return null;
	}

	@Override
	public Object visit(ASTQuery node, Object data) {		
		return null;
	}

	@Override
	public Object visit(ASTHiddenMarkovModel node, Object data) {	
		return null;
	}

	@Override
	public Object visit(ASTIdentifier node, Object data) {		
		return null;
	}

	@Override
	public Object visit(ASTQuotedIdentifier node, Object data) {	
		return null;
	}

	@Override
	public Object visit(ASTStates node, Object data) {		
		return null;
	}

	@Override
	public Object visit(ASTStateList node, Object data) {		
		return null;
	}
	
	@Override
	public Object visit(ASTProbabilityTransitionList node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTProbabilityTransition node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTProbabiltyList node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTProbability node, Object data) {
		return null;
	}	

}

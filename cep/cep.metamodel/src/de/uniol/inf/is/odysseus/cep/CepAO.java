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
package de.uniol.inf.is.odysseus.cep;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.cep.metamodel.State;
import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;
import de.uniol.inf.is.odysseus.cep.metamodel.Transition;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.OutputSchemaSettable;

public class CepAO<T> extends AbstractLogicalOperator implements
		OutputSchemaSettable {

	private static final long serialVersionUID = 1L;

	private StateMachine<T> firstStateMachine = new StateMachine<T>();
	private StateMachine<T> secondStateMachine;
	private SDFSchema outSchema;
	private Map<Integer, String> portNames = new HashMap<Integer, String>();

	public CepAO(CepAO<T> cepAO) {
		super(cepAO);
		this.firstStateMachine = cepAO.firstStateMachine;
		this.outSchema = cepAO.outSchema == null ? null : new SDFSchema(
				cepAO.outSchema.getURI(), cepAO.outSchema);
		this.portNames = new HashMap<Integer, String>(cepAO.portNames);
	}

	public CepAO() {
	}

	@Override
	public SDFSchema getOutputSchema() {
		return outSchema;
	}

	public StateMachine<T> getStateMachine() {
		return firstStateMachine;
	}

	public synchronized StateMachine<T> getSecondStateMachine() {
		return secondStateMachine;
	}

	public void prepareNegation() {
		// To process state machines with negative states we have to
		// create two state machines, one containing all states (and
		// treating the negative state as positive) and one containing only
		// the positive states.
		// The resulting state machine fires if the first automata does not
		// reach the
		// final state but the second one
		// TODO: Can the first/last state be negative? How to check this??
		if (firstStateMachine.containsNegativeStates()) {
			secondStateMachine = new StateMachine<T>();
			List<State> newStates = new LinkedList<State>();

			Iterator<State> iter = firstStateMachine.getStates().iterator();

			while (iter.hasNext()) {
				State fsmState = iter.next();
				if (fsmState.isNegated() && iter.hasNext()) {
					fsmState = iter.next();
				}

				State copy = new State(fsmState, false);
				newStates.add(copy);
				List<Transition> nextTransistions = new LinkedList<Transition>();
				findNextNonNegativeTransitions(fsmState, nextTransistions,
						new LinkedList<State>());
				for (Transition nt : nextTransistions) {
					copy.addTransition(nt);
				}

			}

			if (newStates.size() == 0) {
				throw new IllegalArgumentException(
						"An state machine only with negative states is not supported!!");
			}

			secondStateMachine.setStates(newStates);
			secondStateMachine.setInitialState(newStates.get(0).getId());
			secondStateMachine.getSymTabScheme(true);

//			System.err.println(firstStateMachine);
//			System.err.println(secondStateMachine);
		}

	}

	// Find for a state all transitions to the next
	// non negated target
	private void findNextNonNegativeTransitions(State state,
			List<Transition> nextTransistions, List<State> visited) {
		List<Transition> trans = state.getTransitions();
		for (Transition t : trans) {
			State nextState = t.getNextState();
			if (!visited.contains(nextState)) {
				if (nextState.isNegated()) {
					visited.add(nextState);
					findNextNonNegativeTransitions(t.getNextState(),
							nextTransistions, visited);
				} else {
					nextTransistions.add(t);
				}
			}
		}
	}

	@Override
	public void setOutputSchema(SDFSchema outputSchema) {
		this.outSchema = outputSchema.clone();
	}

	@Override
	public void setOutputSchema(SDFSchema outputSchema, int port) {
		if (port == 0) {
			setOutputSchema(outputSchema);
		} else {
			throw new IllegalArgumentException("no such port: " + port);
		}
	}

	public void setInputTypeName(int port, String name) {
		portNames.put(port, name);
	}

	public String getInputTypeName(int port) {
		return portNames.get(port);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new CepAO<T>(this);
	}

}

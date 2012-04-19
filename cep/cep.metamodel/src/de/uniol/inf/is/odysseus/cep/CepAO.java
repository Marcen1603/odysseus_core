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
import java.util.Set;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.cep.metamodel.State;
import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;
import de.uniol.inf.is.odysseus.cep.metamodel.Transition;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;

public class CepAO<T> extends AbstractLogicalOperator{

	private static final long serialVersionUID = 1L;

	private StateMachine<T> firstStateMachine = new StateMachine<T>();
	private StateMachine<T> secondStateMachine;
	private Map<Integer, String> portNames = new HashMap<Integer, String>();
	private boolean oneMatchPerInstance = true;
	private SDFSchema outputSchemaIntern;

	private int rate;
	
	

	public CepAO(CepAO<T> cepAO) {
		super(cepAO);
		this.firstStateMachine = cepAO.firstStateMachine;
		this.secondStateMachine = cepAO.secondStateMachine;
		this.portNames = new HashMap<Integer, String>(cepAO.portNames);
		this.oneMatchPerInstance = cepAO.oneMatchPerInstance;
		this.rate =  cepAO.rate;
		this.outputSchemaIntern = cepAO.outputSchemaIntern;
	}

	public CepAO() {
	}

	public void setOutputSchemaIntern(SDFSchema outputSchemaIntern) {
		this.outputSchemaIntern = outputSchemaIntern;
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
	protected SDFSchema getOutputSchemaIntern(int pos) {
		
		StateMachine<T> m = this.getStateMachine();
		Set<String> types = m.getStateTypeSet();
		
		for (LogicalSubscription s : this.getSubscribedToSource()) {
			String name = this.getInputTypeName(s.getSinkInPort());
			if (name == null) {
				SDFSchema schema = s.getSchema();
				name = schema.getURI();
				if (!types.contains(name)) {
					throw new IllegalArgumentException("Type " + name
							+ " no input for Operator");
				}
				this.setInputTypeName(s.getSinkInPort(), name);
			}
		}
		
		List<SDFAttribute> outAttributeList = new LinkedList<SDFAttribute>();
		// Try to optimize output types
		List<SDFAttribute> outAttributes = outputSchemaIntern
				.getAttributes();
		for (SDFAttribute outAttr : outAttributes) {
			String attr = outAttr.getQualName();
			//WRITE|MIN|MAX|SUM|COUNT|AVG
			if (attr.startsWith("WRITE") || attr.startsWith("MIN") || attr.startsWith("MAX")) {
				int start = attr.lastIndexOf("_");
				int point = attr.lastIndexOf(".");
				String state = attr.substring(start + 1, point);
				String attributeName = attr.substring(point + 1);
				int port = getPortForName(state);
				SDFSchema inSchema = getInputSchema(port);
				boolean found = false;
				if (inSchema != null) {
					for (SDFAttribute inAttr : inSchema) {
						if (inAttr.getAttributeName().equalsIgnoreCase(
								attributeName)) {
							outAttributeList.add(new SDFAttribute(outAttr
									.getSourceName(), outAttr
									.getAttributeName(), inAttr.getDatatype()));
							found = true;
						}
					}
				}
				if (!found) {
					outAttributeList.add(outAttr);
				}
			} else if (outAttr.getQualName().startsWith("COUNT")) {
				outAttributeList.add(new SDFAttribute(outAttr
						.getSourceName(), outAttr
						.getAttributeName(), SDFDatatype.LONG));
			} else if (outAttr.getQualName().startsWith("AVG") || outAttr.getQualName().startsWith("SUM")) {
				outAttributeList.add(new SDFAttribute(outAttr
						.getSourceName(), outAttr
						.getAttributeName(), SDFDatatype.DOUBLE));
			}

		}

		SDFSchema output = new SDFSchema(outputSchemaIntern.getURI(),
				outAttributeList);
		setOutputSchema(output);

		return output;
	}
	
	public void setInputTypeName(int port, String name) {
		portNames.put(port, name);
	}

	public String getInputTypeName(int port) {
		return portNames.get(port);
	}
	
	public int getPortForName(String stateVarName){
		String name = null;
		List<State> states = firstStateMachine.getStates();
		for (State s:states){
			if (s.getVar().equalsIgnoreCase(stateVarName)){
				name = s.getType();
				break;
			}
		}
		
		for (Entry<Integer, String> e : portNames.entrySet()){
			if (e.getValue().equals(name)){
				return e.getKey();
			}
		}
		return -1;
	}

	public boolean isOneMatchPerInstance() {
		return oneMatchPerInstance;
	}
	
	public void setOneMatchPerInstance(boolean oneMatchPerInstance) {
		this.oneMatchPerInstance = oneMatchPerInstance;
	}
		
	public void setHeartbeatRate(int rate){
		this.rate = rate;
	}

	public int getHeartbeatRate() {
		return rate;
	}
	@Override
	public AbstractLogicalOperator clone() {
		return new CepAO<T>(this);
	}

}

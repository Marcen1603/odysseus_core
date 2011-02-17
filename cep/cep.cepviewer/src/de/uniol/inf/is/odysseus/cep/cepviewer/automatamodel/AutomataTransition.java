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
package de.uniol.inf.is.odysseus.cep.cepviewer.automatamodel;

import org.eclipse.draw2d.ConnectionRouter;

import de.uniol.inf.is.odysseus.cep.metamodel.Transition;

/**
 * This class defines an transition in an automata.
 * 
 * @author Christian
 */
public class AutomataTransition extends AbstractTransition {

	/**
	 * This is the constructor.
	 */
	public AutomataTransition(Anchor sourceAnchor, Anchor targetAnchor,
			Transition transition, AbstractState nextState) {
		super(sourceAnchor, targetAnchor, transition, nextState);
		// set the connection router which directly connects two the anchors
		setConnectionRouter(ConnectionRouter.NULL);
	}
}
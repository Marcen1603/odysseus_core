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
package de.uniol.inf.is.odysseus.rcp.editor.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import de.uniol.inf.is.odysseus.rcp.editor.model.Operator;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorConnection;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorPlan;

public class MyEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart arg0, Object object) {
		if( object instanceof OperatorPlan ) {
			return new OperatorPlanEditPart((OperatorPlan)object);
		} else	if (object instanceof Operator) {
			return new OperatorEditPart((Operator) object);
		} else if( object instanceof OperatorConnection ) {
			return new OperatorConnectionEditPart((OperatorConnection)object);
		} else {
			assert(false);
			return null;
		}
	}

}

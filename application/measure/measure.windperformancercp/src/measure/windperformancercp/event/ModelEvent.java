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
package measure.windperformancercp.event;

//import de.uniol.inf.is.odysseus.event.AbstractEvent;
//import org.eclipse.swt.events.TypedEvent;

import measure.windperformancercp.model.IDialogResult;
import measure.windperformancercp.model.IModel;


public class ModelEvent extends AbstractEvent<IModel, IDialogResult>{
	
	public ModelEvent(IModel model, ModelEventType type, IDialogResult value){
		super(model, type, value);		
	}
		
}

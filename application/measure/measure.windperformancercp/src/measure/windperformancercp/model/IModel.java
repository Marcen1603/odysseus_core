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
package measure.windperformancercp.model;

import measure.windperformancercp.event.IEventHandler;
import measure.windperformancercp.model.IDialogResult;

/**
 * Interface for models (e.g. source model, performance model, where the global ressources are kept)
 * @author Diana von Gallera
 *
 */
public interface IModel extends IEventHandler {
	//TODO: make it complete
	/**
	 * Returns the number of elements (sources, data stream questions..)
	 */
	public int getElemCount();
//	public Object getIthElement(int i);
	/**
	 * fires a model event
	 */
	public void somethingChanged(IDialogResult res);

}

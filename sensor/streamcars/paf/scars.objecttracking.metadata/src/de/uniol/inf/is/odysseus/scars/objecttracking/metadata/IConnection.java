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
package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;

/**
 * This class represents an interface for an rated connection between two objects. It could be used to define the
 * connections between cars within the association process in the objecttracking architecture.
 *
 * @author Volker Janz
 *
 */
public interface IConnection {

	public TupleIndexPath getLeftPath();
	public TupleIndexPath getRightPath();

	public void setLeftPath(TupleIndexPath newleft);
	public void setRightPath(TupleIndexPath newright);

	public double getRating();
	public void setRating(double newrating);

}

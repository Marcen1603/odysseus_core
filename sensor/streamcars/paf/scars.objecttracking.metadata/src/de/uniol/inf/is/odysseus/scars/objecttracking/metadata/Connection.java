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
 * This class represents a rated connection between two objects. It could be used to define the connections between cars within
 * the association process in the objecttracking architecture.
 *
 * @author Volker Janz
 *
 */
public class Connection implements IConnection {

	private TupleIndexPath leftPath;
	private TupleIndexPath rightPath;
	private double rating;

	public Connection(TupleIndexPath leftPath, TupleIndexPath rightPath, double rating) {
		this.leftPath = leftPath;
		this.rightPath = rightPath;
		this.rating = rating;
	}

	public Connection(Connection copy) {
		this.leftPath = copy.getLeftPath().clone();
		this.rightPath = copy.getRightPath().clone();
	}

	@Override
	public TupleIndexPath getLeftPath() {
		return this.leftPath;
	}

	@Override
	public TupleIndexPath getRightPath() {
		return this.rightPath;
	}

	@Override
	public void setLeftPath(TupleIndexPath newleft) {
		this.leftPath = newleft;
	}

	@Override
	public void setRightPath(TupleIndexPath newright) {
		this.rightPath = newright;
	}

	@Override
	public double getRating() {
		return this.rating;
	}

	@Override
	public void setRating(double newrating) {
		this.rating = newrating;
	}

}

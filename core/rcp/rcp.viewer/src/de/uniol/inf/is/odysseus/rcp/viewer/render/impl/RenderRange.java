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
package de.uniol.inf.is.odysseus.rcp.viewer.render.impl;

import de.uniol.inf.is.odysseus.rcp.viewer.view.Vector;


public final class RenderRange {

	public int x;
	public int y;
	public int height;
	public int width;

	public boolean contains( int x, int y ) {
		return x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height;
	}
	
	public boolean contains( Vector v ) {
		return contains( (int)v.getX(), (int)v.getY() );
	}
	
	public boolean intersects (int x, int y, int width, int height) {
		return (x < this.x + this.width) && (y < this.y + this.height) &&
			(x + width > this.x) && (y + height > this.y);
	}
	
	public boolean intersects( Vector start, int width, int height ) {
		return intersects( (int)start.getX(), (int)start.getY(), width, height );
	}
}

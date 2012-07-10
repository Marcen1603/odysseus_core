/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.rcp.viewer.view;


public class Vector {

	private double x;
	private double y;
	
	public static final Vector EMPTY_VECTOR = new Vector(0,0);
	
	public Vector( double x, double y ) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public Vector clone() {
		return new Vector(x,y);
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public Vector add( Vector v ) {
		return new Vector( v.x + x, v.y + y );
	}
	
	public Vector add( double x, double y ) {
		return new Vector( this.x + x, this.y + y );
	}
	
	public Vector sub( Vector v ) {
		return new Vector( x - v.x, y - v.y );
	}
	
	public Vector sub( double x, double y ) {
		return new Vector( this.x - x, this.y - y );
	}
	
	public Vector mul( int factor ) {
		return new Vector( x * factor, y * factor );
	}
	
	public Vector mul( double factor ) {
		return new Vector( x * factor, y * factor);
	}
	
	public double mul( Vector v ) {
		return v.x * x + v.y * y;
	}
	
	public Vector div( int factor ) {
		return new Vector( x / factor, y / factor );
	}
	
	public Vector div( double factor ) {
		return new Vector(x / factor, y / factor);
	}
	
	public float getLength() {
		return (float)Math.sqrt(x * x + y * y);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append(x).append(", ").append(y).append("}");
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof Vector)) return false;
		if( obj == this ) return true;
		Vector v = (Vector)obj;
		return v.x == x && v.y == y;
	}
}

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
/*
 * Created on 09.12.2004
 *
 */
package de.uniol.inf.is.odysseus.sourcedescription.sdf.description;

/**
 * @author Marco Grawunder
 *
 */
public class SDFPatternNotCompatible extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2147299382888159453L;

	/**
     * 
     */
    public SDFPatternNotCompatible() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public SDFPatternNotCompatible(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public SDFPatternNotCompatible(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public SDFPatternNotCompatible(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
    }
}

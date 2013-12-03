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
package de.uniol.inf.is.odysseus.core.server.usermanagement;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class ViewAlreadyExistException extends Exception {


    /**
	 * 
	 */
	private static final long serialVersionUID = -7903992932148607127L;

	/**
     * 
     */
    public ViewAlreadyExistException() {
    }

    /**
     * @param message
     */
    public ViewAlreadyExistException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ViewAlreadyExistException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ViewAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

}

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
package de.uniol.inf.is.odysseus.planmanagement.optimization.exception;

import de.uniol.inf.is.odysseus.planmanagement.configuration.AppEnv;

/**
 * QueryOptimizationException describes an {@link Exception} which occurs during
 * optimization.
 * 
 * @author Wolf Bauer
 * 
 */
public class QueryOptimizationException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4360303442668293801L;

	/**
	 * Constructor of QueryOptimizationException.
	 * 
	 * @param m
	 *            detailed Exception message.
	 */
	public QueryOptimizationException(String m) {
		super(m);
	}

	public QueryOptimizationException(Exception e) {
		super(e);
	}
	
	/**
	 * Constructor of QueryOptimizationException.
	 * 
	 * @param m
	 *            detailed Exception message.
	 * @param e
	 *            {@link Throwable} which raised this exception.
	 */
	public QueryOptimizationException(String m, Throwable e) {
		super(m
				+ (e != null ? AppEnv.LINE_SEPARATOR + "Additional info:"
						+ AppEnv.LINE_SEPARATOR + e.getMessage() : ""),e);
	}
}

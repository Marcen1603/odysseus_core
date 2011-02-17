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
package de.uniol.inf.is.odysseus.planmanagement.executor.exception;

/**
 * NoCompilerLoadedException describes an {@link Exception} which occurs if no
 * compiler services is registered.
 * 
 * @author Wolf Bauer
 * 
 */
public class NoCompilerLoadedException extends PlanManagementException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3471219280532519112L;

	/**
	 * Constructor of NoCompilerLoadedException. Message is:
	 * "Compiler plug-in is not loaded."
	 * 
	 */
	public NoCompilerLoadedException() {
		super("Compiler plugin is not loaded.");
	}
}
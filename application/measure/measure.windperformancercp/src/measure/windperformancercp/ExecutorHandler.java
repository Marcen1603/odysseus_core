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
package measure.windperformancercp;

import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;

/**
 * Diese Hilfsklasse verwaltet die vom Declarative Service zur Verfuegung
 * gestellten <code>IExecutor</code>. Der Nutzer kann damit die aktuelle
 * Instanz der Klasse bekommen, falls benoetigt.
 * <p>
 * Der Declative Service ruft die Methoden <code>bindExecutor()</code> und
 * <code>unbindExecutor</code> selbststaendig auf. Der Nutzer sollte sie nicht
 * explizit aufrufen.
 * 
 * @author Timo Michelsen
 * 
 */
public class ExecutorHandler {

	private static IExecutor executor;
	
	public ExecutorHandler(){
	}


	/**
	 * Binds Odysseus  <code>IExecutor</code>. , that handles query execution
	 * Called by declarative Service. User shall not call this method.
	 * @param ex
	 */
	public void bindExecutor(IExecutor ex){
		executor = ex;		
	}
	
	/**
	 * Unbinds Odysseus Executor, that handles query execution
	 * Called by declarative Service. User shall not call this method.
	 * @param ex
	 */
	public void unbindExecutor( IExecutor ex ) {
		executor = null;		
	}
	
	/**
	 * Returns the actual 
	 * <code>IExecutor</code>. 
	 * 
	 * @return Aktuelle <code>IExecutor</code>-Instanz oder
	 *         <code>null</code>.
	 */
	public static IExecutor getExecutor() {
		return executor;
	}
	
}

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
package de.uniol.inf.is.odysseus.broker.exceptions;

/**
 * This exception is thrown, if something goes
 * wrong by translation or transformation of
 * a cyclic query.
 * 
 * This is a runtime exception, since most
 * errors occur due to wrong user inputs
 * 
 * @author Andre Bolles
 *
 */
public class CyclicQueryException extends RuntimeException{

	
	private static final long serialVersionUID = 1L;

	public CyclicQueryException(){
		super();
	}
	
	public CyclicQueryException(String msg){
		super(msg);
	}
	
	public CyclicQueryException(Exception nestedException){
		super(nestedException);
	}
}

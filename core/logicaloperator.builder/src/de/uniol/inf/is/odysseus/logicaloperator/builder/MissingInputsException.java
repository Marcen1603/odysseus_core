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
package de.uniol.inf.is.odysseus.logicaloperator.builder;

public class MissingInputsException extends Exception {

	private int minPortCount;

	public int getMinPortCount() {
		return this.minPortCount;
	}
	
	public MissingInputsException(int minPortCount) {
		super("need at least " + minPortCount + " input operators");
		this.minPortCount = minPortCount;
	}

	private static final long serialVersionUID = 6522687973588913632L;

}

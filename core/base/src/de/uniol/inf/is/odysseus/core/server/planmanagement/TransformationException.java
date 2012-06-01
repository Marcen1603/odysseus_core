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
package de.uniol.inf.is.odysseus.core.server.planmanagement;

import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public class TransformationException extends RuntimeException {

	private static final long serialVersionUID = -8930049065359776237L;
	private List<ILogicalOperator> untranslatedOperators;
	private TransformationConfiguration config;

	public TransformationException() {
	}

	public TransformationException(String message) {
		super(message);
	}

	public TransformationException(Throwable cause) {
		super(cause);
	}

	public TransformationException(String message, Throwable cause) {
		super(message, cause);
	}

	public TransformationException(TransformationConfiguration config,
			List<ILogicalOperator> errors) {
		this.config = config;
		this.untranslatedOperators = errors;
	}

	public List<ILogicalOperator> getUntranslatedOperators() {
		return Collections.unmodifiableList(untranslatedOperators);
	}

	public TransformationConfiguration getConfig() {
		return config;
	}
	
	@Override
	public String getMessage() {
		StringBuilder builder = new StringBuilder("transformation failed ");
		if (untranslatedOperators != null) {
			builder.append("; unable to transform: ");
			for(ILogicalOperator op : this.untranslatedOperators) {
				builder.append(op.toString());
			}
		}
		if (this.config != null) {
			builder.append("; configuration used " + config.toString());
		}
		if (super.getMessage() != null){
			builder.append("; ").append(super.getMessage());
		}
		return builder.toString(); 
	}

}

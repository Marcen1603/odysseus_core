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
package de.uniol.inf.is.odysseus.core.server.util;

/**
 * @author Jonas Jacobi
 */
public abstract class ToStringVisitor<T> implements INodeVisitor<T, String> {

	private StringBuilder builder;
	private boolean wasup;

	public ToStringVisitor() {
		reset();
	}

	@Override
	public void descendAction(T to) {
		if (this.wasup) {
			this.builder.append(',');
		} else {
			this.builder.append('(');
		}
		this.wasup = false;
	}

	@Override
	public void ascendAction(T sub) {
		if (this.wasup) {
			this.builder.append(" )");
		}
		this.wasup = true;
	}

	@Override
	public String getResult() {
		if (this.wasup) {
			this.builder.append(" )");
		}
		return this.builder.toString();
	}

	public void reset() {
		this.builder = new StringBuilder();
		this.wasup = false;
	}
	
	protected StringBuilder getBuilder() {
		return builder;
	}
}

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
package de.uniol.inf.is.odysseus.core.server.planmanagement;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;

public class QueryParseException extends PlanManagementException {
	private static final long serialVersionUID = -3000645620548786308L;
	
	private int line = -1;
	private int column = -1;
	
	public QueryParseException(String message) {
		super(message);
	}
	
	public QueryParseException(Exception e) {
		super(e);
	}
	
	public QueryParseException(String message, Throwable e) {
		super(message, e);
	}
	
	public QueryParseException(String message, int line, int column) {
		super(message);
		this.line = line;
		this.column = column;
	}
	
	public QueryParseException(Exception e, int line, int column) {
		super(e);
		this.line = line;
		this.column = column;
	}
	
	public QueryParseException(String message, Throwable e, int line, int column) {
		super(message, e);
		this.line = line;
		this.column = column;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

}

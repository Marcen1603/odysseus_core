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
/* Generated By:JJTree: Do not edit this line. ASTWindow.java */

package de.uniol.inf.is.odysseus.parser.cql.parser;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;

public abstract class ASTWindow extends SimpleNode {

	protected WindowType type;
	private Long size;
	private Long advance;

	public ASTWindow(int id) {
		super(id);
	}

	public ASTWindow(NewSQLParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * 
	 * @throws QueryParseException */
	@Override
	public Object jjtAccept(NewSQLParserVisitor visitor, Object data) throws QueryParseException {
		return visitor.visit(this, data);
	}

	public void setType(WindowType type) {
		this.type = type;
	}

	public abstract boolean isUnbounded();

	public abstract boolean isPartitioned();

	public Long getSize() {
		return this.size;
	}

	public void setSize(Long size) {
		this.size = size;
	}
	

	public void setAdvance(Long advance) {
		if (advance == 1) {
			this.advance = null;
		} else {
			this.advance = advance;
		}
	}
	
	public Long getAdvance() {
		return this.advance;
	}
	
	public Long getSlide() {
		return null;
	}

	public final WindowType getType() {
		return this.type;
	}

	public boolean hasOffset() {
		return false;
	}

	public boolean hasTimeout() {
		return false;
	}

	public long getOffset() {
		return 0;
	}

	public long getTimeout() {
		return 0;
	}
	
	public abstract ASTPartition getPartition();

}

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
/* Generated By:JJTree: Do not edit this line. ASTStreamSQLWindow.java */

package de.uniol.inf.is.odysseus.parser.cql.parser;

import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;


public class ASTStreamSQLWindow extends ASTWindow {
	private Long offset;
	private Long timeout;
	private Long slide;
	private boolean isUnbounded = false;

	@Override
	public Long getSlide() {
		return slide;
	}

	public ASTStreamSQLWindow(int id) {
		super(id);
	}

	public ASTStreamSQLWindow(NewSQLParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * 
	 * @throws QueryParseException */
	@Override
	public Object jjtAccept(NewSQLParserVisitor visitor, Object data) throws QueryParseException {
		return visitor.visit(this, data);
	}

	@Override
	public ASTPartition getPartition() {
		return (ASTPartition) jjtGetChild(jjtGetNumChildren()-1);

	}

	@Override
	public void setAdvance(Long advance) {
		if (this.slide != null && advance != null && advance != 1) {
			throw new IllegalArgumentException(
					"cannot both of advance and slide parameters");
		}
		super.setAdvance(advance);
	}

	public void setSlide(Long slide) {
		if (slide != null && getAdvance() != null && getAdvance() != 1) {
			throw new IllegalArgumentException(
					"cannot both of advance and slide parameters");
		}
		this.slide = ((slide == null) || (slide == 1)) ? null : slide;
	}

	public void setUnbounded(boolean value) {
		this.isUnbounded = value;
	}

	@Override
	public long getOffset() {
		return this.offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	@Override
	public boolean hasOffset() {
		return this.offset != null;
	}

	@Override
	public long getTimeout() {
		return this.timeout;
	}

	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}

	@Override
	public boolean hasTimeout() {
		return this.timeout != null;
	}

	@Override
	public boolean isPartitioned() {
		return jjtGetNumChildren() > 0 && jjtGetChild(jjtGetNumChildren()-1) instanceof ASTPartition;
	}

	@Override
	public boolean isUnbounded() {
		return this.isUnbounded;
	}
}

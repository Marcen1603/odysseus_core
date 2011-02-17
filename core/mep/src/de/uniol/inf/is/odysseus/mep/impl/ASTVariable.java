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
/* Generated By:JJTree: Do not edit this line. ASTVariable.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.mep.impl;

public @SuppressWarnings("all")
class ASTVariable extends SimpleNode {
	public ASTVariable(int id) {
		super(id);
	}

	public ASTVariable(MEPImpl p, int id) {
		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(MEPImplVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	private String identifier;

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}
}
/*
 * JavaCC - OriginalChecksum=23816d72be7064594a44590879038323 (do not edit this
 * line)
 */

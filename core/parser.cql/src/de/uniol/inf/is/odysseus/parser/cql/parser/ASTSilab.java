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
/* Generated By:JJTree: Do not edit this line. ASTSilab.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package de.uniol.inf.is.odysseus.parser.cql.parser;

public class ASTSilab extends SimpleNode {
	private boolean tupleMode = false;
	
	private boolean mvMode = false;
	
  public ASTSilab(int id) {
    super(id);
  }

  public ASTSilab(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  @Override
public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
  
	public void setTupleMode() {
		this.tupleMode = true;
	}
	
	public boolean useTupleMode() {
		return this.tupleMode;
	}
	
	public void setMVMode(){
		this.mvMode = true;
	}
	
	public boolean useMVMode(){
		return this.mvMode;
	}
}
/* JavaCC - OriginalChecksum=654c9a5238a0b2ede369edec5b15d49e (do not edit this line) */

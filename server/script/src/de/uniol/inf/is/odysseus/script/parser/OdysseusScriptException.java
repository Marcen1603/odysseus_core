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
package de.uniol.inf.is.odysseus.script.parser;

public class OdysseusScriptException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String exceptionMessage = "Error during processing query-file";
	
	private PreParserStatement failedStatement;
	
	public OdysseusScriptException() {
		this(exceptionMessage);
	}
	
	public OdysseusScriptException( String msg ) {
		super(msg);
	}
	
	public OdysseusScriptException( String msg, Throwable ex ) {
		super(msg, ex);
	}
	
	public OdysseusScriptException( Throwable ex) {
		this(exceptionMessage, ex);
	}
	
	public OdysseusScriptException( String msg, PreParserStatement failedStatement ) {
		super(msg);
		this.failedStatement = failedStatement;
	}
	
	public OdysseusScriptException( String msg, Throwable ex, PreParserStatement failedStatement ) {
		super(msg, ex);
		this.failedStatement = failedStatement;
	}
	
	public OdysseusScriptException( Throwable ex, PreParserStatement failedStatement) {
		this(exceptionMessage, ex);
		this.failedStatement = failedStatement;
	}
	
	public String getRootMessage(){
		Throwable current = this;
		while(current.getCause()!=null){
			current = current.getCause();
		}
		return current.getMessage();				
	}

	public PreParserStatement getFailedStatement() {
		return failedStatement;
	}

	public void setFailedStatement(PreParserStatement failedStatement) {
		this.failedStatement = failedStatement;
	}
	
	
	
	
}

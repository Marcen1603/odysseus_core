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
package de.uniol.inf.is.odysseus.core.util;

public interface IExtendedGraphNodeVisitor<NodeType, ResultType> 
{
	enum NodeActionResult
	{
		MARK_AND_CONTINUE,
		MARK_AND_BREAK,
		CONTINUE_ONLY,
		BREAK_ONLY
	}
	
	public NodeActionResult nodeAction(NodeType node);
	
	public void beforeFromSinkToSourceAction(NodeType sink, NodeType source);
	public void afterFromSinkToSourceAction(NodeType sink, NodeType source);
	
	public void beforeFromSourceToSinkAction(NodeType source, NodeType sink);
	public void afterFromSourceToSinkAction(NodeType source, NodeType sink);
	
	public ResultType getResult();
}

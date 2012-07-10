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
package de.uniol.inf.is.odysseus.p2p.peer;

import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListener;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

public interface IQueryProvider {
	void addQuery(P2PQuery query);
	Set<String> getQueryIds();
	P2PQuery getQuery(String queryID);
	int getQueryCount();
	boolean hasQuery(String queryId);
	IExecutionListener getListenerForQuery(String queryID);
	void removeQuery(String queryId);
	int getQueryCount(Lifecycle lifecycle);
	int getQueryCount(List<Lifecycle> lifecycle);
}

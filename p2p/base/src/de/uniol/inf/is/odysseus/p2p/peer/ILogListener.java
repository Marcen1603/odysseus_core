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
package de.uniol.inf.is.odysseus.p2p.peer;

public interface ILogListener {

	void addEvent(String queryId, String event);

	void addAdminPeer(String queryId, String adminPeerName);

	void addQuery(String queryId);

	void logAction(String queryId, String action);

	void setSubplans(String queryId, int subplans);

	void setSplittingStrategy(String queryId, String splittingStrategy);

	void removeQueryOrSubplan(String id);

	void addBid(String queryId, Integer bids);

	void logEvent(String queryId, String event);

	void setScheduler(String queryId, String scheduler);

	void setSchedulerStrategy(String queryId, String schedulerStrategy);

	void addStatus(String queryId, String string);

	void addTab(String id, String query);

	void addResult(String queryId, Object o);

	void setStatus(String queryId, String status);

}

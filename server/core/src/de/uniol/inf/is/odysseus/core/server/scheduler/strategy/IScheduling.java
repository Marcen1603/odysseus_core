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
package de.uniol.inf.is.odysseus.core.server.scheduler.strategy;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.scheduler.ISchedulingEventListener;

public interface IScheduling {
	public boolean schedule(long maxTime);
	public boolean schedule(long maxTime, int trainSize);
	public IPhysicalQuery getPlan();
	boolean isSchedulingPaused();
	boolean isSchedulingBlocked();
	public void addSchedulingEventListener(ISchedulingEventListener eventListener);
	public void removeSchedulingEventListener(ISchedulingEventListener eventListener);
	public boolean isSchedulable();
}

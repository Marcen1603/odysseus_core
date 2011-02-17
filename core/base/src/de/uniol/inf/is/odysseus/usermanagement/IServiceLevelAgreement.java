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
package de.uniol.inf.is.odysseus.usermanagement;

import java.io.Serializable;
import java.util.List;

import de.uniol.inf.is.odysseus.ICSVToString;

public interface IServiceLevelAgreement extends Serializable, ICSVToString{
	/**
	 * Add a new PercentileConstraints. 
	 * @param pc
	 */
	public void addPercentilConstraint(IPercentileConstraint pc) throws PercentileConstraintOverlapException;
	public IPercentileConstraint getPercentilConstraint(double currentSLAConformance) throws NotInitializedException;
	public void init() throws IllegalServiceLevelDefinition;
	public double oc(double currentSLAConformance) throws NotInitializedException;
	public double mg(double currentSLAConformance) throws NotInitializedException;
	public double getMaxOcMg(double currentSLAConformance) throws NotInitializedException;	
	public int getMaxUsers();
	void setMaxUsers(int maxUsers);
	public List<IPercentileConstraint> getPercentilConstraints();
	public String getName();
	public boolean isInitialized();
	void preCalc(int elements);
}
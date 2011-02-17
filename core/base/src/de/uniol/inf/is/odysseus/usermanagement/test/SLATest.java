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
package de.uniol.inf.is.odysseus.usermanagement.test;

import de.uniol.inf.is.odysseus.usermanagement.IServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.IllegalServiceLevelDefinition;
import de.uniol.inf.is.odysseus.usermanagement.NotInitializedException;
import de.uniol.inf.is.odysseus.usermanagement.PercentileConstraintOverlapException;
import de.uniol.inf.is.odysseus.usermanagement.PercentileContraint;
import de.uniol.inf.is.odysseus.usermanagement.ServiceLevelAgreement;

public class SLATest {

	public static void main(String[] args)
			throws PercentileConstraintOverlapException,
			NotInitializedException, IllegalServiceLevelDefinition {
		IServiceLevelAgreement sla = new ServiceLevelAgreement("Test");
		PercentileContraint pc = new PercentileContraint(0.01, 1, 0, true);
		sla.addPercentilConstraint(pc);
		pc = new PercentileContraint(0, 0.01, 1000, true);
		
		sla.addPercentilConstraint(pc);
		sla.init();
		
		System.out.println(sla);		

	}
}

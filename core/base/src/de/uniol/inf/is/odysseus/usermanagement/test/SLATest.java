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

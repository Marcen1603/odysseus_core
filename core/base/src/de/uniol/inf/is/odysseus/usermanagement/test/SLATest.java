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
		PercentileContraint pc = new PercentileContraint(1, 0.9, 0, true);
		sla.addPercentilConstraint(pc);
		pc = new PercentileContraint(0.8, 0.7, 200);
		sla.addPercentilConstraint(pc);

		pc = new PercentileContraint(0.7, 0.6, 600);
		sla.addPercentilConstraint(pc);

		pc = new PercentileContraint(0.6, 0.5, 800);
		sla.addPercentilConstraint(pc);

		pc = new PercentileContraint(0.5, 0.3, 1300);
		sla.addPercentilConstraint(pc);

		pc = new PercentileContraint(0.3, 0.0, 1400);
		sla.addPercentilConstraint(pc);

		// Execption provozieren
		try {
			pc = new PercentileContraint(0.6, 0.5, 2000000);
			sla.addPercentilConstraint(pc);
		} catch (PercentileConstraintOverlapException p) {
			System.out.println("Richtig erkannt :-) " + p);
		}

		try {
			System.out.println("1 --> " + sla.getPercentilConstraint(1));
		} catch (NotInitializedException e) {
			System.out.println("Richtig erkannt :-) " + e);
		}

		try {
			sla.init();
		} catch (IllegalServiceLevelDefinition e) {
			System.out.println("Richtig erkannt :-) " + e);

		}
		pc = new PercentileContraint(0.9, 0.8, 100);
		sla.addPercentilConstraint(pc);

		System.out.println(sla);
		
		sla.init();
		
		System.out.println(sla);

		System.out.println("MaxPenalty " + sla.getMaxPenalty());

		System.out.println("i;oc;mg;penalty;slapenalty");
		for (double i=1;i>=0;i=Math.round((i-0.001)*1000.0)/1000.0){
			double oc = sla.oc(i);
			double mg = sla.mg(i);
			System.out.println((int)(i*1000)+";" + Math.round(oc*100)/100+";"+  Math.round(mg*100)/100+";"+Math.round(Math.max(oc,mg)*100)/100+";"+(int)sla.getPercentilConstraint(i).getPenalty());
		}

		

	}
}

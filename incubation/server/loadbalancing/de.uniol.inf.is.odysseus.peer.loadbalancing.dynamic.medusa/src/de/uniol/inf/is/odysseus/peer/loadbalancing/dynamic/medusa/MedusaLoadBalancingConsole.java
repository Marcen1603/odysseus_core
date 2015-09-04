package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa.datamodel.ContractRegistry;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa.datamodel.MedusaPricingContract;

public class MedusaLoadBalancingConsole implements CommandProvider {
	
	private static IPeerDictionary peerDictionary;
	
	public static void bindPeerDictionary(IPeerDictionary serv) {
		peerDictionary = serv;
	}
	
	public static void unbindPeerDictionary(IPeerDictionary serv) {
		if(peerDictionary==serv) {
			peerDictionary = null;
		}
	}

	@Override
	public String getHelp() {
		StringBuilder sb = new StringBuilder();
		sb.append("Load Balancing: Medusa Strategy specific commands.\n");
		sb.append("medusaGetMaxLoad                -  Shows maximum Load Value this computer can achieve\n");
		sb.append("medusaGetMaxCosts               -  Shows maximum Price this computer will take for one load unit\n");
		sb.append("medusaGetCurrentLoad            -  Shows current Load according to Medusa Strategy\n");
		sb.append("medusaGetCurrentCosts           - Shows current Costs according to Medusa Strategy\n");
		sb.append("medusaShowCosts <percentage>    - Shows costs for particular percentage.\n");
		sb.append("medusaShowCostDifference <percentage1> <percentage2> - shows Difference in Costs between two points\n");
		sb.append("medusaMinPrice                    - Shows minimum Price given to other Peers\n");
		sb.append("medusaMaxPrice                    - Shows maximum Price given to other Peers\n");
		
		return sb.toString();
	}
	
	public static void _medusaGetMaxLoad(CommandInterpreter ci) {
		ci.println(PriceCalculator.getMaximumLoad());
	}
	

	public static void _medusaGetMaxCosts(CommandInterpreter ci) {
		ci.println(PriceCalculator.getMaximumCosts());
	}

	public static void _medusaGetCurrentCosts(CommandInterpreter ci) {
		ci.println(PriceCalculator.getCurrentCosts());
	}
	
	public static void _medusaGetCurrentLoad(CommandInterpreter ci) {
		ci.println(PriceCalculator.getCurrentLoad());
	}
	
	public static void _medusaShowCosts(CommandInterpreter ci) {
		
		final String ERROR_USAGE = "Usage: medusaShowCosts <percentage> ";
		
		String percentageString = ci.nextArgument();
		if (Strings.isNullOrEmpty(percentageString)) {
			ci.println(ERROR_USAGE);
			return;
		}

		double percentage;

		try {
			percentage = Double.parseDouble(percentageString);
		} catch (NumberFormatException e) {
			ci.println(ERROR_USAGE);
			return;
		}
		
		ci.println(PriceCalculator.calculateLocalCosts(percentage));
		
	}
	

	public static void _medusaShowCostDifference(CommandInterpreter ci) {
		
		final String ERROR_USAGE = "Usage: medusaShowCostDifference <percentage1> <percentage2> ";
		
		String percentageString = ci.nextArgument();
		if (Strings.isNullOrEmpty(percentageString)) {
			ci.println(ERROR_USAGE);
			return;
		}

		double percentage1;

		try {
			percentage1 = Double.parseDouble(percentageString);
		} catch (NumberFormatException e) {
			ci.println(ERROR_USAGE);
			return;
		}
		

		percentageString = ci.nextArgument();
		if (Strings.isNullOrEmpty(percentageString)) {
			ci.println(ERROR_USAGE);
			return;
		}

		double percentage2;

		try {
			percentage2 = Double.parseDouble(percentageString);
		} catch (NumberFormatException e) {
			ci.println(ERROR_USAGE);
			return;
		}

		ci.println(PriceCalculator.getCostDifference(percentage1, percentage2));
		
	}
	
	public static void _medusaMinPrice(CommandInterpreter ci) {
		ci.println(PriceCalculator.getMinPrice());
	}
	
	public static void _medusaMaxPrice(CommandInterpreter ci) {
		ci.println(PriceCalculator.getMaxPrice());
	}
	
	public static void _showContracts(CommandInterpreter ci) {
		for(MedusaPricingContract contract : ContractRegistry.getAllContracts()) {
			String peerName = peerDictionary.getRemotePeerName(contract.getContractPartner());
			StringBuilder sb = new StringBuilder();
			sb.append("Contract with ");
			sb.append(peerName);
			sb.append(" MIN_PRICE:");
			sb.append(contract.getMinPrice());
			sb.append(" MAX_PRICE:");
			sb.append(contract.getMaxPrice());
			ci.println(sb.toString());
		}
		
	}
	
}

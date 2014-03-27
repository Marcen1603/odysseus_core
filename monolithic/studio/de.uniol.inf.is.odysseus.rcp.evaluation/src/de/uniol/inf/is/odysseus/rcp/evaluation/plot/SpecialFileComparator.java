package de.uniol.inf.is.odysseus.rcp.evaluation.plot;

import java.io.File;
import java.util.Comparator;

public class SpecialFileComparator implements Comparator<File> {

	@Override
	public int compare(File left, File right) {
		String l = left.getName();
		String r = right.getName();
		if (l.contains("=") && r.contains("=")) {
			String[] splitsL = l.split("=");
			String[] splitsR = r.split("=");
			if (splitsL[0].equals(splitsR[0])) {
				if (splitsL[1].equals(splitsR[1])) {
					return 0;
				}
				
				try {
					Integer leftInt = Integer.parseInt(splitsL[1].trim());
					Integer rightInt = Integer.parseInt(splitsR[1].trim());
					return leftInt.compareTo(rightInt);
				} catch (NumberFormatException e) {
					return splitsL[1].compareTo(splitsR[1]);
				}
			}
			
			return splitsL[0].compareTo(splitsR[0]);
		}
		return 0;
	}
}

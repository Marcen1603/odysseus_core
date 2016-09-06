package de.uniol.inf.is.odysseus.iql.odl.types;

import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.SDFElement;

public class ODLUtils {
	public static List<String> splitURL(String toSplit) {
		return Arrays.asList(SDFElement.splitURI(toSplit));
	}
}

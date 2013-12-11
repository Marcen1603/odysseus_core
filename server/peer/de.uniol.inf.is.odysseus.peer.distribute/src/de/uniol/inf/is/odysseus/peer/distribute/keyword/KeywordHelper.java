package de.uniol.inf.is.odysseus.peer.distribute.keyword;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

final class KeywordHelper {

	private KeywordHelper() {
	}

	public static List<String> generateParameterList(String[] splitted) {
		Preconditions.checkNotNull(splitted, "Splitted list of parameters must not be null!");

		if (splitted.length == 1) {
			return Lists.newArrayList();
		}

		List<String> parameters = Lists.newArrayList();
		for (int i = 1; i < splitted.length; i++) {
			parameters.add(splitted[i]);
		}

		return parameters;
	}
}

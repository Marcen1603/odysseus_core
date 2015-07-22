package de.uniol.inf.is.odysseus.parallelization.interoperator.constants;

public class InterOperatorParallelizationConstants {
	private static final String PATTERN_PAIRS = "((([a-zA-Z0-9_]+)|([(][a-zA-Z0-9_]+([:][a-zA-Z0-9_]+)+[)]))[,])"
			+ "*(([a-zA-Z0-9_]+)|([(][a-zA-Z0-9_]+([:][a-zA-Z0-9_]+)+[)]))";
	private static final String PATTERN_WITH_ATTRIBUTESNAMES = "([(][a-zA-Z0-9_]+[=]"
			+ PATTERN_PAIRS
			+ "[)])([\\s][(][a-zA-Z0-9_]+[=]"
			+ PATTERN_PAIRS
			+ "[)])*";
	private static final String PATTERN_WITHOUT_ATTRIBUTENAMES = "("
			+ PATTERN_PAIRS + ")([\\s]" + PATTERN_PAIRS + ")*";
	public static final String PATTERN_KEYWORD = PATTERN_WITH_ATTRIBUTESNAMES
			+ "|" + PATTERN_WITHOUT_ATTRIBUTENAMES;
	
	public static final int AUTO_BUFFER_SIZE = 10000000;
}

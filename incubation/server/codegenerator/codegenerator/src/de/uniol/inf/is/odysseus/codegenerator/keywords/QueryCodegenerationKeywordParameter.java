package de.uniol.inf.is.odysseus.codegenerator.keywords;

import de.uniol.inf.is.odysseus.script.parser.parameter.IKeywordParameter;

/**
 * Defines the parameters for the #CODENERATION keyword. This parameter is used
 * in parameter helper for parsing the keyword parameters. Also supports the
 * definition of custom patterns, needed in Parameterhelper
 * 
 * @author MarcPreuschaft
 *
 */
public enum QueryCodegenerationKeywordParameter implements IKeywordParameter {
	TARGETPLATFROM("targetPlatform", 0, false), TARGETDIRECTORY(
			"targetDirectory", 1, false), ODYSSEUSPATH("odysseusDirectory", 2,
			false), SCHEDULER("scheduler", 3, false), OPTIONS("options", 4, true), QUERYNAME(
			"queryname", 5, true);

	private String name;
	private int position;
	private boolean isOptional;

	private QueryCodegenerationKeywordParameter(String name, int position,
			boolean isOptional) {
		this.name = name;
		this.position = position;
		this.isOptional = isOptional;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getPosition() {
		return position;
	}

	@Override
	public boolean isOptional() {
		return isOptional;
	}

}

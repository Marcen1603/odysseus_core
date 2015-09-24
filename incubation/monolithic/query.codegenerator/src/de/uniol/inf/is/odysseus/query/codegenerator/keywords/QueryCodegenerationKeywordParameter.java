package de.uniol.inf.is.odysseus.query.codegenerator.keywords;

import de.uniol.inf.is.odysseus.script.parser.parameter.IKeywordParameter;


public enum QueryCodegenerationKeywordParameter implements
IKeywordParameter {
TARGETPLATFROM("targetPlatform", 0, false), 
TARGETDIRECTORY("targetDirectory", 1, false), 
ODYSSEUSPATH("odysseusDirectory", 2, false),
EXECUTOR("executor", 3, false);


private String name;
private int position;
private boolean isOptional;

private QueryCodegenerationKeywordParameter(String name,
	int position, boolean isOptional) {
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

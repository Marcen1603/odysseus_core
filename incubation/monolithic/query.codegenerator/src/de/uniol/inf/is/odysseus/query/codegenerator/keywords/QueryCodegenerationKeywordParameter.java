package de.uniol.inf.is.odysseus.query.codegenerator.keywords;

import de.uniol.inf.is.odysseus.script.parser.parameter.IKeywordParameter;


public enum QueryCodegenerationKeywordParameter implements
IKeywordParameter {
TARGETPLATFROM("targetPlatform", 0, false), 
TEMPDIRECTORY("tempDirectory", 1, false), 
DESTINATIONDIRECTORY("destinationDirectory", 2, false), 
ODYSSEUSPATH("odysseusDirectory", 3, false),
EXECUTOR("executor", 4, false);




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

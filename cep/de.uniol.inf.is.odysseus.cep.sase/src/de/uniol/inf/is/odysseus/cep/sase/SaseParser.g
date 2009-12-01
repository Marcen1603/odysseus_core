parser grammar SaseParser;

options { 
	output = AST;
	tokenVocab = SaseLexer;
}

tokens{
	NOTSTATE;
	STATE;
	KSTATE;
	NOTKSTATE;
}

	
@header { 
	package de.uniol.inf.is.odysseus.cep.sase; 
	import java.util.Map;
	import java.util.TreeMap;
	import java.util.List;
	import java.util.ArrayList;
}

@members{
	private Map<String, String> variableMap = new TreeMap<String, String>();
	private List<String> kleeneAttributes = new ArrayList<String>();
	private List<String> states = new ArrayList<String>();
	private Map<String, String> stateVariables = new TreeMap<String, String>();
	
	private String getVariableType(CommonTree nameNode){
		String name = nameNode.getText();
		String type = variableMap.get(name);
		if (type == null){
			String msg = "The variable \""+name+"\" is not declared.";
			throw new RuntimeException(msg);
		}
		return type;
	}
	
	private void addState(String name, String type, boolean isKleeneAttribute){
		if (variableMap.get(name) != null){
			String msg = "The variable \""+name+"\" is already declared.";
			throw new RuntimeException(msg);	
		}
		variableMap.put(name, type);
		if (isKleeneAttribute){
			states.add(name+"[1]");
			stateVariables.put(name+"[1]", name);
			states.add(name+"[i]");
			stateVariables.put(name+"[i]", name);
			kleeneAttributes.add(name);
		}else{
			states.add(name);
			stateVariables.put(name, name);			
		}
	}
	
	public Map<String, String> getVariables(){
		return variableMap;
	}
	public List<String> getKleeneAttributes(){
		return kleeneAttributes;
	}
	public List<String> getStates(){
		return states;
	}
	public Map<String, String> getStateVariables(){
		return stateVariables;
	}

}

query
	:  patternPart (wherePart)? (withinPart)?
	;
	
withinPart
	: WITHIN INTEGER TIMEUNIT
	;
	
wherePart
	: WHERE whereDecl -> ^(WHERE whereDecl)
	;
	
patternPart 
	: PATTERN patternDecl -> ^(PATTERN patternDecl)
	;
	
	
patternDecl
	:	SEQ LBRACKET pItem (COMMA pItem)* RBRACKET -> ^(SEQ pItem*)
	;
		
	
pItem 	:	 (singlePItem | kleenePITem | notSinglePItem | notKleenePITem)
	;

notSinglePItem
	:	NOT LBRACKET TYPE NAME RBRACKET {addState($NAME.text,$TYPE.text, false);} -> ^(NOTSTATE TYPE NAME) 
	;

	
singlePItem
	:	TYPE NAME {addState($NAME.text,$TYPE.text, false);} -> ^(STATE TYPE NAME) 
	;

kleenePITem
	:	TYPE PLUS NAME KLEENEBRACKET {addState($NAME.text,$TYPE.text, true);} -> ^(KSTATE TYPE NAME) 
	;
	
notKleenePITem
	:	NOT LBRACKET TYPE PLUS NAME KLEENEBRACKET  RBRACKET {addState($NAME.text,$TYPE.text, true);} -> ^(NOTKSTATE TYPE NAME) 
	;
	
whereDecl
	:	SKIP_METHOD LBRACKET (NAME KLEENEBRACKET?(COMMA NAME KLEENEBRACKET?)*) RBRACKET ;

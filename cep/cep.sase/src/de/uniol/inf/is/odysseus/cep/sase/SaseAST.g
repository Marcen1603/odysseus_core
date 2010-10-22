tree grammar SaseAST;

options {
  tokenVocab   = SaseParser;
  ASTLabelType = CommonTree;
  language     = Java;
}

@header {
	package de.uniol.inf.is.odysseus.cep.sase; 
	import java.util.LinkedList;
	import java.util.Map;
	import java.util.HashMap;
	import java.util.Iterator;
	
	import de.uniol.inf.is.odysseus.datadictionary.DataDictionary;
	import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
	import de.uniol.inf.is.odysseus.cep.CepAO;
	import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
	import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
	import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
	import de.uniol.inf.is.odysseus.cep.metamodel.CepVariable;
	import de.uniol.inf.is.odysseus.cep.metamodel.EAction;
	import de.uniol.inf.is.odysseus.cep.metamodel.EEventSelectionStrategy;
	import de.uniol.inf.is.odysseus.cep.metamodel.OutputScheme;
	import de.uniol.inf.is.odysseus.cep.metamodel.State;
	import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;
	import de.uniol.inf.is.odysseus.cep.metamodel.Transition;
	import de.uniol.inf.is.odysseus.cep.epa.metamodel.relational.RelationalMEPCondition;
	import de.uniol.inf.is.odysseus.cep.epa.metamodel.relational.RelationalMEPOutputSchemeEntry;
	import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.Write;
	import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.ISymbolTableOperationFactory;
	import de.uniol.inf.is.odysseus.usermanagement.User;
	
	import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;
}

@members {

  static Logger _logger = null;
  static Logger getLogger(){
    if (_logger == null){
      _logger = LoggerFactory.getLogger(SaseAST.class);
    }
    return _logger;
  } 

	List<String> simpleState = null;
	List<String> kleeneState = null;
	Map<String, String> simpleAttributeState = null;
	Map<String, String> kleeneAttributeState = null;
	ISymbolTableOperationFactory symTableOpFac = null;
	User user = null;
	
	
  public void setUser(User user){
    this.user = user;
  }
  private String transformAttribute(PathAttribute attrib,State s){
      String index = ""; // getKleenePart.equals("[i]")
      String aggregation = attrib.getAggregation();
      String stateIdentifier = attrib.getAttribute();
      String realAttributeName = attrib.getRealAttributeName();

      // UNterschiedliche Fälle
      // 1. Das Attribut gehört zum aktuellen Zustand und ist kein
      // KleeneAttribut --> aktuell
      // 2. Das Attribut gehört zum aktuellen Zustand und ist ein
      // Kleenattribut --> historisch
      // 2.a mit dem Kleenepart identisch zum Zustand --> aktuell
      // 2.b. mit einem anderen Kleenpart --> historisch

      if (attrib.getStatename().equals(s.getId())) {
        if (!attrib.isKleeneAttribute()) {
          aggregation = "";
          stateIdentifier = "";
          index = "";
        } else {
          if (s.getId().endsWith(attrib.getKleenePart())) {
            aggregation = "";
            stateIdentifier = "";
            index = "";
          }
        }
      } else {
        if (attrib.isKleeneAttribute()) {
          if (attrib.getKleenePart().equals("[1]")) {
            index = "0";
          } else if (attrib.getKleenePart().equals("[-1]")) {
            index = "-1";
          } else if (attrib.getKleenePart().equals("[i]")) {
            index = "";
          }
        }
      }
      
     return aggregation
          + CepVariable.getSeperator() + stateIdentifier
          + CepVariable.getSeperator() + index
          + CepVariable.getSeperator() + realAttributeName;
  }

	 private String tranformAttributesToMetamodell(AttributeExpression ce, State s) {
    // In die Syntax des Metamodells umwandeln
    String ret = new String(ce.getFullExpression());
    for (PathAttribute attrib : ce.getAttributes()) {
      String fullAttribName = attrib.getFullAttributeName();
      String convertedAttributeName = transformAttribute(attrib,s);
      ret = replaceStrings(ret, fullAttribName, convertedAttributeName);
    }

    return ret;
  }

  private String replaceStrings(String ret, String oldString, String newString) {
//     getLogger().debug("replace " + oldString + " in " + ret + " with "
//     + newString);
    ret = ret.replace(oldString, newString);
//     getLogger().debug("--> " + ret);
    return ret;
  }
	
       private long getTime(String value, CommonTree unit){
          
          long time = Long.parseLong(value);
          if (unit != null){ 
          int uType = unit.getToken().getType();
            if (uType == WEEK){
              time *= 7*24*60*60*1000; 
            }else if (uType == DAY){
              time *=24*60*60*1000;
            }else if (uType == HOUR){
              time *=60*60*1000;
            }else if (uType == MINUTE){
              time *=60*1000;
            }else if (uType == SECOND){
              time *=1000;
            }
          }
          return time;    
      }
      
      private Transition getTransition(PathAttribute attr, State s){
           Transition t = null;
           if (attr != null) {
             t = s.getTransition(s.getId() + "_begin");
              if (t == null && attr.isKleeneAttribute()) {
                if (attr.getKleenePart().equals("[i]")) {
                 t = s.getTransition(s.getId() + "_take");
                } else {
                  t = s.getTransition(s.getId() + "_proceed");
                }
              }
           }
           return t;
      }

}

@rulecatch{
catch(RecognitionException e){
  throw e;
}
}
start returns [ILogicalOperator op]
  :
  ^(CREATEVIEW n=NAME q=query) // Create a new Logical View
  {	DataDictionary.getInstance().setLogicalView(n.getText(), q, user);
	  getLogger().debug("Created New View "+n+" "+q);
	  $op = q;}
  | o=query {$op = o;} // Only Query
  ;

query returns [ILogicalOperator op]
@init {
    CepAO cepAo = new CepAO();
    simpleState = new ArrayList<String>();
    kleeneState = new ArrayList<String>();
    simpleAttributeState = new HashMap<String, String>();
    kleeneAttributeState = new HashMap<String, String>();
    List<String> sourceNames = new ArrayList<String>();
}
  :
  ^(QUERY patternPart[cepAo, sourceNames] wherePart[cepAo] withinPart[cepAo] returnPart[cepAo])
  {
    // Initialize Schema
    cepAo.getStateMachine().getSymTabScheme(true);
    getLogger().debug("Created State Machine "+cepAo.getStateMachine());
    // Set Inputs for cepAO;
    int port = 0;
    for (String sn : sourceNames) {
      getLogger().debug("Bind "+sn+" to Port "+port);      
      ILogicalOperator ao = DataDictionary.getInstance().getView(sn, user);
      if (ao != null) {
        cepAo.subscribeToSource(ao, port, 0, ao.getOutputSchema());
        cepAo.setInputTypeName(port, sn);
        port++;
      } else {
        throw new RuntimeException("Source " + sn + " not found");
      }
    }
    $op = cepAo;
    
   }
  ;

patternPart[CepAO cepAo, List<String> sourceNames]
@init {
List<State> states = new LinkedList<State>();
}
  :
  ^(SEQ state[states, sourceNames]*)
  {
	  if (states.size() > 0) {
      states.add(new State("<ACCEPTING>", "", null, true));
      StateMachine sm = cepAo.getStateMachine();
      sm.setStates(states);
      sm.setInitialState(states.get(0).getId());
      // Calculate transistions
      for (int i = 0; i < states.size() - 1; i++) {
         State source = states.get(i);
         State dest = states.get(i + 1);
         if (source.getId().endsWith("[i]")) {
           RelationalMEPCondition con = new RelationalMEPCondition("");
           con.setEventType(source.getType());
           con.setEventPort(sourceNames.indexOf(source.getType()));
           // TODO: Ist das mit dem EAction.discard immer richtig?
           source.addTransition(new Transition(source.getId()+ "_proceed", dest, con, EAction.discard));
           con = new RelationalMEPCondition("");
           con.setEventType(source.getType());
           con.setEventPort(sourceNames.indexOf(source.getType()));
           source.addTransition(new Transition(source.getId()+ "_take", source,con, EAction.consumeBufferWrite));
         } else {
            RelationalMEPCondition con = new RelationalMEPCondition("");
            con.setEventType(source.getType());
            con.setEventPort(sourceNames.indexOf(source.getType()));
            source.addTransition(new Transition(source.getId()+ "_begin", dest, con,EAction.consumeBufferWrite));
         }
         if (i > 0 && i < states.size() - 1) {
            RelationalMEPCondition con = new RelationalMEPCondition("");
            con.setEventType(source.getType());
            con.setEventPort(sourceNames.indexOf(source.getType()));
            // Ignore auf sich selbst!
            source.addTransition(new Transition(source.getId()+ "_ignore", source, con,EAction.discard));
          }
       }
    } else {
        throw new RuntimeException("Illegal Sequence");
    }
  }
  ;

state[List<State> states, List<String> sourceNames]
  :
  ^(STATE statename=NAME attrName=NAME not=NOTSIGN?)
  {
		  if (not != null) throw new RuntimeException("Negative states not supported now!");
		  String _statename = statename.getText();
		  if (!sourceNames.contains(_statename)){
		    sourceNames.add(_statename);
		  }
		  String _attributeName = attrName.getText();
		//	getLogger().debug("Einfacher Zustand "+_statename+" "+_attributeName);
			// Anm. _statename darf mehrfach vorkommen  Variablenname nicht
			simpleState.add(_statename);
			if (simpleAttributeState.get(_attributeName) == null){
				simpleAttributeState.put(_attributeName,_statename);
				states.add(new State(_attributeName,_attributeName, _statename, false));
			}else{
        throw new RuntimeException("Double attribute definition "+_attributeName); 
			}
		}
  |
  ^(KSTATE statename=NAME attrName=NAME not=NOTSIGN?)
  {
		  if (not != null) throw new RuntimeException("Negative states not supported now!");
		  String _statename = statename.getText();
      String _attributeName = attrName.getText();
		 // getLogger().debug("Kleene Zustand "+_statename+" "+_attributeName);
		  kleeneState.add(_statename);
		  sourceNames.add(_statename);
		  if (kleeneAttributeState.get(_attributeName) == null){
		    kleeneAttributeState.put(_attributeName, _statename);
		    states.add(new State(_attributeName + "[1]", _attributeName, _statename, false));
		    states.add(new State(_attributeName + "[i]", _attributeName, _statename, false));  
		  }else{
		    throw new RuntimeException("Double attribute definition "+_attributeName); 
		  }
		 }
  ;

wherePart[CepAO cepAo]
  :
  ^(WHERE str=whereStrat[cepAo.getStateMachine()] we=whereExpression[cepAo.getStateMachine()])
  |
  ;

whereStrat[StateMachine sm]
  :
  ^(SKIP_TILL_NEXT_MATCH param=paramlist)
  {sm.setEventSelectionStrategy(EEventSelectionStrategy.SKIP_TILL_NEXT_MATCH);}
  |
  ^(SKIP_TILL_ANY_MATCH param=paramlist)
  {sm.setEventSelectionStrategy(EEventSelectionStrategy.SKIP_TILL_ANY_MATCH);}
  |
  ^(STRICT_CONTIGUITY paramlist)
  {sm.setEventSelectionStrategy(EEventSelectionStrategy.PARTITION_CONTIGUITY);}
  |
  ^(PARTITION_CONTIGUITY paramlist)
  {sm.setEventSelectionStrategy(EEventSelectionStrategy.STRICT_CONTIGUITY);}
  ;

paramlist
  :
  ^(PARAMLIST attributeName*)
  {System.err.println("WARNING: Attributes currently ignored in selection strategy");}
  ;

attributeName
  :
  ^(KATTRIBUTE NAME)
  |
  ^(ATTRIBUTE NAME)
  ;

whereExpression[StateMachine stmachine]
@init {
  List<AttributeExpression> compareExpressions = new ArrayList<AttributeExpression>();
  List assignExpressions = new ArrayList();
}
  :
  ^(
    WHEREEXPRESSION
    (
      compareExpression[stmachine,compareExpressions]
      | idexpression[stmachine.getStates(),compareExpressions]
      | assignment[stmachine, assignExpressions]
    )*
   )
  {
    List<State> finalStates = stmachine.getFinalStates();
    for (State finalState : finalStates) {
      List<State> pathes = stmachine.getAllPathesToStart(finalState);
      for (State s : pathes) {
        Iterator<AttributeExpression> compareIter = compareExpressions
            .iterator();
        while (compareIter.hasNext()) {
          AttributeExpression ce = compareIter.next();
          PathAttribute attr = ce.get(s.getId());
          if (attr != null) {
            Transition t = getTransition(attr, s);
            // Anpassen der Variablennamen für das Metamodell:
            String fullExpression = tranformAttributesToMetamodell(ce, s);
            t.appendAND(fullExpression);
            compareIter.remove();
          }
        }// while
        Iterator<AssignExpression> assignIter = assignExpressions.iterator();
        while(assignIter.hasNext()){
          AssignExpression ae = assignIter.next();
          PathAttribute attr = ae.get(s.getId());
          if (attr != null){
            Transition t = getTransition(attr, s);
            // Anpassen der Variablennamen für das Metamodell:
            String fullExpression = tranformAttributesToMetamodell(ae, s);
            t.addAssigment(transformAttribute(ae.getAttributToAssign(),s), fullExpression);
            assignIter.remove();
          }
        }
      }
    }
    
    List<State> states = stmachine.getStates();
    for (State s : states) {
        Transition ignore = s.getTransition(s.getId() + "_ignore");
        if (ignore != null) {
          switch (stmachine.getEventSelectionStrategy()){
            case SKIP_TILL_NEXT_MATCH:
              ignore.getCondition().setEventTypeChecking(true);
	            Transition t = s.getTransition(s.getId() + "_take");
	            if (t == null){
	              t = s.getTransition(s.getId() + "_begin");
	            }
	           ignore.appendAND(t.getCondition().getLabel());
	           ignore.negateExpression();
	           break;
	          case STRICT_CONTIGUITY:
	              ignore.getCondition().setEventTypeChecking(false);
                ignore.appendAND("false");
                break;  
            case PARTITION_CONTIGUITY:
                throw new RuntimeException("PARTITION_CONTIGUITY not implemented yet");
                //break;
            case SKIP_TILL_ANY_MATCH:      
              ignore.getCondition().setEventTypeChecking(false);          
            default:
              ignore.getCondition().setEventTypeChecking(false);
	        }
	    
      }
    }
     
       
  }
  ;

compareExpression[StateMachine sm,List<AttributeExpression> compareExpressions]
@init {
		 StringBuffer left= new StringBuffer();
     StringBuffer right=new StringBuffer();
     List<PathAttribute> attribs = new ArrayList<PathAttribute>();
}
  :
  ^(
    COMPAREEXPRESSION mathExpression[left, attribs] op=
    (
      COMPAREOP
      | SINGLEEQUALS
    )
    mathExpression[right, attribs]
   )
  {StringBuffer ret = new StringBuffer(left);
   if (op.getToken().getType() == SINGLEEQUALS){
      ret.append("==");
   }else{
      ret.append($op);
   }
   ret.append(right);
   compareExpressions.add(new AttributeExpression(attribs, ret.toString()));}
  ;

assignment[StateMachine sm,List assignExpressions]
@init{
  List<PathAttribute> singleAttrib = new ArrayList<PathAttribute>();
  List<PathAttribute> attribs = new ArrayList<PathAttribute>();
  StringBuffer right=new StringBuffer();
}:
^(ASSIGNMENT attributeTerm[singleAttrib] mathExpression[right,attribs]){
    assignExpressions.add(new AssignExpression(singleAttrib.get(0),attribs,right.toString()));
};

mathExpression[StringBuffer exp, List<PathAttribute> attribs]
@init {
      StringBuffer left=new StringBuffer();
      StringBuffer right=new StringBuffer();
      List<PathAttribute> tmpAttrib = new ArrayList<PathAttribute>();
}
  :
  ^(
    (
      op=PLUS
      | op=MINUS
      | op=MULT
      | op=DIVISION
    )
    mathExpression[left, attribs] mathExpression[right, attribs]
   )
  {exp.append("(").append(left).append(" ").append(op).append(right).append(")");}
  | attributeTerm[tmpAttrib] {
    // TODO: Es darf nur ein Attribut drin sein!
    PathAttribute p = tmpAttrib.get(0);
    attribs.add(p);
    exp.append(p);
  }
  | num=NUMBER {exp.append(num.getText());}
  | lit=STRING_LITERAL {exp.append(lit.getText());}
  ;

attributeTerm[List<PathAttribute> attribs]
@init {
      StringBuffer usage = new StringBuffer();
      StringBuffer name = new StringBuffer();
}
  :
  ^(KMEMBER kAttributeUsage[name,usage] member=NAME)
  {
      PathAttribute p = new PathAttribute(name.toString(),usage.toString(),member.getText(),Write.class.getSimpleName().toUpperCase());
      attribs.add(p);
      name = new StringBuffer(); 
      usage = new StringBuffer();
     }
  |
  ^(MEMBER attribName=NAME member=NAME)
  {
     PathAttribute p = new PathAttribute(attribName.getText(),null,member.getText(),Write.class.getSimpleName().toUpperCase()); 
      attribs.add(p);}
  |
  ^(
    AGGREGATION op=
    (
      MIN
      | MAX
      | SUM
      | COUNT
      | AVG
    )
    var=NAME
    (member=NAME)?
   )
  { 
    PathAttribute p = new PathAttribute(var.getText(),"[i-1]",member==null?"":""+member,
        symTableOpFac.getOperation(op.getText()).getName());
    attribs.add(p);
   }
  ;

kAttributeUsage[StringBuffer name, StringBuffer usage]
  :
  ^(NAME CURRENT)
  {usage.append("[i]"); name.append($NAME);}
  |
  ^(NAME FIRST)
  {usage.append("[1]");name.append($NAME);}
  |
  ^(NAME PREV)
  {usage.append("[i-1]");name.append($NAME);}
  |
  ^(NAME LEN)
  {usage.append("[i-1]");name.append($NAME);}
  ;

idexpression[List<State> states, List<AttributeExpression> compareExpressions]
  :
  ^(IDEXPRESSION var=NAME)
  {
          String t=CepVariable.getSeperator();
          for (int i = 0; i < states.size() - 2; i++) {
            List<PathAttribute> attr = new ArrayList<PathAttribute>();
            PathAttribute a = new PathAttribute(states.get(i).getId(),var.getText());
            attr.add(a);
            PathAttribute b = new PathAttribute(states.get(i + 1).getId(), var.getText());
            attr.add(b);
            StringBuffer expr = new StringBuffer();
            expr.append(t).append(t).append(t).append(states.get(i).getId()).append(".").append(var.getText());
            expr.append(t).append(t).append(t).append(states.get(i+1).getId()).append(".").append(var.getText());
            compareExpressions.add(new AttributeExpression(attr,expr.toString()));
        }
  }
  ;

withinPart[CepAO cepAo]
  :
  ^(
    WITHIN value=NUMBER
    (
      unit=
      (
        WEEK
        | DAY
        | HOUR
        | MINUTE
        | SECOND
        | MILLISECOND
      )
    )?
   )
  {
    Long time = getTime(value.getText(),unit);
    getLogger().debug("Setting Windowsize to "+time+" milliseconds");
    cepAo.getStateMachine().setWindowSize(time);
 }
  |
  ;

returnPart[CepAO cepAo]
@init {
List<PathAttribute> retAttr = new ArrayList<PathAttribute>();
}
  :
  ^(RETURN attributeTerm[retAttr]*)
  {
    RelationalMEPOutputSchemeEntry e = null;
    OutputScheme scheme = new OutputScheme();
    SDFAttributeList attrList = new SDFAttributeList();
    for (PathAttribute p : retAttr) {
      String op = p.getAggregation();
      String a = p.getStatename();
      String i = p.getKleenePart();
      if ("[i]".equals(i)){
        i = "";
      }else if ("[i-1]".equals(i)){
        i = "-1";
      }
      String path = p.getPath();
      e = new RelationalMEPOutputSchemeEntry(CepVariable.getStringFor(op, a, i, a
          + "." + path));
      scheme.append(e);
      SDFAttribute attr = new SDFAttribute(e.getLabel());
      // TODO: Set correct Datatypes
      attr.setDatatype(new SDFDatatype("String"));
      attrList.add(attr);
    }
    cepAo.getStateMachine().setOutputScheme(scheme);    
    cepAo.setOutputSchema(attrList);
  }
  |
  ;

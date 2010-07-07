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
	
	import de.uniol.inf.is.odysseus.base.DataDictionary;
	import de.uniol.inf.is.odysseus.base.ILogicalOperator;
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
	import de.uniol.inf.is.odysseus.cep.epa.metamodel.relational.RelationalJEPCondition;
	import de.uniol.inf.is.odysseus.cep.epa.metamodel.relational.RelationalJEPOutputSchemeEntry;
	import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.Write;
	import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.ISymbolTableOperationFactory;
	
	import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;
}

@members {

  Logger _logger = null;
  Logger getLogger(){
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
	
	// TODO: Hierfuer eine bessere Loesung finden
	 private String tranformAttributesToMetamodell(CompareExpression ce, State s) {
    // In die Syntax des Metamodells umwandeln
    String ret = new String(ce.getFullExpression());
    for (PathAttribute attrib : ce.getAttributes()) {
      String index = ""; // getKleenePart.equals("[i]")
      String fullAttribName = attrib.getFullAttributeName();
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

      ret = replaceStrings(ret, fullAttribName, aggregation
          + CepVariable.getSeperator() + stateIdentifier
          + CepVariable.getSeperator() + index
          + CepVariable.getSeperator() + realAttributeName);
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

}

start returns [ILogicalOperator op]
  :
  ^(CREATEVIEW n=NAME q=query) // Create a new Logical View
  {	DataDictionary.getInstance().setLogicalView(n.getText(), q);
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
      ILogicalOperator ao = DataDictionary.getInstance().getView(sn);
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
           RelationalJEPCondition con = new RelationalJEPCondition("");
           con.setEventType(source.getType());
           con.setEventPort(sourceNames.indexOf(source.getType()));
           source.addTransition(new Transition(source.getId()+ "_proceed", dest, con, EAction.consumeNoBufferWrite));
           con = new RelationalJEPCondition("");
           con.setEventType(source.getType());
           con.setEventPort(sourceNames.indexOf(source.getType()));
           source.addTransition(new Transition(source.getId()+ "_take", source,con, EAction.consumeBufferWrite));
         } else {
            RelationalJEPCondition con = new RelationalJEPCondition("");
            con.setEventType(source.getType());
            con.setEventPort(sourceNames.indexOf(source.getType()));
            source.addTransition(new Transition(source.getId()+ "_begin", dest, con,EAction.consumeBufferWrite));
         }
         if (i > 0 && i < states.size() - 1) {
            RelationalJEPCondition con = new RelationalJEPCondition("");
            // Achtung! Ignore hat keinen Typ!
            //con.setEventType(source.getType());
            // con.con.setEventPort(sourceNames.indexOf(source.getType()));
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
		  sourceNames.add(_statename);
		  String _attributeName = attrName.getText();
		//	getLogger().debug("Einfacher Zustand "+_statename+" "+_attributeName);
			// Anm. _statename darf mehrfach vorkommen  Variablenname nicht
			simpleState.add(_statename);
			if (simpleAttributeState.get(_attributeName) == null){
				simpleAttributeState.put(_attributeName,_statename);
				states.add(new State(_attributeName, _attributeName, _statename, false));
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
  List<CompareExpression> compareExpressions = new ArrayList<CompareExpression>();
}
  :
  ^(
    WHEREEXPRESSION
    (
      compareExpression[stmachine,compareExpressions]
      | idexpression[stmachine.getStates(),compareExpressions]
    )*
   )
  {
     //getLogger().debug("Compare Expressions");
//     for (CompareExpression expr:compareExpressions){
//       getLogger().debug(expr);
//     }
    List<State> finalStates = stmachine.getFinalStates();
    for (State finalState : finalStates) {
      List<State> pathes = stmachine.getAllPathesToStart(finalState);
      for (State s : pathes) {
        Iterator<CompareExpression> compareIter = compareExpressions
            .iterator();
        while (compareIter.hasNext()) {
          CompareExpression ce = compareIter.next();
          //getLogger().debug(ce);
          PathAttribute attr = ce.get(s.getId());
          if (attr != null) {
            Transition t = s.getTransition(s.getId() + "_begin");

            if (t == null && attr.isKleeneAttribute()) {
              if (attr.getKleenePart().equals("i")) {
                t = s.getTransition(s.getId() + "_take");
              } else {
                t = s.getTransition(s.getId() + "_proceed");
              }
            }
            // Anpassen der Variablennamen für das Metamodell:
            String fullExpression = tranformAttributesToMetamodell(
                ce, s);
            //String fullExpression = ce.getFullExpression();
            t.append(fullExpression);
            t = s.getTransition(s.getId() + "_ignore");
            if (t != null) {
              switch (stmachine.getEventSelectionStrategy()) {
              case PARTITION_CONTIGUITY:
                t.append(fullExpression);
                // negation later!
                break;
              case SKIP_TILL_NEXT_MATCH:
                t.append(fullExpression);
                t.getCondition().setEventTypeChecking(false);
                // negation later!
                break;
              case STRICT_CONTIGUITY:
                t.append("false");
                break;
              case SKIP_TILL_ANY_MATCH:
              default:
                t.getCondition().setEventTypeChecking(false);
              }
            }
            compareIter.remove();
          }
        }
      }
    }
    // SET NEGATION on whole expression!
    if (stmachine.getEventSelectionStrategy() == EEventSelectionStrategy.PARTITION_CONTIGUITY
        || stmachine.getEventSelectionStrategy() == EEventSelectionStrategy.SKIP_TILL_NEXT_MATCH) {
      List<State> states = stmachine.getStates();
      for (State s : states) {
        Transition t = s.getTransition(s.getId() + "_ignore");
        if (t != null) {
          t.negateExpression();
        }
      }
    }
     
       
  }
  ;

compareExpression[StateMachine sm,List<CompareExpression> compareExpressions]
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
   compareExpressions.add(new CompareExpression(attribs, ret.toString()));}
  ;

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

idexpression[List<State> states, List<CompareExpression> compareExpressions]
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
            compareExpressions.add(new CompareExpression(attr,expr.toString()));
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
    RelationalJEPOutputSchemeEntry e = null;
    OutputScheme scheme = new OutputScheme();
    SDFAttributeList attrList = new SDFAttributeList();
    for (PathAttribute p : retAttr) {
      String op = p.getAggregation();
      String a = p.getAttribute();
      String i = p.getKleenePart();
      String path = p.getPath();
      e = new RelationalJEPOutputSchemeEntry(CepVariable.getStringFor(op, a, i, a
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

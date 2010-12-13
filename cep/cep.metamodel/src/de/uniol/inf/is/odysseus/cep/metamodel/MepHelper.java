package de.uniol.inf.is.odysseus.cep.metamodel;

import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.mep.IExpression;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.mep.ParseException;
import de.uniol.inf.is.odysseus.mep.Variable;

public class MepHelper {

	@SuppressWarnings("unchecked")
	public static IExpression initMEPExpressionFromLabel(String label, Map<CepVariable, Variable> symbolTable /*INOUT*/) throws ParseException {
		@SuppressWarnings("rawtypes")
		IExpression expression = MEP.parse(transformToMepVar(label));
		Set<Variable> v = expression.getVariables();
		for (Variable s : v) {
			symbolTable.put(transformToOutVar(s.getIdentifier()), s);
		}
		return expression;
	}

	static public String transformToMepVar(String in) {
		String str = in.replace(CepVariable.getSeperator()+"-1"+CepVariable.getSeperator(), "a$0");
		str = str.replace(CepVariable.getSeperator(), "a$1");
		str = str.replace("[", "a$2");
		return str.replace("]", "a$3");
	}

	static public CepVariable transformToOutVar(String out) {
		String str = out.replace("a$0",CepVariable.getSeperator()+"-1"+CepVariable.getSeperator());
		str = str.replace("a$1", CepVariable.getSeperator());
		str = str.replace("a$2", "[");
		str = str.replace("a$3", "]");
		return new CepVariable(str);
	}
	
}

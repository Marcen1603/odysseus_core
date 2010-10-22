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
		IExpression expression = MEP.parse(label);
		Set<Variable> v = expression.getVariables();
		for (Variable s : v) {
			symbolTable.put(transformToOutVar(s.getIdentifier()), s);
		}
		return expression;
	}

	static public String transformToMepVar(String in) {
		String str = in.replace(CepVariable.getSeperator()+"-1"+CepVariable.getSeperator(), "$0");
		str = str.replace(CepVariable.getSeperator(), "$1");
		str = str.replace("[", "$2");
		return str.replace("]", "$3");
	}

	static public CepVariable transformToOutVar(String out) {
		String str = out.replace("$0",CepVariable.getSeperator()+"-1"+CepVariable.getSeperator());
		str = str.replace("$1", CepVariable.getSeperator());
		str = str.replace("$2", "[");
		str = str.replace("$3", "]");
		return new CepVariable(str);
	}
	
}

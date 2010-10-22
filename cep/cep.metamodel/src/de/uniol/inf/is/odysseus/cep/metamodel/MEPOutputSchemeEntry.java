package de.uniol.inf.is.odysseus.cep.metamodel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.mep.IExpression;
import de.uniol.inf.is.odysseus.mep.ParseException;
import de.uniol.inf.is.odysseus.mep.Variable;

public class MEPOutputSchemeEntry extends AbstractOutputSchemeEntry {

	IExpression<?> expression;
	protected Map<CepVariable, Variable> symbolTable = new HashMap<CepVariable, Variable>();
	
	public MEPOutputSchemeEntry(String label) {
		super(label);
		try{
			expression = MepHelper.initMEPExpressionFromLabel(label, symbolTable);
		}catch(ParseException e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public Set<CepVariable> getVarNames() {
		return symbolTable.keySet();
	}

	@Override
	public Object getValue() {
		return expression.getValue();
	}

	@Override
	public void setValue(CepVariable varName, Object newValue) {
		symbolTable.get(varName).bind(newValue);
	}

}

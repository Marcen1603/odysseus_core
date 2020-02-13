package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;

public class ListTupleProjectFunction extends AbstractLambdaListFunction2 {

	private static final long serialVersionUID = 482155249187485954L;
	int[] posList = null;

	public ListTupleProjectFunction() {
		super("ListTupleProject");
	}

	@Override
	public List<Object> getValue() {
		if (posList == null) {
			String expr = getInputValue(1);
			init(expr);
		}
		List<Object> out = new ArrayList<Object>();
		List<Object> inList = getInputValue(0);
		for (Object listElement : inList) {
			if (posList.length == 1) {
				out.add(((Tuple<?>) listElement).getAttribute(posList[0]));
			} else {
				out.add(((Tuple<?>) listElement).restrict(posList, true));
			}
		}
		return out;
	}

	private void init(String pos) {
		String[] parse = pos.split(",");
		posList = new int[parse.length];
		for (int i = 0; i < parse.length; i++) {
			posList[i] = Integer.parseInt(parse[i]);
		}
	}

	@Override
	protected void fillReturnList(List<Object> out, Object o) {

	}

}

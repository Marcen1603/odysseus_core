package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.List;
import java.util.Random;

public class ListRandomElementFunction extends AbstractListPosElementFunction {

	private static final long serialVersionUID = -3282877303737235603L;

	private Random rnd = new Random();
	
	public ListRandomElementFunction() {
		super("rnd");
	}

	@Override
	protected int getPos(List<Object> l) {
		if (l.size() > 0){
			return rnd.nextInt(l.size());		
		}else{
			return -1;
		}
	}
		
}

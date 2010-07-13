package de.uniol.inf.is.odysseus.scars.objecttracking;

import java.util.ArrayList;

public class ConnectionList<L, R, W extends java.lang.Number> extends ArrayList<Connection<L, R, W>> {

	private static final long serialVersionUID = 1L;
	
	public ArrayList<R> getRightElementsForLeftElement(L leftelement) {
		ArrayList<R> tmplist = new ArrayList<R>();
		for(int i = 0; i < this.size(); i++) {
			if(leftelement == this.get(i).getLeft()) {
				tmplist.add(this.get(i).getRight());
			}
		}
		return tmplist;
	}
	
	public ArrayList<L> getLeftElementsForRightElement(R rightelement) {
		ArrayList<L> tmplist = new ArrayList<L>();
		for(int i = 0; i < this.size(); i++) {
			if(rightelement == this.get(i).getRight()) {
				tmplist.add(this.get(i).getLeft());
			}
		}
		return tmplist;
	}

}

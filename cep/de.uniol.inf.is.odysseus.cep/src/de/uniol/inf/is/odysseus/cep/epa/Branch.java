package de.uniol.inf.is.odysseus.cep.epa;

import java.util.LinkedList;

/**
 * Instanzen dieser Klasse repräsentieren Verzweigungen im Verzweigungsspeicher
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class Branch {

	private StateMachineInstance instance;

	private Branch parent;

	private LinkedList<Branch> children;

	/**
	 * Erzeugt ein neues Verzweigungsobjekt.
	 * 
	 * @param instance
	 *            Eine Instanz, die aus einer Verzweigung hervorgegangen ist
	 *            oder zu einer Verzweigung geführt hat
	 * @param parent
	 *            Elternknoten im Verzweigungsbaum, oder null wenn es sich um
	 *            die Wurzel des Verzweigunsgbaumes handelt.
	 */
	public Branch(StateMachineInstance instance, Branch parent) {
		this.instance = instance;
		this.parent = parent;
		this.children = new LinkedList<Branch>();
	}

	/**
	 * Liefert die Automateninstanz, die an der Verzweigung beteiligt ist.
	 * 
	 * @return Die an der Verzweigung beteiligte Automateninstanz.
	 */
	public StateMachineInstance getInstance() {
		return instance;
	}

	/**
	 * Liefert den Elternknoten im Verzweigungsbaum.
	 * 
	 * @return Elternknoten oder null wenn es sich um die Wurzel des
	 *         Verzweigungsbaumes handelt
	 */
	public Branch getParent() {
		return parent;
	}

	/**
	 * Setzt den Elternkonten im Verzweigungsbaum.
	 * 
	 * @param parent
	 *            Der neue Elternknoten im Verzweigungsbaum oder null wenn es
	 *            sich um die Wurzel handelt.
	 */
	public void setParent(Branch parent) {
		this.parent = parent;
	}

	/**
	 * Liefert eine Liste mit allen Kindknoten der Verzweigung.
	 * @return Liste mit allen Kindknoten.
	 */
	public LinkedList<Branch> getChildren() {
		return children;
	}
	
	public String toString(String indent) {
		String str = "";
		str += indent + "Branch: " + this.hashCode() + "\n";
		indent += "  ";
		for (Branch child : this.children) {
			str += child.toString(indent);
		}
		return str;
	}

}

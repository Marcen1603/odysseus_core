/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.cep.epa;

import java.util.LinkedList;

/**
 * Instanzen dieser Klasse repräsentieren Verzweigungen im Verzweigungsspeicher
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class Branch<R> {

	private StateMachineInstance<R> instance;

	private Branch<R> parent;

	private LinkedList<Branch<R>> children;

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
	public Branch(StateMachineInstance<R> instance, Branch<R> parent) {
		this.instance = instance;
		this.parent = parent;
		this.children = new LinkedList<Branch<R>>();
	}

	/**
	 * Liefert die Automateninstanz, die an der Verzweigung beteiligt ist.
	 * 
	 * @return Die an der Verzweigung beteiligte Automateninstanz.
	 */
	public StateMachineInstance<R> getInstance() {
		return instance;
	}

	/**
	 * Liefert den Elternknoten im Verzweigungsbaum.
	 * 
	 * @return Elternknoten oder null wenn es sich um die Wurzel des
	 *         Verzweigungsbaumes handelt
	 */
	public Branch<R> getParent() {
		return parent;
	}

	/**
	 * Setzt den Elternkonten im Verzweigungsbaum.
	 * 
	 * @param parent
	 *            Der neue Elternknoten im Verzweigungsbaum oder null wenn es
	 *            sich um die Wurzel handelt.
	 */
	public void setParent(Branch<R> parent) {
		this.parent = parent;
	}

	/**
	 * Liefert eine Liste mit allen Kindknoten der Verzweigung.
	 * @return Liste mit allen Kindknoten.
	 */
	public LinkedList<Branch<R>> getChildren() {
		return children;
	}
	
	public String toString(String indent) {
		String str = "";
		str += indent + "Branch: " + this.hashCode() + "\n";
		indent += "  ";
		for (Branch<R> child : this.children) {
			str += child.toString(indent);
		}
		return str;
	}

}

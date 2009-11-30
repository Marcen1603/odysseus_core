package de.uniol.inf.is.odysseus.cep.epa;

import java.util.LinkedList;

/**
 * Der BranchingBuffer dient zum temporären Speichern von nichtdeterministischen
 * Verzweigungen der Automaten. Über die Schnittstelle des BranchingBuffers
 * können nichtdeterministische Verzweigungen hinzugefügt und wieder entfernt
 * werden.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class BranchingBuffer<R> {

	private LinkedList<Branch<R>> branches;
	
	/**
	 * Erzeugt einen neuen leeren BranchingBuffer.
	 */
	public BranchingBuffer() {
		this.branches = new LinkedList<Branch<R>>();
	}

	/**
	 * Fügt die Verzweigung der Automateninstanz originalInstance in die
	 * neuen Automateninstanzen der Liste newInstances in den
	 * BranchingBuffer ein. Ist originalInstance bereits im
	 * BranchingBuffer vorhanden, so werden die neuen Verzweigungen an der
	 * entsprechenden Stelle im Verzweigungsbaum eingehängt, ansonsten ein neuer
	 * Verzweigungsbaum erzeugt.
	 * 
	 * @param originalInstance
	 *            die Automateninstanz, die zur Verzweigung geführt hat
	 * @param newInstances
	 *            die aus originalInstance hervorgegangenen
	 *            Automateninstanzen
	 */
	public void addBranch(StateMachineInstance<R> originalInstance,
			LinkedList<StateMachineInstance<R>> newInstances) {
		Branch<R> parent = this.findBranch(originalInstance);
		if (parent == null) {
			parent = new Branch<R>(originalInstance, null);
			this.branches.add(parent);
		}

		for (int i = 0; i < newInstances.size(); i++) {
			Branch<R> newBranch = new Branch<R>(newInstances.get(i), parent);
			parent.getChildren().add(newBranch);
		}
	}

	/**
	 * Fügt die Verzweigung der Automateninstanz originalInstance in die
	 * neue Automateninstanz newInstance in den BranchingBuffer ein. Ist
	 * originalInstance bereits im BranchingBuffer vorhanden, so werden die
	 * neuen Verzweigungen an der entsprechenden Stelle im Verzweigungsbaum
	 * eingehängt, ansonsten ein neuer Verzweigungsbaum erzeugt.
	 * 
	 * @param originalInstance
	 *            die Automateninstanz, die zur Verzweigung geführt hat
	 * @param newInstance
	 *            die aus originalInstance hervorgegangenen
	 *            Automateninstanz
	 */
	public void addBranch(StateMachineInstance<R> originalInstance,
			StateMachineInstance<R> newInstance) {
		if (newInstance == null || originalInstance == null) {
			throw new NullPointerException();
		}
		Branch<R> parent = this.findBranch(originalInstance);
		if (parent == null) {
			parent = new Branch<R>(originalInstance, null);
			this.branches.add(parent);
		}

		Branch<R> newBranch = new Branch<R>(newInstance, parent);
		parent.getChildren().add(newBranch);
	}

	/**
	 * durchsucht den BranchingBuffer nach einer Verzweigung der
	 * Automateninstanz instance
	 * 
	 * @param instance
	 *            Automateninstanz nach der gesucht wird
	 * @return die Verzweigung der Automateninstanz instance oder null,
	 *         falls keine Verzweigung vorhanden ist
	 */
	private Branch<R> findBranch(StateMachineInstance<R> instance) {
		for (int i = 0; i < this.branches.size(); i++) {
			Branch<R> result = findBranch(instance, this.branches.get(i));
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Durchsucht einen (Teil-)Baum des BranchingBuffers rekursiv nach 
	 * instance und liefert im Erfolgsfall das entsprechende Verzweigungsobjekt
	 * zurück. Auf diese Methode sollte nicht direkt zugegriffen werden. Um eine
	 * Verzweigung zu suchen sollte stattdessen die Methode
	 * findBranch(State state) aufgerufen werden.
	 * 
	 * @param instance
	 *            AutomatenInstanz, nach der im Verzweigungsspeicher gesucht
	 *            wird
	 * @param branch
	 *            Knoten des Baumes, der nach instance durchsucht werden
	 *            soll
	 * @return Das Verzweigungsobjekt, das die Automateninstanz instance
	 *         enthält oder null, falls nicht gefunden wurde.
	 */
	private Branch<R> findBranch(StateMachineInstance<R> instance, Branch<R> branch) {
		if (branch.getInstance() == instance) {
			return branch;
		} else {
			for (int i = 0; i < branch.getChildren().size(); i++) {
				Branch<R> result = this.findBranch(instance, branch.getChildren()
						.get(i));
				if (result != null) {
					return result;
				}
			}
			return null;
		}
	}

	/**
	 * Entfernt die übergebene Instanz und alle damit verbundenen Verzweigungen
	 * aus dem Verzweigungsspeicher.
	 * 
	 * @param instance
	 *            Die Automateninstanz, die aus dem Verzweigungsspeicher
	 *            entfernt werden soll.
	 */
	public void removeAllNestedBranches(StateMachineInstance<R> instance) {
		Branch<R> searchedBranch = this.findBranch(instance);
		if (searchedBranch != null) {
			while (searchedBranch.getParent() != null) {
				searchedBranch = searchedBranch.getParent();
			}
			this.branches.remove(searchedBranch);
		}
	}

	/**
	 * Entfernt die Verzweigung der Instanz instance aus dem
	 * Verzweigungsspeicher
	 * 
	 * @param instance
	 */
	public void removeBranch(StateMachineInstance<R> instance) {
		Branch<R> searchedBranch = this.findBranch(instance);
		if (searchedBranch != null) {
			if (searchedBranch.getParent() == null) {
				// aus liste entfernen, children in liste einfügen
				for (int i = 0; i < searchedBranch.getChildren().size(); i++) {
					this.branches.add(searchedBranch.getChildren().get(i));
					searchedBranch.getChildren().get(i).setParent(null);
				}
				this.branches.remove(searchedBranch);
			} else {
				// parent der children auf parent setzen
				Branch<R> parent = searchedBranch.getParent();
				parent.getChildren().remove(searchedBranch);
				for (int i = 0; i < searchedBranch.getChildren().size(); i++) {
					parent.getChildren().add(
							searchedBranch.getChildren().get(i));
					searchedBranch.getChildren().get(i).setParent(parent);
				}
			}
		}
	}

	/**
	 * Gibt die Wurzel des Verzweigungsbaums für die übergebene Verzweigung
	 * zurück.
	 * 
	 * @param branch
	 *            Verzweigung, für die die Wurzel ermittelt werden soll.
	 * @return Die Wurzel des Verzweigungsbaums, in dem branch enthalten
	 *         ist.
	 */
	private Branch<R> getRoot(Branch<R> branch) {
		if (branch.getParent() != null) {
			return this.getRoot(branch.getParent());
		} else {
			return branch;
		}
	}

	/**
	 * Liefert die Liste aller Automateninstanzen, die über
	 * nichtdeterministische Verzweigungen mit der Automateninstanz
	 * instance verwandt sind. Die übergebene Automateninstanz ist
	 * ebenfalls enthalten.
	 * 
	 * @param instance
	 *            Automateninstanz, von der alle verwandten Automateninstanzen
	 *            gesucht werden sollen.
	 * @return Liste mit allen Automateninstanzen, die mit instance
	 *         verwandt sind.
	 */
	public LinkedList<StateMachineInstance<R>> getAllNestedStateMachineInstances(
			StateMachineInstance<R> instance) {
		LinkedList<StateMachineInstance<R>> instances = new LinkedList<StateMachineInstance<R>>();
		Branch<R> searchedBranch = this.findBranch(instance);
		if (searchedBranch != null) {
			Branch<R> root = this.getRoot(searchedBranch);
			this.addAllSubInstancesToList(root, instances);
			instances.add(root.getInstance());
		}
		return instances;
	}

	/**
	 * Fügt alle Automateninstanzen die aus der übergebenen Verzweigung
	 * hervorgegangen sind rekursiv in die übergebene Liste ein.
	 * 
	 * @param branch
	 *            Verzweigung, deren Sub-Automateninstanzen gesammelt werden.
	 * @param instances
	 *            Liste der Instanzen. Wird von der Methode manipuliert, darf
	 *            nicht null sein.
	 */
	private void addAllSubInstancesToList(Branch<R> branch,
			LinkedList<StateMachineInstance<R>> instances) {
		if (branch.getChildren() != null) {
			for (Branch<R> child : branch.getChildren()) {
				instances.add(child.getInstance());
				this.addAllSubInstancesToList(child, instances);
			}
		}
	}

	protected LinkedList<Branch<R>> getBranches() {
		return branches;
	}
	
	
}

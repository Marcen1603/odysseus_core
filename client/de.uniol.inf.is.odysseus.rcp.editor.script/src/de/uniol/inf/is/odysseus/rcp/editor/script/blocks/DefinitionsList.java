package de.uniol.inf.is.odysseus.rcp.editor.script.blocks;

import java.util.Collection;
import java.util.LinkedList;

import java.util.Objects;
import com.google.common.collect.Lists;

public class DefinitionsList extends LinkedList<Definition> {

	private static final long serialVersionUID = 1L;

	private final Collection<IDefinitionsListListener> listeners = Lists.newArrayList();

	public void addNewDefinition() {
		add(new Definition("key", "value"));

		fireListeners();
	}

	@Override
	public boolean add(Definition e) {
		boolean result = super.add(e);
		fireListeners();
		return result;
	}

	@Override
	public Definition set(int index, Definition element) {
		Definition result = super.set(index, element);
		fireListeners();

		return result;
	}

	@Override
	public boolean remove(Object o) {
		boolean result = super.remove(o);
		fireListeners();

		return result;
	}

	public void moveUp(Definition definition) {
		if (definition == null) {
			return;
		}

		int index = indexOf(definition);
		if (index > 0) {
			swap(index, index-1);
		}
	}

	public void moveDown(Definition definition) {
		int index = indexOf(definition);
		if (index < size() - 1) {
			swap(index, index + 1);
		}
		
	}
	
	public void swap( int indexA, int indexB ) {
		Definition temp = get(indexA);
		set(indexA, get(indexB));
		set(indexB, temp);
	}

	public void addListener(IDefinitionsListListener listener) {
		Objects.requireNonNull(listener, "listener must not be null!");

		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	public void removeListener(IDefinitionsListListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	public final void fireListeners() {
		synchronized (listeners) {
			for (IDefinitionsListListener listener : listeners) {
				try {
					listener.definitionListChanged(this);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
	}
}

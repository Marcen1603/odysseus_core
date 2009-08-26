package de.uniol.inf.is.odysseus.visualquerylanguage.model.operators;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractOperator implements INodeContent{
	
	private String name;
	private String typ;
	Collection<IParam<?>> constructParameterList;
	Collection<IParam<?>> setterParameterList;
	private Collection<INodeContentChangeListener<INodeContent>> listeners = new ArrayList<INodeContentChangeListener<INodeContent>>();
	
	public AbstractOperator(String name, String typ, Collection<IParam<?>> constructParameters, Collection<IParam<?>> setterParameters) {
		this.name = name;
		this.typ = typ;
		this.constructParameterList = constructParameters;
		this.setterParameterList = setterParameters;
	}



	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Collection<IParam<?>> getConstructParameterList() {
		return constructParameterList;
	}
	
	@Override
	public Collection<IParam<?>> getSetterParameterList() {
		return setterParameterList;
	}

	@Override
	public String getTyp() {
		return typ;
	}



	@Override
	public void addNodeContentChangeListener(
			INodeContentChangeListener<INodeContent> listener) {
		if( listener == null )
			return;
		if( listeners.contains( listener ))
			return;
		
		synchronized( listeners ) {
			listeners.add( listener );
		}
		
	}



	@Override
	public void notifyNodeContentChangeListener() {
		synchronized(listeners) {
			for( INodeContentChangeListener<INodeContent> l : listeners ) {
				if( l != null )
					l.nodeContentChanged( this );
			}
		}
	}

}

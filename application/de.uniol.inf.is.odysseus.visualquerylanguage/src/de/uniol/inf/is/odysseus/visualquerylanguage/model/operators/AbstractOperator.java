package de.uniol.inf.is.odysseus.visualquerylanguage.model.operators;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.graphics.Image;

public abstract class AbstractOperator implements INodeContent{
	
	private String name;
	private String typ;
	private Image image;
	Collection<IParamConstruct<?>> constructParameterList = new ArrayList<IParamConstruct<?>>();
	Collection<IParamSetter<?>> setterParameterList = new ArrayList<IParamSetter<?>>();
	private Collection<INodeContentChangeListener<INodeContent>> listeners = new ArrayList<INodeContentChangeListener<INodeContent>>();
	
	public AbstractOperator(String name, String typ, Image image, Collection<IParamConstruct<?>> constructParameters, Collection<IParamSetter<?>> setterParameters) {
		this.name = name;
		this.typ = typ;
		this.image = image;
		this.constructParameterList = constructParameters;
		this.setterParameterList = setterParameters;
	}



	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Collection<IParamConstruct<?>> getConstructParameterList() {
		return constructParameterList;
	}
	
	@Override
	public Collection<IParamSetter<?>> getSetterParameterList() {
		return setterParameterList;
	}

	@Override
	public String getType() {
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
	
	@Override
	public Collection<IParamConstruct<?>> getNewConstructParameterListInstance() {
		Collection<IParamConstruct<?>> list = new ArrayList<IParamConstruct<?>>();
		for (IParamConstruct<?> iParamConstruct : constructParameterList) {
			list.add(ParamConstructFactory.getInstance().createParam(iParamConstruct.getType(), iParamConstruct.getPosition(), iParamConstruct.getName()));
		}
		return list;
	}
	
	@Override
	public Collection<IParamSetter<?>> getNewSetterParameterListInstance() {
		Collection<IParamSetter<?>> list = new ArrayList<IParamSetter<?>>();
		for (IParamSetter<?> iParamSetter : setterParameterList) {
			list.add(ParamSetterFactory.getInstance().createParam(iParamSetter.getType(), iParamSetter.getSetter(), iParamSetter.getName()));
		}
		return list;
	}
	
	@Override
	public Image getImage() {
		return image;
	}
	
}

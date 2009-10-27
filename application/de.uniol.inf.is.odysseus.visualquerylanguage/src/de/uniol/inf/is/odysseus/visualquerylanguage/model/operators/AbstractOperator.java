package de.uniol.inf.is.odysseus.visualquerylanguage.model.operators;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;

public abstract class AbstractOperator implements INodeContent{
	
	private String name;
	private String type;
	private String imageName;
	private Image image;
	Collection<IParamConstruct<?>> constructParameterList = new ArrayList<IParamConstruct<?>>();
	Collection<IParamSetter<?>> setterParameterList = new ArrayList<IParamSetter<?>>();
	private Collection<INodeContentChangeListener<INodeContent>> listeners = new ArrayList<INodeContentChangeListener<INodeContent>>();
	
	private ILogicalOperator op;
	
	public AbstractOperator(String name, String type, Image image, Collection<IParamConstruct<?>> constructParameters, Collection<IParamSetter<?>> setterParameters) {
		this.name = name;
		this.type = type;
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
		return type;
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
			IParamConstruct<?> param = ParamConstructFactory.getInstance().createParam(iParamConstruct.getType(), iParamConstruct.getTypeList(), iParamConstruct.getPosition(), iParamConstruct.getName());
			param.setEditor(iParamConstruct.hasEditor());
			list.add(param);
		}
		return list;
	}
	
	@Override
	public Collection<IParamSetter<?>> getNewSetterParameterListInstance() {
		Collection<IParamSetter<?>> list = new ArrayList<IParamSetter<?>>();
		for (IParamSetter<?> iParamSetter : setterParameterList) {
			IParamSetter<?> param = ParamSetterFactory.getInstance().createParam(iParamSetter.getType(), iParamSetter.getTypeList(), iParamSetter.getSetter(), iParamSetter.getName());
			param.setEditor(iParamSetter.hasEditor());
			list.add(param);
		}
		return list;
	}
	
	@Override
	public Image getImage() {
		return image;
	}
	
	@Override
	public void setOperator(ILogicalOperator op) {
		this.op = op;
	}
	
	@Override
	public ILogicalOperator getOperator() {
		return this.op;
	}
	
	@Override
	public void setImageName(String name) {
		this.imageName = name;
	}
	
	@Override
	public String getImageName() {
		return this.imageName;
	}
}

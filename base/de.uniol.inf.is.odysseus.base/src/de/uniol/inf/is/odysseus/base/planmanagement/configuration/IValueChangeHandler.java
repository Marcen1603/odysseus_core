package de.uniol.inf.is.odysseus.base.planmanagement.configuration;

public interface IValueChangeHandler<T extends IMapValue<?>> {
	public void addValueChangeListener(IValueChangeListener<T> listener);

	public void removeValueChangeListener(IValueChangeListener<T> listener);
}

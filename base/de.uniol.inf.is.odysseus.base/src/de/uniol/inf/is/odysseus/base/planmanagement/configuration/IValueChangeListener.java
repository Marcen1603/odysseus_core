package de.uniol.inf.is.odysseus.base.planmanagement.configuration;

public interface IValueChangeListener<T extends IMapValue<?>> {
   public void settingChanged(T newValueContainer);
}

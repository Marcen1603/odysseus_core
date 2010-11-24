package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings;

import java.util.List;


public interface IChartSettingChangeable{

	public List<MethodSetting> getChartSettings();
	public void chartSettingsChanged();
}

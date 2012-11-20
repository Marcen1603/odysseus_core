package de.offis.gui.client.util;

import de.offis.gui.client.widgets.ConfigurationForm;

/**
 * Simple configuration callback.
 *
 * @author Alexander Funk
 * 
 */
public interface ConfigurationDialogCallback {
	public void onConfigurationSave(ConfigurationForm form);

	public void onConfigurationCancel();
}

package de.uniol.inf.is.odysseus.rcp.login;

import java.util.Collection;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import java.util.Objects;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.rcp.login.cfg.ILoginConfigurationProvider;
import de.uniol.inf.is.odysseus.rcp.login.cfg.OdysseusRCPLoginConfigurationProvider;
import de.uniol.inf.is.odysseus.rcp.login.contrib.LoginContributionFactory;
import de.uniol.inf.is.odysseus.rcp.login.contrib.LoginContributionLifecycle;
import de.uniol.inf.is.odysseus.rcp.login.extension.LoginContributionRegistry;
import de.uniol.inf.is.odysseus.rcp.login.view.ILoginWindow;
import de.uniol.inf.is.odysseus.rcp.login.view.LoginWindow;

public final class Login {

	private static final Logger LOG = LoggerFactory.getLogger(Login.class);
	
	private static final String SHOW_WINDOW_CONFIG_KEY = "__config__window_show";
	
	private static final LoginContributionFactory FACTORY = new LoginContributionFactory();
	private static final ILoginConfigurationProvider CONFIG = new OdysseusRCPLoginConfigurationProvider();
	
	private Login() {
		// do not instantiate this
	}
	
	public static void login(Shell parentShell, boolean forceShow, boolean cancelOK) {
		LOG.debug("Begin logging in. ForceShow = {}, CancelOK = {}", forceShow, cancelOK);
		
		Objects.requireNonNull(parentShell, "Parent shell for login must not be null!");
		
		LoginContributionRegistry registry = RCPLoginPlugIn.getLoginContributionRegistry();
		if( registry.isEmpty() ) {
			LOG.debug("Login contribution registry is empty. Cannot log in.");
			
			showErrorMessage(parentShell, "Login not possible since there are no login contributions.");
			if( !cancelOK ) {
				LOG.debug("Since cancelOK = false, exiting application");
				System.exit(1);
			} 
			LOG.debug("Since cancelOK = true, cancel login process");
			return;
		}
		LOG.debug("Got {} login contributions in registry.", registry.getAllContributions().size());
		
		LOG.debug("Creating instances of login contributions");
		Collection<Class<? extends ILoginContribution>> contributionClasses = registry.getAllContributions();
		Collection<ILoginContribution> contributions = FACTORY.create(contributionClasses);
		if( contributions.isEmpty() ) {
			LOG.debug("Could not create a single login contribution.");
			showErrorMessage(parentShell, "Login not possible since all registered contributions are invalid. See error log for details.");
			if( !cancelOK ) {
				LOG.debug("Since cancelOK = false, exiting application");
				System.exit(1);
			} 
			LOG.debug("Since cancelOK = true, cancel login process");
			return;
		}
		LOG.debug("Created {} login contributions. {} failed constructing.", contributions.size(), registry.getAllContributions().size() - contributions.size());
		
		LoginContributionLifecycle lifecycle = new LoginContributionLifecycle(contributions);
		LOG.debug("Created lifecycle handling");
		lifecycle.onInitAll();
		
		LOG.debug("Creating configuration map");
		Map<String, String> configMap = createConfigMap();
		lifecycle.onLoadAll(configMap);
		
		boolean showWindow = determineIfShowWindowNeeded(configMap);
		LOG.debug("Loaded from config ShowWindow = {}", showWindow);
		boolean allValid = lifecycle.areAllValid();
		LOG.debug("Login contributions are valid = {}", allValid);
		
		boolean couldFinish = false;
		do {
			if( forceShow || !allValid || showWindow ) {
				
				LOG.debug("Show login window now");
				ILoginWindow window = new LoginWindow(parentShell);
				window.show( contributions, showWindow );
				
				if( window.isOK() ) {
					LOG.debug("Login window closed with OK");
					Map<String, String> savedConfig = lifecycle.onSaveAll();
					
					LOG.debug("Show window next time = {}", window.isShowAgain());
					insertShowWindowSetting( savedConfig, window.isShowAgain());
					saveConfigMap(savedConfig);
					LOG.debug("Saved config map");
					
				} else if( window.isCancel() ) {
					LOG.debug("Login window closed with CANCEL");
					if( !cancelOK ) {
						LOG.debug("Since cancelOK = false, exiting application");
						System.exit(1);
					} 
					LOG.debug("Since cancelOK = true, cancel login process");
					return;
				}
			} else {
				LOG.debug("Do not show the login window. Automatically login now.");
			}
			
			couldFinish = lifecycle.onFinishAll();
			if( !couldFinish ) {
				forceShow = true;
			}
			
		} while( !couldFinish );
	}

	private static void insertShowWindowSetting(Map<String, String> savedConfig, boolean showAgain) {
		savedConfig.put(SHOW_WINDOW_CONFIG_KEY, String.valueOf(showAgain));
	}
	
	private static void saveConfigMap(Map<String, String> savedConfig) {
		LOG.debug("Saving config map");
		for( String key : savedConfig.keySet() ) {
			String value = savedConfig.get(key);
			
			LOG.debug("Save {} --> {}", key, value);
			CONFIG.set(key, value);
		}
	}

	private static Map<String, String> createConfigMap() {
		LOG.debug("Loading config map");
		Map<String, String> configMap = Maps.newHashMap();
		
		Collection<String> configKeys = CONFIG.getKeys();
		for( String configKey : configKeys ) {
			Optional<String> optValue = CONFIG.get(configKey);
			if( optValue.isPresent() ) {
				
				LOG.debug("Load {} --> {}", configKey, optValue.get());
				configMap.put(configKey, optValue.get());
			}
		}
		
		return configMap;
	}
	
	private static boolean determineIfShowWindowNeeded(Map<String, String> configMap) {
		if( configMap.containsKey(SHOW_WINDOW_CONFIG_KEY)) {
			try {
				return Boolean.parseBoolean(configMap.get(SHOW_WINDOW_CONFIG_KEY));
			} catch( Throwable t ) {
			}
		}
		return true;
	}
	
	private static void showErrorMessage(Shell shell, String text) {
		MessageBox box = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
		box.setMessage(text);
		box.setText("Error");
		box.open();
	}
}

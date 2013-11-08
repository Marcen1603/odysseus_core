package de.uniol.inf.is.odysseus.rcp.login;

import java.util.Collection;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.rcp.login.cfg.ILoginConfigurationProvider;
import de.uniol.inf.is.odysseus.rcp.login.cfg.OdysseusRCPLoginConfigurationProvider;
import de.uniol.inf.is.odysseus.rcp.login.contrib.LoginContributionFactory;
import de.uniol.inf.is.odysseus.rcp.login.contrib.LoginContributionLifecycle;
import de.uniol.inf.is.odysseus.rcp.login.extension.LoginContributionRegistry;
import de.uniol.inf.is.odysseus.rcp.login.view.ILoginWindow;
import de.uniol.inf.is.odysseus.rcp.login.view.LoginWindow;

public final class Login {

	private static final String SHOW_WINDOW_CONFIG_KEY = "__config__window_show";
	
	private static final LoginContributionFactory FACTORY = new LoginContributionFactory();
	private static final ILoginConfigurationProvider CONFIG = new OdysseusRCPLoginConfigurationProvider();
	
	private Login() {
		// do not instantiate this
	}
	
	public static void login(Shell parentShell, boolean forceShow, boolean cancelOK) {
		Preconditions.checkNotNull(parentShell, "Parent shell for login must not be null!");
		
		LoginContributionRegistry registry = RCPLoginPlugIn.getLoginContributionRegistry();
		if( registry.isEmpty() ) {
			showErrorMessage(parentShell, "Login not possible since there are no login contributions.");
			if( !cancelOK ) {
				System.exit(1);
			} 
			return;
		}
		
		Collection<Class<? extends ILoginContribution>> contributionClasses = registry.getAllContributions();
		Collection<ILoginContribution> contributions = FACTORY.create(contributionClasses);
		if( contributions.isEmpty() ) {
			showErrorMessage(parentShell, "Login not possible since all registered contributions are invalid. See error log for details.");
			if( !cancelOK ) {
				System.exit(1);
			} 
			return;
		}
		
		LoginContributionLifecycle lifecycle = new LoginContributionLifecycle(contributions);
		lifecycle.onInitAll();
		
		Map<String, String> configMap = createConfigMap();
		lifecycle.onLoadAll(configMap);
		
		boolean showWindow = determineIfShowWindowNeeded(configMap);
		boolean allValid = lifecycle.areAllValid();
		
		do {
			if( forceShow || !allValid || showWindow ) {
				
				ILoginWindow window = new LoginWindow(parentShell);
				window.show( contributions, showWindow );
				
				if( window.isOK() ) {
					Map<String, String> savedConfig = lifecycle.onSaveAll();
					insertShowWindowSetting( savedConfig, window.isShowAgain());
					saveConfigMap(savedConfig);
					
				} else if( window.isCancel() ) {
					if( !cancelOK ) {
						System.exit(1);
					}
				}
			}
		} while( !lifecycle.onFinishAll() );
	}

	private static void insertShowWindowSetting(Map<String, String> savedConfig, boolean showAgain) {
		savedConfig.put(SHOW_WINDOW_CONFIG_KEY, String.valueOf(showAgain));
	}
	
	private static void saveConfigMap(Map<String, String> savedConfig) {
		for( String key : savedConfig.keySet() ) {
			CONFIG.set(key, savedConfig.get(key));
		}
	}

	private static Map<String, String> createConfigMap() {
		Map<String, String> configMap = Maps.newHashMap();
		
		Collection<String> configKeys = CONFIG.getKeys();
		for( String configKey : configKeys ) {
			Optional<String> optValue = CONFIG.get(configKey);
			if( optValue.isPresent() ) {
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

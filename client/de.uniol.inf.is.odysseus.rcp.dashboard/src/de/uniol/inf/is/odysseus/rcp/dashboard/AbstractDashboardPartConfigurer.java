package de.uniol.inf.is.odysseus.rcp.dashboard;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;


public abstract class AbstractDashboardPartConfigurer<T extends IDashboardPart> implements IDashboardPartConfigurer<T> {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractDashboardPartConfigurer.class);
	
	private final List<IConfigurerListener> listeners = Lists.newArrayList();
	
	@Override
	public void addListener(IConfigurerListener listener) {
		Objects.requireNonNull(listener, "IConfigurerListener must not be null!");
		
		synchronized( listeners ) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(IConfigurerListener listener) {
		synchronized( listeners ) {
			listeners.remove(listener);
		}
	}
	
	protected final void fireListener() {
		synchronized( listeners ) {
			for( IConfigurerListener listener : listeners ) {
				try {
					listener.configChanged(this);
				} catch( Throwable t ) {
					LOG.error("Could not execute configurer-listener", t);
				}
			}
		}
	}

}

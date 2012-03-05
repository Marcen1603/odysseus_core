package de.uniol.inf.is.odysseus.test.runner;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.test.ITestComponent;

public class Tester implements IApplication {

	private static final Logger LOG = LoggerFactory.getLogger(Tester.class);

	private static Collection<ITestComponent> testComponents = new ArrayList<ITestComponent>();

	public void addTestComponent(ITestComponent component) {
		LOG.debug("Add TestComponent " + component);
		testComponents.add(component);
	}

	public void removeTestComponent(ITestComponent component) {
		testComponents.remove(component);
		LOG.debug("Remove TestComponent : " + component);
	}

	@Override
	public Object start(IApplicationContext context) throws Exception {
		final String[] args = (String[]) context.getArguments().get("application.args");
		
		LOG.debug("Starting tests (args = " + argsToString(args) + ")");

		for( ITestComponent testComponent : testComponents ) {
			testComponent.startTesting(args);
		}
		
		return null;
	}

	private static String argsToString(String[] strings) {
		if (strings == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		for (int i = 0; i < strings.length; i++) {
			sb.append(strings[i]);
			if (i < strings.length - 1) {
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public void stop() {

	}

}

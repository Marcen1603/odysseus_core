package de.uniol.inf.is.odysseus.test.component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.test.ExecutorProvider;
import de.uniol.inf.is.odysseus.test.StatusCode;
import de.uniol.inf.is.odysseus.test.context.BasicTestContext;
import de.uniol.inf.is.odysseus.test.context.ITestContext;
import de.uniol.inf.is.odysseus.test.set.TestSet;

public abstract class AbstractTestComponent<T extends ITestContext> implements ITestComponent<T>{

	protected IServerExecutor executor;
	protected List<TestSet> testsets;
	protected ISession session;
	protected static Logger LOG = LoggerFactory.getLogger(AbstractTestComponent.class);
	
	@Override
	public void setupTest(T context) {
		executor = ExecutorProvider.getExeuctor();
		session = UserManagementProvider.getSessionmanagement().login(context.getUsername(), context.getPassword().getBytes(), UserManagementProvider.getDefaultTenant());
		testsets = createTestSets(context);
	}
	
	public abstract List<TestSet> createTestSets(T context);

	@SuppressWarnings("unchecked")
	public T createTestContext(){
		return (T) createBasicTestContext();
	}
	
	public BasicTestContext createBasicTestContext(){
		BasicTestContext testcontext = new BasicTestContext();
		testcontext.setPassword("manager");
		testcontext.setUsername("System");
		URL bundleentry = FrameworkUtil.getBundle(getClass()).getEntry(".");

		URL rootPath;
		try {
			rootPath = FileLocator.toFileURL(bundleentry);
			testcontext.setDataRootPath(rootPath);
		} catch (IOException e) {
			System.out.println("Bundleentry was: "+bundleentry);
			e.printStackTrace();
		}
		return testcontext;		
	}

	/**
	 * Taken from de.uniol.inf.is.odysseus.test.TestComponent
	 * 
	 * @param executor
	 */
	private static void tryStartExecutor(IServerExecutor executor) {
		try {
			LOG.debug("Starting executor");
			System.out.println("current thread: "+Thread.currentThread().getName());
			executor.startExecution();
		} catch (PlanManagementException e1) {			
			throw new RuntimeException(e1);
		}
	}

	/**
	 * Taken from de.uniol.inf.is.odysseus.test.TestComponent
	 * 
	 * @param executor
	 */
	private static void tryStopExecutor(IServerExecutor executor) {
		try {
			LOG.debug("Stopping executor");
			executor.stopExecution();
		} catch (PlanManagementException e) {
			LOG.error("Exception during stopping executor", e);
		}
	}
	
	@Override
	public List<StatusCode> runTest(T context) {
		List<StatusCode> codes = new ArrayList<>();
		int i = 1;
		tryStartExecutor(executor);
		for (TestSet set : testsets) {
			System.out.println("Running sub test " + i + " of " + testsets.size() + "....");
			StatusCode code = executeTestSet(set);
			System.out.println("Running sub test " + i + " done, ended with code: " + code);
			codes.add(code);
			i++;
		}
		tryStopExecutor(executor);
		return codes;
	}
	
	
	protected abstract StatusCode executeTestSet(TestSet set);

	@Override
	public String getName() {
		return getClass().getSimpleName();		
	}
	
	@Override
	public String toString() {
		return getName();
	}
}

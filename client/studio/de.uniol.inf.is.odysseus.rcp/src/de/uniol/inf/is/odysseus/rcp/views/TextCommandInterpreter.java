package de.uniol.inf.is.odysseus.rcp.views;

import java.util.Dictionary;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

public class TextCommandInterpreter implements CommandInterpreter {

	private final String[] args;
	private final Text text;
	
	private int argIndex = 0;
	
	public TextCommandInterpreter( Text text, String[] args ) {
		this.args = args;
		this.text = text;
	}
	
	@Override
	public String nextArgument() {
		if (argIndex < args.length) {
			return this.args[argIndex++];
		}
		return null;
	}

	@Override
	public Object execute(String cmd) {
		return null;
	}

	@Override
	public void print(final Object o) {
		if( o == null ) {
			text.append("null");
		} else {
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					text.append(o.toString());
				}
			});
		}
	}

	@Override
	public void println() {
		print("\n");
	}

	@Override
	public void println(Object o) {
		print(o);
		println();
	}

	@Override
	public void printStackTrace(Throwable t) {
		println(getStackTrace(t));
	}

	private static String getStackTrace(Throwable e) {
		final StringBuilder report = new StringBuilder();
		report.append("Message:\n").append(e.getMessage()).append("\n\n");
		final StackTraceElement[] stack = e.getStackTrace();
		for (final StackTraceElement s : stack) {
			report.append("\tat ");
			report.append(s);
			report.append("\n");
		}
		report.append("\n");
		// cause
		Throwable throwable = e.getCause();
		while (throwable != null) {
			final StackTraceElement[] trace = throwable.getStackTrace();
			int m = trace.length - 1, n = stack.length - 1;
			while ((m >= 0) && (n >= 0) && trace[m].equals(stack[n])) {
				m--;
				n--;
			}
			final int framesInCommon = trace.length - 1 - m;

			report.append("Caused by: ").append(throwable.getClass().getSimpleName()).append(" - ").append(throwable.getMessage());
			report.append("\n\n");
			for (int i = 0; i <= m; i++) {
				report.append("\tat ").append(trace[i]).append("\n");
			}
			if (framesInCommon != 0) {
				report.append("\t... ").append(framesInCommon).append(" more\n");
			}
			throwable = throwable.getCause();
		}
		return report.toString();
	}
	@Override
	public void printDictionary(Dictionary<?, ?> dic, String title) {
	}

	@Override
	public void printBundleResource(Bundle bundle, String resource) {
	}
}

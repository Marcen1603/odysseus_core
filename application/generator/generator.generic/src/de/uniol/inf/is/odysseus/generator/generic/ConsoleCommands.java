/**
 *
 */
package de.uniol.inf.is.odysseus.generator.generic;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class ConsoleCommands implements CommandProvider {

    //@SuppressWarnings("static-method")
    public void _stopClients(final CommandInterpreter ci) {
        Activator.stop();
    }

    //@SuppressWarnings("static-method")
    public void _pauseClients(final CommandInterpreter ci) {
        Activator.pause();
    }

    //@SuppressWarnings("static-method")
    public void _proceedClients(final CommandInterpreter ci) {
        Activator.proceed();
    }

    //@SuppressWarnings("static-method")
    public void _stats(final CommandInterpreter ci) {
        Activator.printStatus();
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public String getHelp() {
        String s = "\tstopClients - stops all clients of for all servers\n";
        s = s + "\tpauseClients - pauses all clients of for all servers\n";
        s = s + "\tproceedClients - proceeds all clients of for all servers\n";
        s = s + "\tstats - prints current status for all servers\n";
        return s;
    }

}

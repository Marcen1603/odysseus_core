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

    public void _stopClients(CommandInterpreter ci) {
        Activator.stop();
    }

    public void _pauseClients(CommandInterpreter ci) {
        Activator.pause();
    }

    public void _proceedClients(CommandInterpreter ci) {
        Activator.proceed();
    }

    public void _stats(CommandInterpreter ci) {
        Activator.printStatus();
    }

    @Override
    public String getHelp() {
        String s = "\tstopClients - stops all clients of for all servers\n";
        s = s + "\tpauseClients - pauses all clients of for all servers\n";
        s = s + "\tproceedClients - proceeds all clients of for all servers\n";
        s = s + "\tstats - prints current status for all servers\n";
        return s;
    }

}

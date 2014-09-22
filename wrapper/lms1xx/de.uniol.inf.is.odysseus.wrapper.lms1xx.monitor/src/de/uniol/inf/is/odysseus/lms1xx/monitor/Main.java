/*******************************************************************************
 * LMS1xx protocol visualization and logging
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.uniol.inf.is.odysseus.lms1xx.monitor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.lms1xx.monitor.connection.LMS1xxConnectionImpl;
import de.uniol.inf.is.odysseus.lms1xx.monitor.ui.LMSScreen;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class Main {

    /**
     * @param args
     */
    public static void main(final String[] args) {
        if (args.length >= 1) {
            final List<LMS1xxConnection> connections = new LinkedList<LMS1xxConnection>();
            for (final String arg : args) {
                final String[] hostIp = arg.split(":");
                final LMS1xxConnection connection;
                if (hostIp.length == 2) {
                    connection = new LMS1xxConnectionImpl(hostIp[0], Integer.parseInt(hostIp[1]));
                }
                else {
                    connection = new LMS1xxConnectionImpl(hostIp[0], 2111);
                }
                connections.add(connection);

                try {
                    connection.open();
                }
                catch (final FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
            final LMSScreen screen = new LMSScreen(connections);
            while (screen.isEnabled()) {
                try {
                    Thread.sleep(1000);
                }
                catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (final LMS1xxConnection connection : connections) {
                try {
                    connection.close();
                }
                catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            help();
        }
    }

    public static void help() {
        System.out.println("Usage: lms-monitor <Host/IP:Port> [Host/IP:Port]... \n\n" + "Starts the lms-monitor.\n");
    }
}

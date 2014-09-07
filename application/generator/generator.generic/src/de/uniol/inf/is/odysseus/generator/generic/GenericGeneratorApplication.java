/**********************************************************************************
 * Copyright 2014 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.generator.generic;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.generator.StreamServer;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class GenericGeneratorApplication implements IApplication {
    private static final Logger LOG = LoggerFactory.getLogger(GenericGeneratorApplication.class);
    private StreamServer genericServer;

    /**
     * {@inheritDoc}
     */
    @Override
    public Object start(final IApplicationContext context) throws Exception {
        context.applicationRunning();
        final String[] args = new String[6];
        args[0] = "-gP";
        args[1] = System.getenv("gP");
        args[2] = "-gF";
        args[3] = System.getenv("gF");
        args[4] = "-gS";
        args[5] = System.getenv("gS");

        long number = -1l;
        int port = 54325;
        long frequency = 1000l;
        String schemaFile = "schema.txt";
        String out = null;
        if (args[1] != null) {
            port = Integer.parseInt(args[1]);
        }
        args[1] = port + "";
        if (args[3] != null) {
            frequency = Long.parseLong(args[3]);
        }
        args[3] = frequency + "";
        if (args[5] != null) {
            schemaFile = args[5];
        }
        args[5] = schemaFile + "";
        if (args[7] != null) {
            out = args[7];
        }
        args[7] = out + "";
        if (args[9] != null) {
            number = Long.parseLong(args[9]);
        }
        args[9] = number + "";
        GenericGeneratorApplication.LOG.debug("Generator started " + args[0] + " " + args[1] + " " + args[2] + " " + args[3] + " " + args[4] + " " + args[5] + " " + args[6] + " " + args[7] + " "
                + args[8] + " " + args[9]);

        this.genericServer = new StreamServer(port, new GenericProvider(schemaFile, frequency, out, number));
        this.genericServer.start();
        return IApplicationContext.EXIT_ASYNC_RESULT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        if (this.genericServer != null) {
            this.genericServer.stopClients();
        }
    }

}

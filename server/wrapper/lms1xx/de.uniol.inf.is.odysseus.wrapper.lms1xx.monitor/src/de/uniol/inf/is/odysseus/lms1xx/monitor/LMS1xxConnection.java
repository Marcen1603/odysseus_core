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

import de.uniol.inf.is.odysseus.lms1xx.monitor.ui.LMSScreen;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public interface LMS1xxConnection {
    /**
     * Close the connection
     * 
     * @throws IOException
     */
    void close() throws IOException;

    /**
     * 
     * @return <code>true</code> if the sensor is connected
     */
    boolean isConnected();

    /**
     * Open the connection
     * 
     * @throws FileNotFoundException
     */
    void open() throws FileNotFoundException;

    /**
     * Register the screen to the sensor
     * 
     * @param lmsScreen
     *            The screen
     */
    void registerListener(LMSScreen lmsScreen);

}

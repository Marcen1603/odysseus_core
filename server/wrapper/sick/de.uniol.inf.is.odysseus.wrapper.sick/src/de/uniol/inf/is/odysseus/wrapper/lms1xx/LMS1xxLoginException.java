/*******************************************************************************
 * LMS1xx protocol handler for the Odysseus data stream management system
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
package de.uniol.inf.is.odysseus.wrapper.lms1xx;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class LMS1xxLoginException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 8192339199073587329L;

    /**
     * 
     */
    public LMS1xxLoginException() {
    }

    /**
     * @param message
     */
    public LMS1xxLoginException(final String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public LMS1xxLoginException(final Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public LMS1xxLoginException(final String message, final Throwable cause) {
        super(message, cause);
    }

}

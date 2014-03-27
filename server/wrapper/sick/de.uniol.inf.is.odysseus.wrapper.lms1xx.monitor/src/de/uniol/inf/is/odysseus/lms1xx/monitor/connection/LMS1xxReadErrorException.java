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
package de.uniol.inf.is.odysseus.lms1xx.monitor.connection;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class LMS1xxReadErrorException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 5992676508905656264L;

    /**
     * 
     */
    public LMS1xxReadErrorException() {
    }

    /**
     * @param message
     */
    public LMS1xxReadErrorException(final String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public LMS1xxReadErrorException(final Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public LMS1xxReadErrorException(final String message, final Throwable cause) {
        super(message, cause);
    }

}

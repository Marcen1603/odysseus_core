package com.pi4j.wiringpi;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  Serial.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2013 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import com.pi4j.util.NativeLibraryLoader;

/**
 * <p>
 * WiringPi includes a simplified serial port handling library. It can use the on-board serial port,
 * or any USB serial device with no special distinctions between them. You just specify the device
 * name in the initial open function.
 * </p>
 * 
 * <p>
 * Note: The file descriptor (fd) returned is a standard Linux filehandle. You can use the standard
 * read(), write(), etc. system calls on this filehandle as required. E.g. you may wish to write a
 * larger block of binary data where the serialPutchar() or serialPuts() function may not be the
 * most appropriate function to use, in which case, you can use write() to send the data.
 * </p>
 * 
 * <p>
 * Before using the Pi4J library, you need to ensure that the Java VM in configured with access to
 * the following system libraries:
 * <ul>
 * <li>pi4j</li>
 * <li>wiringPi</li>
 * </ul>
 * <blockquote> This library depends on the wiringPi native system library.</br> (developed by
 * Gordon Henderson @ <a href="https://projects.drogon.net/">https://projects.drogon.net/</a>)
 * </blockquote>
 * </p>
 * 
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @see <a
 *      href="https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/">https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class Serial {

    /**
     * The default hardware COM port provided via the Raspberry Pi GPIO header.
     * 
     * @see #serialOpen(String,int)
     */
    public static final String DEFAULT_COM_PORT = "/dev/ttyAMA0";

    // private constructor 
    private Serial() {
        // forbid object construction 
    }
    
    static {
        // Load the platform library
        NativeLibraryLoader.load("pi4j", "libpi4j.so");
    }

    /**
     * <p>int serialOpen (char *device, int baud);</p>
     * 
     * <p>
     * This opens and initializes the serial device and sets the baud rate. It sets the port into
     * raw mode (character at a time and no translations), and sets the read timeout to 10 seconds.
     * The return value is the file descriptor or -1 for any error, in which case errno will be set
     * as appropriate.
     * </p>
     * 
     * <p>
     * (ATTENTION: the 'device' argument can only be a maximum of 128 characters.)
     * </p>
     * 
     * @see #DEFAULT_COM_PORT
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/">https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/</a>
     * 
     * @param device The device address of the serial port to access. You can use constant
     *            'DEFAULT_COM_PORT' if you wish to access the default serial port provided via the
     *            GPIO header.
     * @param baud The baud rate to use with the serial port.
     * @return The return value is the file descriptor or -1 for any error, in which case errno will
     *         be set as appropriate.
     */
    public static native int serialOpen(String device, int baud);

    /**
     * <p>void serialClose (int fd);</p>
     * 
     * <p>
     * Closes the device identified by the file descriptor given.
     * </p>
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/">https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/</a>
     * @param fd <p>
     *            The file descriptor of the serial port.
     *            </p>
     */
    public static native void serialClose(int fd);

    /**
     * <h1>void serialFlush (int fd);</h1>
     * 
     * <p>This discards all data received, or waiting to be send down the given device.</p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/">https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/</a>
     * @param fd The file descriptor of the serial port.
     */
    public static native void serialFlush(int fd);

    /**
     * <p>void serialPutchar (int fd, unsigned char c);</p>
     * 
     * <p>Sends the single byte to the serial device identified by the given file descriptor.</p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/">https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/</a>
     * @param fd The file descriptor of the serial port.
     * @param data The character to transmit to the serial port.
     */
    public static native void serialPutchar(int fd, char data);

    /**
     * <p>void serialPuts (int fd, char *s);</p>
     * 
     * <p>Sends the nul-terminated string to the serial device identified by the given file descriptor.</p>
     * 
     * <p>(ATTENTION: the 'data' argument can only be a maximum of 1024 characters.)</p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/">https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/</a>
     * @param fd The file descriptor of the serial port.
     * @param data The data string to transmit to the serial port.
     */
    public static native void serialPuts(int fd, String data);

    /**
     * <p>void serialPuts (int fd, String data, String...arguments);</p>
     * 
     * <p>
     * Sends the nul-terminated formatted string to the serial device identified by the given file
     * descriptor.
     * </p>
     * 
     * <p>(ATTENTION: the 'data' argument can only be a maximum of 1024 characters.)</p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/">https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/</a>
     * @param fd The file descriptor of the serial port.
     * @param data The formatted data string to transmit to the serial port.
     * @param args Arguments to the format string.
     */
    public static void serialPuts(int fd, String data, String... args) {
        serialPuts(fd, String.format(data, (Object[]) args));
    }

    /**
     * <p>int serialDataAvail (int fd);</p>
     * 
     * Returns the number of characters available for reading, or -1 for any error condition, in
     * which case errno will be set appropriately.
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/">https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/</a>
     * @param fd The file descriptor of the serial port.
     * @return Returns the number of characters available for reading, or -1 for any error
     *         condition, in which case errno will be set appropriately.
     */
    public static native int serialDataAvail(int fd);

    /**
     * <p>int serialGetchar (int fd);</p>
     * 
     * <p>Returns the next character available on the serial device. This call will block for up to 10
     * seconds if no data is available (when it will return -1)</p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/">https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/</a>
     * @param fd The file descriptor of the serial port.
     * @return Returns the next character available on the serial device. This call will block for
     *         up to 10 seconds if no data is available (when it will return -1)
     */
    public static native int serialGetchar(int fd);
}

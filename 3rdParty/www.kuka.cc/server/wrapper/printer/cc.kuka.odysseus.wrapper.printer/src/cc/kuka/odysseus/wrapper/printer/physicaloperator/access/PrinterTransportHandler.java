/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
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
package cc.kuka.odysseus.wrapper.printer.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.Sides;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class PrinterTransportHandler extends AbstractTransportHandler {
    /** Logger */
    private final Logger LOG = LoggerFactory.getLogger(PrinterTransportHandler.class);

    private final static String PRINTER = "PRINTER";
    private final static String SIDES = "SIDES";

    private final static String NAME = "Printer";

    /** The print service. */
    private PrintService printService;
    /** The output stream */
    private OutputStream output;

    /** The printer name. */
    private String printer;
    /** The sides setting. */
    private Sides sides;

    /**
     *
     */
    public PrinterTransportHandler() {
        super();
    }

    public PrinterTransportHandler(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
        super(protocolHandler, options);
        this.init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(final byte[] message) throws IOException {
        this.output.write(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
        final PrinterTransportHandler handler = new PrinterTransportHandler(protocolHandler, options);
        return handler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getInputStream() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OutputStream getOutputStream() {
        return this.output;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return PrinterTransportHandler.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processInOpen() throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processOutOpen() throws IOException {
        this.printService = null;
        if ("".equals(this.printer)) {
            this.printService = PrintServiceLookup.lookupDefaultPrintService();
        }
        if (this.printService == null) {
            final DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PAGEABLE;
            final PrintRequestAttributeSet patts = new HashPrintRequestAttributeSet();
            patts.add(MediaSize.ISO.A4);
            patts.add(this.sides);
            final PrintService[] printServices = PrintServiceLookup.lookupPrintServices(flavor, patts);
            if (printServices.length == 0) {
                this.LOG.error("No printer found");
                this.LOG.debug("Available printers: " + Arrays.asList(printServices));
                throw new IOException("No Printer found");
            }
            if ((printServices.length == 1) && ("".equals(this.printer))) {
                this.printService = printServices[0];
            }
            else {
                for (final PrintService ps : printServices) {
                    if (ps.getName().equalsIgnoreCase(this.printer)) {
                        this.printService = ps;
                        break;
                    }
                }
            }
        }
        if (this.printService == null) {
            throw new IOException("No Printer found");
        }
        this.output = new PrinterTransportHandlerOutputStream(this.printService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processInClose() throws IOException {
        throw new UnsupportedOperationException();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processOutClose() throws IOException {
        this.output.close();
        this.printService = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSemanticallyEqualImpl(final ITransportHandler o) {
        if (!(o instanceof PrinterTransportHandler)) {
            return false;
        }
        final PrinterTransportHandler other = (PrinterTransportHandler) o;
        if (!this.getPrinter().equalsIgnoreCase(other.getPrinter())) {
            return false;
        }

        return true;
    }

    /**
     * @return the printerName
     */
    public String getPrinter() {
        return this.printer;
    }

    private void init() {
        final OptionMap options = this.getOptionsMap();
        if (options.containsKey(PrinterTransportHandler.PRINTER)) {
            this.printer = options.get(PrinterTransportHandler.PRINTER);
        }
        else {
            this.printer = "";
        }
        if (options.containsKey(PrinterTransportHandler.SIDES)) {
            if ("DUPLEX".equalsIgnoreCase(options.get(PrinterTransportHandler.SIDES))) {
                this.sides = Sides.DUPLEX;
            }
            else if ("TUMBLE".equalsIgnoreCase(options.get(PrinterTransportHandler.SIDES))) {
                this.sides = Sides.TUMBLE;
            }
            else {
                this.sides = Sides.ONE_SIDED;
            }
        }
        else {
            this.sides = Sides.ONE_SIDED;
        }
    }

    private static class PrinterTransportHandlerOutputStream extends OutputStream {
        /** Logger */
        private final Logger LOG = LoggerFactory.getLogger(PrinterTransportHandlerOutputStream.class);

        /** The print service. */
        private final PrintService printService;
        private boolean closing = false; /* To avoid recursive closing */
        private ByteBuffer buffer = ByteBuffer.allocate(30 * 1024);

        /**
         *
         */
        public PrinterTransportHandlerOutputStream(final PrintService printService) {
            this.printService = printService;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write(final int b) throws IOException {
            synchronized (this) {
                this.checkOverflow();
                this.buffer.put((byte) b);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void flush() throws IOException {
            synchronized (this) {
                this.buffer.flip();
                if (this.buffer.hasRemaining()) {
                    try (InputStream is = new ByteBufferInputStream(this.buffer)) {
                        final DocAttributeSet attributeSet = new HashDocAttributeSet();
                        final SimpleDoc pdfDoc = new SimpleDoc(is, DocFlavor.INPUT_STREAM.PDF, attributeSet);
                        final DocPrintJob printJob = this.printService.createPrintJob();

                        try {
                            printJob.print(pdfDoc, new HashPrintRequestAttributeSet());
                        }
                        catch (final PrintException e) {
                            e.printStackTrace();
                            this.LOG.error(e.getMessage(), e);
                        }
                    }
                    this.buffer.clear();
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void close() throws IOException {
            synchronized (this) {
                if (!this.closing) {
                    this.closing = true;
                    this.flush();
                    this.buffer = null;
                }
            }
        }

        private void checkOverflow() {
            if (((Integer.SIZE / 8) + this.buffer.position()) >= this.buffer.capacity()) {
                final ByteBuffer newBuffer = ByteBuffer.allocate((this.buffer.position() + (Integer.SIZE / 8)) * 2);
                final int pos = this.buffer.position();
                this.buffer.flip();
                newBuffer.put(this.buffer);
                this.buffer = newBuffer;
                this.buffer.position(pos);
            }
        }
    }

    public static class ByteBufferInputStream extends InputStream {

        private final ByteBuffer buf;

        public ByteBufferInputStream(final ByteBuffer buf) {
            this.buf = buf;
        }

        @Override
        public int read() throws IOException {
            if (!this.buf.hasRemaining()) {
                return -1;
            }
            return this.buf.get() & 0xFF;
        }

        @Override
        public int read(final byte[] bytes, final int off, final int len) throws IOException {
            if (!this.buf.hasRemaining()) {
                return -1;
            }

            final int length = Math.min(len, this.buf.remaining());
            this.buf.get(bytes, off, length);
            return length;
        }
    }

}

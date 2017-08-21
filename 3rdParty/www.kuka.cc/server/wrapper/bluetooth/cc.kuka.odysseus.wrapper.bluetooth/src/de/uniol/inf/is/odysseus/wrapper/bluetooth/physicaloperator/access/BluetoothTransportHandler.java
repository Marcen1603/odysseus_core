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
package de.uniol.inf.is.odysseus.wrapper.bluetooth.physicaloperator.access;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.bluetooth.RemoteDeviceHelper;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class BluetoothTransportHandler extends AbstractPushTransportHandler implements DiscoveryListener {
    public static final String NAME = "Bluetooth";

    /** Logger */
    private final Logger LOG = LoggerFactory.getLogger(BluetoothTransportHandler.class);


    /** Init parameter. */
    public static final String SERVICE = "SERVICE";

    private LocalDevice localDevice;
    private DiscoveryAgent discoveryAgent;
    private boolean serviceDiscover = false;

    /**
     *
     */
    public BluetoothTransportHandler() {
        super();
    }

    public BluetoothTransportHandler(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
        super(protocolHandler, options);
        this.init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(final byte[] message) throws IOException {
        throw new IllegalArgumentException("Currently not implemented");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
        final BluetoothTransportHandler handler = new BluetoothTransportHandler(protocolHandler, options);
        return handler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return BluetoothTransportHandler.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processInOpen() throws IOException {
        this.updateDeviceList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processOutOpen() throws IOException {
        throw new IllegalArgumentException("Output not supported");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processInClose() throws IOException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processOutClose() throws IOException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deviceDiscovered(final RemoteDevice btDevice, final DeviceClass cod) {
        final StringBuilder sb = new StringBuilder();
        sb.append(btDevice.getBluetoothAddress()).append(",");
        sb.append(cod.getMajorDeviceClass()).append(",");
        sb.append(cod.getMinorDeviceClass()).append(",");
        sb.append(cod.getServiceClasses()).append(",");
        sb.append(this.getNormelizedRSSI(btDevice)).append("\n");
        try {
            final ByteBuffer charBuffer = getEncoder().encode(CharBuffer.wrap(sb));
            final ByteBuffer buffer = ByteBuffer.allocate(charBuffer.capacity() + 4);
            buffer.putInt(charBuffer.capacity());
            buffer.put(charBuffer);
            this.fireProcess(buffer);
        }
        catch (final CharacterCodingException e) {
            this.LOG.error(e.getMessage(), e);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void inquiryCompleted(final int discType) {
        switch (discType) {
            case DiscoveryListener.INQUIRY_COMPLETED:
                this.LOG.debug("INQUIRY_COMPLETED");
                break;
            case DiscoveryListener.INQUIRY_TERMINATED:
                this.LOG.debug("INQUIRY_TERMINATED");
                break;
            case DiscoveryListener.INQUIRY_ERROR:
                this.LOG.debug("INQUIRY_ERROR");
                break;
            default:
                this.LOG.debug("Unknown Response Code");
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serviceSearchCompleted(final int transID, final int respCode) {
        this.LOG.debug("SERVICE_SEARCH_COMPLETED TransID:" + transID + " RespCode: " + respCode);
        if (respCode == DiscoveryListener.SERVICE_SEARCH_COMPLETED) {
            this.LOG.debug("SERVICE_SEARCH_COMPLETED");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void servicesDiscovered(final int transID, final ServiceRecord[] servRecord) {
        for (int i = 0; i < servRecord.length; i++) {
        }
    }

    private double getNormelizedRSSI(final RemoteDevice btDevice) {
        int rssi = -100;
        try {
            rssi = RemoteDeviceHelper.readRSSI(btDevice);
        }
        catch (final IOException e) {
            this.LOG.warn(e.getMessage(), e);
        }
        return (100.0 + rssi) / 100.0;
    }

    private void init() {
        final OptionMap options = this.getOptionsMap();
        if (options.containsKey(BluetoothTransportHandler.SERVICE)) {
            this.setServiceDiscover(Boolean.parseBoolean(options.get(BluetoothTransportHandler.SERVICE)));
        }
        else {
            this.setServiceDiscover(false);
        }

        try {
            this.localDevice = LocalDevice.getLocalDevice();
            this.discoveryAgent = this.localDevice.getDiscoveryAgent();
            // localDevice.setDiscoverable(DiscoveryAgent.GIAC);
        }
        catch (final BluetoothStateException e) {
            this.LOG.error(e.getMessage(), e);
        }

    }

    /**
     * @param serviceDiscover
     *            the serviceDiscover to set
     */
    public void setServiceDiscover(final boolean serviceDiscover) {
        this.serviceDiscover = serviceDiscover;
    }

    /**
     * @return the serviceDiscover
     */
    public boolean isServiceDiscover() {
        return this.serviceDiscover;
    }

    private void updateDeviceList() {
        try {
            this.discoveryAgent.startInquiry(DiscoveryAgent.GIAC, this);
        }
        catch (final BluetoothStateException e) {
            this.LOG.warn(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSemanticallyEqualImpl(final ITransportHandler other) {
        if (other instanceof BluetoothTransportHandler) {
            return true;
        }
        return false;
    }

}

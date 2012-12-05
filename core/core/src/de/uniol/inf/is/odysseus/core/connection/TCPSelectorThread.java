package de.uniol.inf.is.odysseus.core.connection;

import java.io.IOException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * NIO Selector thread based on the work of
 * Nuno Santos, nfsantos@sapo.pt
 */
public class TCPSelectorThread implements Runnable {
    private static TCPSelectorThread instance;
    private final Selector        selector;
    private final Thread          selectorThread;
    private final List<Runnable>  pendingInvocations = new ArrayList<Runnable>();
    private boolean               closeRequested;

    public static synchronized TCPSelectorThread getInstance() throws IOException {
        if (TCPSelectorThread.instance == null) {
            TCPSelectorThread.instance = new TCPSelectorThread();
        }
        return TCPSelectorThread.instance;
    }

    private TCPSelectorThread() throws IOException {
        this.selector = Selector.open();
        this.selectorThread = new Thread(this);
        this.selectorThread.start();
    }

    public void addChannelInterest(final SelectableChannel channel, final int interest,
            final CallbackErrorHandler errorHandler) {
        this.invoke(new Runnable() {
            @Override
            public void run() {
                try {
                    TCPSelectorThread.this.addChannelInterestNow(channel, interest);
                }
                catch (final IOException e) {
                    errorHandler.handleError(e);
                }
            }
        });
    }

    public void removeChannelInterest(final SelectableChannel channel, final int interest,
            final CallbackErrorHandler errorHandler) {
        this.invoke(new Runnable() {
            @Override
            public void run() {
                try {
                    TCPSelectorThread.this.removeChannelInterestNow(channel, interest);
                }
                catch (final IOException e) {
                    errorHandler.handleError(e);
                }
            }
        });
    }

    public void registerChannel(final SelectableChannel channel, final int selectionKeys,
            final SelectorHandler handlerInfo, final CallbackErrorHandler errorHandler) {
        this.invoke(new Runnable() {
            @Override
            public void run() {
                try {
                    TCPSelectorThread.this.registerChannelNow(channel, selectionKeys, handlerInfo);
                }
                catch (final IOException e) {
                    errorHandler.handleError(e);
                }
            }
        });
    }

    public void invoke(final Runnable run) {
        synchronized (this.pendingInvocations) {
            this.pendingInvocations.add(run);
        }
        this.selector.wakeup();
    }

    public void blockingInvoke(final Runnable task) throws InterruptedException {
        if (Thread.currentThread() == this.selectorThread) {
            task.run();
        }
        else {
            final Object latch = new Object();
            synchronized (latch) {
                this.invoke(new Runnable() {
                    @Override
                    public void run() {
                        task.run();
                        latch.notify();
                    }
                });
                latch.wait();
            }
        }
    }

    void addChannelInterestNow(final SelectableChannel channel, final int interest) throws IOException {
        if (Thread.currentThread() != this.selectorThread) {
            throw new IOException("Method can only be called from selector thread");
        }
        final SelectionKey sk = channel.keyFor(this.selector);
        this.changeKeyInterest(sk, sk.interestOps() | interest);
    }

    void removeChannelInterestNow(final SelectableChannel channel, final int interest) throws IOException {
        if (Thread.currentThread() != this.selectorThread) {
            throw new IOException("Method can only be called from selector thread");
        }
        final SelectionKey sk = channel.keyFor(this.selector);
        this.changeKeyInterest(sk, sk.interestOps() & ~interest);
    }

    void registerChannelNow(final SelectableChannel channel, final int selectionKeys, final SelectorHandler handlerInfo)
            throws IOException {
        if (Thread.currentThread() != this.selectorThread) {
            throw new IOException("Method can only be called from selector thread");
        }

        if (!channel.isOpen()) {
            throw new IOException("Channel is not open.");
        }

        try {
            if (channel.isRegistered()) {
                final SelectionKey sk = channel.keyFor(this.selector);
                assert sk != null : "Channel is already registered with other selector";
                sk.interestOps(selectionKeys);
                final Object previousAttach = sk.attach(handlerInfo);
                assert previousAttach != null;
            }
            else {
                channel.configureBlocking(false);
                channel.register(this.selector, selectionKeys, handlerInfo);
            }
        }
        catch (final Exception e) {
            final IOException ioe = new IOException("Error registering channel.");
            ioe.initCause(e);
            throw ioe;
        }
    }

    private void doInvocations() {
        synchronized (this.pendingInvocations) {
            for (int i = 0; i < this.pendingInvocations.size(); i++) {
                final Runnable task = this.pendingInvocations.get(i);
                task.run();
            }
            this.pendingInvocations.clear();
        }
    }

    private void changeKeyInterest(final SelectionKey sk, final int newInterest) throws IOException {
        try {
            sk.interestOps(newInterest);
        }
        catch (final CancelledKeyException cke) {
            final IOException ioe = new IOException("Failed to change channel interest.");
            ioe.initCause(cke);
            throw ioe;
        }
    }

    @Override
    public void run() {
        while (true) {
            this.doInvocations();
            if (this.closeRequested) {
                return;
            }
            int selectedKeys = 0;
            try {
                selectedKeys = this.selector.select();
            }
            catch (final IOException ioe) {

                ioe.printStackTrace();
                continue;
            }

            if (selectedKeys == 0) {
                continue;
            }

            final Iterator it = this.selector.selectedKeys().iterator();
            while (it.hasNext()) {
                final SelectionKey sk = (SelectionKey) it.next();
                it.remove();
                try {
                    final int readyOps = sk.readyOps();
                    sk.interestOps(sk.interestOps() & ~readyOps);
                    final SelectorHandler handler = (SelectorHandler) sk.attachment();

                    if (sk.isAcceptable()) {

                        ((AcceptorSelectorHandler) handler).onAccept();

                    }
                    else if (sk.isConnectable()) {

                        ((ConnectorSelectorHandler) handler).onConnect();

                    }
                    else {
                        final ReadWriteSelectorHandler rwHandler = (ReadWriteSelectorHandler) handler;

                        if (sk.isReadable()) {

                            rwHandler.onRead();
                        }

                        if (sk.isValid() && sk.isWritable()) {

                            rwHandler.onWrite();
                        }
                    }
                }
                catch (final Throwable t) {
                    t.printStackTrace();
                    return;
                }
            }
        }
    }
}

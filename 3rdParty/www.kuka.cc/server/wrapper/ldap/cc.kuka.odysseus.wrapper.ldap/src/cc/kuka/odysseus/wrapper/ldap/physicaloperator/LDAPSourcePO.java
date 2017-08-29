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
package cc.kuka.odysseus.wrapper.ldap.physicaloperator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.StartFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class LDAPSourcePO extends AbstractSource<Tuple<?>> implements ActionListener {
    private Hashtable<Object, Object> environment;
    private final SDFSchema schema;

    private InitialDirContext dirContext;
    private String filter;
    private String base;
    private TransferThread thread;

    /**
     * Class constructor.
     *
     */
    public LDAPSourcePO(String base, String filter, SDFSchema schema, Hashtable<Object, Object> environment) {
        this.base = base;
        this.filter = filter;
        this.schema = schema;
        this.environment = environment;
    }

    /**
     * Class constructor.
     *
     * @param operator
     */
    public LDAPSourcePO(LDAPSourcePO operator) {
        super(operator);
        this.base = operator.base;
        this.filter = operator.filter;
        this.schema = operator.schema.clone();
        this.environment = operator.environment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void process_open() throws OpenFailedException {
        try {
            this.dirContext = new InitialDirContext(this.environment);
            SearchControls ctls = new SearchControls();
            String[] attributes = new String[this.schema.size()];
            for (int i = 0; i < this.schema.size(); i++) {
                attributes[i] = this.schema.get(i).getAttributeName();
            }
            ctls.setReturningAttributes(attributes);
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            this.thread = new TransferThread(this.dirContext, this.base, this.filter, ctls);
            this.thread.start();
        }
        catch (NamingException e) {
            throw new OpenFailedException(e);
        }
    }

    @Override
    protected void process_start() throws StartFailedException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void process_close() {
        this.thread.interrupt();
        if (this.dirContext != null) {
            try {
                this.dirContext.close();
            }
            catch (NamingException e) {
                e.printStackTrace();
            }
        }
        super.process_close();
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public LDAPSourcePO clone() {
        return new LDAPSourcePO(this);
    }

    private class TransferThread extends Thread {
        NamingEnumeration<SearchResult> answer;

        /**
         * Class constructor.
         *
         * @throws NamingException
         *
         */
        public TransferThread(InitialDirContext dirContext, String base, String filter, SearchControls ctls) throws NamingException {
            this.answer = dirContext.search(base, filter, ctls);
        }

       // @SuppressWarnings("synthetic-access")
        @Override
        public void run() {
            long waitTime = 10l;
            try {
                while ((this.answer.hasMore()) && (!interrupted())) {
                    SearchResult result = this.answer.next();
                    Attributes attributes = result.getAttributes();
                    Tuple<?> t = new Tuple<>(attributes.size(), false);
                    int pos = 0;
                    NamingEnumeration<String> ids = attributes.getIDs();
                    while (ids.hasMore()) {
                        t.setAttribute(pos, attributes.get(ids.next()));
                        pos++;
                    }
                    LDAPSourcePO.this.transfer(t);
                    sleep(waitTime);
                }
                LDAPSourcePO.this.propagateDone();
            }
            catch (NamingException e) {
                e.printStackTrace();
                interrupt();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}

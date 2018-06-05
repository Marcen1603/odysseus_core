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

import javax.naming.NamingException;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class LDAPSinkPO extends AbstractSink<Tuple<ITimeInterval>> implements ActionListener {
    private InitialDirContext dirContext;
    private final SDFSchema schema;
    private Hashtable<Object, Object> environment;
    private String base;

    /**
     * Class constructor.
     *
     */
    public LDAPSinkPO(String base, SDFSchema schema, Hashtable<Object, Object> environment) {
        this.base = base;
        this.schema = schema;
        this.environment = environment;
    }

    /**
     * Class constructor.
     *
     * @param operator
     */
    public LDAPSinkPO(LDAPSinkPO operator) {
        this.base = operator.base;
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
        super.process_open();
        try {
            this.dirContext = new InitialDirContext(this.environment);
        }
        catch (NamingException e) {
            throw new OpenFailedException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void process_next(Tuple<ITimeInterval> object, int port) {
        if (isOpen()) {
            int i = 0;
            ModificationItem[] items = new ModificationItem[this.schema.size()];
            for (final SDFAttribute attribute : this.schema) {
                BasicAttribute attr = new BasicAttribute(attribute.getAttributeName(), object.getAttribute(i));
                ModificationItem mi = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
                items[i] = mi;
                i++;
            }
            try {
                this.dirContext.modifyAttributes("user", items);
            }
            catch (NamingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processPunctuation(IPunctuation punctuation, int port) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void process_close() {
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
    public LDAPSinkPO clone() {
        return new LDAPSinkPO(this);
    }
}

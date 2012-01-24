/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.usermanagement;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import de.uniol.inf.is.odysseus.usermanagement.ISession;


/**
 * @author Christian Kuka <christian@kuka.cc>
 */
// TODO use EH-Cache (http://ehcache.org/) for Sessions (ckuka)
public class SessionStore implements Map<String, ISession> {
    private final Map<String, ISession> sessionStore = new ConcurrentHashMap<String, ISession>();
    private static SessionStore instance;

    private SessionStore() {

    }

    public static SessionStore getInstance() {
        if (SessionStore.instance == null) {
            SessionStore.instance = new SessionStore();
        }
        return SessionStore.instance;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#clear()
     */
    @Override
    public void clear() {
        this.sessionStore.clear();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    @Override
    public boolean containsKey(final Object key) {
        return this.sessionStore.containsKey(key);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    @Override
    public boolean containsValue(final Object value) {
        return this.sessionStore.containsValue(value);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#entrySet()
     */
    @Override
    public Set<Map.Entry<String, ISession>> entrySet() {
        return Collections.unmodifiableSet(this.sessionStore.entrySet());
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#get(java.lang.Object)
     */
    @Override
    public ISession get(final Object key) {
        return this.sessionStore.get(key);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return this.sessionStore.isEmpty();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#keySet()
     */
    @Override
    public Set<String> keySet() {
        return Collections.unmodifiableSet(this.sessionStore.keySet());
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    @Override
    public ISession put(final String key, final ISession value) {
        return ((ConcurrentHashMap<String, ISession>)this.sessionStore).putIfAbsent(key, value);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#remove(java.lang.Object)
     */
    @Override
    public ISession remove(final Object key) {
        return this.sessionStore.remove(key);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#size()
     */
    @Override
    public int size() {
        return this.sessionStore.size();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#values()
     */
    @Override
    public Collection<ISession> values() {
        return Collections.unmodifiableCollection(this.sessionStore.values());
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#putAll(java.util.Map)
     */
    @Override
    public void putAll(final Map<? extends String, ? extends ISession> sessions) {
        this.sessionStore.putAll(sessions);
    }
}

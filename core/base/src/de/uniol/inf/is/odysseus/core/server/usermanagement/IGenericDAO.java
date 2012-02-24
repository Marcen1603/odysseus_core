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
package de.uniol.inf.is.odysseus.core.server.usermanagement;

import java.io.Serializable;
import java.util.List;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @param <T>
 *            The entity class
 * @param <PK>
 *            The class of the primary key
 */
public interface IGenericDAO<T, PK extends Serializable> {

    T create(T entity);

    void delete(T entity);

    T find(PK id);
    
    T findByName(String name);

    List<T> findAll();

//    List<T> findAll(Integer position, Integer max);

//    void refresh(T entity);

    void update(T entity);
}

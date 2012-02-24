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
package de.uniol.inf.is.odysseus.usermanagement.jpa.persistence.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import de.uniol.inf.is.odysseus.core.server.usermanagement.IGenericDAO;
import de.uniol.inf.is.odysseus.usermanagement.jpa.domain.impl.AbstractEntityImpl;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
abstract public class GenericDAOImpl<T extends AbstractEntityImpl<T>, PK extends Serializable> implements IGenericDAO<T, PK> {
    protected EntityManager entityManager;
    private final Class<T> type;
    private Class<?> cls;

    public GenericDAOImpl(final Class<T> type) {
        this.type = type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.usermanagement.persistence.GenericDAO#create
     * (de.uniol.inf.is.odysseus.core.server.usermanagement.domain.AbstractEntity)
     */
    @Override
    public T create(final T entity) {
        final EntityTransaction transaction = this.entityManager.getTransaction();
        transaction.begin();
        try {
            this.entityManager.persist(entity);
            this.entityManager.flush();
        } catch (final Exception e) {
            transaction.rollback();
        } finally {
            if (transaction.isActive()) {
                transaction.commit();
            }
        }
        return entity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.usermanagement.persistence.GenericDAO#delete
     * (de.uniol.inf.is.odysseus.core.server.usermanagement.domain.AbstractEntity)
     */
    @Override
    public void delete(final T entity) {
        final EntityTransaction transaction = this.entityManager.getTransaction();
        transaction.begin();
        try {
            this.entityManager.remove(this.entityManager.merge(entity));
            this.entityManager.flush();
        } catch (final Exception e) {
            transaction.rollback();
        } finally {
            if (transaction.isActive()) {
                transaction.commit();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.usermanagement.persistence.GenericDAO#find(java
     * .io.Serializable)
     */
    @Override
    public T find(final PK id) {
        return this.entityManager.find(this.type, id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.usermanagement.persistence.GenericDAO#findAll()
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll() {
        String entity = this.getEntityClass().getSimpleName();
        entity = entity.substring(0, entity.length() - 4);
        final Query query = this.entityManager.createQuery(String.format("select o from %s o", entity));
        // query.setHint(QueryHints.READ_ONLY, HintValues.TRUE);
        try {
            return query.getResultList();
        } catch (final NoResultException e) {
            return new ArrayList<T>();
        }
    }

//    /*
//     * (non-Javadoc)
//     * 
//     * @see
//     * de.uniol.inf.is.odysseus.core.server.usermanagement.persistence.GenericDAO#findAll
//     * (java.lang.Integer, java.lang.Integer)
//     */
//    @SuppressWarnings("unchecked")
//    @Override
//    public List<T> findAll(final Integer position, final Integer max) {
//        String entity = this.getEntityClass().getSimpleName();
//        entity = entity.substring(0, entity.length() - 4);
//        final Query query = this.entityManager.createQuery(String.format("select o from %s o", entity));
//        query.setFirstResult(position);
//        query.setMaxResults(max);
//        try {
//            return query.getResultList();
//        } catch (final NoResultException e) {
//            return new ArrayList<T>();
//        }
//    }

//    /*
//     * (non-Javadoc)
//     * 
//     * @see
//     * de.uniol.inf.is.odysseus.core.server.usermanagement.persistence.GenericDAO#refresh
//     * (de.uniol.inf.is.odysseus.core.server.usermanagement.domain.AbstractEntity)
//     */
//    @Override
//    public void refresh(final T entity) {
//        final EntityTransaction transaction = this.entityManager.getTransaction();
//        transaction.begin();
//        try {
//            this.entityManager.refresh(entity);
//        } catch (final Exception e) {
//            transaction.rollback();
//        } finally {
//            if (transaction.isActive()) {
//                transaction.commit();
//            }
//        }
//    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.usermanagement.persistence.GenericDAO#update
     * (de.uniol.inf.is.odysseus.core.server.usermanagement.domain.AbstractEntity)
     */
    @Override
    public void update(T entity) {
        final EntityTransaction transaction = this.entityManager.getTransaction();
        transaction.begin();
        try {
            entity = this.entityManager.merge(entity);
        } catch (final Exception e) {
            transaction.rollback();
        } finally {
            if (transaction.isActive()) {
                transaction.commit();
            }
        }
    }

    protected ParameterMap startNamedQuery(final String query) {
        return ParameterMap.getNamedQuery(query);
    }

    protected ParameterMap startQuery(final String query) {
        return ParameterMap.getQuery(query);
    }

    @SuppressWarnings("unchecked")
    protected List<T> getResultList(final ParameterMap query) {
        try {
            return this.fillQuery(query).getResultList();
        } catch (final NoResultException e) {
            return new ArrayList<T>();
        }
    }

    @SuppressWarnings("unchecked")
    protected T getSingleResult(final ParameterMap query) {
        try {
            return (T)this.fillQuery(query).getSingleResult();
        } catch (final NoResultException e) {
            return null;
        } catch (final NonUniqueResultException e) {
            return null;
        }
    }

    protected int executeUpdate(final ParameterMap query) {
        try {
            return this.fillQuery(query).executeUpdate();
        } catch (final NoResultException e) {
            return -1;
        }
    }

    private Class<?> getEntityClass() {
        if (this.cls == null) {
            try {
                final ParameterizedType genericSuperclass = (ParameterizedType)this.getClass().getGenericSuperclass();
                final Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
                this.cls = (Class<?>)actualTypeArguments[0];
            } catch (final Throwable t) {
                this.cls = AbstractEntityImpl.class;
            }
        }
        return this.cls;
    }

    private Query fillQuery(final ParameterMap query) {
        final Query qry = query.isNamedQuery() ? this.entityManager.createNamedQuery(query.getQuery()) : this.entityManager.createQuery(query.getQuery());
        for (final Entry<String, Object> entry : query.entrySet()) {
            qry.setParameter(entry.getKey(), entry.getValue());
        }
        return qry;
    }
}

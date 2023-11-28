package org.suleware.ecommerce.ecommerce.models.dao;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.suleware.ecommerce.ecommerce.models.entity.Cliente;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class ClienteDaoImpl implements IClienteDao {

    @PersistenceContext
    EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return em.createQuery("from Cliente").getResultList();
    }

    @Override
    @Transactional
    public void save(Cliente cliente) {
        em.persist(cliente);
    }

}

package org.suleware.ecommerce.ecommerce.models.dao;

import java.util.List;

import org.springframework.stereotype.Component;
import org.suleware.ecommerce.ecommerce.models.entity.Cliente;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class ClienteDaoImpl implements IClienteDao {

    @PersistenceContext
    EntityManager em;

    @Override
    public List<Cliente> findAll() {
        return em.createQuery("from Cliente").getResultList();
    }

}

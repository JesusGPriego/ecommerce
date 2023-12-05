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

    @Override

    public void save(Cliente cliente) {
        if (cliente.getId() != null && cliente.getId() > 0) {
            em.merge(cliente);
        } else {
            em.persist(cliente);
        }
    }

    @Override
    public Cliente findOne(Long id) {
        return em.find(Cliente.class, id);
    }

    @Override

    public void delete(Long id) {
        Cliente cliente = findOne(id);
        em.remove(cliente);
    }

}

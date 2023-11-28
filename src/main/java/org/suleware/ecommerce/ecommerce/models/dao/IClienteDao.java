package org.suleware.ecommerce.ecommerce.models.dao;

import java.util.List;

import org.suleware.ecommerce.ecommerce.models.entity.Cliente;

public interface IClienteDao {
    public List<Cliente> findAll();
}

package org.suleware.ecommerce.ecommerce.models.dao;

import org.springframework.data.repository.CrudRepository;
import org.suleware.ecommerce.ecommerce.models.entity.Cliente;

public interface IClienteDao extends CrudRepository<Cliente, Long> {
}

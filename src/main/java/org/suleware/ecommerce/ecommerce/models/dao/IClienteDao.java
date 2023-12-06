package org.suleware.ecommerce.ecommerce.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.suleware.ecommerce.ecommerce.models.entity.Cliente;

public interface IClienteDao extends JpaRepository<Cliente, Long> {
}

package org.suleware.ecommerce.ecommerce.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.suleware.ecommerce.ecommerce.models.entity.Cliente;

public interface IClienteService {
    public List<Cliente> findAll();

    public Page<Cliente> findAll(int pageNo, int pageSize);

    public void save(Cliente cliente);

    public Cliente findOne(Long id);

    public void delete(Long id);
}

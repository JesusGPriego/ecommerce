package org.suleware.ecommerce.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.suleware.ecommerce.ecommerce.models.dao.IClienteDao;
import org.suleware.ecommerce.ecommerce.models.entity.Cliente;

@Controller
public class AppController {

    @Autowired
    private IClienteDao clienteDao;

    @GetMapping(value = "/listar")
    public String listar(Model model) {
        model.addAttribute("titulo", "Lista de clientes");
        List<Cliente> clientes = clienteDao.findAll();
        model.addAttribute("clientes", clientes);
        return "listar";
    }

}

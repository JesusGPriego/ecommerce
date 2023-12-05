package org.suleware.ecommerce.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.suleware.ecommerce.ecommerce.models.dao.IClienteDao;
import org.suleware.ecommerce.ecommerce.models.entity.Cliente;

import jakarta.validation.Valid;

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

    @GetMapping(value = "/form")
    public String crear(Model model) {

        model.addAttribute("titulo", "Formulario de Cliente");

        Cliente cliente = new Cliente();
        model.addAttribute("cliente", cliente);

        return "form";
    }

    @PostMapping("/form")
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de cliente");
            return "form";
        }

        clienteDao.save(cliente);
        return "redirect:/listar";
    }

}

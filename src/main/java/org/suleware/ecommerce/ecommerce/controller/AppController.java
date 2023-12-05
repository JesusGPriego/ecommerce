package org.suleware.ecommerce.ecommerce.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.suleware.ecommerce.ecommerce.models.entity.Cliente;
import org.suleware.ecommerce.ecommerce.models.service.IClienteService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@SessionAttributes("cliente")
public class AppController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping(value = "/listar")
    public String listar(Model model) {
        model.addAttribute("titulo", "Lista de clientes");
        List<Cliente> clientes = clienteService.findAll();
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
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model, SessionStatus status) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de cliente");
            return "form";
        }

        clienteService.save(cliente);
        status.setComplete();
        return "redirect:/listar";
    }

    @GetMapping("/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Model model) {
        Cliente cliente = null;
        if (id > 0) {
            cliente = clienteService.findOne(id);
        } else {
            return "listar";
        }
        model.addAttribute("cliente", cliente);
        return "form";
    }

    @GetMapping(value = "/eliminar")
    public String getMethodName(@RequestParam(value = "id") Long id) {
        if (id > 0) {
            clienteService.delete(id);
        }
        return "redirect:/listar";
    }

}

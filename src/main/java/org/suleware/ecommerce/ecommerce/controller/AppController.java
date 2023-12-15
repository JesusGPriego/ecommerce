package org.suleware.ecommerce.ecommerce.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.suleware.ecommerce.ecommerce.models.entity.Cliente;
import org.suleware.ecommerce.ecommerce.models.service.IClienteService;
import org.suleware.ecommerce.ecommerce.util.paginator.PageRender;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@SessionAttributes("cliente")
public class AppController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping(value = "/listar")
    public String listar(@RequestParam(name = "page", defaultValue = "0") int pageNo, Model model) {

        Pageable pageable = PageRequest.of(pageNo, 4);

        Page<Cliente> clientes = clienteService.findAll(pageable);

        PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

        // Pasamos dato/info a la vista con model:
        model.addAttribute("titulo", "Listado de clientes:");
        model.addAttribute("clientes", clientes);
        model.addAttribute("page", pageRender);
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
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model, RedirectAttributes flash,
            SessionStatus status) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de cliente");
            return "form";
        }

        clienteService.save(cliente);
        status.setComplete();
        flash.addFlashAttribute("success", "Cliente creado con éxito");
        return "redirect:/listar/1";
    }

    @GetMapping("/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
        Cliente cliente = null;
        if (id > 0) {
            cliente = clienteService.findOne(id);
            if (cliente == null) {
                flash.addFlashAttribute("error", "El cliente no existe");
                return "redirect:/listar/1";
            }
        } else {
            flash.addFlashAttribute("error", "Id de cliente no válido");
            return "listar";
        }
        model.addAttribute("cliente", cliente);
        return "form";
    }

    @GetMapping(value = "/eliminar")
    public String getMethodName(@RequestParam(value = "id") Long id, RedirectAttributes flash) {
        if (id > 0) {
            clienteService.delete(id);
            flash.addFlashAttribute("success", "El cliente ha sido eliminado con éxito");
        }
        return "redirect:/listar/1";
    }

}

package org.suleware.ecommerce.ecommerce.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.suleware.ecommerce.ecommerce.models.entity.Cliente;
import org.suleware.ecommerce.ecommerce.models.service.IClienteService;
import org.suleware.ecommerce.ecommerce.util.paginator.PageRender;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@SessionAttributes("cliente")
public class AppController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private IClienteService clienteService;

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
        Path pathFoto = Paths.get("uploads").resolve(filename).toAbsolutePath();
        log.info("pathFoto: " + pathFoto);
        Resource recurso = null;
        try {
            recurso = new UrlResource(pathFoto.toUri());
            if (!recurso.exists() || !recurso.isReadable()) {
                throw new RuntimeException("Error: no se puede cargar la imagen: " + pathFoto.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable(value = "id") long id, Model model, RedirectAttributes flash) {
        Cliente cliente = clienteService.findOne(id);
        if (cliente == null) {
            flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
            return "redirect://listar";
        }
        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "Detalle cliente: " + cliente.getNombre());
        return "ver";
    }

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
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model,
            @RequestParam("file") MultipartFile foto,
            RedirectAttributes flash,
            SessionStatus status) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de cliente");
            return "form";
        }

        if (!foto.isEmpty()) {

            String uniqueFileName = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();

            Path rootPath = Paths.get("uploads").resolve(uniqueFileName);
            Path rootAbsolutePath = rootPath.toAbsolutePath();
            log.info("rootPath: " + rootPath);
            log.info("rootAbsolutePath: " + rootAbsolutePath);

            try {
                Files.copy(foto.getInputStream(), rootAbsolutePath);
                flash.addFlashAttribute("info", "Subido correctamente " + uniqueFileName);
                cliente.setFoto(uniqueFileName);
            } catch (IOException e) {
                e.getMessage();
            }
        }

        clienteService.save(cliente);
        status.setComplete();
        flash.addFlashAttribute("success", "Cliente creado con éxito");
        return "redirect:/listar";
    }

    @GetMapping("/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
        Cliente cliente = null;
        if (id > 0) {
            cliente = clienteService.findOne(id);
            if (cliente == null) {
                flash.addFlashAttribute("error", "El cliente no existe");
                return "redirect:/listar";
            }
        } else {
            flash.addFlashAttribute("error", "Id de cliente no válido");
            return "listar";
        }
        model.addAttribute("cliente", cliente);
        return "form";
    }

    @GetMapping(value = "/eliminar/{id}")
    public String getMethodName(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
        if (id > 0) {
            Cliente cliente = clienteService.findOne(id);

            clienteService.delete(id);
            flash.addFlashAttribute("success", "Cliente eliminado con éxito!");

        }
        return "redirect:/listar";
    }
}

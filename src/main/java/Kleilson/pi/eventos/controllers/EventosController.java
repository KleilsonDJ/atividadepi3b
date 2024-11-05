package Kleilson.pi.eventos.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import Kleilson.pi.eventos.models.Evento;
import Kleilson.pi.eventos.repositories.EventoRepository;

@Controller
@RequestMapping("/eventos")
public class EventosController {

    @Autowired
    private EventoRepository er;

    @GetMapping("/form")
    public String form() {
        return "eventos/formEvento"; 
    }

    @PostMapping
    public String adicionar(Evento evento) {
        er.save(evento);
        return "redirect:/eventos"; // Verifique se existe um método GetMapping("/") em "/eventos"
    }

    @GetMapping
    public ModelAndView lista() {
        List<Evento> eventos = er.findAll();
        ModelAndView mv = new ModelAndView("eventos/lista");
        mv.addObject("eventos", eventos);
        return mv;
    }

    @GetMapping("/{id}")
    public ModelAndView detalhamento(@PathVariable Long id) {
        ModelAndView md = new ModelAndView();
        Optional<Evento> opt = er.findById(id);
        if (opt.isEmpty()) {
            md.setViewName("redirect:/eventos");
            return md;
        }

        Evento evento = opt.get();
        md.setViewName("eventos/detalhes");
        md.addObject("evento", evento);

        return md;
    }
}

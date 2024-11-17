package Kleilson.pi.eventos.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import Kleilson.pi.eventos.models.Convidado;
import Kleilson.pi.eventos.models.Evento;
import Kleilson.pi.eventos.repositories.ConvidadoRepository;
import Kleilson.pi.eventos.repositories.EventoRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/eventos")
public class EventosController {

    @Autowired
    private EventoRepository er;
    @Autowired
    private ConvidadoRepository cr;

    @GetMapping("/form")
    public String form(Evento evento) {
        return "eventos/formEvento"; 
    }

    @PostMapping
    public String salvar(@Valid Evento evento, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return form(evento); 
        }
        er.save(evento);
        attributes.addFlashAttribute("mensagem", "Evento Salvo com sucesso!");
        return "redirect:/eventos"; 
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
        ModelAndView mv = new ModelAndView();
        Optional<Evento> opt = er.findById(id);
        if (opt.isEmpty()) {
            mv.setViewName("redirect:/eventos");
            return mv;
        }

        Evento evento = opt.get();
        mv.setViewName("eventos/detalhes");
        mv.addObject("evento", evento);

        List<Convidado> convidados = cr.findByEvento(evento);
        mv.addObject("convidados", convidados);

        return mv;
    }

    @PostMapping("/{idEventos}")
    public String salvaConvidado(@PathVariable Long idEventos, @Valid Convidado convidado, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "redirect:/eventos/" + idEventos;
        }

        Optional<Evento> opt = er.findById(idEventos);
        if (opt.isEmpty()) {
            return "redirect:/eventos";
        }
        Evento evento = opt.get();
        convidado.setEvento(evento);

        cr.save(convidado);
        attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
        return "redirect:/eventos/" + idEventos;
    }

    @GetMapping("/{id}/selecionar")
    public ModelAndView selecionarEvento(@PathVariable Long id) {
        ModelAndView md = new ModelAndView();
        Optional<Evento> opt = er.findById(id);
        if (opt.isEmpty()) {
            md.setViewName("redirect:/eventos");
            return md;
        }

        Evento evento = opt.get();
        md.setViewName("eventos/formEvento");
        md.addObject("evento", evento);

        return md;
    }

    @GetMapping("/{idEvento}/convidados/{idConvidado}/selecionar")
    public ModelAndView selecionarConvidado(@PathVariable Long idEvento, @PathVariable Long idConvidado) {
        ModelAndView md = new ModelAndView();

        Optional<Evento> optEvento = er.findById(idEvento);
        Optional<Convidado> optConvidado = cr.findById(idConvidado);

        if (optEvento.isEmpty() || optConvidado.isEmpty()) {
            md.setViewName("redirect:/eventos");
            return md;
        }

        Evento evento = optEvento.get();
        Convidado convidado = optConvidado.get();

        if (!evento.getId().equals(convidado.getEvento().getId())) {
            md.setViewName("redirect:/eventos");
            return md;
        }

        md.setViewName("eventos/detalhes");
        md.addObject("convidado", convidado);
        md.addObject("evento", evento);
        md.addObject("convidados", cr.findByEvento(evento));

        return md;
    }

    @GetMapping("/{id}/remover")
    public String apagarEvento(@PathVariable Long id, RedirectAttributes attributes) {
        Optional<Evento> opt = er.findById(id);
        if (opt.isPresent()) {
            Evento evento = opt.get();
            List<Convidado> convidados = cr.findByEvento(evento);
            cr.deleteAll(convidados);
            er.delete(evento);
            attributes.addFlashAttribute("mensagem", "Evento removido com sucesso!");
        }
        return "redirect:/eventos";
    }

    @GetMapping("/{idEventos}/convidados/{idConvidado}/remover")
    public String removerConvidado(@PathVariable Long idEventos, @PathVariable Long idConvidado, RedirectAttributes attributes) {
        Optional<Convidado> optConvidado = cr.findById(idConvidado);
        if (optConvidado.isPresent()) {
            Convidado convidado = optConvidado.get();

            if (convidado.getEvento().getId().equals(idEventos)) {
                cr.delete(convidado);
                attributes.addFlashAttribute("mensagem", "Convidado removido com sucesso!");
            }
        }
        return "redirect:/eventos/" + idEventos; // A mensagem será exibida na página de detalhes do evento
    }

}  
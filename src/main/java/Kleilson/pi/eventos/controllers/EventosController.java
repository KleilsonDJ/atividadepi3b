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

import Kleilson.pi.eventos.models.Convidado;
import Kleilson.pi.eventos.models.Evento;
import Kleilson.pi.eventos.repositories.ConvidadoRepository;
import Kleilson.pi.eventos.repositories.EventoRepository;

@Controller
@RequestMapping("/eventos")
public class EventosController {

    @Autowired
    private EventoRepository er;
    @Autowired
    private ConvidadoRepository cr;

    @GetMapping("/form")
    public String form() {
        return "eventos/formEvento"; 
    }

    @PostMapping
    public String adicionar(Evento evento) {
        er.save(evento);
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
        ModelAndView md = new ModelAndView();
        Optional<Evento> opt = er.findById(id);
        if (opt.isEmpty()) {
            md.setViewName("redirect:/eventos");
            return md;
        }

        Evento evento = opt.get();
        md.setViewName("eventos/detalhes");
        md.addObject("evento", evento);
        
        List<Convidado> convidados = cr.findByEvento(evento);
        md.addObject("convidados", convidados);
        
        return md;
    }
    
    @PostMapping("/{idEventos}")
    public String salvaConvidado(@PathVariable Long idEventos, Convidado convidado) {
    	
    	System.out.println("Id eventos" +idEventos);
    	System.out.println(convidado);
    	
    	Optional<Evento> opt = er.findById(idEventos);
    	if(opt.isEmpty()) {
    		return "redirect:/eventos";
    	}
    	Evento evento = opt.get();
    	convidado.setEvento(evento);
    	
    	cr.save(convidado);
    	
    	return "redirect:/eventos/{idEventos}";
    }
    @GetMapping("/{id}/remover")
    public String apagarEvento(@PathVariable Long id) {
    	Optional<Evento> opt = er.findById(id);
    	if(!opt.isEmpty()) {
    		Evento evento = opt.get();
    		
    		List<Convidado> convidados = cr.findByEvento(evento);
    		
    		cr.deleteAll(convidados);
    		er.delete(evento);
    	}
    	return "redirect:/eventos";
    }
    
    @GetMapping("/{idEventos}/convidados/{idConvidado}/remover")
    public String removerConvidado(@PathVariable Long idEventos, @PathVariable Long idConvidado) {
        Optional<Convidado> optConvidado = cr.findById(idConvidado);
        if (optConvidado.isPresent()) {
            Convidado convidado = optConvidado.get();
            // Verifique se o convidado está associado ao evento correto
            if (convidado.getEvento().getId().equals(idEventos)) {
                cr.delete(convidado);  // Remove o convidado do evento
            }
        }
        return "redirect:/eventos/" + idEventos;  // Redireciona para os detalhes do evento
    }
    

}
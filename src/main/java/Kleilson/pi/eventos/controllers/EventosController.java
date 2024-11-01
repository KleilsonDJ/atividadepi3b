package Kleilson.pi.eventos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import Kleilson.pi.eventos.models.Evento;
import Kleilson.pi.eventos.repositories.EventoRepository;


@Controller
public class EventosController {
	
	@Autowired
	private EventoRepository er;
	
    // Exibe o formulário de evento
    @RequestMapping("/eventos/form")
    public String form() {
        return "eventos/formEvento"; // Retorna a página do formulário
    }
   @PostMapping("/eventos")
    public String adicionar(Evento evento) {
	   System.out.println(evento);
	   er.save(evento);
    	return "eventos/evento-adicionar";
    }
   }

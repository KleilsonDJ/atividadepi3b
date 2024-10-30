package Kleilson.pi.eventos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import Kleilson.pi.eventos.models.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long>{

}

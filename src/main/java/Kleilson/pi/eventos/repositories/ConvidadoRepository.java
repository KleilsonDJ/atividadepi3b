package Kleilson.pi.eventos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import Kleilson.pi.eventos.models.Convidado;

public interface ConvidadoRepository extends JpaRepository<Convidado,Long> {

}

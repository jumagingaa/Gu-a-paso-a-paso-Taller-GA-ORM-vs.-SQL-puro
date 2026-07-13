package ec.edu.uteq.tallerjpa.repository;

import ec.edu.uteq.tallerjpa.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByNombre(String nombre);
}
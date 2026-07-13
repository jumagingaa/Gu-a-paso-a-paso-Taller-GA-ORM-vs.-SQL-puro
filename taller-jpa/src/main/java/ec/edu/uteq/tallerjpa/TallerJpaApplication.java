package ec.edu.uteq.tallerjpa;

import ec.edu.uteq.tallerjpa.entity.Producto;
import ec.edu.uteq.tallerjpa.service.ProductoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
public class TallerJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TallerJpaApplication.class, args);
    }

    @Bean
    CommandLineRunner ejecutar(ProductoService service) {
        return args -> {

            // -------------------------------
            // Medición con nanoTime
            // -------------------------------
            long inicio = System.nanoTime();

            List<Producto> productos = service.listar();

            long fin = System.nanoTime();

            System.out.printf(
                    "nanoTime: %d productos en %.3f ms%n",
                    productos.size(),
                    (fin - inicio) / 1_000_000.0
            );

            // -------------------------------
            // Medición con StopWatch
            // -------------------------------
            StopWatch sw = new StopWatch("listar-jpa");

            sw.start("SELECT productos");

            service.listar();

            sw.stop();

            System.out.printf(
                    "StopWatch: %.3f ms%n",
                    sw.getTotalTimeNanos() / 1_000_000.0
            );

            System.out.println(sw.prettyPrint());

            // -------------------------------
            // Crear producto
            // -------------------------------
            Producto nuevo = new Producto();

            nuevo.setNombre("Producto JPA");
            nuevo.setPrecio(new BigDecimal("99.99"));
            nuevo.setStock(10);

            Producto creado = service.guardar(nuevo);

            System.out.println("Creado ID: " + creado.getId());

            // -------------------------------
            // Eliminar
            // -------------------------------
            service.eliminar(creado.getId());

            System.out.println("Producto eliminado.");
        };
    }

}
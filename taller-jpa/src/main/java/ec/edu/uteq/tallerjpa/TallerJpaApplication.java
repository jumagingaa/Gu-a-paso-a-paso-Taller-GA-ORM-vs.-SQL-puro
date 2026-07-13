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

            System.out.println("=== VERSION SPRING DATA JPA ===");

            // ------------------------------------------------------------
            // 1. Medición con System.nanoTime()
            // ------------------------------------------------------------
            long inicio = System.nanoTime();

            List<Producto> lista1 = service.listar();

            long fin = System.nanoTime();

            double ms1 = (fin - inicio) / 1_000_000.0;

            System.out.printf(
                    "nanoTime : %d filas en %.3f ms%n",
                    lista1.size(),
                    ms1
            );

            // ------------------------------------------------------------
            // 2. Medición con Spring StopWatch
            // ------------------------------------------------------------
            StopWatch sw = new StopWatch("listar-jpa");

            sw.start("SELECT * FROM productos");

            List<Producto> lista2 = service.listar();

            sw.stop();

            System.out.printf(
                    "StopWatch: %d filas en %.3f ms%n",
                    lista2.size(),
                    sw.getTotalTimeNanos() / 1_000_000.0
            );

            System.out.println(sw.prettyPrint());

            // ------------------------------------------------------------
            // 3. Crear producto
            // ------------------------------------------------------------
            Producto nuevo = new Producto();

            nuevo.setNombre("Producto de prueba");
            nuevo.setPrecio(new BigDecimal("99.99"));
            nuevo.setStock(5);

            Producto creado = service.guardar(nuevo);

            System.out.println("Creado con id = " + creado.getId());

            // ------------------------------------------------------------
            // 4. Eliminar producto
            // ------------------------------------------------------------
            service.eliminar(creado.getId());

            System.out.println("Eliminado: true");
        };
    }
}
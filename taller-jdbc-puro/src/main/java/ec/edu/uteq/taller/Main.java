package ec.edu.uteq.taller;

import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        ProductoRepositorioJdbc seguro = new ProductoRepositorioJdbc();
        ProductoRepositorioInseguro inseguro = new ProductoRepositorioInseguro();

        // ==========================================================
        // 1. SQL Injection (Versión insegura)
        // ==========================================================

        String ataque = "' OR '1'='1";

        System.out.println("========================================");
        System.out.println("VERSION INSEGURA (SQL Injection)");
        System.out.println("========================================");

        List<Producto> filasInseguras =
                inseguro.buscarPorNombreInseguro(ataque);

        System.out.println("Filas obtenidas: " + filasInseguras.size());

        // ==========================================================
        // 2. PreparedStatement
        // ==========================================================

        System.out.println();
        System.out.println("========================================");
        System.out.println("VERSION SEGURA");
        System.out.println("========================================");

        List<Producto> filasSeguras =
                seguro.buscarPorNombreSeguro(ataque);

        System.out.println("Filas obtenidas: " + filasSeguras.size());

        // ==========================================================
        // 3. nanoTime
        // ==========================================================

        long inicio = System.nanoTime();

        List<Producto> lista1 = seguro.listar();

        long fin = System.nanoTime();

        double tiempo = (fin - inicio) / 1_000_000.0;

        System.out.printf("%nListado con nanoTime: %d productos en %.3f ms%n",
                lista1.size(), tiempo);

        // ==========================================================
        // 4. StopWatch
        // ==========================================================

        StopWatch sw = new StopWatch("listar");

        sw.start("SELECT productos");

        List<Producto> lista2 = seguro.listar();

        sw.stop();

        System.out.printf(
                "Listado con StopWatch: %d productos en %.3f ms%n",
                lista2.size(),
                sw.getTotalTimeNanos() / 1_000_000.0
        );

        System.out.println(sw.prettyPrint());

        // ==========================================================
        // 5. Crear
        // ==========================================================

        Long id = seguro.crear(
                "Producto de prueba",
                new BigDecimal("99.99"),
                10
        );

        System.out.println("Producto creado con ID: " + id);

        // ==========================================================
        // 6. Eliminar
        // ==========================================================

        boolean eliminado = seguro.eliminar(id);

        System.out.println("Producto eliminado: " + eliminado);

    }
}
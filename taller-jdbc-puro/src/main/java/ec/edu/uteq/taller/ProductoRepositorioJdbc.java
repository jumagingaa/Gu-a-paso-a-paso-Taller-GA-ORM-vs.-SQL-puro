package ec.edu.uteq.taller;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositorioJdbc {

    public List<Producto> listar() throws SQLException {

        String sql = "SELECT id, nombre, precio, stock FROM productos ORDER BY id";

        List<Producto> lista = new ArrayList<>();

        try (
                Connection con = Conexion.abrir();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                lista.add(new Producto(
                        rs.getLong("id"),
                        rs.getString("nombre"),
                        rs.getBigDecimal("precio"),
                        rs.getInt("stock")
                ));

            }

        }

        return lista;
    }

    public Long crear(String nombre, BigDecimal precio, int stock)
            throws SQLException {

        String sql =
                "INSERT INTO productos(nombre, precio, stock) VALUES (?, ?, ?)";

        try (
                Connection con = Conexion.abrir();
                PreparedStatement ps =
                        con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            ps.setString(1, nombre);
            ps.setBigDecimal(2, precio);
            ps.setInt(3, stock);

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {

                if (keys.next()) {
                    return keys.getLong(1);
                }

            }

        }

        return null;
    }

    public boolean eliminar(long id) throws SQLException {

        String sql = "DELETE FROM productos WHERE id = ?";

        try (
                Connection con = Conexion.abrir();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setLong(1, id);

            int filas = ps.executeUpdate();

            return filas == 1;
        }

    }

}
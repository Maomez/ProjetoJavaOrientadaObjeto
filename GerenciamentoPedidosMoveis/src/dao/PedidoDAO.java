package dao;

import model.Cliente;
import model.Movel;
import model.Pedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PedidoDAO {
    private Connection conn;
    private ClienteDAO clienteDAO;
    private MovelDAO movelDAO;

    public PedidoDAO(Connection conn, ClienteDAO clienteDAO, MovelDAO movelDAO) {
        this.conn = conn;
        this.clienteDAO = clienteDAO;
        this.movelDAO = movelDAO;
    }

    public void createTables() throws SQLException {
        String sqlPedido = "CREATE TABLE IF NOT EXISTS Pedido (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "cliente_id INTEGER, " +
                "data DATE, " +
                "FOREIGN KEY (cliente_id) REFERENCES Cliente(id))";
        String sqlPedidoMovel = "CREATE TABLE IF NOT EXISTS PedidoMovel (" +
                "pedido_id INTEGER, " +
                "movel_id INTEGER, " +
                "FOREIGN KEY (pedido_id) REFERENCES Pedido(id), " +
                "FOREIGN KEY (movel_id) REFERENCES Movel(id), " +
                "PRIMARY KEY (pedido_id, movel_id))";
        Statement stmt = conn.createStatement();
        stmt.execute(sqlPedido);
        stmt.execute(sqlPedidoMovel);
    }

    public void addPedido(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO Pedido (cliente_id, data) VALUES (?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, pedido.getCliente().getId());
        pstmt.setDate(2, new java.sql.Date(pedido.getData().getTime()));
        pstmt.executeUpdate();

        ResultSet rs = pstmt.getGeneratedKeys();
        if (rs.next()) {
            int pedidoId = rs.getInt(1);
            for (Movel movel : pedido.getMoveis()) {
                String sqlMovel = "INSERT INTO PedidoMovel (pedido_id, movel_id) VALUES (?, ?)";
                PreparedStatement pstmtMovel = conn.prepareStatement(sqlMovel);
                pstmtMovel.setInt(1, pedidoId);
                pstmtMovel.setInt(2, movel.getId());
                pstmtMovel.executeUpdate();
            }
        }
    }

    public Pedido getPedido(int id) throws SQLException {
        String sql = "SELECT * FROM Pedido WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            Cliente cliente = clienteDAO.getCliente(rs.getInt("cliente_id"));
            Date data = rs.getDate("data");
            List<Movel> moveis = getMoveisByPedidoId(id);
            return new Pedido(id, cliente, moveis, data);
        }
        return null;
    }

    private List<Movel> getMoveisByPedidoId(int pedidoId) throws SQLException {
        List<Movel> moveis = new ArrayList<>();
        String sql = "SELECT movel_id FROM PedidoMovel WHERE pedido_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, pedidoId);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Movel movel = movelDAO.getMovel(rs.getInt("movel_id"));
            if (movel != null) {
                moveis.add(movel);
            }
        }
        return moveis;
    }

    public List<Pedido> getAllPedidos() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM Pedido";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            Cliente cliente = clienteDAO.getCliente(rs.getInt("cliente_id"));
            Date data = rs.getDate("data");
            List<Movel> moveis = getMoveisByPedidoId(rs.getInt("id"));
            pedidos.add(new Pedido(rs.getInt("id"), cliente, moveis, data));
        }
        return pedidos;
    }

    public void updatePedido(Pedido pedido) throws SQLException {
        String sql = "UPDATE Pedido SET cliente_id = ?, data = ? WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, pedido.getCliente().getId());
        pstmt.setDate(2, new java.sql.Date(pedido.getData().getTime()));
        pstmt.setInt(3, pedido.getId());
        pstmt.executeUpdate();

        String deleteSql = "DELETE FROM PedidoMovel WHERE pedido_id = ?";
        PreparedStatement deletePstmt = conn.prepareStatement(deleteSql);
        deletePstmt.setInt(1, pedido.getId());
        deletePstmt.executeUpdate();

        for (Movel movel : pedido.getMoveis()) {
            String insertSql = "INSERT INTO PedidoMovel (pedido_id, movel_id) VALUES (?, ?)";
            PreparedStatement insertPstmt = conn.prepareStatement(insertSql);
            insertPstmt.setInt(1, pedido.getId());
            insertPstmt.setInt(2, movel.getId());
            insertPstmt.executeUpdate();
        }
    }

    public void deletePedido(int id) throws SQLException {
        String deleteMovelSql = "DELETE FROM PedidoMovel WHERE pedido_id = ?";
        PreparedStatement deleteMovelPstmt = conn.prepareStatement(deleteMovelSql);
        deleteMovelPstmt.setInt(1, id);
        deleteMovelPstmt.executeUpdate();

        String deleteSql = "DELETE FROM Pedido WHERE id = ?";
        PreparedStatement deletePstmt = conn.prepareStatement(deleteSql);
        deletePstmt.setInt(1, id);
        deletePstmt.executeUpdate();
    }
}

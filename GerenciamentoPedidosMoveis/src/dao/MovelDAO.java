package dao;

import model.Movel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MovelDAO {
    private Connection conn;

    public MovelDAO(Connection conn) {
        this.conn = conn;
    }

    public void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Movel (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "descricao TEXT NOT NULL, " +
                "preco REAL NOT NULL)";
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }

    public void addMovel(Movel movel) throws SQLException {
        String sql = "INSERT INTO Movel (descricao, preco) VALUES (?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, movel.getDescricao());
        pstmt.setDouble(2, movel.getPreco());
        pstmt.executeUpdate();
    }

    public Movel getMovel(int id) throws SQLException {
        String sql = "SELECT * FROM Movel WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return new Movel(rs.getInt("id"), rs.getString("descricao"), rs.getDouble("preco"));
        }
        return null;
    }

    public List<Movel> getAllMoveis() throws SQLException {
        List<Movel> moveis = new ArrayList<>();
        String sql = "SELECT * FROM Movel";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            moveis.add(new Movel(rs.getInt("id"), rs.getString("descricao"), rs.getDouble("preco")));
        }
        return moveis;
    }

    public void updateMovel(Movel movel) throws SQLException {
        String sql = "UPDATE Movel SET descricao = ?, preco = ? WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, movel.getDescricao());
        pstmt.setDouble(2, movel.getPreco());
        pstmt.setInt(3, movel.getId());
        pstmt.executeUpdate();
    }

    public void deleteMovel(int id) throws SQLException {
        String sql = "DELETE FROM Movel WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
    }
}

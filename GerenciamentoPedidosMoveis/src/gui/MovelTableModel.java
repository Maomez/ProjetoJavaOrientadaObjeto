package gui;

import dao.DatabaseConnection;
import dao.MovelDAO;
import model.Movel;

import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.List;

public class MovelTableModel extends AbstractTableModel {
    private List<Movel> moveis;
    private final String[] columnNames = {"ID", "Descrição", "Preço"};

    public MovelTableModel() {
        try {
            moveis = new MovelDAO(DatabaseConnection.getConnection()).getAllMoveis();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getRowCount() {
        return moveis.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Movel movel = moveis.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return movel.getId();
            case 1:
                return movel.getDescricao();
            case 2:
                return movel.getPreco();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}

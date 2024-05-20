package gui;

import dao.ClienteDAO;
import dao.DatabaseConnection;
import model.Cliente;

import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.List;

public class ClienteTableModel extends AbstractTableModel {
    private List<Cliente> clientes;
    private final String[] columnNames = {"ID", "Nome", "Endereço"};

    public ClienteTableModel() {
        try {
            clientes = new ClienteDAO(DatabaseConnection.getConnection()).getAllClientes();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getRowCount() {
        return clientes.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Cliente cliente = clientes.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return cliente.getId();
            case 1:
                return cliente.getNome();
            case 2:
                return cliente.getEndereco();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}

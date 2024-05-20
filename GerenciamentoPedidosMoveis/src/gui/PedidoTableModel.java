package gui;

import dao.PedidoDAO;
import model.Pedido;
import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.List;

public class PedidoTableModel extends AbstractTableModel {
    private List<Pedido> pedidos;
    private final String[] columnNames = {"ID", "Cliente", "Data"};

    public PedidoTableModel(PedidoDAO pedidoDAO) {
        try {
            pedidos = pedidoDAO.getAllPedidos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getRowCount() {
        return pedidos.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Pedido pedido = pedidos.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return pedido.getId();
            case 1:
                return (pedido.getCliente() != null) ? pedido.getCliente().getNome() : "Cliente desconhecido";
            case 2:
                return pedido.getData();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}

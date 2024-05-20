package gui;

import dao.ClienteDAO;
import dao.DatabaseConnection;
import dao.MovelDAO;
import dao.PedidoDAO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.SQLException;

public class MainFrame extends JFrame {
    private ClienteDAO clienteDAO;
    private MovelDAO movelDAO;
    private PedidoDAO pedidoDAO;

    private JTable clienteTable;
    private JTable movelTable;
    private JTable pedidoTable;

    public MainFrame() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            clienteDAO = new ClienteDAO(conn);
            movelDAO = new MovelDAO(conn);
            pedidoDAO = new PedidoDAO(conn, clienteDAO, movelDAO);

            clienteDAO.createTable();
            movelDAO.createTable();
            pedidoDAO.createTables();

            initUI();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initUI() {
        setTitle("Gestão de Clientes, Móveis e Pedidos");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel clientePanel = new JPanel(new BorderLayout());
        clienteTable = new JTable(new ClienteTableModel());
        clientePanel.add(new JScrollPane(clienteTable), BorderLayout.CENTER);

        JButton addClienteButton = new JButton("Adicionar Cliente");
        addClienteButton.addActionListener(e -> {
        });
        clientePanel.add(addClienteButton, BorderLayout.SOUTH);

        JPanel movelPanel = new JPanel(new BorderLayout());
        movelTable = new JTable(new MovelTableModel());
        movelPanel.add(new JScrollPane(movelTable), BorderLayout.CENTER);

        JButton addMovelButton = new JButton("Adicionar Móvel");
        addMovelButton.addActionListener(e -> {
        });
        movelPanel.add(addMovelButton, BorderLayout.SOUTH);

        JPanel pedidoPanel = new JPanel(new BorderLayout());
        pedidoTable = new JTable(new PedidoTableModel(pedidoDAO));
        pedidoPanel.add(new JScrollPane(pedidoTable), BorderLayout.CENTER);

        JButton addPedidoButton = new JButton("Adicionar Pedido");
        addPedidoButton.addActionListener(e -> {
        });
        pedidoPanel.add(addPedidoButton, BorderLayout.SOUTH);

        tabbedPane.addTab("Clientes", clientePanel);
        tabbedPane.addTab("Móveis", movelPanel);
        tabbedPane.addTab("Pedidos", pedidoPanel);

        add(tabbedPane);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}

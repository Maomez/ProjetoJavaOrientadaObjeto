package model;

import java.util.Date;
import java.util.List;

public class Pedido {
    private final int id;
    private final Cliente cliente;
    private final List<Movel> moveis;
    private final Date data;

    public Pedido(int id, Cliente cliente, List<Movel> moveis, Date data) {
        this.id = id;
        this.cliente = cliente;
        this.moveis = moveis;
        this.data = data;
    }

    public int getId() { return id; }

    public Cliente getCliente() { return cliente; }

    public List<Movel> getMoveis() { return moveis; }

    public Date getData() { return data; }
}

package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Colecao;

public class ColecaoDaoJdbc implements InterfaceDao<Colecao> {
    private Connection conn;

    public ColecaoDaoJdbc() throws SQLException {
        this.conn = ConnFactory.getConnection();
    }

    @Override
    public void incluir(Colecao entidade) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO minhacolecao (nome, valor, tipo, localImage, cardGameName, possuir) VALUES(?, ?, ?, ?, ?, ?)");
            ps.setString(1, entidade.getNome());
            ps.setDouble(2, Double.parseDouble(entidade.getValor()));
            ps.setString(3, entidade.getTipo());
            ps.setString(4, entidade.getLocalImage());
            ps.setString(5, entidade.getCardGameName());
            ps.setBoolean(6, entidade.isPossuir());
            ps.execute();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public void editar(Colecao entidade) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE minhacolecao SET nome=?, valor=?, tipo=?, localImage=?, cardGameName=?, possuir=? WHERE id=?");
            ps.setString(1, entidade.getNome());
            ps.setDouble(2, Double.parseDouble(entidade.getValor()));
            ps.setString(3, entidade.getTipo());
            ps.setString(4, entidade.getLocalImage());
            ps.setString(5, entidade.getCardGameName());
            ps.setBoolean(6, entidade.isPossuir());
            ps.setInt(7, entidade.getId());

            ps.execute();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public void excluir(Colecao entidade) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM minhacolecao WHERE id=?");

            ps.setInt(1, entidade.getId());
            ps.execute();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public Colecao pesquisarPorId(int id) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM minhacolecao");
            ResultSet rs = ps.executeQuery();
            ArrayList<Colecao> lista = new ArrayList<>();

            while (rs.next()) {
                Colecao c1 = new Colecao();

                c1.setId(rs.getInt("id"));
                c1.setNome(rs.getString("nome"));
                c1.setValor(Double.toString(rs.getDouble("Valor")));
                c1.setTipo(rs.getString("Tipo"));
                c1.setLocalImage(rs.getString("localImage"));
                c1.setCardGameName(rs.getString("cardGameName"));
                c1.setPossuir(rs.getBoolean("possuir"));
                lista.add(c1);
            }

            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getId() == id) {
                    return lista.get(i);
                }
            }

            return null;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public List<Colecao> listar(String param) throws Exception {
        try {
            PreparedStatement ps = null;
            ResultSet rs = null;

            if (param.equals("")) {
                ps = conn.prepareStatement("SELECT * FROM minhacolecao");
            } else {
                ps = conn.prepareStatement(
                        "SELECT * FROM minhacolecao WHERE nome LIKE ? OR valor LIKE ? OR tipo LIKE ? OR cardGameName LIKE ?");
                String likeParam = "%" + param + "%";
                ps.setString(1, likeParam);
                ps.setString(2, likeParam);
                ps.setString(3, likeParam);
                ps.setString(4, likeParam);
            }

            rs = ps.executeQuery();
            ArrayList<Colecao> lista = new ArrayList<>();
            while (rs.next()) {
                Colecao c = new Colecao();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setValor(Double.toString(rs.getDouble("valor")));
                c.setTipo(rs.getString("tipo"));
                c.setLocalImage(rs.getString("localImage"));
                c.setCardGameName(rs.getString("cardGameName"));
                c.setPossuir(rs.getBoolean("possuir"));
                lista.add(c);
            }

            return lista;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
}

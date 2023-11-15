package model.dao;

public class DaoFactory {
    public static ColecaoDaoJdbc novoColecaoDaoJdbc() throws Exception{
        return new ColecaoDaoJdbc();
    }
}

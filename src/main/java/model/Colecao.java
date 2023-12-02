package model;

public class Colecao {
    private int id;
    private String nome;
    private String valor;
    private String tipo;
    private String localImage;
    private String cardGameName;
    private boolean possuir;

    public Colecao() {

    }

    public Colecao(String nome, String valor, String tipo, String localImage, String cardGameName, boolean possuir) {
        this.nome = nome;
        this.valor = valor;
        this.tipo = tipo;
        this.localImage = localImage;
        this.cardGameName = cardGameName;
        this.possuir = possuir;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLocalImage() {
        return localImage;
    }

    public void setLocalImage(String localImage) {
        this.localImage = localImage;
    }

    public String getCardGameName() {
        return cardGameName;
    }

    public void setCardGameName(String cardGameName) {
        this.cardGameName = cardGameName;
    }

    public boolean isPossuir() {
        return possuir;
    }

    public void setPossuir(boolean possuir) {
        this.possuir = possuir;
    }

}

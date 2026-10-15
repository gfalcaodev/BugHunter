package com.gabriel.bughunter;

public class Bug {

    private final String codigo;
    private final String titulo;
    private final String modulo;
    private final String descricao;
    private final int gravidade;
    private boolean corrigido;
    private final int imagemResId;

    public Bug(String codigo,
               String titulo,
               String modulo,
               String descricao,
               int gravidade,
               int imagemResId) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.modulo = modulo;
        this.descricao = descricao;
        this.gravidade = gravidade;
        this.imagemResId = imagemResId;
        this.corrigido = false;
    }

    public String getCodigo()     { return codigo; }
    public String getTitulo()     { return titulo; }
    public String getModulo()     { return modulo; }
    public String getDescricao()  { return descricao; }
    public int getGravidade()     { return gravidade; }
    public int getImagemResId()   { return imagemResId; }
    public boolean isCorrigido()  { return corrigido; }

    public void setCorrigido(boolean corrigido) {
        this.corrigido = corrigido;
    }
}

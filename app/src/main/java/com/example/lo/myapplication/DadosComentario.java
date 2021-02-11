package com.example.lo.myapplication;

public class DadosComentario {
    private String nomeUsu;
    private String url;
    private String txtPostagem;
    private String tipoUsu;

    public String getTipoUsu() {
        return tipoUsu;
    }

    public void setTipoUsu(String tipoUsu) {
        this.tipoUsu = tipoUsu;
    }

    public String getNomeUsu() {
        return nomeUsu;
    }

    public void setNomeUsu(String nomeUsu) {
        this.nomeUsu = nomeUsu;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTxtPostagem() {

        return txtPostagem;
    }

    public void setTxtPostagem(String txtPostagem) {
        this.txtPostagem = txtPostagem;
    }
}

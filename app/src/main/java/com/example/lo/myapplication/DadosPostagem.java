package com.example.lo.myapplication;

public class DadosPostagem {
    private String url;
    private String nomeUsuario;
    private String urlPerfil;
    private String tipoUsuario;
    private long horaPostagem;
    private String textoPostagem;
    private String Email;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public DadosPostagem() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getUrlPerfil(){return urlPerfil;}

    public void setUrlPerfil(String urlPerfil){this.urlPerfil = urlPerfil;}

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public long getHoraPostagem() {
        return horaPostagem;
    }

    public void setHoraPostagem(long horaPostagem) {
        this.horaPostagem = horaPostagem;
    }

    public String getTextoPostagem() {
        return textoPostagem;
    }

    public void setTextoPostagem(String textoPostagem) {
        this.textoPostagem = textoPostagem;
    }
}

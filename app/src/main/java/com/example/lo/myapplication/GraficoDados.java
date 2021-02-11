package com.example.lo.myapplication;

public class GraficoDados {
    //Classe de dados do grafico com a nota e o inteiro da mesma
    private String avaliacao;
    private int avaliacaoInt;
    private String textoDia;
    private String dataCerta;
    private int dia;


    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public GraficoDados() {

    }
    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }

    public int getAvaliacaoInt() {
        return avaliacaoInt;
    }

    public void setAvaliacaoInt(int avaliacaoInt) {
        this.avaliacaoInt = avaliacaoInt;
    }



    public String getTextoDia() {
        return textoDia;
    }


    public void setTextoDia(String textoDia) {
        this.textoDia = textoDia;
    }

    public String getDataCerta() {
        return dataCerta;
    }

    public void setDataCerta(String dataCerta) {
        this.dataCerta = dataCerta;
    }




}

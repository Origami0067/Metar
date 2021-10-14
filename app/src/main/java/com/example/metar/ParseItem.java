package com.example.metar;

public class ParseItem {

    private String url;
    private String titre;


    public ParseItem(String url, String titre) {
        this.url = url;
        this.titre = titre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
}

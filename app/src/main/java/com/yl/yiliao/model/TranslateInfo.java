package com.yl.yiliao.model;


public class TranslateInfo extends BaseModel<TranslateInfo>{
    private String Catalog = "";
    private String FromLang = "";
    private String ToLang = "";
    private String FromText = "";
    private String FromAudio = "";
    private String ToText = "";
    private String ToAudioUrl = "";

    public String getCatalog() {
        return Catalog;
    }

    public void setCatalog(String catalog) {
        Catalog = catalog;
    }

    public String getFromLang() {
        return FromLang;
    }

    public void setFromLang(String fromLang) {
        FromLang = fromLang;
    }

    public String getToLang() {
        return ToLang;
    }

    public void setToLang(String toLang) {
        ToLang = toLang;
    }

    public String getFromText() {
        return FromText;
    }

    public void setFromText(String fromText) {
        FromText = fromText;
    }

    public String getFromAudio() {
        return FromAudio;
    }

    public void setFromAudio(String fromAudio) {
        FromAudio = fromAudio;
    }

    public String getToText() {
        return ToText;
    }

    public void setToText(String toText) {
        ToText = toText;
    }

    public String getToAudioUrl() {
        return ToAudioUrl;
    }

    public void setToAudioUrl(String toAudioUrl) {
        ToAudioUrl = toAudioUrl;
    }

}

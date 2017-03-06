package app.com.example.administrator.myek3.app;

/**
 * Created by Darth Vader on 28.02.2017.
 * Die Klasse Artikel erstellt Produkt Objekte für die Einkaufslite her.
 * Über Getter und Setter gibt sie ihre Attribute preis / bzw. kann man
 * ihre Attribute ändern
 */

public class Article {

// Attribute
    private String articleName;
    private String articleAmount;
    private String articleUnit;
    private double articlePrice;
    private String articleComment;
    private int articleStatus;

// Konstruktoren
    public Article(String articleName){
        this.articleName = articleName;
    }
    public Article(String articleName, String articleAmount){
        this.articleName = articleName;
        this.articleAmount = articleAmount;
    }
    public Article(String articleName, String articleAmount, int articleStatus){
        this.articleName = articleName;
        this.articleAmount = articleAmount;
        this.articleStatus = articleStatus;
    }
    public Article(String articleName, String articleAmount, String articleUnit){
        this.articleName = articleName;
        this.articleAmount = articleAmount;
        this.articleUnit = articleUnit;
    }
    public Article(String articleName, String articleAmount, String articleUnit, double articlePrice, String articleComment){
        this.articleName = articleName;
        this.articleAmount = articleAmount;
        this.articleUnit = articleUnit;
        this.articlePrice = articlePrice;
        this.articleComment = articleComment;
    }

// Setter()
    public void setArticleName(String articleName){
        this.articleName = articleName;
    }

    public void setArticleAmount(String articleAmount) {
        this.articleAmount = articleAmount;
    }
    public void setArticleUnit(String articleUnit){
        this.articleUnit = articleUnit;
    }
    public void setArticleComment(String articleComment) {
        this.articleComment = articleComment;
    }

    public void setArticleStatus(int articleStatus) {
        this.articleStatus = articleStatus;
    }

// Getter()
    public String getArticleName() {
        return articleName;
    }

    public String getArticleAmount() {
        return articleAmount;
    }

    public String getArticleUnit() {
        return articleUnit;
    }

    public String getArticleComment() {
        return articleComment;
    }

    public int getArticleStatus() {
        return articleStatus;
    }
}
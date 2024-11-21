package org.example.dto;

public class Say{

    private final int number;
    private String text;
    private String writer;

    public Say(String text, String writer, int number) {
        this.text = text;
        this.writer = writer;
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public String getWriter() {
        return writer;
    }

    public int getNumber() {
        return number;
    }

    public void setText(String text){
        this.text = text;
    }

    public void setWriter(String writer){
        this.writer = writer;
    }
}

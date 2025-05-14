package com.example.test1.authoredquizzes;

public class modelmyquizzes {
    String author;
    String subject;
    String time;
    String date;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public modelmyquizzes(String author, String subject, String time, String date, String code) {
        this.author = author;
        this.subject = subject;
        this.time = time;
        this.date = date;
        this.code = code;
    }

    String code;


}


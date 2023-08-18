package com.inquiryapplication.inquiryform.Model;

public class InquiryModel {
    private String name,number,email,date,course;
    private int id;


    public int getId() { return id; }

    public void setId(int id) { this.id = id; }


    public String getName() { return name; }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getNumber() { return number; }

    public void setNumber(String number)
    {
        this.number = number;
    }

    public String getEmail() { return email; }

    public void setEmail(String email)
    {
        this.email = email;
    }
    public String getDate() { return date; }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getCourse() { return course; }

    public void setCourse(String course)
    {
        this.course = course;
    }

    public InquiryModel(int id,
                        String name,
                       String number,
                       String email,
                       String date,String course
                        )
    {
        this.id=id;
        this.name = name;
        this.number = number;
        this.email = email;
        this.date = date;
        this.course = course;
    }
}

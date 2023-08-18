package com.inquiryapplication.courses.model;

public class CoursesModel {
    private String name;
    private int id;
    public boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }


    public String getName() { return name; }

    public void setName(String name)
    {
        this.name = name;
    }


    public CoursesModel(int id,
                        String name
                        )
    {
        this.id=id;
        this.name = name;
    }
}

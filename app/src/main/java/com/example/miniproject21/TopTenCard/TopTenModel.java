package com.example.miniproject21.TopTenCard;

public class TopTenModel {

    private String dishname;
    private String dishcount;
    private int dishimage;

    // Constructor
    public TopTenModel(String dishname, String dishcount,int dishimage) {
        this.dishname = dishname;
        this.dishcount = dishcount;
        this.dishimage = dishimage;
    }

    // Getter and Setter
    public String getDishname() {
        return dishname;
    }

    public void setDishname(String dishname) {
        this.dishname = dishname;
    }

    public String getDishcount() {
        return dishcount;
    }

    public void setDishcount(String dishcount) {
        this.dishcount = dishcount;
    }

       public int getDishimage() {
            return dishimage;
        }

        public void setCourse_image(int dishimage) {
            this.dishimage = dishimage;
        }


}

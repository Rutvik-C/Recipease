package com.example.miniproject21.TopTenCard;

public class TopTenModel {

    private String dishName;
    private String dishCount;
    private int dishImage;

    // Constructor
    public TopTenModel(String dishname, String dishcount,int dishimage) {
        this.dishName = dishname;
        this.dishCount = dishcount;
        this.dishImage = dishimage;
    }

    // Getter and Setter
    public String getDishname() {
        return dishName;
    }

    public void setDishname(String dishname) {
        this.dishName = dishname;
    }

    public String getDishcount() {
        return dishCount;
    }

    public void setDishcount(String dishcount) {
        this.dishCount = dishcount;
    }

       public int getDishimage() {
            return dishImage;
        }

        public void setCourse_image(int dishimage) {
            this.dishImage = dishimage;
        }


}

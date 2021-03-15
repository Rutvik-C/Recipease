package com.example.miniproject21.NutrientCard;

public class NutrientCardModel {

    private String nutrientName;
    private String nutrientValue;

    // Constructor
    public NutrientCardModel(String nutrientName, String nutrientValue) {
        this.nutrientName = nutrientName;
        this.nutrientValue = nutrientValue;
        //this.course_image = course_image;
    }

    // Getter and Setter
    public String getNutrientName() {
        return nutrientName;
    }

    public void setNutrientName(String nutrientName) {
        this.nutrientName= nutrientName;
    }

    public String getNutrientValue() {
        return nutrientValue;
    }

    public void setNutrientValue(String nutrientValue) {
        this.nutrientValue = nutrientValue;
    }

       /*ublic int getCourse_image() {
            return course_image;
        }

        public void setCourse_image(int course_image) {
            this.course_image = course_image;
        }*/
}
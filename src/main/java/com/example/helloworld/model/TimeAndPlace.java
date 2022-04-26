package com.example.helloworld.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class TimeAndPlace {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfCollection;

    private String placeOfCollection;

    public Date getDateOfCollection() {
        return dateOfCollection;
    }

    public void setDateOfCollection(Date dateOfCollection) {
        this.dateOfCollection = dateOfCollection;
    }

    public String getPlaceOfCollection() {
        return placeOfCollection;
    }

    public void setPlaceOfCollection(String placeOfCollection) {
        this.placeOfCollection = placeOfCollection;
    }
}

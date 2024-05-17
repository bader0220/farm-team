package me.smartfarm.data.models;

import java.util.List;

public class CorpType {
    private String name ;
    private List<String> unitOfMeasure ;


    public CorpType() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(List<String> unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }
}

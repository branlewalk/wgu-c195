package org.branlewalk.dto;

public class IncrementTypesDTO {

    private int incrementTypesId;
    private String incrementDescription;

    public IncrementTypesDTO(int incrementTypeId, String incrementDescription) {
        this.incrementTypesId = incrementTypeId;
        this.incrementDescription = incrementDescription;
    }

    public int getIncrementTypesId() {
        return incrementTypesId;
    }

    public void setIncrementTypesId(int incrementTypeId) {
        this.incrementTypesId = incrementTypeId;
    }

    public String getIncrementDescription() {
        return incrementDescription;
    }

    public void setIncrementDescription(String incrementDescription) {
        this.incrementDescription = incrementDescription;
    }
}

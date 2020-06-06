package eu.mobile.onko.models;

public enum UserTypeEnum {

    Doctor(1),

    Patient(2);

    private int id;

    UserTypeEnum(int id){
        this.id = id;
    }

    public int getId() { return id; }
}
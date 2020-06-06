package eu.mobile.onko.models;

public enum WeekDayEnum {

    MONDAY(1),

    TUESDAY(2),

    WEDNESDAY(3),

    THURSDAY(4),

    FRIDAY(5),

    SATURDAY(6),

    SUNDAY(7);

    private int id;

    WeekDayEnum(int id){
        this.id = id;
    }

    public int getId() { return id; }
}

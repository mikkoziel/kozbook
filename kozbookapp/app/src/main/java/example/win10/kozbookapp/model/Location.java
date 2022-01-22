package example.win10.kozbookapp.model;

public class Location {
    private int location_id;
    private String name;
    private String extra_info;

    public Location() {
    }

    public Location(int location_id, String name) {
        this.location_id = location_id;
        this.name = name;
    }

    public Location(int location_id, String name, String extra_info) {
        this.location_id = location_id;
        this.name = name;
        this.extra_info = extra_info;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtra_info() {
        return extra_info;
    }

    public void setExtra_info(String extra_info) {
        this.extra_info = extra_info;
    }
}

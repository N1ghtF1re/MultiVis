package men.brakh.emergencymap.models;

public class City {
    private String name;
    private String color;
    private int[] sits;

    public City(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int[] getSits() {
        return sits;
    }

    public void setSits(int[] sits) {
        this.sits = sits;
    }
}

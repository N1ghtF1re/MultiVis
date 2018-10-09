package men.brakh.emergencymap.models;

/**
 * Класс региона
 */
public class Region {
    private String name; // Название региона
    private String color; // Результирующий цвет региона
    private int[] sits; // Массив повторений ситуаций ((i-1)-ый элемент массива - число повторений i-ой ситуации)

    /**
     * Конструктор региона
     * @param name Название региона
     * @param color Цвет региона
     * @param n Количество ситуаций
     */
    public Region(String name, String color, int n) {
        this.name = name;
        this.color = color;
        this.sits = new int[n];
    }

    /**
     * Конструктор региона
     * @param name Название региона
     * @param n Количество ситуаций
     */
    public Region(String name, int n) {
        this.name = name;
        this.sits = new int[n];
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

    public void incSituation(int index) {
        sits[index]++;
    }

    public int getSitRepeats(int index) {
        return sits[index];
    }


    public Boolean equals(Region region1, Region region2) {
        return(region1.getName().equals(region2.getName()));
    }
}

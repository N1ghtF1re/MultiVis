package men.brakh.emergencymap.dto;

import men.brakh.emergencymap.models.Color;

/**
 * Класс для возврата ID вместе с цветом при запросе
 */
public class ColorDTO extends Color {
    private int id;
    public ColorDTO(Color color, int id) {
        super(color);
        this.id = id;
    }
    public int getId() {
        return id;
    }
}
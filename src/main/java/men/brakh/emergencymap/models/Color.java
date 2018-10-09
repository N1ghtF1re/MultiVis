package men.brakh.emergencymap.models;

/**
 * Класс цветов
 */
public class Color {
    private String hex; // HEX-представление цвета
    private int[] rgb = new int[3]; // [0] => r, [1] => g, [2] => b


    /**
     * Создание цвета
     * @param hex HEX-представление цвета
     */
    public Color(String hex) {
        this.hex = hex;
        rgb = hex2Rgb(hex); // Переводим в RGB
    }

    /**
     * Создание цвета
     * @param r RED
     * @param g GREEN
     * @param b BLUE
     */
    public Color(int r, int g, int b) {
        rgb = new int[] {r,g,b};
        hex = rgb2Hex(rgb);
    }

    /**
     * Создание цвета
     * @param rgb Массив rgb ([0] => r, [1] => g, [2] => b)
     */
    public Color(int[] rgb) {
        this.rgb[0] = rgb[0];
        this.rgb[1] = rgb[1];
        this.rgb[2] = rgb[2];
        hex = rgb2Hex(rgb);
    }

    /**
     * Создание копии цвета
     * @param color Цвет, который надо скопировать
     */
    public Color(Color color) {
        int[] myRgb = color.getRgb();
        this.rgb[0] = myRgb[0];
        this.rgb[1] = myRgb[1];
        this.rgb[2] = myRgb[2];
        this.hex = color.hex;
    }

    public int[] getRgb() {
        return rgb;
    }

    public String getHex() {
        return hex;
    }

    /**
     * Осветление цвета и возврат получившегося цвета
     * @param percent Процент освтеления
     * @return Получившийся цвет (this)
     */
    public Color ligherColor(int percent) {
        if (percent < 0) return this;
        if (percent > 100) percent = 100;

        for(int i = 0; i < rgb.length; i++) {
            rgb[i] = rgb[i] + Math.round((255 - rgb[i]) * (float)percent/100);
        }

        return this;
    }


    // СТАТИЧЕСКИЕ МЕТОДЫ ДЛЯ ЦВЕТОВ

    /**
     * Перевод из hex в RGB
     * @param colorStr HEX представление в строке формата #NNNNNN
     * @return Массив rgb ([0] => r, [1] => g, [2] => b)
     */
    public static int[] hex2Rgb(String colorStr) {
        return new int[] {
                Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                Integer.valueOf( colorStr.substring( 5, 7 ), 16 )
        };
    }

    /**
     * Перевод из rgb в hex
     * @param rgb Массив rgb ([0] => r, [1] => g, [2] => b)
     * @return HEX представления формата #NNNNNN
     */
    public static String rgb2Hex(int[] rgb) {
        return String.format("#%02x%02x%02x", rgb[0], rgb[1], rgb[2]);
    }

    /**
     * Получение результирующего цвета путем смешания
     * @param colors Массив цветов, учавствующих в смешании
     * @return Результирующий цвет
     */
    public static Color mixColors(Color[] colors) {
        int[] rgbSum = new int[] {0,0,0};
        for(int i = 0; i < colors.length; i++) {
            int[] currRgb = colors[i].getRgb();

            for(int j = 0; j < rgbSum.length; j++) {
                rgbSum[j] += currRgb[j]; // Смешиваем r, g, b
            }
        }
        for(int i = 0; i < rgbSum.length; i++) {
            rgbSum[i] = rgbSum[i] / colors.length; // Делим r,g,b на количество
        }
        return new Color(rgbSum);
    }

    /**
     * Получение базовых цветов
     * @param n Количество ситуаций
     * @return Массив из базовых цветов
     */
    public static Color[] getBasicColors(int n) {
        Color[] basicColors = new Color[n];
        int currIndex = 0;
        int[] colors = new int[] {255,0,0};

        double coef = 255*5.22;
        int shift = (int) (coef/n);

        basicColors[currIndex++] = new Color(colors);

        Boolean trg_plus = true;
        int status = 1;

        for(int i = 1; i < n; i++) {
            int delta_shift = shift;
            while(true) {
                if(trg_plus) {
                    while((delta_shift != 0) && (colors[status] < 255)) {
                        colors[status]++;
                        delta_shift--;
                    }
                } else {
                    while((delta_shift != 0) && (colors[status] > 0)){
                        colors[status]--;
                        delta_shift--;
                    }
                }
                if (delta_shift == 0) {
                    basicColors[currIndex++] = new Color(colors);
                    break;
                } else {
                    switch (status) {
                        case(1): // g
                            status = 0;
                            break;
                        case(0): // r
                            status = 2;
                            break;
                        case(2): //b
                            status = 1;
                            break;
                    }
                }
                trg_plus = !trg_plus;

            }
        }
        return basicColors;
    }
}

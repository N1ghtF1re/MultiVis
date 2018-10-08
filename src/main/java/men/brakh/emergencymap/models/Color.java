package men.brakh.emergencymap.models;

public class Color {
    private String hex;
    private int[] rgb = new int[3];


    public Color(String hex) {
        this.hex = hex;
        rgb = hex2Rgb(hex);
    }

    public Color(int r, int g, int b) {
        rgb = new int[] {r,g,b};
        hex = rgb2Hex(rgb);
    }
    public Color(int[] rgb) {
        this.rgb = rgb;
        hex = rgb2Hex(rgb);
    }
    public Color(Color color) {
        this.rgb = color.rgb;
        this.hex = color.hex;
    }

    public int[] getRgb() {
        return rgb;
    }

    public Color ligherColor(int percent) {
        if (percent < 0) return this;
        if (percent > 100) percent = 100;

        for(int i = 0; i < rgb.length; i++) {
            rgb[i] = rgb[i] + Math.round((255 - rgb[i]) * percent/100);
        }

        return this;
    }


    public static int[] hex2Rgb(String colorStr) {
        return new int[] {
                Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                Integer.valueOf( colorStr.substring( 5, 7 ), 16 )
        };
    }

    public static String rgb2Hex(int[] rgb) {
        return String.format("#%02x%02x%02x", rgb[0], rgb[1], rgb[2]);
    }

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

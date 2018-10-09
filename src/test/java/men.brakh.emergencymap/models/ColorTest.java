package men.brakh.emergencymap.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class ColorTest {
    @Test
    public void hex2RgbTEST() {
        int[] rgb = new int[] {255,255,255};
        String hex = "#ffffff";
        assertArrayEquals(rgb, Color.hex2Rgb(hex));
        rgb = new int[] {0,0,0};
        hex = "#000000";
        assertArrayEquals(rgb, Color.hex2Rgb(hex));
        rgb = new int[] {193,132,1};
        hex = "#c18401";
        assertArrayEquals(rgb, Color.hex2Rgb(hex));
    }

    @Test
    public void rgbToHexTest() {
        int[] rgb = new int[] {255,255,255};
        String hex = "#ffffff";
        assertEquals(hex, Color.rgb2Hex(rgb));
        rgb = new int[] {0,0,0};
        hex = "#000000";
        assertEquals(hex, Color.rgb2Hex(rgb));
        rgb = new int[] {193,132,1};
        hex = "#c18401";
        assertEquals(hex, Color.rgb2Hex(rgb));
    }
}
package util;

import transforms.Col;

import java.util.Random;

/**
 * Created by zoubas on 12.4.16.
 */

public class ColorGenerator {

    /**
     * Generates random Color
     *
     * @return generated Col
     */
    public static Col generateCol() {
        Random rand = new Random();
        int r = rand.nextInt(255) + 0;
        int g = rand.nextInt(255) + 0;
        int b = rand.nextInt(255) + 0;
        return new Col(r, g, b);
    }
}

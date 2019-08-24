package edu.caltech.cs2.lab03;

import edu.caltech.cs2.libraries.Pixel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Image {
    private Pixel[][] pixels;
    private int num;

    public Image(File imageFile) throws IOException {
        BufferedImage img = ImageIO.read(imageFile);
        this.pixels = new Pixel[img.getWidth()][img.getHeight()];
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                this.pixels[i][j] = Pixel.fromInt(img.getRGB(i, j));
            }
        }
    }

    private Image(Pixel[][] pixels) {
        this.pixels = pixels;
    }

    public Image transpose() {
        Pixel[][] newImage = new Pixel[this.pixels[0].length][this.pixels.length];
        for (int i= 0; i < this.pixels[0].length; i++) {
            for (int j=0; j < this.pixels.length; j++) {
                newImage[i][j] = this.pixels[j][i];
            }
        }
    return new Image(newImage);
    }

    public String decodeText() {
        String message = "";
        Pixel[][] image = this.pixels;
        int[] binary = new int[8];
        int counter = 0;

        for (int i= 0; i < this.pixels.length; i++) {
            for (int j=0; j < this.pixels[0].length; j++) {
                int bit = image[i][j].getLowestBitOfR();
                binary[counter] = bit;
                counter++;
                if (counter == 8) {
                    int letter = 0;
                    for (int k=0; k < binary.length; k++) {
                        letter += Math.pow(2, k) * binary[k];
                    }
                    if (letter != 0) {
                        message += (char) letter;
                    }
                    counter = 0;
                }

            }
        }
        return message;
    }

    public Image hideText(String text) {

        int counter = 0;
        Pixel[][] image = this.pixels;
        String hide = "";

        for (int i=0; i < text.length(); i++) {

            int ascii = text.charAt(i);
            String binary = Integer.toBinaryString(ascii);

            String zeros = "";

            if (binary.length() < 8) {
                for (int j = 0; j < 8 - binary.length(); j++) {
                    zeros += "0";
                }
            }
            else if (binary.length() > 8) {
                binary = binary.substring(binary.length() - 8, binary.length());
            }

            zeros += binary;
            for (int e = zeros.length() - 1; e >= 0; e--) {
                hide += zeros.charAt(e);
            }
        }
        //System.out.print(hide);

        for (int i= 0; i < this.pixels.length; i++) {
            for (int j=0; j < this.pixels[0].length; j++) {
                if (counter < hide.length()) {
                    int num = Character.getNumericValue(hide.charAt(counter));
                    image[i][j] = image[i][j].fixLowestBitOfR(num);
                    counter++;
                }
                else {
                    image[i][j] = image[i][j].fixLowestBitOfR(0);
                }

                }

            }
        return new Image(image);
    }

    public BufferedImage toBufferedImage() {
        BufferedImage b = new BufferedImage(this.pixels.length, this.pixels[0].length, BufferedImage.TYPE_4BYTE_ABGR);
        for (int i = 0; i < this.pixels.length; i++) {
            for (int j = 0; j < this.pixels[0].length; j++) {
                b.setRGB(i, j, this.pixels[i][j].toInt());
            }
        }
        return b;
    }

    public void save(String filename) {
        File out = new File(filename);
        try {
            ImageIO.write(this.toBufferedImage(), filename.substring(filename.lastIndexOf(".") + 1, filename.length()), out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package testclasses;

import java.util.function.Function;

/**
 * Created by Maciek on 22.01.2018
 */


public class Crypto {

    public static void main(String[] args) {
        String text = "hello HELLO";

        System.out.println("Oryginalny tekst: " + text);

        System.out.println("Zaszyfrowany tekst: " + encrypt(text, t -> {
            int ascii = t.hashCode();
            if (ascii >= 65 && ascii <= 122) {
                if ((ascii <= 77) || (ascii >= 97 && ascii <= 109)) ascii += 13;
                else ascii -= 13;
            }
            return (char)ascii;
        }));
    }

    public static String encrypt(String text, Function converter) {
        char[] lettersArray = text.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char letter:lettersArray){
            builder.append(converter.apply(letter));
        }
    return builder.toString();
    }
}

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package org.example.java.app.app;

import org.example.java.app.list.LinkedList;

import static org.example.java.app.utilities.StringUtils.join;
import static org.example.java.app.utilities.StringUtils.split;
import static org.example.java.app.app.MessageUtils.getMessage;

import org.apache.commons.text.WordUtils;

public class App {
    public static void main(String[] args) {
        LinkedList tokens;
        tokens = split(getMessage());
        String result = join(tokens);
        System.out.println(WordUtils.capitalize(result));
    }
}
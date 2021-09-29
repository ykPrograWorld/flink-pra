package org.example.datastru;

import java.util.Stack;

public class Test {
    public static void main(String[] args) {
        Stack<String> stringStack = new Stack<>();
        stringStack.push("1");
        stringStack.push("2");
        while (!stringStack.isEmpty()){
            System.out.println(stringStack.pop());
        }
    }

}

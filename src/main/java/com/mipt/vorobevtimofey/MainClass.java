package com.mipt.vorobevtimofey;

public class MainClass {
    private int usableNumber;
    private String usableString;
    protected static double usableDouble;
    public final long CONST_LONG = 100_000_000;

    public static void main(String[] args) {
        for (int i = 0; i <= 15; ++i) {
            System.out.println("Iter: " + i);
        }
    }
}

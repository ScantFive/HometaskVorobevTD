package com.mipt.vorobevtimofey;

public interface InterfaceForStudent {
    default Object study(Object std) {
        return std;
    }
}

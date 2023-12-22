package com.example.golf_apk.common;

public enum KeyType {
    ACCESS_TOKEN("accessToken"),
    REFRESH_TOKEN("refreshToken"),
    NAME("name"),
    JOIN_DATE("joinDate"),
    BIRTH("birth"),
    START_DATE("start_date"),
    END_DATE("end_date");

    private final String value;

    KeyType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

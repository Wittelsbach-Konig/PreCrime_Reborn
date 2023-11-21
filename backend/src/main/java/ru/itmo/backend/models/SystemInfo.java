package ru.itmo.backend.models;

import lombok.Data;

@Data
public class SystemInfo {
    private final String version = "0.0.1";
    private final String[] authors = {"Guskov", "Kiryushin", "Khudyakov"};
    private final String url = "github.com";
}

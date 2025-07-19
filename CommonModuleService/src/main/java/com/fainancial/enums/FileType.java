package com.fainancial.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum FileType {
    DOCUMENT(Arrays.asList("txt", "text", "doc", "docx", "pdf")),
    EXCEL(Arrays.asList("csv","xlsx"));

    private final List<String> type;

    FileType(List<String> type) {
        this.type = type;
    }

}

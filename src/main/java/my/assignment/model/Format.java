package my.assignment.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Format {
    ZIP("zip"),
    EML("eml");

    private final String format;

    public static Format fromString(String text) {
        for (Format f : Format.values()) {
            if (f.format.equalsIgnoreCase(text)) {
                return f;
            }
        }
        throw new IllegalArgumentException("Unknown format [" + text + ']');
    }
}

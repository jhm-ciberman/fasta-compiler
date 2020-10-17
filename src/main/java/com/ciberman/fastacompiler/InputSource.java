package com.ciberman.fastacompiler;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class InputSource {
    private final Reader reader;
    private final String fileName;

    public InputSource(InputStream stream, String fileName) {
        this(new InputStreamReader(stream), fileName);
    }

    public InputSource(Reader reader, String fileName) {
        this.reader = reader;
        this.fileName = fileName;
    }

    public Reader getReader() {
        return reader;
    }

    public String getFileName() {
        return fileName;
    }
}

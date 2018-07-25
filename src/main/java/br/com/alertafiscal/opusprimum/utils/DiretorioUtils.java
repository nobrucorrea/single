package br.com.alertafiscal.opusprimum.utils;

import java.io.File;

public class DiretorioUtils {

    public static final String FS = System.getProperty("file.separator");
    private final StringBuilder builder;

    public DiretorioUtils() {
        builder = new StringBuilder();
    }

    public File asFile() {
        return new File(toString());
    }
    
    public File asFile(String file) {
        return new File(toString(), file);
    }

    public DiretorioUtils folder(String folder) {
        if (builder.length() == 0) {
            concat(System.getProperty("user.home"));
        }

        concat(folder);

        File fileFolder = new File(toString());
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }

        return this;
    }

    private void concat(String folder) {
        builder.append(folder);
        builder.append(FS);
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}

package br.edu.fafica.androidwebaccess.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by pedrodimoura on 11/10/16.
 */

public class StringUtil {

    /**
     * Converte uma InputStream numa String
     *
     * @param is InputStream que será convertido
     * @return String
     * @throws IOException
     */
    public static String streamToString(InputStream is) throws IOException {
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int lidos;
        while ((lidos = is.read(bytes)) > 0) {
            baos.write(bytes, 0, lidos);
        }

        return new String(baos.toByteArray());
    }

}

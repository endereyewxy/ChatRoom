package chatroom.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class MD5 {
    static {
        try {
            MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static String md5(String s) {
        try {
            final MessageDigest digester = MessageDigest.getInstance("MD5");
            final byte[]        digest   = digester.digest(s.getBytes(StandardCharsets.UTF_8));
            final StringBuilder builder  = new StringBuilder();
            for (byte b : digest)
                builder.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
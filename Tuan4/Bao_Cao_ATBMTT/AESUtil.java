import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

    private static final String ALGORITHM = "AES";

    private static final String TRANSFORMATION =
            "AES/CBC/PKCS5Padding";

    // Sinh khóa ngẫu nhiên 16 ký tự
        public static String generateRandomKey() {

                String chars =
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "abcdefghijklmnopqrstuvwxyz"
                + "0123456789";

        SecureRandom random =
                new SecureRandom();

        StringBuilder key =
                new StringBuilder();

        for(int i = 0; i < 16; i++) {

                key.append(
                        chars.charAt(
                                random.nextInt(
                                        chars.length())));
        }

        return key.toString();
    }

    // Chuyển chuỗi 16 ký tự thành khóa AES
    public static SecretKey createKeyFromString(
            String keyText) {

        return new SecretKeySpec(
                keyText.getBytes(),
                ALGORITHM);
    }

    // Sinh IV ngẫu nhiên 16 byte
    public static byte[] generateIV() {

        byte[] iv =
                new byte[16];

        SecureRandom random =
                new SecureRandom();

        random.nextBytes(iv);

        return iv;
    }

    // Mã hóa
    public static String encrypt(
            String plainText,
            SecretKey secretKey,
            byte[] iv)
            throws Exception {

        Cipher cipher =
                Cipher.getInstance(
                        TRANSFORMATION);

        cipher.init(
                Cipher.ENCRYPT_MODE,
                secretKey,
                new IvParameterSpec(iv));

        byte[] encryptedBytes =
                cipher.doFinal(
                        plainText.getBytes(
                                "UTF-8"));

        return Base64.getEncoder()
                .encodeToString(
                        encryptedBytes);
    }

    // Giải mã
    public static String decrypt(
            String cipherText,
            SecretKey secretKey,
            byte[] iv)
            throws Exception {

        Cipher cipher =
                Cipher.getInstance(
                        TRANSFORMATION);

        cipher.init(
                Cipher.DECRYPT_MODE,
                secretKey,
                new IvParameterSpec(iv));

        byte[] decodedBytes =
                Base64.getDecoder()
                        .decode(cipherText);

        byte[] decryptedBytes =
                cipher.doFinal(
                        decodedBytes);

        return new String(
                decryptedBytes,
                "UTF-8");
    }

    // IV -> Base64
    public static String ivToString(
            byte[] iv) {

        return Base64.getEncoder()
                .encodeToString(iv);
    }

    // Base64 -> IV
    public static byte[] stringToIV(
            String ivString) {

        return Base64.getDecoder()
                .decode(ivString);
    }
}
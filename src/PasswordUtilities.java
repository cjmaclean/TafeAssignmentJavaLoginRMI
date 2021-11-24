import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author 30039802 - Caspian Maclean
 */
public class PasswordUtilities {

    // Crypto code based on the howto at https://www.baeldung.com/java-password-hashing
    static SecureRandom random;

    // keyLength is intended constant.
    // iterations constant for now, but stored in case we want multiple values later.
    static int iterations = 1024;
    static int keyLength = 128;

    public static void initPasswordUtilities() throws NoSuchAlgorithmException, InvalidKeySpecException {
        random = new SecureRandom();
    }

    private static byte[] saltedHashedPassword(String password, byte[] salt, int iterations) throws NoSuchAlgorithmException, InvalidKeySpecException {

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] hash = factory.generateSecret(spec).getEncoded();
        return hash;
    }

    private static byte[] saltedHashedPassword(String password, User user) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return saltedHashedPassword(password, user.salt, user.iterations);
    }

    public static boolean isCorrectPassword(User user, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] computedHash = saltedHashedPassword(password, user);
        return Arrays.equals(computedHash, user.hash);
    }

    public static User prepareUser(String userName, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {

        byte[] salt = new byte[16];
        random.nextBytes(salt);
        
        // hash can be computed using the salt and iterations stored in the User,
        // so set up the rest of the User, then the hash.
        
        User newUser = new User();
        newUser.userName = userName;
        newUser.salt = salt;
        newUser.iterations = iterations;

        newUser.hash = saltedHashedPassword(password, newUser);

        return newUser;

    }

}

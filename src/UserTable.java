import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;

/**
 *
 * @author 30039802 - Caspian Maclean
 */
public class UserTable {

    private HashMap<String, User> usersByName = new HashMap<>();

    public UserTable() {
        try {
            PasswordUtilities.initPasswordUtilities();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            // Shouldn't fail, but if it does, there's not much we can do
            System.out.println("Unexpected exception - cannot initialise PasswordUtilities");
        }
    }

    public boolean passwordCorrect(String user, String passwordIn) {
        User foundUser = usersByName.get(user);
        // null if not found, which needs to be checked for
        if (foundUser != null) {
            //return passwordIn.equals(storedPassword);
            try {
                return PasswordUtilities.isCorrectPassword(foundUser, passwordIn);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
                // in case of failure, don't allow the login
                return false;
            }
        }
        return false;
    }

    public void addUserToTable(String userName, String password) {
        try {
            User user = PasswordUtilities.prepareUser(userName, password);
            usersByName.put(userName, user);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            // Shouldn't fail, but if it does, don't add the user
            System.out.println("Unexpected exception - cannot add user");
        }
    }
}


/**
 *
 * @author Caspian Maclean 30039802
 */
public class SessionImplementation implements Session {

    private Server server;
    private UserTable userTable;

    private boolean loggedIn = false;
    private boolean admin = false;
    // admin is always false when loggedIn == false. setAdmin and setLoggedIn
    // maintain this property.
    private String sessionUsername = "";

    public SessionImplementation(Server server, UserTable userTable) {
        this.server = server;
        this.userTable = userTable;
    }
    
        private void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
        setAdmin(isAdmin() && loggedIn);
    }

    private void setAdmin(boolean admin) {
        this.admin = admin && loggedIn;
    }

        // Session methods
    @Override
    public boolean login(String username, String password) {
        if (userTable.passwordCorrect(username, password)) {
            setLoggedIn(true);
            sessionUsername = username;
            setAdmin("admin".equals(username));
        } else {
            setLoggedIn(false);
        }
        return loggedIn;
    }

    @Override
    public boolean isLoggedIn() {
        return loggedIn;
    }

    @Override
    public boolean isAdmin() {
        return admin;
    }

    @Override
    public String getMessage() {
        if (!loggedIn) {
            return server.getLoggedOutMessage();
        } else if (admin) {
            return server.getAdminMessage();
        } else {
            return server.getUserMessage(sessionUsername);
        }
    }

    @Override
    public String createLogin(String username, String password) {
        if (isAdmin()) {
            userTable.addUserToTable(username, password);
            return "user added: " + username;
        } else {
            return "failed, not authorised";
        }
    }
}

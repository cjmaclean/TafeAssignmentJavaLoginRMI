import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author 30039802 - Caspian Maclean
 */
public class Server implements Hello, Session {

    UserTable users = new UserTable();

    public Server() {
        users.addUserToTable("fred", "ffff");
        users.addUserToTable("admin", "admin");
    }

    private void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
        setAdmin(isAdmin() && loggedIn);
    }

    private void setAdmin(boolean admin) {
        this.admin = admin && loggedIn;
    }

    @Override
    public String sayHello() {
        return "Hello, world!";
    }

    private boolean loggedIn = false;
    private boolean admin = false;
    // admin is always false when loggedIn == false. setAdmin and setLoggedIn
    // maintain this property.
    private String sessionUsername = "";

    // Session methods
    @Override
    public boolean login(String username, String password) {
        if ("admin".equals(username) && "password".equals(password)) {
            setLoggedIn(true);
            setAdmin(true);
            sessionUsername = username;
        } else if ("fred".equals(username) && "fred".equals(password)) {
            setLoggedIn(true);
            setAdmin(false);
            sessionUsername = username;
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
        if(!loggedIn) {
            return "Access denied";
        } else if (admin) {
            return "Welcome to the server. You may create more accounts.";
        } else {
            return "Hello" + sessionUsername + ". Welcome to the server.";
        }
    }
    @Override
    public String createLogin(String username, String password) {
        return "failed, not supported yet";
    }

    public static void main(String args[]) {

        try {
            Server obj = new Server();
            Session sessionStub = (Session) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("Session", sessionStub);

            System.err.println("Server ready");
        } catch (RemoteException | AlreadyBoundException e) {
            System.err.println("Server exception: " + e.toString());
        }
    }
}

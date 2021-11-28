import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Caspian Maclean 30039802
 *
 * Question 4 – JMC wishes to have a standard login functionality for all their
 * terminals around the ship this should be accomplished via logging into a
 * central server to test user and password combinations (you must have at least
 * one administrator password setup) You must create a two Server Client
 * program; each must use two different IPC mechanisms to communicate. Your
 * program must have a login that uses standard hashing techniques
 *
 * This project uses RMI for the connection.
 * This class is the server program
 * 
 */
public class Server implements SessionManager {

    int sessionCounter = 0;
    UserTable users = new UserTable();

    public Server() {
        users.addUserToTable("fred", "ffff");
        users.addUserToTable("admin", "admin");
    }

    public String getLoggedOutMessage() {
        return "Access denied";
    }
    public String getAdminMessage() {
        return "Welcome to the server, you may create more accounts.  "
                + "There have been " + sessionCounter + " connection today.";
    }
    public String getUserMessage(String sessionUsername) {
        return "Hello " + sessionUsername + ". Welcome to the server.";
    }
    
    public static void main(String args[]) {

        try {
            Server obj = new Server();
            SessionManager sessionManagerStub = (SessionManager) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("SessionManager", sessionManagerStub);

            System.err.println("Server ready");
        } catch (RemoteException | AlreadyBoundException e) {
            System.err.println("Server exception: " + e.toString());
        }
    }

    @Override
    public Session getSession() throws RemoteException {
        sessionCounter++;
        Session newSession = new SessionImplementation(this, users);
        Session newSessionStub = (Session) UnicastRemoteObject.exportObject(newSession, 0);
        return newSessionStub;
    }
}

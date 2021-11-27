
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author 30039802 - Caspian Maclean
 */
public class Server implements Hello, SessionManager {

    UserTable users = new UserTable();

    public Server() {
        users.addUserToTable("fred", "ffff");
        users.addUserToTable("admin", "admin");
    }

    @Override
    public String sayHello() {
        return "Hello, world!";
    }

    public String getLoggedOutMessage() {
        return "Access denied";
    }
    public String getAdminMessage() {
        return "Welcome to the server. You may create more accounts.";
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
        Session newSession = new SessionImplementation(this, users);
        Session newSessionStub = (Session) UnicastRemoteObject.exportObject(newSession, 0);
        return newSessionStub;
    }
}

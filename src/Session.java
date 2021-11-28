import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Caspian Maclean 30039802
 */

public interface Session extends Remote {

    boolean login(String username, String password) throws RemoteException;

    boolean isLoggedIn() throws RemoteException;

    boolean isAdmin() throws RemoteException;

    String getMessage() throws RemoteException;

    String createLogin(String username, String password) throws RemoteException;
}

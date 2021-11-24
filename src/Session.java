
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Session extends Remote {

    boolean login(String username, String password) throws RemoteException;

    boolean isLoggedIn() throws RemoteException;

    boolean isAdmin() throws RemoteException;

    String getMessage() throws RemoteException;

    String createLogin(String username, String password) throws RemoteException;
}

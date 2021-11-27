
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Caspian Maclean 30039802
 */
public interface SessionManager extends Remote {
    Session getSession() throws RemoteException;
}

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 *
 * @author Caspian Maclean 30039802
 */

public class Client {

    private Client() {
    }

    private static void test() {
        String host = null;
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            SessionManager sessionManagerStub = (SessionManager) registry.lookup("SessionManager");
            Session sessionStub = sessionManagerStub.getSession();
            String response = sessionStub.getMessage();
            System.out.println("response: " + response);
            sessionStub.login("admin", "admin");
            response = sessionStub.getMessage();
            System.out.println("response: " + response);
            sessionStub.login("fred", "ffff");
            response = sessionStub.getMessage();
            System.out.println("response: " + response);

        } catch (RemoteException | NotBoundException e) {
            System.err.println("Client exception: " + e.toString());
        }

    }

    public static void main(String[] args) {

        if ((args.length >= 1) && args[0].equals("test")) {
            System.out.println("Running automated test");
            test();
            return;
        }
        String host = (args.length < 1) ? null : args[0];
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Running interactive client");
            Registry registry = LocateRegistry.getRegistry(host);
            SessionManager sessionManagerStub = (SessionManager) registry.lookup("SessionManager");
            Session sessionStub = sessionManagerStub.getSession();
            while (true) {
                try {
                    System.out.println("Commands: quit / login <user> <pass> / message / create <user> <pass>");
                    System.out.print("Command> ");
                    
                    String lineInput = sc.nextLine();
                    String[] lineInputWords = lineInput.split(" ");
                    if (lineInput.equalsIgnoreCase("quit")) {
                        System.exit(0); // stop client.
                    } else if (lineInputWords.length == 3 && lineInputWords[0].equals("login")) {
                        String userName = lineInputWords[1];
                        String password = lineInputWords[2];
                        if (sessionStub.login(userName, password)) {
                            System.out.println("Logged in");
                        } else {
                            System.out.println("Login failed");
                        }
                    } else if (lineInputWords.length == 1 && lineInputWords[0].equals("message")) {
                        String response = sessionStub.getMessage();
                        System.out.println("response: " + response);
                    } else if (lineInputWords.length == 3 && lineInputWords[0].equals("create")) {
                        String userName = lineInputWords[1];
                        String password = lineInputWords[2];
                        String response = sessionStub.createLogin(userName, password);
                        System.out.println("response: " + response);
                    } else {
                        System.out.println("command not understood");
                    }
                } catch (IOException e) {
                    // handle IO exception here
                }
            }
        } catch (RemoteException | NotBoundException e) {
            System.err.println("Client exception: " + e.toString());
        }
    }
}

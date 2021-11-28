/*
 * Copyright (c) 2004, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * -Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 * -Redistribution in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 * Neither the name of Oracle nor the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL
 * NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF
 * USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR
 * ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that Software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

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

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
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Server implements Hello, Session {

    private void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
        setAdmin(isAdmin() && loggedIn);
    }

    private void setAdmin(boolean admin) {
        this.admin = admin && loggedIn;
    }

    public Server() {}

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

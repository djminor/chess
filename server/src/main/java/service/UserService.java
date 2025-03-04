package service;

import dataaccess.AuthDataAccess;
import dataaccess.UserDataAccess;
import org.eclipse.jetty.server.Authentication;
import service.request.LoginRequest;
import service.request.LogoutRequest;
import service.request.RegisterRequest;
import service.result.LoginResult;
import service.result.LogoutResult;
import service.result.RegisterResult;

import java.util.Objects;
import java.util.UUID;

public class UserService {

    public static RegisterResult register(RegisterRequest registerRequest) {
        if (UserDataAccess.findUser(registerRequest.username()) != null) {
            return new RegisterResult("Error: Already taken", "");
        }
        else {
            UserDataAccess.addUser(registerRequest.username(), registerRequest.password(), registerRequest.email());
            String authToken = UUID.randomUUID().toString();
            AuthDataAccess.addAuth(registerRequest.username(), authToken);
            return new RegisterResult(registerRequest.username(), authToken);
        }
    }
    public static LoginResult login(LoginRequest loginRequest) {
        if(UserDataAccess.findUser(loginRequest.username()) == null) {
            return new LoginResult("Error: Unauthorized", "");
        }
        else {
            if(!Objects.equals(Objects.requireNonNull(UserDataAccess.findUser(loginRequest.username())).password(), loginRequest.password())) {
                return new LoginResult("Error: Unauthorized", "");
            }
            else {
                String authToken = UUID.randomUUID().toString();
                AuthDataAccess.addAuth(loginRequest.username(), authToken);
                return new LoginResult(loginRequest.username(), authToken);
            }
        }
    }
    public static LogoutResult logout(LogoutRequest logoutRequest) {
        if (AuthDataAccess.findAuthData(logoutRequest.authToken()) != null) {
            System.out.print("It's not null");
            AuthDataAccess.deleteAuthData(logoutRequest.authToken());
            return new LogoutResult(logoutRequest.authToken());
        }
        else {
            return new LogoutResult("Error: bad request");
        }
    }
}

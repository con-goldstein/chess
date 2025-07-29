package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import results.*;
import java.io.*;
import java.net.*;
import requests.*;

public class ServerFacade {
    String serverurl;
    public String authToken;
    public String username;

    public ServerFacade(String url) {
        serverurl = url;
    }


    public RegisterResult register(RegisterRequest request) throws AlreadyTakenException, UnauthorizedException, BadRequestException {
        RegisterResult result = makeRequest("POST", "/user", request, RegisterResult.class);
        authToken = result.authToken();
        username = result.username();
        return result;
    }

    public LoginResult login(LoginRequest request) throws AlreadyTakenException, UnauthorizedException, BadRequestException {
        LoginResult result = makeRequest("POST", "/session", request, LoginResult.class);
        authToken = result.authToken();
        return result;
    }

    public ListResult list() throws AlreadyTakenException, UnauthorizedException, BadRequestException {
        return makeRequest("GET", "/game", null, ListResult.class);
    }

    public CreateResult create(String gameName) throws AlreadyTakenException, BadRequestException, UnauthorizedException {
        return makeRequest("POST", "/game", new CreateRequest(authToken, gameName), CreateResult.class);
    }

    public boolean logout() throws AlreadyTakenException, UnauthorizedException, BadRequestException {
        makeRequest("DELETE", "/session", null, null);
        return true;
    }

    public void join(JoinRequest request) throws AlreadyTakenException, UnauthorizedException, BadRequestException {
        makeRequest("PUT", "/game", request, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass)
            throws AlreadyTakenException, BadRequestException, UnauthorizedException {
        try {
            HttpURLConnection http = (HttpURLConnection) (new URI(serverurl + path)).toURL().openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            http.setRequestProperty("authorization", authToken);
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (AlreadyTakenException e) {
            throw new AlreadyTakenException(e.getMessage());
        }catch (BadRequestException e){
                throw new BadRequestException(e.getMessage());
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        } catch (UnauthorizedException e){
            throw new UnauthorizedException(e.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }
    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, BadRequestException, UnauthorizedException, AlreadyTakenException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                String errorMessage = "Random error";
                InputStreamReader reader = new InputStreamReader(respErr);
                JsonObject json = new Gson().fromJson(reader, JsonObject.class);
                if (json.has("message")) {
                    errorMessage = json.get("message").getAsString();
                }
                //badRequest
                if (status == 400) {
                    throw new BadRequestException(errorMessage);
                }
                //unauthorized
                else if (status == 401) {
                    throw new UnauthorizedException(errorMessage);
                } else if (status == 403) {
                    throw new AlreadyTakenException(errorMessage);
                }
            }
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}

package Server;

import com.google.gson.Gson;
import results.*;
import java.io.*;
import java.net.*;
import requests.*;

public class ServerFacade {
    String serverurl;

    public ServerFacade(String url) {
        serverurl = url;
    }


    public RegisterResult register(RegisterRequest request) {
        return makeRequest("POST", "/user", request, RegisterResult.class);
    }

    public LoginResult login(LoginRequest request) {
        return makeRequest("POST", "/sesion", request, LoginResult.class);
    }

    public ListResult list(){
        return makeRequest("GET", "/game", null, ListResult.class);
    }

    public CreateResult create(CreateRequest request){
        return makeRequest("POST", "/game", request, CreateResult.class);
    }

    public void logout(LogoutRequest request){
        makeRequest("DELETE", "/session", request, null);
    }

    public void join(JoinRequest request){
        makeRequest("PUT", "/game", request, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) {
        try {
            HttpURLConnection http = (HttpURLConnection) (new URI(serverurl + path)).toURL().openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            writeBody(request, http);
            http.connect();
            return readBody(http, responseClass);

        } catch (Exception e) {
            throw new RuntimeException(e);
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
}

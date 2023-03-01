package Webprotocol.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class WebProtocolMethod {
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    public static User userCreationMethod (URI uri, User user) throws IOException, InterruptedException {
        String requestUser = gson.toJson(user);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "/users"))
                .POST(HttpRequest.BodyPublishers.ofString(requestUser))
                .header("Content-type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("POST test: " + response.statusCode());
        return gson.fromJson(response.body(), User.class);
    }

    public static User userUpdateMethod (URI uri, int id, User user22) throws IOException, InterruptedException {
        String requestUser = gson.toJson(user22);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "/users/" + id))
                .PUT(HttpRequest.BodyPublishers.ofString(requestUser))
                .header("Content-type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("POST test: " + response.statusCode());
        return gson.fromJson(response.body(), User.class);
    }
    public static void userDeleteMethod (URI uri, int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "/users/" + id))
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("DEL test: " + response.statusCode());
    }
        public static List<User> informationAboutAllUsers(URI uri) throws IOException, InterruptedException {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri + "/users"))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("GET test: " + response.statusCode());
            Type userList = new TypeToken<List<User>>() {
            }.getType();
            return gson.fromJson(response.body(), userList);
        }

            public static User informationUserSpecificId (URI uri, int id) throws IOException, InterruptedException {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "/users/" + id))
                .GET()
                .build();
            HttpResponse <String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println("GET id test: " + response.statusCode());
                return gson.fromJson(response.body(), User.class);

        }
        public static User userSpecificName (URI uri, String username) throws IOException, InterruptedException {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri + "/users?username=" + username))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("GET username: " + response.statusCode());
            JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();
            return gson.fromJson(jsonArray.get(0), User.class);
        }
    public static int lastPost(URI uri, int userId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "/users/" + userId + "/posts"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
        int psId = -1;
        for (String line : responseBody.split("\n")) {
            String[] parts = line.trim().split(",");
            for (String part : parts) {
                if (part.contains("\"id\":")) {
                    int postId = Integer.parseInt(part.trim().split(":")[1].trim());
                    if (postId > psId) {
                        psId = postId;
                    }
                    break;
                }
            }
        }
        System.out.println("Last post.");
        return psId;
    }

    public static String lastComments (URI uri, int userId, int postId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "/posts/" + postId + "/comments?userId=" + userId))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("All comments.");
        return response.body();
    }
    public static void commentsJson(String commentsJson, int userId, int postId) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        File file = new File("user-" + userId + "-post-" + postId + "-comments.json");
        FileWriter writer = new FileWriter(file);

        JsonArray commentsArray = JsonParser.parseString(commentsJson).getAsJsonArray();
        gson.toJson(commentsArray, writer);

        System.out.println("File writed.");
        writer.close();
    }
    public static void openTasksUser(URI uri, int userId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "/users/" + userId + "/todos"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JSONArray todos = new JSONArray(response.body());
        JSONArray completedTodos = new JSONArray();
        for (int i = 0; i < todos.length(); i++) {
            JSONObject todo = todos.getJSONObject(i);
            if (!todo.getBoolean("completed")) {
                completedTodos.put(todo);
            }
        }
        System.out.println("Uncomplited todos find.");
        FileWriter writer = new FileWriter("user-" + userId + "-uncompleted-todos.json");
        new GsonBuilder().setPrettyPrinting().create().toJson(completedTodos, writer);
        writer.close();
        System.out.println("File writed");
    }



}

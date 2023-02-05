package server;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new HttpTaskServer().startServer();
        new KVServer().start();
        KVTaskClient kvTaskClient = new KVTaskClient("http://localhost:8078/register");
    }
}

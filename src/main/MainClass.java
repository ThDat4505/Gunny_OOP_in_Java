package main;

import static database.JDBCConnection.close;
import static database.JDBCInitializer.init;

public class MainClass {

    public static void main(String[] args) {
        try {
            init();
            new Game();
        }
        finally {
            close();
        }
    }
}

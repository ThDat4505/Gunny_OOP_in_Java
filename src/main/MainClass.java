package main;

import static database.JDBCConnection.close;
import static database.JDBCInitializer.init;

public class MainClass {

    public static void main(String[] args) {
        try {
            new Game();
            init();
        }
        finally {
            close();
        }

    }
}

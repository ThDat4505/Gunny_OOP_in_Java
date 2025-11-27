package main;

import static database.JDBCInitializer.init;

public class MainClass {

    public static void main(String[] args) {
        new Game();
        init();

    }
}

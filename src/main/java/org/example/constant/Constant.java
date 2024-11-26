package org.example.constant;

public enum Constant {
    DB_PATH("src/main/java/org/example/db/wiseSaying/"),
    TEST_DB_PATH("src/test/java/org/example/db/wiseSaying/");

    private final String data;

    Constant(String data){
        this.data = data;
    }

    public String getData(){
        return data;
    }
}

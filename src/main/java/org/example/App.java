package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true){
            System.out.println("== 명언 앱 ==");
            System.out.print("명령) ");

            if(br.readLine().equals("종료"))
                break;
        }

        br.close();
    }
}

package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Say{

    private final String text;
    private final String writer;

    public Say(String text, String writer) {
        this.text = text;
        this.writer = writer;
    }

    public String getText() {
        return text;
    }

    public String getWriter() {
        return writer;
    }
}

public class App {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("== 명언 앱 ==");
        while (true){
            System.out.print("명령) ");

            String s = br.readLine();

            if(s.equals("종료"))
                break;
            else if(s.equals("등록")){
                System.out.print("명언 : ");
                String text = br.readLine();
                System.out.print("작가 : ");
                String writer = br.readLine();

                Say say = new Say(text, writer);
            }
        }

        br.close();
    }
}

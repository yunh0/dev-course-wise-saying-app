package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Say{

    private final int number;
    private final String text;
    private final String writer;

    public Say(String text, String writer, int number) {
        this.text = text;
        this.writer = writer;
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public String getWriter() {
        return writer;
    }

    public int getNumber() {
        return number;
    }
}

public class App {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("== 명언 앱 ==");
        int cnt = 1;

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

                Say say = new Say(text, writer, cnt);
                System.out.println(cnt + "번 명언이 등록되었습니다.");
                cnt++;
            }
        }
        br.close();
    }
}

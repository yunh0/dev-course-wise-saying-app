package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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

    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static final List<Say> sayList = new ArrayList<>();
    private static int cnt = 1;

    private static void add() throws IOException {
        System.out.print("명언 : ");
        String text = br.readLine();
        System.out.print("작가 : ");
        String writer = br.readLine();

        Say say = new Say(text, writer, cnt);
        sayList.add(say);
        System.out.println(cnt + "번 명언이 등록되었습니다.");
        cnt++;
    }

    private static void list(){
        System.out.println("번호 / 작가 / 명언");
        System.out.println("-----------------");
        for (Say say : sayList) {
            System.out.println(say.getNumber() + " / " +
                    say.getWriter() + " / " +
                    say.getText()
            );
        }
    }

    private static void remove(int id) {
        boolean removeOk = false;

        for(Say say : sayList){
            if(say.getNumber() == id){
                sayList.remove(say);
                System.out.println(id + "번 명언이 삭제되었습니다.");
                removeOk = true;
            }
        }

        if(!removeOk){
            System.out.println(id + "번 명언은 존재하지 않습니다.");
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("== 명언 앱 ==");

        boolean exit = true;

        while (exit){
            System.out.print("명령) ");
            StringTokenizer st = new StringTokenizer(br.readLine(), "?");

            if(st.countTokens() == 1){
                switch (st.nextToken()) {
                    case "종료" -> {
                        exit = false;
                    }
                    case "등록" -> {
                        add();
                    }
                    case "목록" ->{
                        list();
                    }
                }
            }
            else{
                switch (st.nextToken()){
                    case "삭제" -> {
                        String s = st.nextToken();
                        if(s.startsWith("id=")){
                            int id = Integer.parseInt(s.substring(3));
                            remove(id);
                        }
                    }
                }
            }
        }
        br.close();
    }
}

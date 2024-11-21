package org.example.Controller;

import org.example.Service.WiseSayingService;
import org.example.dto.Say;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class WiseSayingController {

    private final WiseSayingService wiseSayingService;
    private final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public WiseSayingController(WiseSayingService wiseSayingService){
        this.wiseSayingService = wiseSayingService;
    }

    private void add() throws IOException {
        System.out.print("명언 : ");
        String text = br.readLine();
        System.out.print("작가 : ");
        String writer = br.readLine();

        int id = wiseSayingService.add(text, writer);
        System.out.println(id + "번 명언이 등록되었습니다.");
    }

    private void list() {
        List<Say> say = wiseSayingService.getList();

        System.out.println("번호 / 작가 / 명언");
        System.out.println("-----------------");

        for(int i=say.size()-1; i>=0; i--){
            System.out.println(say.get(i).getNumber() + " / " +
                    say.get(i).getWriter() + " / " +
                    say.get(i).getText()
            );
        }
    }

    private void build() throws IOException {
        wiseSayingService.build();
        System.out.println("data.json 파일의 내용이 갱신되었습니다.");
    }

    private void handleCommand(String command) throws IOException {
        if(command.startsWith("삭제?id=")){
            int id = parsingId(command);
            if(id == -1)
                return;

            Say say = wiseSayingService.getSayById(id);
            if(say == null){
                System.out.println("해당 id가 존재하지 않습니다.");
                return;
            }

            wiseSayingService.delete(id);
            System.out.println(id + "번 명언이 삭제되었습니다.");
        }
        else if(command.startsWith("수정?id=")){
            int id = parsingId(command);
            if(id == -1)
                return;

            Say say = wiseSayingService.getSayById(id);
            if(say == null){
                System.out.println("해당 id가 존재하지 않습니다.");
                return;
            }

            System.out.println("명언(기존) : " + say.getText());
            System.out.print("명언 : ");
            String text = br.readLine();

            System.out.println("작가(기존) : " + say.getWriter());
            System.out.print("작가 : ");
            String writer = br.readLine();
            wiseSayingService.update(id, text, writer);
            System.out.println(id + "번 명언이 수정되었습니다.");
        }
        else{
            System.out.println("알 수 없는 명령입니다.");
        }
    }

    private int parsingId(String command){
        try {
            String [] commands = command.split("=");
            if(commands.length != 2 || commands[1].isEmpty()){
                System.out.println("id 형식이 잘못되었습니다.");
                return -1;
            }
            return Integer.parseInt(commands[1]);
        } catch (NumberFormatException e){
            System.out.println("id 형식이 잘못되었습니다.");
            return -1;
        }
    }

    public void run() throws IOException {
        System.out.println("== 명언 앱 ==");

        boolean exit = true;
        while (exit){
            System.out.print("명령) ");
            String command = br.readLine();

            switch (command) {
                case "종료" -> exit = false;
                case "등록" -> add();
                case "목록" -> list();
                case "빌드" -> build();
                default -> handleCommand(command);
            }
        }
    }
}

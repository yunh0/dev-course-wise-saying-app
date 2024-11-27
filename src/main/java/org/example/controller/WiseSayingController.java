package org.example.controller;

import org.example.service.WiseSayingService;
import org.example.dto.Say;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class WiseSayingController {

    private final WiseSayingService wiseSayingService;
    private final BufferedReader br;

    public WiseSayingController(WiseSayingService wiseSayingService, BufferedReader br) {
        this.wiseSayingService = wiseSayingService;
        this.br = br;
    }

    public void run() {
        System.out.println("== 명언 앱 ==");

        boolean exit = true;
        while (exit){
            try {
                System.out.print("명령) ");
                String command = br.readLine();

                if(command.equals("종료")){
                    exit = false;
                }
                else{
                    handleCommand(command);
                }
            } catch (IOException e){
                System.out.println("명령어 입력 중 오류가 발생했습니다.");
            } catch (Exception e){
                System.out.println("알 수 없는 오류가 발생했습니다.");
            }
        }
    }

    private void handleCommand(String command) throws IOException {
        try{
            switch (command.split("\\?")[0]){
                case "등록" -> add();
                case "목록" -> list(command);
                case "빌드" -> build();
                case "삭제" -> delete(command);
                case "수정" -> update(command);
                default -> throw new IllegalArgumentException("알 수 없는 명령입니다.");
            }
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    private void add() {
        try {
            System.out.print("명언 : ");
            String text = br.readLine();
            System.out.print("작가 : ");
            String writer = br.readLine();

            int id = wiseSayingService.add(text, writer);
            System.out.println(id + "번 명언이 등록되었습니다.");
        } catch (IOException e) {
            System.out.println("입력 중 오류가 발생했습니다.");
        }
    }

    private void list(String command) {
        try{
            List<Say> sayList;

            if(command.startsWith("목록?")){
                sayList = parseListCommand(command);
            }
            else {
                sayList = wiseSayingService.getList();
            }

            if (sayList.isEmpty()) {
                System.out.println("검색 결과가 없습니다.");
            }
            else{
                printSayingList(sayList);
            }
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    private void build() {
        try {
            wiseSayingService.build();
            System.out.println("data.json 파일의 내용이 갱신되었습니다.");
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류가 발생했습니다.");
        }
    }

    private void delete(String command){
        try {
            if(!command.startsWith("삭제?id=")){
                throw new IllegalArgumentException("알 수 없는 명령입니다.");
            }

            int id = parsingId(command);
            Say say = wiseSayingService.getSayById(id);

            if(say == null){
                throw new IllegalArgumentException("해당 id가 존재하지 않습니다.");
            }

            wiseSayingService.delete(id);
            System.out.println(id + "번 명언이 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void update(String command) {
        try {
            if (!command.startsWith("수정?id=")) {
                throw new IllegalArgumentException("알 수 없는 명령입니다.");
            }

            int id = parsingId(command);

            Say say = wiseSayingService.getSayById(id);
            if(say == null){
                throw new IllegalArgumentException("해당 id가 존재하지 않습니다.");
            }

            System.out.println("명언(기존) : " + say.getText());
            System.out.print("명언 : ");
            String text = br.readLine();

            System.out.println("작가(기존) : " + say.getWriter());
            System.out.print("작가 : ");
            String writer = br.readLine();

            wiseSayingService.update(id, text, writer);
            System.out.println(id + "번 명언이 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("입력 중 오류가 발생했습니다.");
        }
    }

    private List<Say> parseListCommand(String command){
        String[] query = command.split("\\?", 2);
        if (query.length != 2 || query[1].isEmpty()) {
            throw new IllegalArgumentException("잘못된 목록 검색 명령입니다.");
        }

        String[] params = query[1].split("&");
        if (params.length != 2) {
            throw new IllegalArgumentException("검색 조건이 잘못되었습니다.");
        }

        String[] keywordTypePair = params[0].split("=");
        String[] keywordPair = params[1].split("=");

        if (keywordTypePair.length != 2 || keywordPair.length != 2 ||
                !"keywordType".equals(keywordTypePair[0]) || !"keyword".equals(keywordPair[0])) {
            throw new IllegalArgumentException("검색 조건이 잘못되었습니다.");
        }

        String keywordType = keywordTypePair[1];
        String keyword = keywordPair[1];

        System.out.println("----------------------");
        System.out.println("검색타입 : " + keywordType);
        System.out.println("검색어 : " + keyword);
        System.out.println("----------------------");

        return wiseSayingService.search(keywordType, keyword);
    }

    private int parsingId(String command){
        try{
            String [] commands = command.split("=");
            if(commands.length != 2 || commands[1].isEmpty()){
                throw new IllegalArgumentException("id 형식이 잘못되었습니다.");
            }
            return Integer.parseInt(commands[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("id는 숫자로만 구성되어야 합니다.");
        }
    }

    private void printSayingList(List<Say> sayList){
        System.out.println("번호 / 작가 / 명언");
        System.out.println("-----------------");

        for(int i=sayList.size()-1; i>=0; i--){
            System.out.println(sayList.get(i).getNumber() + " / " +
                    sayList.get(i).getWriter() + " / " +
                    sayList.get(i).getText()
            );
        }
    }
}

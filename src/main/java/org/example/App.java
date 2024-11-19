package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

class Say{

    private final int number;
    private String text;
    private String writer;

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

    public void setText(String text){
        this.text = text;
    }

    public void setWriter(String writer){
        this.writer = writer;
    }
}

public class App {

    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static final List<Say> sayList = new ArrayList<>();
    private static final String FOLDER = "db/wiseSaying/";
    private static final String LAST_ID_FILE = FOLDER + "lastId.txt";
    private static int cnt = 1;

    private static void initialize() throws IOException {
        File folder = new File(FOLDER);
        if(!folder.exists()){
            folder.mkdirs();
        }

        File lastIdFile = new File(LAST_ID_FILE);
        if(!lastIdFile.exists()){
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(lastIdFile))){
                bw.write("0");
            }
        }
    }

    private static int getLastId() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(LAST_ID_FILE))){
            return Integer.parseInt(reader.readLine().trim());
        }
    }

    private static void saveLastId(int id) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LAST_ID_FILE))){
            bw.write(String.valueOf(id));
        }
    }

    private static void saveSaying(Say say) throws IOException {
        String path = FOLDER + say.getNumber() + ".json";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))){
            bw.write("{\n");
            bw.write("  \"id\": " + say.getNumber() + ",\n");
            bw.write("  \"content\": \"" + say.getText() + "\",\n");
            bw.write("  \"author\": \"" + say.getWriter() + "\"\n");
            bw.write("}");
        }
    }

    private static Say loadSaying(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            int id = 0;
            String text = "";
            String writer = "";

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("\"id\"")) {
                    id = Integer.parseInt(line.split(":")[1].trim().replace(",", "").replace("\"", ""));
                } else if (line.startsWith("\"content\"")) {
                    text = line.split(":")[1].trim().replace(",", "").replace("\"", "");
                } else if (line.startsWith("\"author\"")) {
                    writer = line.split(":")[1].trim().replace(",", "").replace("\"", "");
                }
            }
            return new Say(text, writer, id);
        } catch (Exception e){
            System.err.println("파일 읽기 오류: " + file.getName() + " (" + e.getMessage() + ")");
            throw new IOException("파일 로드 중 오류 발생", e);
        }
    }

    private static void loadAllSaying() throws IOException {
        File folder = new File(FOLDER);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));

        if(files != null){
            for(File file : files){
                try {
                    sayList.add(loadSaying(file));
                } catch (IOException e){
                    continue;
                }
            }
        }
    }

    private static void add() throws IOException {
        System.out.print("명언 : ");
        String text = br.readLine();
        System.out.print("작가 : ");
        String writer = br.readLine();

        Say say = new Say(text, writer, cnt);
        sayList.add(say);
        saveSaying(say);
        saveLastId(cnt);

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

    private static void remove(int id) throws IOException {
        boolean removeOk = false;

        for(Say say : sayList){
            if(say.getNumber() == id){
                sayList.remove(say);
                String path = FOLDER + id + ".json";
                new File(path).delete();

                System.out.println(id + "번 명언이 삭제되었습니다.");
                removeOk = true;
                break;
            }
        }

        if(!removeOk){
            System.out.println(id + "번 명언은 존재하지 않습니다.");
        }
    }

    private static void update(int id) throws IOException {
        boolean isUpdate = false;

        for(Say say : sayList){
            if(say.getNumber() == id){
                System.out.println("명언(기존) : " + say.getText());
                System.out.print("명언 : ");
                say.setText(br.readLine());

                System.out.println("작가(기존) : " + say.getWriter());
                System.out.print("작가 : ");
                say.setWriter(br.readLine());

                saveSaying(say);
                isUpdate = true;
                break;
            }
        }

        if(!isUpdate){
            System.out.println(id + "번 명언은 존재하지 않습니다.");
        }
    }

    public static void main(String[] args) throws IOException {
        initialize();
        cnt = getLastId() + 1;
        loadAllSaying();

        System.out.println("== 명언 앱 ==");

        boolean exit = true;

        while (exit){
            System.out.print("명령) ");
            StringTokenizer st = new StringTokenizer(br.readLine(), "?");

            if(st.countTokens() == 1){
                switch (st.nextToken()) {
                    case "종료" -> exit = false;
                    case "등록" -> add();
                    case "목록" -> list();
                    default -> System.out.println("명령이 잘못되었습니다.");
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
                        else{
                            System.out.println("명령이 잘못되었습니다.");
                        }
                    }
                    case "수정" -> {
                        String s = st.nextToken();
                        if(s.startsWith("id=")){
                            int id = Integer.parseInt(s.substring(3));
                            update(id);
                        }
                        else{
                            System.out.println("명령이 잘못되었습니다.");
                        }
                    }
                    default -> System.out.println("명령이 잘못되었습니다.");
                }
            }
        }
        br.close();
    }
}

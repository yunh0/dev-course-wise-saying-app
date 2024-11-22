package org.example.repository;

import org.example.dto.Say;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WiseSayingRepository {

    private final String FOLDER = "db/wiseSaying/";
    private final String LAST_ID_FILE = FOLDER + "lastId.txt";

    public WiseSayingRepository() throws IOException{
        initialize();
    }

    private void initialize() throws IOException {
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

    public int getLastId() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(LAST_ID_FILE))){
            return Integer.parseInt(reader.readLine().trim());
        }
    }

    public void saveLastId(int id) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LAST_ID_FILE))){
            bw.write(String.valueOf(id));
        }
    }

    public static Say loadSaying(File file) throws IOException {
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
        }
    }

    public List<Say> loadAllSaying() {
        List<Say> sayList = new ArrayList<>();
        File folder = new File(FOLDER);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json") && !name.equals("data.json"));

        if(files != null){
            for(File file : files){
                try {
                    sayList.add(loadSaying(file));
                } catch (IOException e){
                    continue;
                }
            }
        }
        return sayList;
    }

    public void saveSaying(Say say) throws IOException {
        String path = FOLDER + say.getNumber() + ".json";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))){
            bw.write("{\n");
            bw.write("  \"id\": " + say.getNumber() + ",\n");
            bw.write("  \"content\": \"" + say.getText() + "\",\n");
            bw.write("  \"author\": \"" + say.getWriter() + "\"\n");
            bw.write("}");
        }
    }

    public void deleteSaying(int id){
        String path = FOLDER + id + ".json";
        new File(path).delete();
    }

    public void build() throws IOException {
        List<Say> sayList = loadAllSaying();
        String path = FOLDER + "data.json";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))){
            bw.write("[\n");
            for (int i = 0; i < sayList.size(); i++) {
                Say say = sayList.get(i);
                bw.write("  {\n");
                bw.write("      \"id\": " + say.getNumber() + ",\n");
                bw.write("      \"content\": \"" + say.getText() + "\",\n");
                bw.write("      \"author\": \"" + say.getWriter() + "\"\n");
                if (i == sayList.size() - 1) {
                    bw.write("  }\n");
                } else {
                    bw.write("  },\n");
                }
            }
            bw.write("]");
        }
    }
}

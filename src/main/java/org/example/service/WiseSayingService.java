package org.example.service;

import org.example.repository.WiseSayingRepository;
import org.example.dto.Say;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WiseSayingService {

    private final WiseSayingRepository wiseSayingRepository;
    private final List<Say> sayList = new ArrayList<>();
    private int currentId;

    public WiseSayingService(WiseSayingRepository wiseSayingRepository) throws IOException {
        this.wiseSayingRepository = wiseSayingRepository;
        this.currentId = wiseSayingRepository.getLastId() + 1;
        this.sayList.addAll(wiseSayingRepository.loadAllSaying());
    }

    public Say getSayById(int id){
        for(Say say : sayList){
            if(say.getNumber() == id){
                return say;
            }
        }
        return null;
    }

    public List<Say> getList(){
        return new ArrayList<>(sayList);
    }

    public int add(String text, String writer) throws IOException {
        Say say = new Say(text, writer, currentId);
        int id = currentId;

        sayList.add(say);
        wiseSayingRepository.saveSaying(say);
        wiseSayingRepository.saveLastId(currentId);
        currentId++;

        return id;
    }

    public void update(int id, String text, String writer) throws IOException {
        Say say = getSayById(id);
        say.setText(text);
        say.setWriter(writer);
        wiseSayingRepository.saveSaying(say);
    }

    public void delete(int id) {
        Say say = getSayById(id);
        sayList.remove(say);
        wiseSayingRepository.deleteSaying(id);
    }

    public void build() throws IOException {
        wiseSayingRepository.build(sayList);
    }
}

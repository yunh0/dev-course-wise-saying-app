package org.example.service;

import org.example.dto.PageResult;
import org.example.repository.WiseSayingRepository;
import org.example.dto.Say;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WiseSayingService {

    private final WiseSayingRepository wiseSayingRepository;

    public WiseSayingService(WiseSayingRepository wiseSayingRepository) {
        this.wiseSayingRepository = wiseSayingRepository;
    }

    public Say getSayById(int id){
        for(Say say : wiseSayingRepository.loadAllSaying()){
            if(say.getNumber() == id){
                return say;
            }
        }
        return null;
    }

    public List<Say> getList(){
        return wiseSayingRepository.loadAllSaying();
    }

    public PageResult getPaginatedList(int page, int pageSize, List<Say> sayList) {
        int totalSayings = sayList.size();
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalSayings);
        int totalPage = (totalSayings / pageSize) +  ((totalSayings % pageSize > 0) ? 1 : 0);

        if(page > totalPage){
            throw new IllegalArgumentException("검색 결과가 없습니다.");
        }

        List<Say> paginatedList = sayList.subList(startIndex, endIndex);

        return new PageResult(page, totalPage ,paginatedList);
    }

    public int add(String text, String writer) throws IOException {
        int id = wiseSayingRepository.getLastId();

        Say say = new Say(text, writer, id+1);
        wiseSayingRepository.saveSaying(say);
        wiseSayingRepository.saveLastId(id+1);

        return id+1;
    }

    public void update(int id, String text, String writer) throws IOException {
        Say say = getSayById(id);
        say.setText(text);
        say.setWriter(writer);
        wiseSayingRepository.saveSaying(say);
    }

    public void delete(int id) {
        wiseSayingRepository.deleteSaying(id);
    }

    public void build() throws IOException {
        wiseSayingRepository.build();
    }

    public PageResult search(String keywordType, String keyword, int page, int pageSize){
        List<Say> searchSayList = new ArrayList<>();

        for(Say say : wiseSayingRepository.loadAllSaying()){
            if ("content".equals(keywordType) && say.getText().contains(keyword)) {
                searchSayList.add(say);
            }
            else if ("author".equals(keywordType) && say.getWriter().contains(keyword)) {
                searchSayList.add(say);
            }
        }

        return getPaginatedList(page, pageSize, searchSayList);
    }
}
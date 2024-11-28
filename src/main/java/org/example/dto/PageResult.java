package org.example.dto;

import java.util.List;

public class PageResult {

    private final int page;
    private final int totalPage;
    private final List<Say> sayList;

    public PageResult(int page, int totalPage, List<Say> sayList) {
        this.page = page;
        this.totalPage = totalPage;
        this.sayList = sayList;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPage;
    }

    public List<Say> getSayList() {
        return sayList;
    }

    public boolean isEmpty() {
        return sayList.isEmpty();
    }
}

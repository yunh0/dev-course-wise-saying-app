package org.example.controller;

import org.example.AppTest;
import org.example.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;

import static org.assertj.core.api.Assertions.assertThat;

public class WiseSayingControllerTest {

    @BeforeEach
    void setUp(){
        AppTest.deleteTestFiles();
    }

    @Test
    @DisplayName("등록 - 정상작동")
    void shouldAddWiseSayingCorrectlyTest() {
        BufferedReader br = TestUtil.getBufferedReader("""
                등록
                명언1
                작가1
                종료
                """
        );

        String result = AppTest.run(br, false);
        System.out.println(result);

        assertThat(result).contains("1번 명언이 등록되었습니다.");
    }

    @Test
    @DisplayName("목록 출력 - 정상작동")
    void shouldDisplayListOfWiseSayingsTest() {
        BufferedReader br1 = TestUtil.getBufferedReader("""
                등록
                명언1
                작가1
                종료
                """
        );
        AppTest.run(br1, false);

        BufferedReader br2 = TestUtil.getBufferedReader("""
                목록
                종료
                """
        );

        String result = AppTest.run(br2, false);
        System.out.println(result);

        assertThat(result)
                .contains("번호 / 작가 / 명언")
                .contains("1 / 작가1 / 명언1");
    }

    @Test
    @DisplayName("삭제 - 정상작동")
    void shouldDeleteWiseSayingCorrectlyTest() {
        BufferedReader br1 = TestUtil.getBufferedReader("""
                등록
                명언1
                작가1
                종료
                """
        );
        AppTest.run(br1, false);

        BufferedReader br2 = TestUtil.getBufferedReader("""
                삭제?id=1
                종료
                """
        );
        String result = AppTest.run(br2, false);
        System.out.println(result);
        assertThat(result).contains("1번 명언이 삭제되었습니다.");
    }

    @Test
    @DisplayName("수정 - 정상작동")
    void shouldUpdateWiseSayingCorrectlyTest() {
        BufferedReader br1 = TestUtil.getBufferedReader("""
                등록
                명언1
                작가1
                종료
                """
        );
        AppTest.run(br1, false);

        BufferedReader br2 = TestUtil.getBufferedReader("""
                수정?id=1
                졸리다
                남윤호
                종료
                """
        );
        String result = AppTest.run(br2, false);
        System.out.println(result);

        assertThat(result)
                .contains("명언(기존) : 명언1")
                .contains("작가(기존) : 작가1")
                .contains("1번 명언이 수정되었습니다.");
    }

    @Test
    @DisplayName("검색(Content) - 정상작동")
    void shouldSearchWiseSayingByContentCorrectlyTest(){
        BufferedReader br1 = TestUtil.getBufferedReader("""
                등록
                과거에 집착하지마라
                작가1
                등록
                과거는 과거일뿐
                작가2
                등록
                미래를 보며 살아라
                작가3
                종료
                """
        );
        AppTest.run(br1, false);

        BufferedReader br2 = TestUtil.getBufferedReader("""
                목록?keywordType=content&keyword=과거
                종료
                """
        );
        String result = AppTest.run(br2, false);
        System.out.println(result);

        assertThat(result)
                .contains("검색타입 : content")
                .contains("검색어 : 과거")
                .contains("번호 / 작가 / 명언")
                .contains("2 / 작가2 / 과거는 과거일뿐")
                .contains("1 / 작가1 / 과거에 집착하지마라");

    }

    @Test
    @DisplayName("검색(Author) - 정상작동")
    void shouldSearchWiseSayingByAuthorCorrectlyTest(){
        BufferedReader br1 = TestUtil.getBufferedReader("""
                등록
                과거에 집착하지마라
                작자미상
                등록
                과거는 과거일뿐
                남윤호
                등록
                미래를 보며 살아라
                작자미상
                종료
                """
        );
        AppTest.run(br1, false);

        BufferedReader br2 = TestUtil.getBufferedReader("""
                목록?keywordType=author&keyword=작자
                종료
                """
        );
        String result = AppTest.run(br2, false);
        System.out.println(result);

        assertThat(result)
                .contains("검색타입 : author")
                .contains("검색어 : 작자")
                .contains("번호 / 작가 / 명언")
                .contains("3 / 작자미상 / 미래를 보며 살아라")
                .contains("1 / 작자미상 / 과거에 집착하지마라");

    }

    @Test
    @DisplayName("페이징(기본 목록 출력, 페이지 값 제공 X) - 정상작동")
    void shouldWiseSayingListWithoutPageParameterTest(){
        BufferedReader br1 = TestUtil.getBufferedReader("""
                목록
                종료
                """
        );
        String result = AppTest.run(br1, true);

        System.out.println(result);

        assertThat(result)
                .contains("번호 / 작가 / 명언")
                .contains("10 / 작자미상 10 / 명언 10")
                .contains("6 / 작자미상 6 / 명언 6")
                .contains("[페이지 1 / 2]")
                .doesNotContain("5 / 작자미상 5 / 명언 5");
    }

    @Test
    @DisplayName("페이징(기본 목록 출력, 페이지 값 제공 O) - 정상작동")
    void shouldListWiseSayingsWithPageParameterTest(){
        BufferedReader br1 = TestUtil.getBufferedReader("""
                목록?page=2
                종료
                """
        );
        String result = AppTest.run(br1, true);

        System.out.println(result);

        assertThat(result)
                .contains("번호 / 작가 / 명언")
                .contains("5 / 작자미상 5 / 명언 5")
                .contains("1 / 작자미상 1 / 명언 1")
                .contains("[페이지 2 / 2]")
                .doesNotContain("7 / 작자미상 7 / 명언 7");
    }

    @Test
    @DisplayName("페이징(author로 검색한 목록 출력, 페이지 값 제공 X) - 정상작동")
    void shouldSearchWiseSayingsByAuthorWithoutPageParameterTest(){
        BufferedReader br1 = TestUtil.getBufferedReader("""
                목록?keywordType=author&keyword=작자
                종료
                """
        );
        String result = AppTest.run(br1, true);

        System.out.println(result);

        assertThat(result)
                .contains("검색타입 : author")
                .contains("검색어 : 작자")
                .contains("번호 / 작가 / 명언")
                .contains("10 / 작자미상 10 / 명언 10")
                .contains("6 / 작자미상 6 / 명언 6")
                .contains("[페이지 1 / 2]")
                .doesNotContain("5 / 작자미상 5 / 명언 5");
    }

    @Test
    @DisplayName("페이징(content로 검색한 목록 출력, 페이지 값 제공 X) - 정상작동")
    void shouldSearchWiseSayingsByContentWithoutPageParameterTest(){
        BufferedReader br1 = TestUtil.getBufferedReader("""
                목록?keywordType=content&keyword=명언
                종료
                """
        );
        String result = AppTest.run(br1, true);

        System.out.println(result);

        assertThat(result)
                .contains("검색타입 : content")
                .contains("검색어 : 명언")
                .contains("번호 / 작가 / 명언")
                .contains("10 / 작자미상 10 / 명언 10")
                .contains("6 / 작자미상 6 / 명언 6")
                .contains("[페이지 1 / 2]")
                .doesNotContain("5 / 작자미상 5 / 명언 5");
    }

    @Test
    @DisplayName("페이징(author로 검색한 목록 출력, 페이지 값 제공 O) - 정상작동")
    void shouldSearchWiseSayingsByAuthorWithPageParameterTest(){
        BufferedReader br1 = TestUtil.getBufferedReader("""
                목록?page=2?keywordType=author&keyword=작자
                종료
                """
        );
        String result = AppTest.run(br1, true);

        System.out.println(result);

        assertThat(result)
                .contains("검색타입 : author")
                .contains("검색어 : 작자")
                .contains("번호 / 작가 / 명언")
                .contains("5 / 작자미상 5 / 명언 5")
                .contains("1 / 작자미상 1 / 명언 1")
                .contains("[페이지 2 / 2]")
                .doesNotContain("6 / 작자미상 6 / 명언 6");
    }

    @Test
    @DisplayName("페이징(content로 검색한 목록 출력, 페이지 값 제공 O) - 정상작동")
    void shouldSearchWiseSayingsByContentWithPageParameterTest(){
        BufferedReader br1 = TestUtil.getBufferedReader("""
                목록?page=2?keywordType=content&keyword=명언
                종료
                """
        );
        String result = AppTest.run(br1, true);

        System.out.println(result);

        assertThat(result)
                .contains("검색타입 : content")
                .contains("검색어 : 명언")
                .contains("번호 / 작가 / 명언")
                .contains("5 / 작자미상 5 / 명언 5")
                .contains("1 / 작자미상 1 / 명언 1")
                .contains("[페이지 2 / 2]")
                .doesNotContain("6 / 작자미상 6 / 명언 6");
    }

}

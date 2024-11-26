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
        BufferedReader br = TestUtil.genBufferedReader("""
                등록
                명언1
                작가1
                종료
                """
        );

        String result = AppTest.run(br);
        System.out.println(result);

        assertThat(result).contains("1번 명언이 등록되었습니다.");
    }

    @Test
    @DisplayName("목록 출력 - 정상작동")
    void shouldDisplayListOfWiseSayingsTest() {
        BufferedReader br1 = TestUtil.genBufferedReader("""
                등록
                명언1
                작가1
                종료
                """
        );
        AppTest.run(br1);

        BufferedReader br2 = TestUtil.genBufferedReader("""
                목록
                종료
                """
        );

        String result = AppTest.run(br2);
        System.out.println(result);

        assertThat(result)
                .contains("번호 / 작가 / 명언")
                .contains("1 / 작가1 / 명언1");
    }

    @Test
    @DisplayName("삭제 - 정상작동")
    void shouldDeleteWiseSayingCorrectlyTest() {
        BufferedReader br1 = TestUtil.genBufferedReader("""
                등록
                명언1
                작가1
                종료
                """
        );
        AppTest.run(br1);

        BufferedReader br2 = TestUtil.genBufferedReader("""
                삭제?id=1
                종료
                """
        );
        String result = AppTest.run(br2);
        System.out.println(result);
        assertThat(result).contains("1번 명언이 삭제되었습니다.");
    }

    @Test
    @DisplayName("수정 - 정상작동")
    void update_test() {
        BufferedReader br1 = TestUtil.genBufferedReader("""
                등록
                명언1
                작가1
                종료
                """
        );
        AppTest.run(br1);

        BufferedReader br2 = TestUtil.genBufferedReader("""
                수정?id=1
                졸리다
                남윤호
                종료
                """
        );
        String result = AppTest.run(br2);
        System.out.println(result);

        assertThat(result)
                .contains("명언(기존) : 명언1")
                .contains("작가(기존) : 작가1")
                .contains("1번 명언이 수정되었습니다.");
    }
}

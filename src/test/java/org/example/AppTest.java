package org.example;

import org.example.constant.Constant;
import org.example.controller.WiseSayingController;
import org.example.repository.WiseSayingRepository;
import org.example.service.WiseSayingService;

import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;

public class AppTest {

    private static WiseSayingRepository repository;

    public static String run(BufferedReader br, boolean makeSampleData) {
        ByteArrayOutputStream output = TestUtil.setOutToByteArray();

        try {
            repository = new WiseSayingRepository(Constant.TEST_DB_PATH.getData());
            WiseSayingService service = new WiseSayingService(repository);
            WiseSayingController controller = new WiseSayingController(service, br); // Reader 주입
            if (makeSampleData){
                createSampleData();
            }
            controller.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String result = output.toString().trim();
        TestUtil.clearSetOutToByteArray(output);
        return result;
    }

    public static void deleteTestFiles(){
        TestUtil.deleteTestFiles(Constant.TEST_DB_PATH.getData());
    }

    public static void createSampleData(){
        try {
            if (repository != null) {
                repository.createSampleData();
            } else {
                throw new IllegalStateException("레포지토리가 생성되지 않았습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

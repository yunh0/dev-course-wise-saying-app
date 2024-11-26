package org.example;

import org.example.constant.Constant;
import org.example.controller.WiseSayingController;
import org.example.repository.WiseSayingRepository;
import org.example.service.WiseSayingService;

import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;

public class AppTest {

    public static String run(BufferedReader br) {
        ByteArrayOutputStream output = TestUtil.setOutToByteArray();

        try {
            WiseSayingRepository repository = new WiseSayingRepository(Constant.TEST_DB_PATH.getData());
            WiseSayingService service = new WiseSayingService(repository);
            WiseSayingController controller = new WiseSayingController(service, br); // Reader 주입
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
}

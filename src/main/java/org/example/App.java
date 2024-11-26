package org.example;

import org.example.constant.Constant;
import org.example.controller.WiseSayingController;
import org.example.repository.WiseSayingRepository;
import org.example.service.WiseSayingService;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class App {

    public static void main(String[] args) {
        try{
            WiseSayingRepository wiseSayingRepository = new WiseSayingRepository(Constant.DB_PATH.getData());
            WiseSayingService wiseSayingService = new WiseSayingService(wiseSayingRepository);
            WiseSayingController wiseSayingController = new WiseSayingController(wiseSayingService, new BufferedReader(new InputStreamReader(System.in)));
            wiseSayingController.run();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
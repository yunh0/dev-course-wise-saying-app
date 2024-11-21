package org.example;

import org.example.Controller.WiseSayingController;
import org.example.Repository.WiseSayingRepository;
import org.example.Service.WiseSayingService;

public class App {

    public static void main(String[] args) {
        try{
            WiseSayingRepository wiseSayingRepository = new WiseSayingRepository();
            WiseSayingService wiseSayingService = new WiseSayingService(wiseSayingRepository);
            WiseSayingController wiseSayingController = new WiseSayingController(wiseSayingService);
            wiseSayingController.run();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
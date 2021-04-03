package io.wijaya.ceres.cli;

import com.google.cloud.vision.v1.ImageAnnotatorClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import io.wijaya.ceres.http.controller.VisionController;
import picocli.CommandLine.Command;

@Command(name = "vision")
public class Vision implements Runnable {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public void run() {
        System.out.println("Running Ceres Vision");

        try {
            ImageAnnotatorClient gcVisionClient = ImageAnnotatorClient.create();
            VisionController visionController = new VisionController(gcVisionClient);

            Javalin app = Javalin.create(config -> {
                config.defaultContentType = "application/json";
            }).routes(() -> {
                ApiBuilder.post("/vision/api/read", visionController::readImage);
            }).start(7700);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }
    }

}

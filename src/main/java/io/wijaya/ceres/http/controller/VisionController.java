package io.wijaya.ceres.http.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;
import com.pivovarit.function.ThrowingFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.http.BadGatewayResponse;
import io.javalin.http.Context;
import io.wijaya.ceres.http.dto.ImageResponse;
import io.wijaya.ceres.http.dto.VisionResponse;

public class VisionController {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private ImageAnnotatorClient visionClient;

    public VisionController(ImageAnnotatorClient visionClient) {
        this.visionClient = visionClient;
    }

    public void readImage(Context ctx) throws Exception {
        try {
            List<AnnotateImageRequest> requests = ctx.uploadedFiles("images[]").stream().map(ThrowingFunction.unchecked(f -> {
                Image img = Image.newBuilder().setContent(ByteString.readFrom(f.getContent())).build();
                return AnnotateImageRequest.newBuilder()
                    .addFeatures(Feature.newBuilder().setType(Type.LOGO_DETECTION).build())
                    .addFeatures(Feature.newBuilder().setType(Type.TEXT_DETECTION).build())
                    .setImage(img).build();
            })).collect(Collectors.toList());

            BatchAnnotateImagesResponse response = visionClient.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();
            
            List<ImageResponse> images = new ArrayList<>();
            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    return;
                }

                List<String> logos = new ArrayList<>();
                for (EntityAnnotation annotation : res.getLogoAnnotationsList()) {
                    logos.add(annotation.getDescription());
                }

                images.add(new ImageResponse(logos));
            }

            ctx.json(new VisionResponse(images));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BadGatewayResponse(e.getMessage());
        }
        
    }

}

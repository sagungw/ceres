package io.wijaya.ceres.http.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
public class VisionResponse {

    @Setter @Getter
    private List<ImageResponse> images;

}

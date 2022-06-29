package com.envisioniot.payload.cbor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Desc:
 *
 * @author zhonghua.wu
 * @date 2022-06-28 23:56:47
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CborUploadMeasurepoint {

    private String id;
    private String version;
    private String method;
    private Params params;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Params {
        private Map<String, Object> measurepoints;
        private Long timestamp;
    }

}

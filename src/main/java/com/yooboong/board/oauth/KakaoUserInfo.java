package com.yooboong.board.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class KakaoUserInfo {
    @JsonProperty("id")
    private String id;

    @JsonProperty("connected_at")
    private String connected_at;
}

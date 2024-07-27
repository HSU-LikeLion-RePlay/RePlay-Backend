package com.likelion.RePlay.domain.learning.web.dto;

import com.likelion.RePlay.global.enums.Category;
import com.likelion.RePlay.global.enums.District;
import com.likelion.RePlay.global.enums.State;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningWriteRequestDTO {
    private String phoneId;
    private String title;
    private String date;
    private String locate; // 카카오맵으로 검색한 주소
    private double latitude; // 카카오맵에서 받아온 위도
    private double longitude; // 카카오맵에서 받아온 경도
    private State state;
    private District district;
    private Category category;
    private String content; // 놀이 설명
    private Long totalCount;
    private String cost; // 참가비용
    private String costDescription; //참가비용 설명
    private String imageUrl;
}
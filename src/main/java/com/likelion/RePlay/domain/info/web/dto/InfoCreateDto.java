package com.likelion.RePlay.domain.info.web.dto;

import com.likelion.RePlay.domain.info.entity.Info;
import com.likelion.RePlay.global.enums.RoleName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class InfoCreateDto {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class InfoCreateRequest {
        @NotNull(message="권한을 입력해주세요.")
        private RoleName roleName; //권한

        @NotBlank(message="제목을 입력해주세요.")
        private String title; //제목

        @NotBlank(message="내용을 입력해주세요.")
        private String content; //내용

        @NotEmpty(message = "작성자 이름을 입력해주세요.")
        private String writer; // 작성자 (관리자 이름)

        @NotBlank(message="몇 호인지 입력해주세요.")
        private Long infoNum; //생생정보터 호 수

        public Info toEntity(){
            return Info.builder()
                    .title(title)
                    .content(content)
                    .infoNum(infoNum)
                    .writer(writer)
                    .build();

        }

    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateInfo {
        private Long infoId;
        private LocalDateTime updatedAt;

        public CreateInfo(Long infoId, LocalDateTime updatedAt) {
            this.infoId = infoId;
            this.updatedAt = updatedAt;
        }
    }

}
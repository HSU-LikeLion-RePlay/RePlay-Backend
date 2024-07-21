package com.likelion.RePlay.entity.info;

import com.likelion.RePlay.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="INFO")
public class Info extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="INFO_ID")
    private Long infoId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

}

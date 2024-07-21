package com.likelion.RePlay.entity.learning;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.likelion.RePlay.entity.User;
import com.likelion.RePlay.enums.Category;
import com.likelion.RePlay.enums.IsCompleted;
import com.likelion.RePlay.enums.IsRecruit;
import com.likelion.RePlay.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="LEARNING")
public class Learning extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="LEARNING_ID")
    private Long learningId;

    @Column(name="TITLE")
    private String title;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name="DATE")
    private Date date;

    @Enumerated(EnumType.STRING)
    private IsRecruit isRecruit;

    @Column(name="RECRUIT_COUNT")
    private Long recruitCount;

    @Column(name="CONTENT")
    private String content;

    @Column(name="LATITUDE")
    private float latitude; // null 가능하다면 Float

    @Column(name="LONGITUDE")
    private float longitude;

    @Enumerated(EnumType.STRING)
    private IsCompleted isCompleted;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "learning", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LearningReview> learningReviews;

    @OneToMany(mappedBy = "learning", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LearningComment> learningComments;

    @OneToMany(mappedBy = "learning", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LearningImage> learningImages;

    @OneToMany(mappedBy = "learning", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LearningScrap> learningScraps;

    @OneToMany(mappedBy = "learning", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LearningApply> learningApplies;
}

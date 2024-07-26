package com.likelion.RePlay.domain.playing.service;

import com.likelion.RePlay.domain.playing.web.dto.PlayingFilteringDTO;
import com.likelion.RePlay.domain.playing.web.dto.PlayingListDTO;
import com.likelion.RePlay.domain.playing.web.dto.PlayingWriteRequestDTO;
import com.likelion.RePlay.domain.playing.entity.Playing;
import com.likelion.RePlay.domain.playing.entity.PlayingApply;
import com.likelion.RePlay.domain.playing.entity.QPlaying;
import com.likelion.RePlay.domain.playing.repository.PlayingApplyRepository;
import com.likelion.RePlay.domain.playing.repository.PlayingRepository;
import com.likelion.RePlay.domain.user.entity.User;
import com.likelion.RePlay.domain.user.repository.UserRepository;
import com.likelion.RePlay.global.enums.District;
import com.likelion.RePlay.global.enums.IsCompleted;
import com.likelion.RePlay.global.enums.IsRecruit;
import com.likelion.RePlay.global.enums.State;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class PlayingServiceImpl implements PlayingService {

    private final UserRepository userRepository;
    private final PlayingRepository playingRepository;
    private final PlayingApplyRepository playingApplyRepository;

    @Override
    public ResponseEntity<CustomAPIResponse<?>> writePost(PlayingWriteRequestDTO playingWriteRequestDTO) {

        // 게시글 작성자가 DB에 존재하는가?
        Optional<User> findUser = userRepository.findByPhoneId(playingWriteRequestDTO.getPhoneId());

        // 없다면 오류 반환
        if (findUser.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 사용자입니다."));
        }

        String dateStr = playingWriteRequestDTO.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 M월 d일 a h시 m분");
        Date date = new Date();
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 존재한다면 게시글을 DB에 저장한다. 자기소개는 User 엔티티에 저장한다.
        User user = findUser.get();
        user.changeIntroduce(playingWriteRequestDTO.getIntroduce());
        userRepository.save(user);

        Playing newPlaying = Playing.builder()
                .user(user)
                .title(playingWriteRequestDTO.getTitle())
                .category(playingWriteRequestDTO.getCategory())
                .date(date)
                .isRecruit(IsRecruit.TRUE)
                .isCompleted(IsCompleted.FALSE)
                .totalCount(playingWriteRequestDTO.getTotalCount())
                .recruitmentCount(0L)
                .content(playingWriteRequestDTO.getContent())
                .cost(Long.valueOf(playingWriteRequestDTO.getCost()))
                .costDescription(playingWriteRequestDTO.getCostDescription())
                .locate(playingWriteRequestDTO.getLocate())
                .latitude(playingWriteRequestDTO.getLatitude())
                .longitude(playingWriteRequestDTO.getLongitude())
                .state(playingWriteRequestDTO.getState())
                .district(playingWriteRequestDTO.getDistrict())
                .imageUrl(playingWriteRequestDTO.getImageUrl())
                .build();

        playingRepository.save(newPlaying);

        return ResponseEntity.status(201)
                .body(CustomAPIResponse.createSuccess(201, null, "게시글을 성공적으로 작성하였습니다."));
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getAllPosts() {
        List<Playing> playings = playingRepository.findAll();

        List<PlayingListDTO.PlayingResponse> playingResponses = new ArrayList<>();

        for (Playing playing : playings) {
            playingResponses.add(PlayingListDTO.PlayingResponse.builder()
                    .category(playing.getCategory())
                    .title(playing.getTitle())
                    .state(playing.getState())
                    .district(playing.getDistrict())
                    .date(playing.getDate())
                    .totalCount(playing.getTotalCount())
                    .recruitmentCount(playing.getRecruitmentCount())
                    .imageUrl(playing.getImageUrl())
                    .build());
        }

        // 사용자에게 반환하기위한 최종 데이터
        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, playingResponses, "게시글 목록을 성공적으로 불러왔습니다."));
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getPost(Long playingId) {

        Optional<Playing> findPlaying = playingRepository.findById(playingId);
        User user = findPlaying.get().getUser();

        if (findPlaying.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 게시글입니다."));
        }

        PlayingListDTO.PlayingResponse playingResponse = PlayingListDTO.PlayingResponse.builder()
                .nickname(user.getNickname())
                .introduce(user.getIntroduce())
                .category(findPlaying.get().getCategory())
                .title(findPlaying.get().getTitle())
                .date(findPlaying.get().getDate())
                .locate(findPlaying.get().getLocate())
                .state(findPlaying.get().getState())
                .district(findPlaying.get().getDistrict())
                .totalCount(findPlaying.get().getTotalCount())
                .recruitmentCount(findPlaying.get().getRecruitmentCount())
                .content(findPlaying.get().getContent())
                .cost(findPlaying.get().getCost())
                .costDescription(findPlaying.get().getCostDescription())
                .imageUrl(findPlaying.get().getImageUrl())
                .build();

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, playingResponse, "특정 게시글을 성공적으로 불러왔습니다."));
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> filtering(PlayingFilteringDTO playingFilteringDTO) {

        // 1. 모든 게시글을 DB에서 불러오기
        List<Playing> allPlayings = playingRepository.findAll();

        // 2. 문자열로 된 dateList를 Date 객체로 변환
        List<Date> parsedDates = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM d", Locale.KOREA); // "8월 2일" 형식으로 변환
        try {
            for (String dateString : playingFilteringDTO.getDateList()) {
                // 연도와 시간을 설정하지 않고 날짜와 월만 설정
                Date date = formatter.parse(dateString);
                parsedDates.add(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CustomAPIResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), "날짜 형식이 잘못되었습니다."));
        }

        // 3. Date 조건과 일치하는 게시글만 남기기 (Date가 null일 경우 필터링하지 않음)
        List<Playing> filteredByDate = allPlayings;
        if (!parsedDates.isEmpty()) {
            filteredByDate = filteredByDate.stream()
                    .filter(playing -> {
                        LocalDate playingDate = playing.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        return parsedDates.stream().anyMatch(date -> {
                            LocalDate filterDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            return playingDate.getMonth() == filterDate.getMonth() && playingDate.getDayOfMonth() == filterDate.getDayOfMonth();
                        });
                    })
                    .collect(Collectors.toList());
            System.out.println("날짜 필터 : " + filteredByDate.size() + "개 있습니다.");
        } else {
            System.out.println("필터가 적용되지 않았습니다.");
        }

        // 4. State 및 District 조건과 일치하는 게시글만 남기기 (Null일 경우 필터링하지 않음)
        List<Playing> filteredByLocation = filteredByDate;
        if (playingFilteringDTO.getStateList() != null && !playingFilteringDTO.getStateList().isEmpty()) {
            filteredByLocation = filteredByLocation.stream()
                    .filter(playing -> {
                        boolean matches = false;
                        for (int i = 0; i < playingFilteringDTO.getStateList().size(); i++) {
                            State state = playingFilteringDTO.getStateList().get(i);
                            District district = (playingFilteringDTO.getDistrictList() != null && playingFilteringDTO.getDistrictList().size() > i)
                                    ? playingFilteringDTO.getDistrictList().get(i)
                                    : null;
                            if (playing.getState().equals(state) &&
                                    (district == null || district.equals(District.ALL) || playing.getDistrict().equals(district))) {
                                matches = true;
                                break;
                            }
                        }
                        return matches;
                    })
                    .collect(Collectors.toList());
            System.out.println("시 및 구 필터 : " + filteredByLocation.size() + "개 있습니다.");
        } else {
            System.out.println("필터가 적용되지 않았습니다.");
        }

        // 5. Category 조건과 일치하는 게시글만 남기기 (Null일 경우 필터링하지 않음)
        List<Playing> filteredByCategory = filteredByLocation;
        if (playingFilteringDTO.getCategory() != null) {
            filteredByCategory = filteredByCategory.stream()
                    .filter(playing -> playing.getCategory().equals(playingFilteringDTO.getCategory()))
                    .collect(Collectors.toList());
            System.out.println("카테고리 필터 : " + filteredByCategory.size() + "개 있습니다.");
        } else {
            System.out.println("필터가 적용되지 않았습니다.");
        }

        // 6. 모든 조건에 부합하는 게시글만 ResponseBody로 전달하기
        List<PlayingListDTO.PlayingResponse> playingResponse = new ArrayList<>();
        for (Playing result : filteredByCategory) {
            PlayingListDTO.PlayingResponse response = PlayingListDTO.PlayingResponse.builder()
                    .category(result.getCategory())
                    .title(result.getTitle())
                    .state(result.getState())
                    .district(result.getDistrict())
                    .date(result.getDate())
                    .totalCount(result.getTotalCount())
                    .recruitmentCount(result.getRecruitmentCount())
                    .imageUrl(result.getImageUrl())
                    .build();
            playingResponse.add(response);
        }

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, playingResponse, "조건에 맞는 게시글들을 성공적으로 불러왔습니다."));
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> recruitPlaying(Long playingId, String phoneId) {

        // 놀이터 게시글이 DB에 존재하는가?
        Optional<Playing> findPlaying = playingRepository.findById(playingId);
        Optional<User> findUser = userRepository.findByPhoneId(phoneId);
        Optional<PlayingApply> findPlayingApply = playingApplyRepository.findByUserPhoneId(phoneId);

        // 존재하지 않는다면 오류 반환
        if (findPlaying.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 게시글입니다."));
        }

        // 인원이 다 차거나 모집 완료된 활동일 경우 오류 반환
        if (findPlaying.get().getIsRecruit() == IsRecruit.FALSE) {
            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "인원이 마감되었습니다."));
        } else if (findPlaying.get().getIsCompleted() == IsCompleted.TRUE) {
            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "모집 완료된 활동입니다."));
        }

        // 이미 신청한 활동일 경우, 오류 반환
        for (PlayingApply apply : findUser.get().getPlayingApplies()) {
            if (apply.getPlayingApplyId().equals(findPlayingApply.get().getPlayingApplyId())) {
                return ResponseEntity.status(400)
                        .body(CustomAPIResponse.createFailWithout(400, "이미 신청한 활동입니다."));
            }
        }

        // 신청하지 않은 활동일 경우
        // 해당 게시글 신청 정보에 해당 유저의 정보를 추가한다.
        findPlayingApply.get().changeUser(findUser.get());
        findPlayingApply.get().changePlaying(findPlaying.get());

        // 해당 게시글에 모집 인원을 추가한다.
        findPlaying.get().changeRecruitmentCount(findPlaying.get().getRecruitmentCount() + 1);

        // 모집인원이 다 찼을 경우, 모집중 -> 모집완료로 바꾼다.
        if (findPlaying.get().getRecruitmentCount() == findPlaying.get().getTotalCount()) {
            findPlaying.get().changeIsRecruit(IsRecruit.FALSE);
        }

        PlayingListDTO.PlayingResponse playingResponse = PlayingListDTO.PlayingResponse.builder()
                .category(findPlaying.get().getCategory())
                .title(findPlaying.get().getTitle())
                .date(findPlaying.get().getDate())
                .cost(findPlaying.get().getCost())
                .build();
        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, playingResponse, "활동 신청이 성공적으로 완료되었습니다."));
    }
}
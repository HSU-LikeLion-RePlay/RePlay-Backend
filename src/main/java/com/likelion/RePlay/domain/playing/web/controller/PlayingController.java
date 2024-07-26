package com.likelion.RePlay.domain.playing.web.controller;

import com.likelion.RePlay.domain.playing.web.dto.PlayingFilteringDTO;
import com.likelion.RePlay.domain.playing.web.dto.PlayingWriteRequestDTO;
import com.likelion.RePlay.domain.playing.service.PlayingServiceImpl;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/playing")
@RequiredArgsConstructor
public class PlayingController {

    private final PlayingServiceImpl playingService;

    @PostMapping("/writePost")
    private ResponseEntity<CustomAPIResponse<?>> writePost(@RequestBody PlayingWriteRequestDTO playingWriteRequestDTO) {
        return playingService.writePost(playingWriteRequestDTO);
    }

    @GetMapping("/")
    private ResponseEntity<CustomAPIResponse<?>> getAllPosts() {
        return playingService.getAllPosts();
    }

    @GetMapping("/{playingId}")
    private ResponseEntity<CustomAPIResponse<?>> getPost(@PathVariable Long playingId) {
        return playingService.getPost(playingId);
    }

    @PostMapping("/filtering")
    private ResponseEntity<CustomAPIResponse<?>> filtering(@RequestBody PlayingFilteringDTO playingFilteringDTO) {
        return playingService.filtering(playingFilteringDTO);
    }

    @PutMapping("/{playingId}/{phoneId}")
    private ResponseEntity<CustomAPIResponse<?>> recruitPlaying(@PathVariable Long playingId, @PathVariable String phoneId) {
        return playingService.recruitPlaying(playingId, phoneId);
    }
}
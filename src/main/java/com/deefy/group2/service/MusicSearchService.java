package com.deefy.group2.service;

import com.deefy.group2.dto.response.MusicSearchResponseDto;

public interface MusicSearchService {

    MusicSearchResponseDto search(String title, String artist, String genre);
}

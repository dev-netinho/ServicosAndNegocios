package com.deefy.group2.mapper;

import com.deefy.group2.dto.request.ListeningHistoryRequest;
import com.deefy.group2.model.ListeningHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.deefy.group2.dto.response.ListeningHistoryResponse;

@Mapper(componentModel = "spring")
public interface ListeningHistoryMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "music.id", target = "musicId")
    @Mapping(source = "dataHoraExecucao", target = "dataHoraExecucao")
    ListeningHistoryResponse toResponse(ListeningHistory entity);


    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "musicId", target = "music.id")
    ListeningHistory toEntity(ListeningHistoryResponse dto);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "musicId", target = "music.id")
    ListeningHistory toEntity(ListeningHistoryRequest request);
}

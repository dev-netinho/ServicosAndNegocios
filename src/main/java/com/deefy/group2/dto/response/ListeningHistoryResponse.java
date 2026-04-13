package com.deefy.group2.dto.response;

import java.time.LocalDateTime;

public record ListeningHistoryResponse(
        Long id,
        Long userId,
        Long musicId,
        LocalDateTime dataHoraExecucao
) {
}

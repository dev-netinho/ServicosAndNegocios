package com.deefy.group2.service;

import com.deefy.group2.dto.request.ListeningHistoryRequest;
import com.deefy.group2.dto.response.ListeningHistoryResponse;

public interface ListeningHistoryService {

    ListeningHistoryResponse saveListeningHistory(ListeningHistoryRequest request);
}

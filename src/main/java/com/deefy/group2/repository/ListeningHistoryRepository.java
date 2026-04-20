package com.deefy.group2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.deefy.group2.model.ListeningHistory;

import java.util.List;

public interface ListeningHistoryRepository extends JpaRepository<ListeningHistory, Long>{

    List<ListeningHistory> findAllByUserIdOrderByDataHoraExecucaoDesc(Long userIthd);
}

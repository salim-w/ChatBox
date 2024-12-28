package com.example.chatbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.chatbox.entities.Message;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {


    List<Message> findByContentContaining(String keyword);
    List<Message> findByRoomId(Long roomId);
    List<Message> findByTimestampAfter(LocalDateTime timestamp);

}



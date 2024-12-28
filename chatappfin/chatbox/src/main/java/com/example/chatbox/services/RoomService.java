package com.example.chatbox.services;
import com.example.chatbox.entities.Room;
import com.example.chatbox.entities.User;
import com.example.chatbox.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }
    public void addUserToRoom(Room room, User user) {
        room.getUsers().add(user);
        roomRepository.save(room);
    }

    public void removeUserFromRoom(Room room, User user) {
        room.getUsers().remove(user);
        roomRepository.save(room);
    }

}

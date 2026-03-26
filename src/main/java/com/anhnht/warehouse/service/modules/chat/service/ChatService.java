package com.anhnht.warehouse.service.modules.chat.service;

import com.anhnht.warehouse.service.modules.chat.entity.ChatRoom;
import com.anhnht.warehouse.service.modules.chat.entity.ChatRoomType;
import com.anhnht.warehouse.service.modules.chat.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatService {

    List<ChatRoomType> getRoomTypes();

    ChatRoom createRoom(Integer creatorId, String roomName, String typeName);

    Page<ChatRoom> getMyRooms(Integer userId, Pageable pageable);

    Page<ChatRoom> getAllRooms(Pageable pageable);

    void addMember(Integer roomId, Integer requesterId, Integer newUserId);

    Message sendMessage(Integer roomId, Integer senderId, String description);

    Page<Message> getMessages(Integer roomId, Integer requesterId, Pageable pageable);
}

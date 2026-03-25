package com.anhnht.warehouse.service.modules.chat.service.impl;

import com.anhnht.warehouse.service.common.constant.ErrorCode;
import com.anhnht.warehouse.service.common.exception.BusinessException;
import com.anhnht.warehouse.service.common.exception.ForbiddenException;
import com.anhnht.warehouse.service.common.exception.ResourceNotFoundException;
import com.anhnht.warehouse.service.modules.chat.entity.*;
import com.anhnht.warehouse.service.modules.chat.repository.*;
import com.anhnht.warehouse.service.modules.chat.service.ChatService;
import com.anhnht.warehouse.service.modules.user.entity.User;
import com.anhnht.warehouse.service.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository       chatRoomRepository;
    private final ChatRoomTypeRepository   chatRoomTypeRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MessageRepository        messageRepository;
    private final UserRepository           userRepository;

    @Override
    @Transactional
    public ChatRoom createRoom(Integer creatorId, String roomName, String typeName) {
        ChatRoomType type = chatRoomTypeRepository.findByTypeNameIgnoreCase(typeName)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.NOT_FOUND,
                        "Chat room type not found: " + typeName));

        User creator = findUser(creatorId);

        ChatRoom room = new ChatRoom();
        room.setRoomName(roomName);
        room.setType(type);
        ChatRoom saved = chatRoomRepository.save(room);

        // Auto-add creator as 'owner'
        ChatRoomMember member = new ChatRoomMember();
        member.setId(new ChatRoomMemberId(saved.getRoomId(), creatorId));
        member.setRoom(saved);
        member.setUser(creator);
        member.setRole("owner");
        chatRoomMemberRepository.save(member);

        return saved;
    }

    @Override
    public Page<ChatRoom> getMyRooms(Integer userId, Pageable pageable) {
        return chatRoomRepository.findByMemberUserId(userId, pageable);
    }

    @Override
    public Page<ChatRoom> getAllRooms(Pageable pageable) {
        return chatRoomRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void addMember(Integer roomId, Integer requesterId, Integer newUserId) {
        ChatRoom room = findRoom(roomId);

        // Only room owner or ADMIN can add members — checked by role in member table
        boolean isOwner = chatRoomMemberRepository.existsByIdRoomIdAndIdUserId(roomId, requesterId);
        if (!isOwner) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        }

        if (chatRoomMemberRepository.existsByIdRoomIdAndIdUserId(roomId, newUserId)) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS, "User is already a member of this room");
        }

        User newUser = findUser(newUserId);
        ChatRoomMember member = new ChatRoomMember();
        member.setId(new ChatRoomMemberId(roomId, newUserId));
        member.setRoom(room);
        member.setUser(newUser);
        member.setRole("member");
        chatRoomMemberRepository.save(member);
    }

    @Override
    @Transactional
    public Message sendMessage(Integer roomId, Integer senderId, String description) {
        ChatRoom room = findRoom(roomId);

        if (!chatRoomMemberRepository.existsByIdRoomIdAndIdUserId(roomId, senderId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        }

        User sender = findUser(senderId);
        Message msg = new Message();
        msg.setRoom(room);
        msg.setSender(sender);
        msg.setDescription(description);
        return messageRepository.save(msg);
    }

    @Override
    public Page<Message> getMessages(Integer roomId, Integer requesterId, Pageable pageable) {
        if (!chatRoomRepository.existsById(roomId)) {
            throw new ResourceNotFoundException(ErrorCode.NOT_FOUND, "Chat room not found: " + roomId);
        }
        if (!chatRoomMemberRepository.existsByIdRoomIdAndIdUserId(roomId, requesterId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        }
        return messageRepository.findByRoomId(roomId, pageable);
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private ChatRoom findRoom(Integer roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.NOT_FOUND,
                        "Chat room not found: " + roomId));
    }

    private User findUser(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.NOT_FOUND,
                        "User not found: " + userId));
    }
}

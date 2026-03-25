package com.anhnht.warehouse.service.modules.chat.repository;

import com.anhnht.warehouse.service.modules.chat.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {

    /** Rooms the current user is a member of. */
    @EntityGraph(attributePaths = {"type"})
    @Query("SELECT r FROM ChatRoom r JOIN ChatRoomMember m ON m.room = r WHERE m.user.userId = :userId")
    Page<ChatRoom> findByMemberUserId(@Param("userId") Integer userId, Pageable pageable);

    /** Admin: all rooms. */
    @EntityGraph(attributePaths = {"type"})
    Page<ChatRoom> findAll(Pageable pageable);
}

package com.book.BookProject.inquiryboard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryBoardRepository extends JpaRepository<InquiryBoard, Long>
{
    @Modifying
    @Query(value = "update INQUIRYBOARD set QVIEW_COUNT=QVIEW_COUNT+1 where qidx=:qidx", nativeQuery = true)
    void viewCount(@Param("qidx") Long qidx); // 조회수 증가

    Page<InquiryBoard> findByTitleContaining(String title, Pageable pageable);
    Page<InquiryBoard> findByContentContaining(String content, Pageable pageable);
    Page<InquiryBoard> findByNickContaining(String nick, Pageable pageable);
}

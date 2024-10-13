package com.book.BookProject.salesboard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SalesBoardRepository extends JpaRepository<SalesBoard, Long> {

    // 최신 게시물 순으로 불러오기
    @Query(value = "select * from SALESBOARD order by sidx desc", nativeQuery = true)
    List<SalesBoard> findSalesBoardPost();

    @Modifying
    @Query(value = "update SALESBOARD set sview_count=sview_count+1 where sidx=:sidx", nativeQuery = true)
    int viewCount(@Param("sidx") Long sidx); // 조회수 증가

    @Modifying
    @Query(value = "update SALESBOARD set slike_count=slike_count+1 where sidx=:sidx", nativeQuery = true)
    int likeCount(@Param("sidx") Long sidx); // 조회수 증가

    @Modifying
    @Query(value = "update SALESBOARD set sdown_count=sdown_count+1 where sidx=:sidx", nativeQuery = true)
    int downCount(@Param("sidx") Long sidx); // 다운로드 수 증가


}


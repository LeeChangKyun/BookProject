package com.book.BookProject.inquiryboard;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InquiryBoardDTO {
    private Long qidx;         // 인덱스
    private int parentIdx;     // 부모 인덱스
    private String parentId;    // 부모 ID
    private String nick;       // 작성자 닉네임
    private int responses;     // 응답 수
    private String title;      // 제목
    private String content;    // 내용
    private String pass;          // 글 비밀번호
    private LocalDate createDate; // 생성 날짜
    private LocalDate updateDate; // 업데이트 날짜
    private int viewCount;     // 조회 수
    private int likeCount;     // 좋아요 수
    private String ofile;      // 원본 파일명
    private String sfile;      // 저장된 파일명
    private int downCount;     // 다운로드 수
}

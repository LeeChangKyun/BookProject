package com.book.BookProject.inquiryboard;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.LocalDate;

@DynamicInsert
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Table(name = "INQUIRYBOARD")
public class InquiryBoard {

    @Id // 엔티티의 주키(primary key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동증가(Auto Increment)를 지정
    private  Long qidx; // 인덱스


    @Column(name="QPARENTIDX", columnDefinition = "INT default 0")
    private int parentIdx; // 부모 인덱스
    @Column(name="QPARENTID", columnDefinition = "VARCHAR(50)")
    private String parentId; // 부모 ID
//    @ManyToOne
//    @JoinColumn(name = "member")
    private String nick; // 외래키, 작성자 닉네임
    @Column(name="QRESPONSES", columnDefinition = "INT default 0")
    private int responses; // 답변 여부
    @Column(name="QTITLE", nullable = false, columnDefinition = "VARCHAR(100)")
    private String title; // 제목
    @Column(name="QCONTENT", nullable = false, columnDefinition = "TEXT")
    private String content; // 내용
    @Column(name="QPASS", columnDefinition = "VARCHAR(20)")
    private String pass; // 글 비밀번호
    @Column(name="QCREATE_DATE", columnDefinition = "DATETIME default CURRENT_TIMESTAMP")
    private LocalDate createDate; // 생성 날짜
    @LastModifiedDate
    @Column(name="QUPDATE_DATE", columnDefinition = "DATETIME default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDate updateDate; // 업데이트 날짜
    @Column(name="QVIEW_COUNT", columnDefinition = "INT default 0")
    private int viewCount; // 조회수
    @Column(name="QLIKE_COUNT", columnDefinition = "INT default 0")
    private int likeCount; // 좋아요 수
    @Column(name="QOFILE", columnDefinition = "VARCHAR(200)")
    private String ofile; // 원본 파일명
    @Column(name="QSFILE", columnDefinition = "VARCHAR(200)")
    private String sfile; // 저장된 파일명
    @Column(name="QDOWN_COUNT", columnDefinition = "INT default 0")
    private int downCount; // 다운로드 수

}

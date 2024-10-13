package com.book.BookProject.salesboard;

import com.book.BookProject.salesboard.Redis.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SalesBoardService {
    @Autowired
    private SalesBoardRepository salesBoardRepository;
    @Autowired
    private SalesBoardMapper salesBoardMapper;

    @Autowired
    private RedisUtil redisUtil;

    // 게시글 리스트 보기
    public List<SalesBoardDTO> getAllSalesBoards(){
        List<SalesBoard> salesBoards = salesBoardRepository.findSalesBoardPost();
        return salesBoardMapper.toDtoList(salesBoards);
    }

    // 게시글 작성하기
    public void createSalesBoard(SalesBoardDTO salesBoardDTO) {
        SalesBoard salesBoard = salesBoardMapper.toEntity(salesBoardDTO);

        salesBoardRepository.save(salesBoard);
    }

    // 게시글 상세 조회
    public SalesBoardDTO getSalesBoardById(Long sidx) {
        SalesBoard salesBoard = salesBoardRepository.findById(sidx).get();

        return salesBoardMapper.toDto(salesBoard);
    }

    // 게시글 삭제하기
    public void deletedSalesBored(Long sidx){
        salesBoardRepository.deleteById(sidx);
    }

    // 게시글 수정하기
    public void updateSalesBored(Long sidx, SalesBoardDTO salesBoardDTO){
        SalesBoard salesBoard = salesBoardRepository.findById(sidx).get();
        SalesBoardDTO originalDTO = salesBoardMapper.toDto(salesBoard);

        originalDTO.setSidx(sidx);
        originalDTO.setNick(salesBoardDTO.getNick());
        originalDTO.setClassification(salesBoardDTO.getClassification());
        originalDTO.setRegion(salesBoardDTO.getRegion());
        originalDTO.setTitle(salesBoardDTO.getTitle());
        originalDTO.setBooktitle(salesBoardDTO.getBooktitle());
        originalDTO.setAuthor(salesBoardDTO.getAuthor());
        originalDTO.setPublisher(salesBoardDTO.getPublisher());
        originalDTO.setPrice(salesBoardDTO.getPrice());
        originalDTO.setContent(salesBoardDTO.getContent());
        originalDTO.setUpdateDate(salesBoardDTO.getCreateDate());

        
        //  이미지 파일 변경되었을때만 업데이트 되도록
        if (salesBoardDTO.getOimage() != null && !salesBoardDTO.getOimage().isEmpty()) {
            originalDTO.setOimage(salesBoardDTO.getOimage());
        }

        if (salesBoardDTO.getSimage() != null && !salesBoardDTO.getSimage().isEmpty()) {
            originalDTO.setSimage(salesBoardDTO.getSimage());
        }

        salesBoardRepository.save(salesBoardMapper.toEntity(originalDTO));
    }

    // 거래게시판 조회수
    public void updateViewCount(Long sidx, HttpServletRequest req)
    {
        String  userId = req.getRemoteAddr(); // IP 주소 가져오기

        String key = "viewCount::" + sidx + "::" + userId;

        if(redisUtil.getData(key) == null){
            salesBoardRepository.viewCount(sidx);

            redisUtil.setDataExpire(key, "viewed", 1200); // 유효시간 20분
        }
    }

    // 거래게시판 좋아요
    public void updateLikeCount(Long sidx)
    {
        salesBoardRepository.likeCount(sidx);
    }

    public void updateDownCount(Long sidx)
    {
        salesBoardRepository.downCount(sidx);
    }

}
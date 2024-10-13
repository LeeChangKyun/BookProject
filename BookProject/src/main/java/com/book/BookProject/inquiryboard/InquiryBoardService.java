package com.book.BookProject.inquiryboard;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InquiryBoardService {

    @Autowired
    private InquiryBoardRepository inquiryBoardRepository;
    @Autowired
    private InquiryBoardMapper inquiryBoardMapper;

    // 문의게시판 리스트
    public Page<InquiryBoardDTO> inquiryBoardList(int page, String searchField, String searchWord)
    {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "qidx"));
        Page<InquiryBoard> inquiryBoardPage;

        if(searchField != null && searchWord != null && !searchWord.isEmpty())
        {
            switch (searchField)
            {
                case "title":
                    inquiryBoardPage = inquiryBoardRepository.findByTitleContaining(searchWord, pageable);
                    break;
                case "content":
                    inquiryBoardPage = inquiryBoardRepository.findByContentContaining(searchWord, pageable);
                    break;
                case "nick":
                    inquiryBoardPage = inquiryBoardRepository.findByNickContaining(searchWord, pageable);
                    break;
                default:
                    inquiryBoardPage = inquiryBoardRepository.findAll(pageable);
            }
        }else{
            inquiryBoardPage = inquiryBoardRepository.findAll(pageable);
        }

        // 엔티티를 dto로 변환
        List<InquiryBoardDTO> dtoList = inquiryBoardMapper.toDtoList(inquiryBoardPage.getContent());

        return new PageImpl<>(dtoList, inquiryBoardPage.getPageable(), inquiryBoardPage.getTotalElements());
    }
//    public Page<InquiryBoardDTO> inquiryBoardList(int page)
//    {
//        Page<InquiryBoard> inquiryBoardPage = inquiryBoardRepository.findAll(PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "qidx")));
//        // 엔티티를 dto로 변환
//        List<InquiryBoardDTO> dtoList = inquiryBoardMapper.toDtoList(inquiryBoardPage.getContent());
//
//        return new PageImpl<>(dtoList, inquiryBoardPage.getPageable(), inquiryBoardPage.getTotalElements());
//    }

    // 문의게시판 상세보기
    public InquiryBoardDTO inquiryBoardView(Long qidx)
    {
        InquiryBoard inquiryBoard = inquiryBoardRepository.findById(qidx).get();

        return inquiryBoardMapper.toDto(inquiryBoard);
    }

    //문의게시판 글 작성
    public void inquiryBoardWrite(InquiryBoardDTO inquiryBoardDTO)
    {
        InquiryBoard inquiryBoard = inquiryBoardMapper.toEntity(inquiryBoardDTO);
        inquiryBoardRepository.save(inquiryBoard);
    }

    //문의게시판 글 수정
    public void inquiryBoardEditor(Long qidx, InquiryBoardDTO inquiryBoardDTO)
    {
        InquiryBoard inquiryBoard = inquiryBoardRepository.findById(qidx).get();
        InquiryBoardDTO originalDTO = inquiryBoardMapper.toDto(inquiryBoard);

        originalDTO.setQidx(qidx);
        originalDTO.setNick(inquiryBoardDTO.getNick());
        originalDTO.setPass(inquiryBoardDTO.getPass());
        originalDTO.setTitle(inquiryBoardDTO.getTitle());
        originalDTO.setContent(inquiryBoardDTO.getContent());

        // 이미지 파일 변경 시에만 업데이트
        if(inquiryBoardDTO.getOfile() != null && !inquiryBoardDTO.getOfile().isEmpty())
        {
            originalDTO.setOfile(inquiryBoardDTO.getOfile());
        }

        if(inquiryBoardDTO.getSfile() != null && !inquiryBoardDTO.getSfile().isEmpty())
        {
            originalDTO.setSfile(inquiryBoardDTO.getSfile());
        }

        inquiryBoardRepository.save(inquiryBoardMapper.toEntity(originalDTO));
    }

    // 문의게시판 글 삭제
    public void inquiryBoardDelete(Long qidx)
    {
        inquiryBoardRepository.deleteById(qidx);
    }

    // 문의게시판 조회수
    @Transactional
    public void inquiryBoardViewCount(Long qidx)
    {
        inquiryBoardRepository.viewCount(qidx);
    }
}

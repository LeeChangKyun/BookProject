package com.book.BookProject.inquiryboard;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InquiryBoardMapper {

    InquiryBoardDTO toDto(InquiryBoard inquiryBoard);

    InquiryBoard toEntity(InquiryBoardDTO dto);

    List<InquiryBoardDTO> toDtoList(List<InquiryBoard> inquiryBoards);

    List<InquiryBoard> toEntityList(List<InquiryBoardDTO> inquiryBoardDTOS);
}

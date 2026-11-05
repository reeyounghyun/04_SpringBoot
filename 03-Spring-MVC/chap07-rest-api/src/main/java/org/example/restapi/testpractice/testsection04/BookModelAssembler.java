package org.example.restapi.testpractice.testsection04;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class BookModelAssembler implements RepresentationModelAssembler<BookDTO, EntityModel<BookDTO>> {

    @Override
    public EntityModel<BookDTO> toModel(BookDTO dto) {

        /*
         * [문제 1] 링크 2개를 붙여서 반환하세요.
         *  - self  → BookHateoasController.findBookByNo(dto.getNo())
         *  - books → BookHateoasController.findAllBooks()
         *
         * [보너스 1] 다음 번호의 도서가 있을 때만 next 링크 추가
         * */

        return EntityModel.of(dto);   // 지금은 링크 없이 감싸기만 해요
    }
}

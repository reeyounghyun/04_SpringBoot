package section01.common;

import java.util.List;

public interface BookDAO {

    /* 설명. 전체 도서 목록 조회 */
    List<BookDTO> selectBookList();

    /* 설명. 도서 번호로 도서 한 권 조회 */
    BookDTO selectOneBook(int sequence);
}

package section01.subsection02.setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import section01.common.BookDAO;
import section01.common.BookDTO;

import java.util.List;

@Service("bookServiceSetter")
public class BookService {

    // final 사용 못함 = setter를 이용해 의존 객체를 교체 가능하다.
    private BookDAO bookDAO;

    @Autowired
    public void setBookDAO(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    // 도서목록 조회
    public List<BookDTO> selectAllBooks() {

        return bookDAO.selectBookList();
    }

    // sequence로 도서 조회
    public BookDTO searchBookBySequence(int sequence) {

        return bookDAO.selectOneBook(sequence);
    }

}

package section01.subsection03.constructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import section01.common.BookDAO;
import section01.common.BookDTO;

import java.util.List;

/*
* 생성자 주입을 권장하는 이유
* 1. 필수 의존성을 빠뜨린채 객체를 생성할 수 없다.
* 2. final 필드를 사용해 의존관계를 불변으로 유지 할 수 있다.
* */
@Service("bookServiceConstructor")
public class BookService {

    // final 필드에는 생성자에서 딱 한 번만 값을 넣을 수 있어요.
    // BookDao 객체가 다른개체 또는 새로만들어진 BookDao 타입의 객체로 변환되지않음.
    private final BookDAO bookDAO;

    // 생성자 주입
    // BookService 빈을 생성할때 BookDAO 타입의 빈을 생성자 인자로 전달한다.
    // @Autowired // Spring Framework 4.3 부터 생성자가 한개면 @Autowired를 생략해도 자동으로 의존성을 주입한다
    public BookService(BookDAO bookDAO) {
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

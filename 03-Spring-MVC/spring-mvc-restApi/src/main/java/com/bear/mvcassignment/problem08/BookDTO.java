package com.bear.mvcassignment.problem08;

/**
 * 제공 코드: 도서 정보를 담는 완성된 DTO. 수정하지 않는다.
 * 문제 8, 문제 9에서 함께 사용한다.
 *
 * price가 int가 아니라 Integer인 이유: 요청 본문에 price가 없으면 null로 들어와서
 * "값이 안 들어왔다"를 구분할 수 있다. (문제 9-5 PATCH, 9-3 유효성 검사에서 사용)
 */
public class BookDTO {

    private int no;
    private String title;
    private String author;
    private Integer price;

    public BookDTO() {
    }

    public BookDTO(int no, String title, String author, Integer price) {
        this.no = no;
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "BookDTO{no=" + no + ", title='" + title + "', author='" + author + "', price=" + price + "}";
    }
}

package section04;


import common.MemberDTO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

    public static void main(String[] args) {

        /*
        일반적인 객체 만든는 방법
        * */
        MemberDTO javaMember1 = new MemberDTO(1, "user01", "pass01", "홍길동");
        MemberDTO javaMember2 = new MemberDTO(1, "user01", "pass01", "홍길동");

        System.out.println("javaMember1 = javaMember2 : " + (javaMember1 == javaMember2));


        ApplicationContext context =
                new AnnotationConfigApplicationContext(ContextConfiguration.class);

        // SpringContext Bean으로 등록되는 객체는 기본적으로 Singleton이다.
        // 객체를 직접 만들지 않아도 ApplicationContext가 객체를 만들어서 가져와줌
        MemberDTO member1 = context.getBean(MemberDTO.class);
        MemberDTO member2 = context.getBean(MemberDTO.class);

        System.out.println("member1 = member2 : " + (member1 == member2));

        /*출력 결과
        * javaMember1 = javaMember2 : false
        member1 = member2 : true

        - ==는 값이 아니라 같은 객체(같은 메모리 주소)인지를 비교합니다.
        - ①은 필드 값이 똑같아도 new를 두 번 했으니 객체가 2개라서 false입니다.
        - ②는 컨테이너가 시작할 때 MemberDTO를 한 번만 만들어 두고, getBean을 할 때마다 그 하나를 계속 돌려줍니다. 그래서 true입니다. 이 동작이 ContextConfiguration 주석에 적으신 **singleton scope(기본값)**입니다.
        *
        *
        * */

    }
}

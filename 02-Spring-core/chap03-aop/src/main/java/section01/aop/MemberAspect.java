package section01.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
// @Aspect: 로깅, 성능측정, 예외 처리같은 공통기능을 모아둔 클래스
// Advice: 실제 공통기능이 작성된 메서드
public class MemberAspect {

    // 경로찾아주자
    // execution() : 기본형태 execution(반환형 패키지.클래스.메서드명(매개변수))
    // *: 무엇이든 가능
    // (..): 매개변수의 갯수와 타입은 무엇이든 가능

    // 타겟 지정 -> "execution( * section01.aop.MemberServiceImpl.processMember(..))"
    @Pointcut("execution( * section01.aop.MemberServiceImpl.processMember(..))")
    public void advicePointcut() {
    }

    /**
     * Before
     **/
    @Before("advicePointcut()")
    public void beforeAdvice() {
        System.out.println("beforeAdvice");
    }

    /**
     * AfterReturning
     **/
    @AfterReturning(value = "advicePointcut()", returning = "result")
    public void AfterReturningAdvice(Object result) {
        System.out.println("[AfterReturning 반환값:]" + result);
    }

    /**
     * After
     **/
    @After("advicePointcut()")
    public void AfterAdvice() {
        System.out.println("After 매서드 실행 후");
    }

    /**
     * AfterThrowing
     **/
    @AfterThrowing(pointcut = "advicePointcut()", throwing = "exception")
    public void afterThowingAdvice(Exception exception) {
        System.out.println("After 매서드 실행 중 예외 발생" + exception.getMessage());
    }

    /**
     * Around
     **/
    @Around("advicePointcut()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.nanoTime();
        System.out.println("Around Advice 실행 전");


        try {
            // proceed() 가 Taget 매서드가 실행된다.
            return joinPoint.proceed();
        } finally {
            long executionTime = System.nanoTime() - startTime;
            System.out.println("Around Advice 실행 후, 실행 시간: " + executionTime + " ns");
        }
    }

    /*within: 해당 클래스 내부를 선택*/
    @Before("within(section01.aop.MemberServiceImpl) && execution(* selectMember(..))")
    public void withinPointCut() {
        System.out.println("[within] MemberServiceImpl 적용");
    }

    /*this: 해당 타입을 선택 함*/
    @Before("this(section01.aop.MemberService) && execution(* checkMember(..))")
    public void thisPointCut() {
        System.out.println("[this] MemberService 적용");
    }

    /*args: 해당 매개변수를 가진 메서드에 적용*/
    @Before(value = "args(name, age) && execution(* registerMember(..))", argNames = "name,age")
    public void argsPointCut(String name, int age) {
        System.out.println("[args] 이름: " + name + ", 나이: " + age);
    }

    // 어노테이션 만들기
    @Before("@annotation(section01.aop.AopLog)")
    public void annotationPointCut() {
        System.out.println("annotationPointCut 실행");
    }

}


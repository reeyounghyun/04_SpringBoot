package com.bear.studyboard.monitoring.section02.counter;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
// 게시글이 올라가는 형태
public class PostCreatedCounter {

    private final Counter counter;

    // MeterRegistry : 메모리 상에 메트릭을 저장해서 보관하는 공간
    public PostCreatedCounter(MeterRegistry meterRegistry) {
        // 카운트 이름을 지을 때 주의사항
        // 프로메테우스 형태로 이름이 바뀔건데,
        // 프로메테우스에서 쓰는 말(예약어)은 피해서 이름을 짓는게 좋다
        this.counter = Counter.builder("study.post.creation")
                .description("등록에 성공한 게시글 수")
                .register(meterRegistry);
    }

    // 게시글 등록됐을 때 카운팅을 할 메서드 (1씩 증가)
    // 카운팅하고싶은 코드 위치에서 메서드 호출
    public void increase() {
        counter.increment();
    }
}

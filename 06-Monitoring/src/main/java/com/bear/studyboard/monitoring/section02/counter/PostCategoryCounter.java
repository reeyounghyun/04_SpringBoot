package com.bear.studyboard.monitoring.section02.counter;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostCategoryCounter {
    private final MeterRegistry meterRegistry;

    // 매개변수 위치에 구분하고싶은 태그의 값을 받아 올 수 있다.
    // 태그 값은 종류가 정해져 있는 것만 사용하는것이 좋다 (해당 개수 만큼 카운터가 만들어진다.)
    // PostCreatedCounter와 달리 미리 등록되지 않고,
    // 어떤 분류가 있을지 서버가 시작될 때는 알 수가 없기 때문이며, 게시글이 처음 등록될때 만들어진다.
    public void increase(String categoryName) {
        Counter counter = Counter.builder("study.post.creation.by.category")
                .description("분류별 등록에 성공한 게시글 수")
                .tag("category", categoryName)
                // 개수가 정해진것으로만 만들어야 한다.
                // categoryName => 게시글ID 태그에 해당하는 만큼 카운터가 만들어진다.
                .register(meterRegistry);

        counter.increment();
    }
}

package spring.advanced.trace.logTrace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import spring.advanced.trace.TraceId;
import spring.advanced.trace.TraceStatus;

/**
 * 기존 Parameter 기반의 Sync -> 필드(인스턴스 변수)로 직접 관리하며 Sync
 * 1. begin() 을 위한 기존의 context 가져오기 by 필드
 * 2. complete() 후 벗어나게 되는 계층의 level 표현을 위한 context 가져오기 by 필드
 */
@Slf4j
//@Component //이제부터는 직접 Spring Bean으로 등록할 예정
public class FieldLogTrace implements LogTrace{

    // *** 동기화를 위해 parameter로 넘기던 TraceId를 어딘가에서는 보관하고 있어야 함
    //     -> 필드에서 가지고 있도록 holder를 만들어줌
    // *** but, 이렇게 사용하면 '동시성 이슈' 발생
    private TraceId traceIdHolder;

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    @Override
    public TraceStatus begin(String message) {
        // * begin()에서 새롭게 생성하던 TraceId를 필드인 holder를 통해 context를 포함한 TraceId로 관리
        syncTraceId();
        //TraceId traceId = new TraceId();

        // * 메서드에서 사용할 traceId는 holder에 저장된 값을 사용하도록 구성
        TraceId traceId = traceIdHolder;

        Long startTimeMs = System.currentTimeMillis();

        //로그 출력
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    private void syncTraceId() {
        if (traceIdHolder == null) {
            traceIdHolder = new TraceId();
        } else {
            traceIdHolder = traceIdHolder.createNextId();
        }
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e) {
        Long stopTimeMs = System.currentTimeMillis();
        long resultMs = stopTimeMs-status.getStatTImeMs();
        TraceId traceId = status.getTraceId();
        if (e == null) {
            log.info("[{}] {}{} times={}ms",
                    traceId.getId(), addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(), resultMs);
        } else {
            log.info("[{}] {}{} times={}ms ex={}",
                    traceId.getId(), addSpace(EX_PREFIX, traceId.getLevel()), status.getMessage(), resultMs, e.toString());
        }
        // 로그 출력 후 TraceId 동기화
        releaseTraceId();
    }

    // *** complete()를 통해 tracer를 호출하는 메서드 내에서 계층을 벗어나갈 때, TraceId를 동기화하기 위한 로직의 메서드 추가
    private void releaseTraceId() {
        if (traceIdHolder.isFirstLevel()) {
            traceIdHolder = null; //destroy
        } else {
            traceIdHolder = traceIdHolder.createPreviousId();
        }
    }

    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append( (i == level-1) ? "|" + prefix : "|   ");
        }
        return sb.toString();
    }

}

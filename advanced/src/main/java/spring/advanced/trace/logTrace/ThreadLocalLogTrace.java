package spring.advanced.trace.logTrace;

import lombok.extern.slf4j.Slf4j;
import spring.advanced.trace.TraceId;
import spring.advanced.trace.TraceStatus;

/**
 * 기존 Parameter 기반의 Sync -> 필드(인스턴스 변수)로 직접 관리하며 Sync
 * 1. begin() 을 위한 기존의 context 가져오기 by 필드
 * 2. complete() 후 벗어나게 되는 계층의 level 표현을 위한 context 가져오기 by 필드
 */
@Slf4j
public class ThreadLocalLogTrace implements LogTrace{

    //ThreadLocal을 사용하도록 필드 변경
    //private TraceId traceIdHolder;
    private ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>();

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    @Override
    public TraceStatus begin(String message) {
        // * begin()에서 새롭게 생성하던 TraceId를 필드인 holder를 통해 context를 포함한 TraceId로 관리
        syncTraceId();
        //TraceId traceId = new TraceId();

        // * 메서드에서 사용할 traceId는 holder에 저장된 값을 사용하도록 구성
        TraceId traceId = traceIdHolder.get();

        Long startTimeMs = System.currentTimeMillis();

        //로그 출력
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    // * 이제 TraceId 자료형인 traceHolder를 직접 사용하는 것이 아닌, ThreadLocal에서 꺼내서 사용
    private void syncTraceId() {
        // ThreadLocal인 tracedIdHolder에 저장된 TraceId를 꺼내서 확인
        // * ThreadLocal은 필드 선언 시 생성까지 완료됐으므로 그 자체의 유무를 파악하는 것이 아닌, 그에 저장된 TraceId의 유무를 확인
        TraceId traceId = traceIdHolder.get();
        //log.info("traceIdHolder = {}", traceIdHolder.equals(null)); //현재 traceIdHolder 인스턴스가 null인지 확인 -> false
        if (traceId == null) {
            traceIdHolder.set(new TraceId());
        } else {
            traceIdHolder.set(traceId.createNextId());
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

    //LocalThread에 저장된 TraceId를 지역변수로 꺼내서 활용
    private void releaseTraceId() {
        TraceId traceId = traceIdHolder.get();
        if (traceId.isFirstLevel()) {
            // * 이제 기존 traceIdHolder가 담고 있는 TraceId를 null로 변경하는 것이 아닌, ThreadLocal 내부 데이터를 삭제
            traceIdHolder.remove();
            //traceIdHolder = null; //destroy
        } else {
            traceIdHolder.set(traceId.createPreviousId());
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

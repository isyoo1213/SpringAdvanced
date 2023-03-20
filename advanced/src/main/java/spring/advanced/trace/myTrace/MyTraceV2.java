package spring.advanced.trace.myTrace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import spring.advanced.trace.TraceId;
import spring.advanced.trace.TraceStatus;

/**
 * V1에서 trace의 id와 level이 분리된 것을 Parameter로 해결하는 버전
 */
@Slf4j
@Component //SpringBean 등록을 통해 Singleton으로 사용하기 위해
public class MyTraceV2 {

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    public TraceStatus begin(String message) {
        TraceId traceId = new TraceId();
        Long startTimeMs = System.currentTimeMillis();

        //로그 출력
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    //V2에서 추가
    public TraceStatus beginSync(TraceId beforeTraceId, String message) {

        // 이제 기존의 traceId 인스턴스에서 id/level 정보를 연계하며 생성하는 TraceId 객체를 생성
        TraceId NextTraceId = beforeTraceId.createNextId();
        //TraceId traceId = new TraceId();

        Long startTimeMs = System.currentTimeMillis();

        //로그 출력
        log.info("[{}] {}{}", NextTraceId.getId(), addSpace(START_PREFIX, NextTraceId.getLevel()), message);
        return new TraceStatus(NextTraceId, startTimeMs, message);
    }

    public void end(TraceStatus status) {
        complete(status, null);
    }

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
    }

    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append( (i == level-1) ? "|" + prefix : "|  ");
        }
        return sb.toString();
    }

}

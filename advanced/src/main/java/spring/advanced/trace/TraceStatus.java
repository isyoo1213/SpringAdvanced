package spring.advanced.trace;

/**
 * 로그 시작/종료시 필요한 요소들 + 작업 시간 계산에 필요한 정보들을 담은 클래스
 */
public class TraceStatus {

    private TraceId traceId;
    private Long statTImeMs;
    private String message;

    public TraceStatus(TraceId traceId, Long statTImeMs, String message) {
        this.traceId = traceId;
        this.statTImeMs = statTImeMs;
        this.message = message;
    }

    public TraceId getTraceId() {
        return traceId;
    }

    public Long getStatTImeMs() {
        return statTImeMs;
    }

    public String getMessage() {
        return message;
    }
}

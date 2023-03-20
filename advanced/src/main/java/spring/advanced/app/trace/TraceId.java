package spring.advanced.app.trace;

import java.util.UUID;

public class TraceId {

    private String id;
    private int level;

    public TraceId() {
        this.id = createId();
        this.level = 0;
    }

    // * 내부적으로만 사용 + id는 동일하고, level 변화를 가지는 TraceId 객체 생성을 위한 생성자
    private TraceId(String id, int level) {
        this.id = id;
        this.level = level;
    }

    //id 생성
    private String createId() {
        //생성된 uuid의 앞 8자리만 잘라서 사용
        return UUID.randomUUID().toString().substring(0, 8);
    }

    //create prev/next TraceId
    public TraceId createNextId() {
        return new TraceId(id, level + 1);
    }

    public TraceId createPreviousId() {
        return new TraceId(id, level - 1);
    }

    //level check
    public boolean isFirstLevel() {
        return level == 0;
    }

    // getter
    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }
}

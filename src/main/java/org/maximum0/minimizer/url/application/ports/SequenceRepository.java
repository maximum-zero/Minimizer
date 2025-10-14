package org.maximum0.minimizer.url.application.ports;

public interface SequenceRepository {

    /**
     * 다음으로 사용할 ID를 가져옵니다.
     * @return DB 시퀀스가 제공하는 다음 ID 값
     */
    long getNextId();

}

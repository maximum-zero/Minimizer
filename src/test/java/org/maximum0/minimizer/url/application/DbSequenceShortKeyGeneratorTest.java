package org.maximum0.minimizer.url.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.maximum0.minimizer.url.application.fake.FakeSequenceRepository;
import org.maximum0.minimizer.url.application.ports.SequenceRepository;
import org.maximum0.minimizer.url.domain.ShortKey;

@DisplayName("DB Sequence 기반 ShortKeyGenerator")
class DbSequenceShortKeyGeneratorTest {
    @DisplayName("초기화된 ID를 기반으로 연속 생성 시, ShortKey를 정확히 생성 및 증가하는지 검증합니다.")
    @Test
    void givenInitialId_whenGenerateMultipleTimes_thenReturnIncrementedShortKey() {
        // given
        final long CURRENT_ID = 1000L;
        SequenceRepository sequenceRepository = new FakeSequenceRepository(CURRENT_ID);
        DbSequenceShortKeyGenerator generator = new DbSequenceShortKeyGenerator(sequenceRepository);

        // when & then
        // ID 1001L - 0000G9
        ShortKey result1 = generator.generate();
        assertNotNull(result1);
        assertEquals(6, result1.getValue().length());
        assertEquals("0000G9", result1.getValue());

        // ID 1002L - 0000GA
        ShortKey result2 = generator.generate();
        assertEquals("0000GA", result2.getValue());

        // ID 1003L - 0000GB
        ShortKey result3 = generator.generate();
        assertEquals("0000GB", result3.getValue());
    }

    @DisplayName("최대 6자리 Base62 범위를 초과하는 큰 ID를 초기화하고 생성 시, IllegalArgumentException 예외가 발생합니다.")
    @Test
    void givenIdExceedingShortKeyLength_whenGenerate_thenThrowIllegalArgumentException() {
        // given
        long valueExceedingSixDigits = 56800235584L;
        SequenceRepository sequenceRepository = new FakeSequenceRepository(valueExceedingSixDigits);
        DbSequenceShortKeyGenerator generator = new DbSequenceShortKeyGenerator(sequenceRepository);

        // when & then
        assertThrows(IllegalArgumentException.class, generator::generate);
    }

}

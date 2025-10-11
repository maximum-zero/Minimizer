package org.maximum0.minimizer.url.application;

import lombok.RequiredArgsConstructor;
import org.maximum0.minimizer.common.common.Base62Encoder;
import org.maximum0.minimizer.url.application.ports.SequenceRepository;
import org.maximum0.minimizer.url.application.interfaces.ShortKeyGenerator;
import org.maximum0.minimizer.url.domain.ShortKey;

@RequiredArgsConstructor
public class DbSequenceShortKeyGenerator implements ShortKeyGenerator {
    private final SequenceRepository sequenceRepository;

    @Override
    public ShortKey generate() {
        long uniqueId = sequenceRepository.getNextId();
        String encode = Base62Encoder.encode(uniqueId);
        return ShortKey.createShortKey(encode);
    }

}

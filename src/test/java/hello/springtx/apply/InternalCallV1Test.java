package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class InternalCallV1Test {

    @Autowired
    private CallService callService;

    @Test
    void t1() {
        callService.external();
    }

    @Test
    void t3() {
        callService.internal();
    }

    @Test
    void t2() {
        log.info("callService ={}", callService.getClass());

    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        CallService callService() {
            return new CallService();
        }

    }

    @Slf4j
    static class CallService {
        public void external() {
            log.info("call external");
            printTx();
            internal();
        }

        @Transactional
        public void internal() {
            log.info("call internal");
            printTx();
        }

        public void printTx() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active ={}", txActive);
        }

    }
}

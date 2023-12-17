package hello.springtx.exception;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class RollbackTest {

    @Autowired
    private RollbackService rollbackService;


    @Test
    void t1() {
        Assertions.assertThatThrownBy(() -> {
                    rollbackService.runtimeException();
                })
                .isInstanceOf(RuntimeException.class);

    }

    @Test
    void t2() {
        Assertions.assertThatThrownBy(() -> {
                    rollbackService.checkedException();
                })
                .isInstanceOf(MyException.class);

    }

    @Test
    void t3() {
        Assertions.assertThatThrownBy(() -> {
                    rollbackService.rollbackFor();
                })
                .isInstanceOf(MyException.class);

    }

    @TestConfiguration
    static class rollbackTestConfig {

        @Bean
        RollbackService rollbackService() {
            return new RollbackService();
        }
    }


    @Slf4j
    static class RollbackService {
        //런타임 :롤백

        @Transactional
        public void runtimeException() {
            log.info("call runtime");
            throw new RuntimeException();
        }


        //체크예외 :커밋
        @Transactional
        public void checkedException() throws MyException {
            log.info("call checkedException");
            throw new MyException();
        }

        @Transactional(rollbackFor = MyException.class)
        public void rollbackFor() throws MyException {
        log.info("call rollback");
            throw new MyException();
        }

    }

    static class MyException extends Exception {
    }

}


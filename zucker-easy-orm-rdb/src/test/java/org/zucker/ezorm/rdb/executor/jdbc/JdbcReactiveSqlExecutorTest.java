package org.zucker.ezorm.rdb.executor.jdbc;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.zucker.ezorm.rdb.TestJdbcReactiveSqlExecutor;
import org.zucker.ezorm.rdb.executor.SqlRequests;
import org.zucker.ezorm.rdb.executor.wrapper.ResultWrappers;
import org.zucker.ezorm.rdb.supports.h2.H2ConnectionProvider;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * @author lind
 * @since 1.0
 */
public class JdbcReactiveSqlExecutorTest {

    JdbcReactiveSqlExecutor sqlExecutor;

    @Before
    @SneakyThrows
    public void init() {
        sqlExecutor = new TestJdbcReactiveSqlExecutor(new H2ConnectionProvider());
    }

    @Test
    public void testExecute() {
        Mono<Void> ddl = sqlExecutor.execute(Mono.just(SqlRequests.of("create table test( id varchar(32) primary key )")))
                .doOnError(Throwable::printStackTrace);

        StepVerifier.create(ddl)
                .verifyComplete();

        Mono<Integer> counter = sqlExecutor
                .update(Flux.range(0, 10)
                        .doOnNext(i -> System.out.println())
                        .map(num -> SqlRequests.prepare("insert into test (id) values (?) ", num)))
                .doOnError(Throwable::printStackTrace);

        StepVerifier.create(counter)
                .expectNext(10)
                .verifyComplete();

        Mono<Long> data = sqlExecutor.select(Mono.just(SqlRequests.of("select * from test")), ResultWrappers.map())
                .doOnError(Throwable::printStackTrace)
                .map(map -> map.get("ID"))
                .count();

        StepVerifier.create(data)
                .expectNext(10L)
                .verifyComplete();

        Mono<Long> count = sqlExecutor.select(Flux.range(0, 10)
                        .map(String::valueOf)
                        .map(num -> SqlRequests.of("select * from test where id = ?", num)), ResultWrappers.map())
                .doOnError(Throwable::printStackTrace)
                .map(map -> map.get("ID"))
                .count();

        StepVerifier.create(count)
                .expectNext(10L)
                .verifyComplete();
    }
}

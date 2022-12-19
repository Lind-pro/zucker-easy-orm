package org.zucker.ezorm.rdb.executor.reactive.r2dbc;

import org.zucker.ezorm.rdb.TestReactiveSqlExecutor;
import org.zucker.ezorm.rdb.executor.SqlRequest;
import org.zucker.ezorm.rdb.executor.SqlRequests;
import org.zucker.ezorm.rdb.executor.wrapper.ResultWrappers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.stream.Collectors;

/**
 * @author lind
 * @since 1.0
 */
public class R2dbcReactiveSqlExecutorTest {

    //TODO

    public void executeTest(R2dbcReactiveSqlExecutor sqlExecutor) {
        try {
            Mono<Void> mono = sqlExecutor.execute(Mono.just(SqlRequests.of("create table test_r2dbc(id varchar(32) primary key")));
            StepVerifier.create(mono).verifyComplete();

            //插入10条数据
            Flux.range(1, 10)
                    .map(i -> SqlRequests.of("insert into test_r2dbc(id)values(?)", "" + i))
                    .as(sqlExecutor::update)
                    .as(StepVerifier::create)
                    .expectNext(10)
                    .verifyComplete();

            //查询ID并合并
            sqlExecutor.select(Mono.just(SqlRequests.of("select id from test_r2dbc")), ResultWrappers.lowerCase(ResultWrappers.map()))
                    .map(map -> map.get("id"))
                    .map(String::valueOf)
                    .map(Integer::valueOf)
                    .collect(Collectors.summingInt(Integer::intValue))
                    .as(StepVerifier::create)
                    .expectNext(55)
                    .verifyComplete();
        } finally {
            sqlExecutor.execute(Mono.just(SqlRequests.of("drop table test_r2dbc"))).block();
        }
    }
}

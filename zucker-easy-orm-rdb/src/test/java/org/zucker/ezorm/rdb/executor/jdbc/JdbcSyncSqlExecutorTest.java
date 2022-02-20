package org.zucker.ezorm.rdb.executor.jdbc;

import lombok.Data;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.zucker.ezorm.rdb.TestSyncSqlExecutor;
import org.zucker.ezorm.rdb.executor.DefaultBatchSqlRequest;
import org.zucker.ezorm.rdb.executor.SqlRequests;
import org.zucker.ezorm.rdb.executor.SyncSqlExecutor;
import org.zucker.ezorm.rdb.executor.wrapper.ResultWrappers;
import org.zucker.ezorm.rdb.supports.h2.H2ConnectionProvider;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @auther: lind
 * @since: 1.0
 */
public class JdbcSyncSqlExecutorTest {

    private SyncSqlExecutor executor;

    @SneakyThrows
    @Before
    public void init() {
        executor = new TestSyncSqlExecutor(new H2ConnectionProvider());
    }

    @Test
    public void testBatch() {
        {
            DefaultBatchSqlRequest batch = new DefaultBatchSqlRequest();

            batch.addBatch(SqlRequests.of("create table test_batch( id varchar(32) )"));
            batch.addBatch(SqlRequests.of("COMMENT on table test_batch is 'test'"));
            executor.execute(batch);
        }
        {
            DefaultBatchSqlRequest batch = new DefaultBatchSqlRequest();

            batch.addBatch(SqlRequests.of("insert into test_batch (id) values(?)", "1"));
            batch.addBatch(SqlRequests.of("insert into test_batch (id) values(?)", "2"));
            batch.addBatch(SqlRequests.of("update test_batch set id = ? where id = ?", "3", "2"));

            Assert.assertEquals(executor.update(batch), 3);

            int sum = executor.select(SqlRequests.of("select id from test_batch"), ResultWrappers.mapStream())
                    .map(map -> map.get("ID"))
                    .map(String::valueOf)
                    .mapToInt(Integer::valueOf)
                    .sum();

            Assert.assertEquals(sum, 4);
        }

    }

    @Test
    @SneakyThrows
    public void testSelectSingle() {
        Map<String, Object> data = executor.select(SqlRequests.prepare("select 1 as data"), ResultWrappers.singleMap());
        Assert.assertNotNull(data);
        Assert.assertEquals(data.get("DATA"), 1);
    }

    @Test
    @SneakyThrows
    public void testSelectPrepare() {
        Map<String, Object> data = executor.select(SqlRequests.prepare("select 1 as data where 1 = ?", 1), ResultWrappers.singleMap());
        Assert.assertNotNull(data);
        Assert.assertEquals(data.get("DATA"), 1);
    }

    @SneakyThrows
    @Test
    public void testSelectTemplate() {
        Map<String, Object> data = executor.select(SqlRequests.template("select 1 as data where 1 = #{id}", Collections.singletonMap("id", 1)), ResultWrappers.singleMap());
        Assert.assertNotNull(data);
        Assert.assertEquals(data.get("DATA"), 1);
    }
}

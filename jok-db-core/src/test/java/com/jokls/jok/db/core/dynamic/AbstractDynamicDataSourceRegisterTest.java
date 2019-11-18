package com.jokls.jok.db.core.dynamic;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.mock.env.MockEnvironment;

import javax.sql.DataSource;
import java.util.Map;
 /* @author: marik.wei
    * @mail: marks@126.com
    * Date: 2019/6/25 13:15
*/
public class AbstractDynamicDataSourceRegisterTest {


    @Test
    public void setEnvironment() {
        AbstractDynamicDataSourceRegister register = new TestDynamicDataSourceRegister();
        MockEnvironment mockEnvironment = new MockEnvironment();
        mockEnvironment.setProperty("jok.datasource.default.driver-class-name","oracle.jdbc.driver.OracleDriver");
        mockEnvironment.setProperty("jok.datasource.account.driver-class-name","oracle.jdbc.driver.OracleDriver");


        mockEnvironment.setProperty("jok.datasource.default.type","com.alibaba.druid.pool.DruidDataSource");
        mockEnvironment.setProperty("jok.datasource.default.loading","false");
        mockEnvironment.setProperty("jok.datasource.default.loading","true");
        mockEnvironment.setProperty("jok.datasource.default.url","jdbc:oracle:thin:@10.14.64.1:1521:test");
        mockEnvironment.setProperty("jok.datasource.default.username","plsale");
        mockEnvironment.setProperty("jok.datasource.default.password","E(4F078A1259745D51)");

        mockEnvironment.setProperty("jok.datasource.account.type","com.alibaba.druid.pool.DruidDataSource");
        mockEnvironment.setProperty("jok.datasource.account.loading","true");
        mockEnvironment.setProperty("jok.datasource.account.url","jdbc:oracle:thin:@192.168.10.111:1521:dsdb");
        mockEnvironment.setProperty("jok.datasource.account.username","root");
        mockEnvironment.setProperty("jok.datasource.account.password","123");


        mockEnvironment.setProperty("jok.datasource.account.bizkeys","1111111");


        register.setEnvironment(mockEnvironment);
    }

    class TestDynamicDataSourceRegister extends AbstractDynamicDataSourceRegister{

        @Override
        public DataSource buildDataSource(String prefix, Map<String, Object> dsMap, Environment env) {

//               need connect the real database
//                return this.getDruidDataSource(prefix, dsMap, env, new DruidDataSource(), type);

//             create default data source for test
                return new DruidDataSource();

        }

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {

        }
    }
}

package org.apache.dubbo.config.spring;

import com.jokls.jok.rpc.annotation.CloudService;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.mock;

public class ServiceBeanTest {

    @Test
    public void testGetService() {
        TestService service = mock(TestService.class);
        ServiceBean serviceBean = new ServiceBean(service);

        String beanName = serviceBean.getBeanName();
        MatcherAssert.assertThat(beanName, not(nullValue()));
    }

    abstract class TestService implements CloudService {

    }
}
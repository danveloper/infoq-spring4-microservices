package infoq.spring4;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import infoq.spring4.data.Phone;
import infoq.spring4.data.User;
import infoq.spring4.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.sql.DataSource;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class MicroserviceWebApplication implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(MicroserviceSpringConfig.class);
        ServletRegistration.Dynamic registration = servletContext.addServlet("dispatcher", new DispatcherServlet(context));
        registration.setLoadOnStartup(1);
        registration.addMapping("/");
    }

    @Configuration
    @ComponentScan
    @EnableWebMvc
    @EnableTransactionManagement
    @EnableJpaRepositories("infoq.spring4.data")
    public static class MicroserviceSpringConfig extends WebMvcConfigurerAdapter {
        @Bean
        public DataSource dataSource() {
            EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
            return builder.setType(EmbeddedDatabaseType.H2).build();
        }

        @Bean
        public EntityManagerFactory entityManagerFactory() {
            HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
            vendorAdapter.setGenerateDdl(true);
            vendorAdapter.setShowSql(true);

            LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
            factory.setJpaVendorAdapter(vendorAdapter);
            factory.setPackagesToScan("infoq.spring4.data");
            factory.setDataSource(dataSource());
            factory.afterPropertiesSet();

            return factory.getObject();
        }

        @Bean
        public HibernateExceptionTranslator hibernateExceptionTranslator(){
            return new HibernateExceptionTranslator();
        }

        @Bean
        public PlatformTransactionManager transactionManager() {
            JpaTransactionManager txManager = new JpaTransactionManager();
            txManager.setEntityManagerFactory(entityManagerFactory());
            return txManager;
        }

        @Override
        public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new Hibernate4Module());
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            converter.setObjectMapper(objectMapper);
            converters.add(converter);
        }
    }

    @Component
    public static class Dataloader {
        @Autowired
        private UserRepository userRepository;

        @PostConstruct
        void init() {
            createUser("danveloper", "dan.woods@infoq.com", "352-555-1212", new Date());
            Calendar calendar = Calendar.getInstance();
            calendar.set(2011, Calendar.JANUARY, 1);
            createUser("doo", "doo@bar.com", "352-111-1111", calendar.getTime());
        }

        private void createUser(String username, final String email, String phoneNo, Date createDate) {
            User user = new User();
            user.setUsername(username);
            user.setCreateDate(createDate);
            user.setEmailAddresses(new HashSet<String>() {{ add(email); }});
            final Phone phone = new Phone();
            phone.setActive(true);
            phone.setCountryCode("US");
            phone.setPhoneNumber(phoneNo);
            user.setPhoneNumbers(new HashSet<Phone>() {{ add(phone); }});

            System.out.println("Saving user!");
            userRepository.save(user);
        }
    }
}

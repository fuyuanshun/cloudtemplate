package boot.wx.config;

import boot.wx.properties.SlaveDataSourceProperties;
import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(SlaveDataSourceProperties.class)
@MapperScan(basePackages = "boot.wx.persistence.slave", sqlSessionTemplateRef = "slaveSqlSessionTemplate")
public class SlaveDataSourceConfig {

    @Autowired
    private SlaveDataSourceProperties properties;

    @Bean
    public DataSource slaveDataSource(){
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        DruidXADataSource druidDataSource = new DruidXADataSource();
        druidDataSource.setUrl(properties.getUrl());
        druidDataSource.setUsername(properties.getUsername());
        druidDataSource.setPassword(properties.getPassword());
        atomikosDataSourceBean.setXaDataSource(druidDataSource);
        atomikosDataSourceBean.setUniqueResourceName("slave");
        return atomikosDataSourceBean;
    }

    @Bean
    public SqlSessionFactory slaveSqlSessionFactory(@Qualifier("slaveDataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        mybatisSqlSessionFactoryBean.setDataSource(dataSource);
        //????????????
        mybatisSqlSessionFactoryBean.setTypeAliasesPackage("boot.wx.entity");
        //????????????
        MybatisConfiguration mybatisConfiguration = new MybatisConfiguration();
        mybatisConfiguration.setMapUnderscoreToCamelCase(true);
        //????????????
        mybatisSqlSessionFactoryBean.setConfiguration(mybatisConfiguration);
        mybatisSqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/slave/*.xml"));
        return mybatisSqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate slaveSqlSessionTemplate(@Qualifier("slaveSqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}

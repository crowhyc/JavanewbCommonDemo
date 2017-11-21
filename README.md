#   Javanewb-Common 
[项目地址](https://github.com/crowhyc)
###   当前版本：1.0-SNAPSHOT
####   集成工具：
 ConfigServer(zookeeper),Mysql(MyBatis),Redis(规范化键名，分布式同步锁)，mongodb（基于springboot),lombok等   
**注意:请在使用时,在idea工具中安装lombok插件支持**

##   1.概述
   本项目以spring-cloud、spring-boot为基础骨架。利用spring微服务套件的快速搭建，规范化管理的优势，集成多种主流开源工具的二次封装。以此达到，业务开发人员可以在不用关心底层的情况下迅速开发，同时可以在本项目中进行工具的扩展和定制，能够方便的对上层业务的开发方式进行规范化。
##   2.集成方法：
###   2.2maven依赖添加：
在项目顶级pom.xml文件中加入如下内容
```xml
<project>
    <parent>
            <groupId>com.javanewb.common</groupId>
            <artifactId>common-pom</artifactId>
            <version>1.0-SNAPSHOT</version>
    </parent>
    <repositories>
        <repository>
            <id>ali-nexus</id>
            <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
        </repository>
        <repository>
            <id>ali-nexus-public-snapshots</id>
            <url>http://maven.aliyun.com/nexus/content/repositories/snapshots/</url>
        </repository>

        <repository>
            <id>javanewb-nexus</id>
            <url>http://x.javanewb.com/nexus/content/repositories/releases/</url>
            <releases>
                <enabled>true</enabled>
            </releases>
        </repository>

        <repository>
            <id>javanewb-nexus-snapshots</id>
            <url>http://x.javanewb.com/nexus/content/repositories/snapshots/</url>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
                <checksumPolicy>warn</checksumPolicy>
            </snapshots>
        </repository>

    </repositories>
    <dependencies>
         <dependency>
            <groupId>com.javanewb.common</groupId>
            <artifactId>starter-web</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

           </dependencies>
</project>
```
###    2.2ConfigServer(zookeeper)配置：
[基础模板地址](https://github.com/crowhyc/ThreadSynchronizer/blob/master/src/main/resources/ZkConfig.txt)   
在module项目resources目录中的bootstrap.yaml文件中加入如下配置：
```yaml
spring:
  profiles: dev
  cloud:
    zookeeper:
      connectString: zk连接地址，多个时用逗号（，）分割
      enabled: true
      config:
        enabled: true
        root: /configurations/javanewb/${profile.active} //zk中配置路径
        defaultContext: apps //前缀
        profileSeparator: ':'  //前缀与profile的分隔符
```
##   3.嵌入式工具：
###   3.1Mysql：
####   3.1.1使用工具
  mybatis：mybatis一直在业内享有良好声誉。对数据库的操作可以有多种方式使用，目前主流的是XML方式。XML的方式可以灵活的对SQL进行编写，同时还具有很大程度的可编码性，受到大家欢迎。但是，其XML+SQL的配置方式较为繁琐，一些简单的CURD都需要自己写SQL进行配置才能使用，虽然后面新版本引入了注解、接口等方式。但是，由于xml配置的方式使SQL能集中存放，并便于管理所以很少会有项目会使用注解或接口的方式。   

  tk.mapper:上面mybatis的介绍中提到，mybatis的XML方式虽然有很多优点。但是，像最简单的CURD都需要自己写SQL进行管理。无疑这造成了开发人员很大程度的重复无意义劳动。此时，开源的mybatis工具[tk.mapper](https://www.oschina.net/p/mybatis-mapper)就派上了用场。它可以在mybatis在mapper进行映射时帮助生成简单的CRUD，极大程度减少了mybatis的使用时的无意义劳动。   
依赖包:

``` 
        <dependency>
            <groupId>com.javanewb.common</groupId>
            <artifactId>starter-mybatis</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

```  

#####   3.1.1.1mysql数据库配置：

数据库使用阿里的Druid连接池，默认情况下只需要配置master就可以了。
```yaml
datasource:
  master:
    driverClassName:com.mysql.jdbc.Driver
	initialSize:1
	maxIdle:5
	maxTotal:5
	maxWait:60000
	minIdle:1
	password:
	removeAbandonedOnBorrow:true
	removeAbandonedTimeout:180
	testOnBorrow:false
	testWhileIdle:true
	timeBetweenEvictionRunsMillis:30000
	url:jdbc:mysql://127.0.0.1:3306/demo_database?useUnicode:true&characterEncoding:utf8
	username:root
	validationQuery:select 1
  slave:
    driverClassName:com.mysql.jdbc.Driver
	initialSize:1
	maxIdle:5
	maxTotal:5
	maxWait:60000
	minIdle:1
	password:
	removeAbandonedOnBorrow:true
	removeAbandonedTimeout:180
	testOnBorrow:false
	testWhileIdle:true
	timeBetweenEvictionRunsMillis:30000
	url:jdbc:mysql://127.0.0.1:3306/demo_database?useUnicode:true&characterEncoding:utf8
	username:root
	validationQuery:select 1
```

#####   3.1.1.2使用配置：
DAO接口需要继承*com.javanewb.common.configuration.mybatis.CommonsMapper<T>* 类,并且放入com.javanewb.**.dao/mapper包下.如果,需要修改DAO接口的包地址.需要在yml配置中添加如下属性:
```yaml
 common:
  mapper-path: {com.xxx.dao,com.xxx.mapper...}
```

实体类地址,需要在application.yaml中加入如下配置
```yaml
mybatis:
  conf:
    entityPath: com.javanewb.common.entity//实体类的地址
```
同时，不要忘记在zk中配置datasource，和active这个profile

#####   3.1.1.3读写分离配置：
如果，需要读写分离配置，需要配置上slave的数据源。同时，需要在yaml配置文件中将common:r-w-split-enabled属性设为true。   
类中的方法需要将操作的动词作为方法名开头。默认有如下的开头做为读操作：***insert,update,modify,new,create,delete,remove***。如果，还有其他前缀的方法需要使用写库。可以使用**common:write-d-b-names**:进行添加，接受参数是一个string数组。

##### 3.1.1.4 xml方法使用配置:
mapper.xml文件需要放在classpath根目录下的mapper文件夹中。用法与传统的mybatis xml文件一样。其他使用方式与mybatis原有方式一样.

####   3.1.2使用方法:

#####   3.1.2.1实体类：
实体类配置中，所有的数据库相关配置使用java通用的jpa注解
```java
@Data
public class User {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String address;
    private String gender;
    private Integer age;
    }
```
#####   3.1.2.2tk.mybatis的使用：
由于tk.mybatis中已经集成部分简单的单表curd操作，所以如果有特殊需求才需要在mapper.xml中进行个性化配置

```java
    @Repository("user")
    public interface UserMapper extends CommonsMapper<User>{//如果需要使用tk.mybatis必须继承此接口 
    
    }

    public class service{
    @Autowired
    private UserMapper userMapper;
    
        public void userInsert() {
            User user = new User();
            user.setUsername("Crowhyc");
            user.setPassword("123456");
            user.setAddress("java");
            user.setAge(25);
            user.setGender("男");
            userMapper.insertUseGeneratedKeys(user);//特别注意，使用自生成的主键方式insert需要在数据库中，将表的主键定义为autoincrement。否则需要显式的set主键
        }
    }
    
``` 

###   3.2redis:
####   3.2.1功能介绍：
   redis是大家比较熟悉的nosql（K-V）内存数据库中的一种，由于他优秀的速度和多种可直接使用的数据结构可以在很多地方用到。k-v的结构虽然给redis的使用带来了方便。但是，很多时候每个项目开发者对k的设计有不同的想法。
导致多项目在使用同一个redis库的时候，key是杂乱无章，惨不忍睹。基于这个问题，redis模块在封装时将key的格式做了强制限定。大家在使用时就不会在看到五花八门的key格式了。目前，考虑到大家分布式结构和集群使用的普遍性，模块内也集成了简单的分布式锁。默认10s过期，避免不当使用时导致的死锁产生。   
   由于时间限制。模块目前只支持k-v和set数据结构的操作，后面会逐步更新支持所有redis的数据结构。
   
####   3.2.2依赖包：

```xml
    <dependency>
            <groupId>com.javanewb.common</groupId>
            <artifactId>starter-redis</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
```

由于，redis的key被强制规范化。所以，我们先要创建一个rediskey的生成类:   
   
```java
@Component
public class RedisConst {
    /**
     * RedisKey通过KeyFactory工厂生成
     * getRedisKey方法通过接收的字符串数组，将每个字符串通过"："冒号连接，
     * 也将app_name与active的profile加上在最前面，在使用redis工具的时候可以方便的分级查看。
     * 例如:portal:dev:shoppingcart:1
     * getRedisKey（expireTime,String... keys）获取有过期时间的key
     * getRedisKey（String... keys）获取永久的key
     */
    @Autowired
    private RedisKeyFactory keyFactory;
    public static final int EXPIRE_TIME = 30 * 60;//秒

    /**
     * @param id keyid
     * @param mainKey 主键
     * @return
     */
    public RedisKey getExpireTimeKey(Long id,String mainKey) {
        return keyFactory.getRedisKey(EXPIRE_TIME, mainKey, id + "");
    }

    /**
     * @param id
     * @param mainKey
     * @return
     */
    public RedisKey getPermanentKey(Long id,String mainKey) {
        return keyFactory.getRedisKey(EXPIRE_TIME, mainKey, id + "");
    }
}
```

Operation的使用：
```java
public class RedisTest extends BaseTest {

    @Autowired
    private CommonRedisTemplate<String> commonRedisTemplate;
    @Autowired
    private RedisConst redisConst;


    @Test
    public void insertValue() {
        String val = "测试内容";
        commonRedisTemplate.opsCommonValue().set(redisConst.getExpireTimeKey(1L,"expireKey"),val);
        commonRedisTemplate.opsCommonValue().set(redisConst.getPermanenstKey(2L,"permanentKey"),val);
    }
}
```
###   3.3lombok：
####   3.3.1介绍：
Lombok是一个可以通过简单的注解形式来帮助我们简化消除一些必须有但显得很臃肿的Java代码的工具，通过使用对应的注解，可以在编译源码的时候生成对应的方法。   
官方地址：https://projectlombok.org/   
github地址：https://github.com/rzwitserloot/lombok。
####  3.3.2使用方法：
#####   3.3.2.1插件安装：
不管是IDEA还是Eclipse，在使用之前都需要安装插件,IDE才能正常的理解lombok的注解，具体的插件安装方法可以从去官网查看。
#####   3.3.2.2注解介绍：

  下面只是介绍了几个常用的注解，更多的请参见https://projectlombok.org/features/index.html。

@Getter / @Setter

  可以作用在类上和属性上，放在类上，会对所有的非静态(non-static)属性生成Getter/Setter方法，放在属性上，会对该属性生成Getter/Setter方法。并可以指定Getter/Setter方法的访问级别。

@EqualsAndHashCode

  默认情况下，会使用所有非瞬态(non-transient)和非静态(non-static)字段来生成equals和hascode方法，也可以指定具体使用哪些属性。

@ToString

  生成toString方法，默认情况下，会输出类名、所有属性，属性会按照顺序输出，以逗号分割。

@NoArgsConstructor, @RequiredArgsConstructor and @AllArgsConstructor

  无参构造器、部分参数构造器、全参构造器，当我们需要重载多个构造器的时候，Lombok就无能为力了。

@Data

  @ToString, @EqualsAndHashCode, 所有属性的@Getter, 所有non-final属性的@Setter和@RequiredArgsConstructor的组合，通常情况下，我们使用这个注解就足够了。
  
  
###   4Web相关:
####   4.1http请求处理:
#####   4.1.1消息返回格式:

``` json
{
  "timestamp": "1511152931522",//时间
  "code": 200,//结果码
  "msg": "status:200:message.isOk", //结果信息
  "msgId": "20f173e22dae486cbbaee103142ef3a8@",//请求唯一编码
  "data": {//数据内容
      }
}
```

#####   4.1.2异常处理方式:

异常信息格式:

``` json
{
  "code": 1006,//异常码
  "msg": "发生异常请处理XXX",//异常信息
  "mdcTarget": "e1e734f78beb434589ac7b162b8e339d@",//请求唯一编码
  "timestamp": 1511153006734//请求时间
}
``` 

异常信息的开发示例:

```java
public class DuplicationBizIdException extends BusinessException {//继承javanewb common包中的businessException 异常父类
    public DuplicationBizIdException() {
        super(ManageErrorCode.DUPLICATION_BIZ_ID//异常码, "重复的业务id"//异常信息);
    }
}

```
注意:如果遇到未捕获的异常,返回会统一为系统错误


###   4.2Swagger：
####   4.2.1Swagger配置：
```yaml
common:
  swagger:
    enabled: true //是否开启
    api-info:
      title: 测试标题
      version: 1.0.0 //版本
      description: Portal-Common测试项目 //描述
    path-regex: /demo.* //需要mapping的url地址正则，注意.*不能丢

```

swagger-codegen 介绍
swagger-codegen 支持 swagger的schema 生成不同语言的客户端代码。 在linux console 输入：swagger-codegen，会列出支持的语法

    Available languages: [android, aspnet5, async-scala, cwiki, csharp, cpprest, dart, flash, python-flask, go, groovy, java, jaxrs, jaxrs-cxf, jaxrs-resteasy, jaxrs-spec, inflector, javascript, javascript-closure-angular, jmeter, nancyfx, nodejs-server, objc, perl, php, python, qt5cpp, ruby, scala, scalatra, silex-PHP, sinatra, rails5, slim, spring, dynamic-html, html, html2, swagger, swagger-yaml, swift, tizen, typescript-angular2, typescript-angular, typescript-node, typescript-fetch, akka-scala, CsharpDotNet2, clojure, haskell, lumen, go-server]

具体命令查看：https://github.com/swagger-api/swagger-codegen#generators

官方地址：http://swagger.io/
github地址：https://github.com/swagger-api/swagger-codegen

###   4.3请求唯一编码:

实现代码：

```java
    public class LoggerMDCFilter implements Filter {
    public static final String HEADER_KEY_KRY_GLOBAL_MSG_ID = "_kry_global_msg_id";
    public static final String IDENTIFIER = "IDENTIFIER";
    public static final String URI_KEY = "URI";

    private String getId(String s) {
        return String.format("%s@", s);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String kryGlobalMsgId = req.getHeader(HEADER_KEY_KRY_GLOBAL_MSG_ID);
        if (StringUtils.isBlank(kryGlobalMsgId)) {
            kryGlobalMsgId = getId(UUIDUtil.getUUID());
        } else {
            kryGlobalMsgId = getId(String.valueOf(kryGlobalMsgId));
        }
        // 唯一标识埋点，设置线程唯一标识，方便查找日志，组合方式 时间戳@请求埋点@线程号
        MDC.put(IDENTIFIER, kryGlobalMsgId);
        // uri_key
        MDC.put(URI_KEY, req.getRequestURI());
        chain.doFilter(request, response);
    }

    /**
     * 销毁MDC
     */
    @Override
    public void destroy() {
        MDC.remove(IDENTIFIER);
        MDC.remove(URI_KEY);
    }
}
```


1. Java 代码中使用：
    
    MDC.get(LoggerMDCFilter.IDENTIFIER);

2. Logback.xml 中配置：
 
```   
    <appender name="Console" class="ch.qos.logback.core.ConsoleAppender">
        <!--<withJansi>true</withJansi>-->
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} %level %X{IDENTIFIER}%thread %X{URI} %logger[%method:%line] - %msg%n
            </pattern>
            <charset>utf8</charset>
        </encoder>
    </appender>
```

  
##   5.Spring-boot autoconfig工具：
###   4.1ActiveMq：
yaml配置：

```yaml
spring:
  activemq:
    broker-url:failover:(tcp://XXX:61616,tcp://XXX:61616,tcp://XXX:61616)
    user:admin
    password:xxxx
    pooled:true是否创建PooledConnectionFactory，而非ConnectionFactory，默认false
    in-memory:true是否是内存模式，默认为true
  jms:
    pub-sub-domain:true是否使用默认的destination type来支持 publish/subscribe，默认: false
```
使用示例：
configuration配置:

```java
@Configuration
@EnableJms
public class AMQConfig{
     //配置通道
     @Bean
        public ActiveMQTopic clusterActiveMQTopic() {
            return new ActiveMQTopic("com.keruyun." + activeProfile + ".shoppingcart");
        }
    //配置监听器容器，及包括的监听器
    @Bean
    public DefaultMessageListenerContainer clusterContainer(ConnectionFactory connectionFactory, ActiveMQTopic clusterActiveMQTopic, NoticeService noticeService) {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setDestination(clusterActiveMQTopic);
        container.setMessageListener(new ClusterListener());
        return container;
    }   
}
```
监听器实现：（实现javax.jms.MessageListener接口）

```java
@Service
@Slf4j
public class ClusterListener implements MessageListener {
       @Override
         public void onMessage(Message message) {
            //收到MQ消息后的具体业务实现
         }
}
```
发布器实现：

```java
@Service
public class MqService {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;//autoconfig根据配置默认构建的对象，如无特殊需求，不用自己构造
    @Autowired
    private ActiveMQTopic clusterActiveMQTopic;//注意属性名要与config中实例化Bean的方法名对应

    public void send(String msg) {
        jmsMessagingTemplate.convertAndSend(clusterActiveMQTopic, msg);//发送消息
    }
}
```
###   4.2Mongodb：
依赖包:

```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-mongodb</artifactId>
            <version>1.5.7.RELEASE</version>
        </dependency>
```

yaml配置
```yaml
spring:         
    # HTTP ENCODING  
    http:  
        encoding.charset: UTF-8  
        encoding.enable: true  
        encoding.force: true  
    data:  
        mongodb://配置方法1  
               host: 127.0.0.1  
               port: 27017  
               username:   
               password:   
               database: xx  
               authenticationDatabase:
        mongodb://配置方法2
               url：	mongodb://用户名:密码@数据库地址1,数据库地址2/数据库名
```
使用示例：

```java
public interface PersonRepository extends MongoRepository<Person, String>{
//MongoRepository<实体,主键类型>
//MongoRepository中已经默认支持了简单的CRUD方法可以直接使用
}
```
上述示例只是简单的CRUD方法，如果需要自定义复杂的方法，[请参考网址](http://blog.csdn.net/itbasketplayer/article/details/8085988)。也可以直接使用Autowire注解获取MongoTemplate使用熟悉的Query，Criteira模式进行查询   
###   4.3Schedule定时工具：
配置：   
只需要在任意Configuration文件上加入@EnableScheduling，保证在启动时这个配置文件会被spring正常加载，便可以轻松开启单服quart定时器。   
使用方法：   
在需要进行定时调用的方法头上添加@Scheduled注解，@Scheduled注解有多种属性用于方便配置执行间隔：
####   属性介绍：
| 属性名 | 用处 | 示例 |    
| :-----: | :---------: | :---: |   
|fixedDelay|按延迟执行|fixedDelay = 5000|      
|cron|按cron表达式执行|cron = "0/5 * * * * *"|      
|fixedRate|按间隔执行|fixedRate = 5000|      
|initialDelay|首次执行的延迟时间|initialDelay = 5000|  



##   5.联系方式： 
E-Mail：crowhyc@163.com   
博客：http://blog.csdn.net/crowhyc   
希望和大家一起将这个工具越做越好，如在阅读或使用中遇到问题，或更好的实现方案、工具。请联系我。   
                                  ***java.云逸***
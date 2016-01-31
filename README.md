# 基于Spring框架快速开发
本框架并不是自己实现以下提及功能，而是基于Spring框架强大的集成能力，整合或配置，
形成一套完整的基于多数据源分库分表（关系型数据库）的支持多租户的解决方案。

## 使用到的相关开源技术及版本

    1. Spring               4.2.4.RELEASE
    2. Spring Data JPA      1.9.2.RELEASE
    3. Spring Data Redis    1.6.2.RELEASE
    4. Jackson              2.4.5
    5. Hibernate            4.3.6.Final

各开源项目之间可能会有版本不兼容，因此应该尽量小心，可以参考本配置。

## 分库支持
此处使用多数据源连接来实现分库，实现Spring提供的`AbstractRoutingDataSource`抽象类，仅需实现`determineCurrentLookupKey`方法即可。

实现多数据源支持
```java
public class DynamicRoutingDataSource extends AbstractRoutingDataSource{

    @Override
    protected Object determineCurrentLookupKey() {
        return TableProvider.getDataSoureName();
    }
}
```
进行数据源配置
```java
    @Bean
    public DataSource dataSource() {
        //配置数据源 ds1
        JDBCDataSource ds1 = new JDBCDataSource();
        //配置数据源 ds2
        JDBCDataSource ds2 = new JDBCDataSource();

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("ds1", ds1);
        targetDataSources.put("ds2", ds2);

        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        //设置数据源映射
        dataSource.setTargetDataSources(targetDataSources);
        //设置默认数据源，当无法映射到数据源时会使用默认数据源
        dataSource.setDefaultTargetDataSource(ds1);
        dataSource.afterPropertiesSet();
        return dataSource;
    }
```

## 分库支持

由于`Spring Data JPA`对JPA规范中的`@Table`使用SpEL，所以无法直接使用`@Table`来动态实现表路由，此处借助Hibernate提供的`Interceptor`，我们可以看下官方对该接口的说明

> Inspection occurs before property values are written and after they are read
> from the database.

即使用该接口可以实现Hibernate与数据库进行交互时，对请求与返回进行处理。Hibernate
提供了这个拦截器的空实现`EmptyInterceptor`，此处我们只需继承并重写`onPrepareStatement`
方法即可。
表映射拦截器实现
```java
public class TableMapperInterceptor extends EmptyInterceptor {

    @Override
    public String onPrepareStatement(String sql) {
        return sql.replace(TableProvider.PREFIX, TableProvider.getTablePrefix());
    }
}
```

实体的配置
```java
@Entity
@Table(name = TableProvider.PREFIX + "_demo")
public class Demo {

    @Id
    private Long id;
    private String name;

    //可以设置更多字段
    //此处省略getter/setter方法
}
```

注：Spring Data Mongo借助SpEL实现分表，可以阅读本人的博文
[MongoDB分表与分片选择的一次实践](http://blog.csdn.net/zjl103/article/details/46927403)

## 多租户支持

多租户并不需要多实现什么，仅在进行数据库操作时，指定相应的租户识别号即可。

## 缓存支持

借助Spring对Cache的支持，我们可以“优雅”的使用缓存，并且是无需关心缓存实现，以下我们展示下缓存的配置与使用。
缓存配置，注册一个CacheManager实例
```java
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        //配置Cache
        ConcurrentMapCacheFactoryBean cache1 = new ConcurrentMapCacheFactoryBean();
        cache1.setName("tenants");
        cache1.afterPropertiesSet();
        Set<Cache> caches = new HashSet<>();
        caches.add(cache1.getObject());
        //可以添加更多的Cache
        cacheManager.setCaches(caches);

        return cacheManager;
    }
```
使用缓存，使用`@CacheConfig`及`@Cacheable`
```
@CacheConfig(cacheNames = "tenants")
public class TenantRepositoryImpl extends SimpleJpaRepository<Tenant, Long> {

    @Cacheable
    @Override
    public Tenant findOne(Long id) {
        return super.findOne(id);
    }
}
```
Spring提供了多个缓存操作的注解

> `@Cacheable` triggers cache population
> `@CacheEvict` triggers cache eviction
> `@CachePut` updates the cache without interfering with the method execution
> `@Caching` regroups multiple cache operations to be applied on a method
> `@CacheConfig` shares some common cache-related settings at class-level

通过上面的展示，我们的应用代码不依赖于具体的缓存实现，后续如果需要变更缓存实现，
我们无需更改我们的应用代码。而且对于后续扩展对新类型缓存的支持也将会很简便，该
缓存仅需实现Spring的`CacheManager`接口即可。
目前Spring 支持的常用缓存类型有：
1. Guava
2. ConcurrentMap
3. EhCache
4. JCache
5. Redis，可以参考本项目的RedisConfig的配置

注：要特别注意分布式缓存的与Hibernate结合时，延迟加载属性的序列化问题。可以阅读
本人的博文[一个Memcache+Hibernate自处理二级缓存问题](http://blog.csdn.net/zjl103/article/details/45484633)

## 框架为我们做了什么

### 简化Dao层的开发
借助Spring Data，通过方法命名式来定义数据库查询（0 sql）。
```java
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByName(String name);
}
```
我们通过定义接口`UserRepository`的`findByName`方法，就实现了通过用户名获取用户的数据库查询。同时在`JpaRepository`中提供了基础的、基于泛型的CRUD数据库操作。

注：Spring在服务启动时会，会检测方法名是否合法，如对于`findByName`方法，如果实体没有`name`属性，将会报错，因此基于此种方式构建的Dao层将会更“可信赖”，可以减少我们的测试量。

### 同一实体返回不同投影给客户端
我们经常会遇到【列表请求】与【详情请求】需要包含不同的字段，以减小网络的传输量，借助Spring Mvc对`@JsonView`的支持，我们无需再定义额外`Vo`来实现。
控制器定义
```java
@RestController
@RequestMapping("/companies")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @JsonView(DetailView.class)
    @RequestMapping(value = "/employees/{employeeId}",method = RequestMethod.GET)
    public Employee get(@PathVariable("employeeId") long employeeId) {
        return service.findOne(employeeId);
    }

    @JsonView(ListView.class)
    @RequestMapping(value = "{companyId}/employees",method = RequestMethod.GET)
    public List list(@PathVariable long companyId) {
        return service.findAll();
    }
}
```
实体定义
```java
@Entity
@Table(name = TableProvider.PREFIX_ + "employee")
public class Employee extends BaseTenantDomain {

    public interface DetailView{}

    public interface ListView{}

    private String name;

    private String enName;

    @JsonView(Detail.class)
    private Department department;

    //此处省略getter/setter方法
}

```
我们在控制器方法`get`指定`@JsonView(DetailView.class)`表示返回的Json将仅包含`Employee`中未使用`@JsonView`注解及使用`@JsonView`注解且包含DetailView.class的字段。

### 提供增量式修改方法
有时我们仅需要修改部分字段，而传统`update`方法为覆盖式的，当你的某个字段没传时，也会被覆盖为空，因此此处特地增加了`BaseService.update(Long id, Map<String, Object> map)`方法，允许增量修改字段，而不需要修改的可以不传。

注：还有另外一种相似的修改场景，操作A仅能修改字段集合a，操作B仅能修改字段集合b，目前暂未支持该功能，但应该可以结合类似`@JsonView`的方式来实现。

## 一些思考
本人的一些思考分享，不一定对，欢迎探讨

### Valid的时机：
1. 数据库保存时，即Servicer与Dao的中间层（或Dao层），该步骤应该是必须的。
2. 在进行业务逻辑处理前，如Controller或Service的第一步，此步骤非必须，但如果使用Valid，则可以省去很多进行业务逻辑时的数据有效性判断（但是当数据需要由业务逻辑补充时，则1与2无法使用同一套验证，此时可以考虑使用group），但如果无业务逻辑处理时，则可以完全委托给第1点所述时验证。

### 缓存操作应该放在哪
1. 对实体的缓存操作，Service层不应该关心数据是从数据库还是从缓存中读取到，即缓存的操作应该是在Dao层且对用户透明的，如Hibernate对二级缓存的使用。
2. 非实体的缓存操作，仅当你需要缓存的与数据库实体不同时，才需要通过Service层手动操作，如xx标志。


### Service层是否需要接口
如果代码（非框架代码）编写过程需要经常改动到继承体系中的代码，意味着无法形成稳定的契约，则该继承体系根本不需要存在。我们常说的面向接口编程，是为了不依赖于实现，而对于业务系统来说，则没有多种业务实现。

在我们最常使用接口的MVC三层架构中，原来的接口可能是为了使用Proxy来提供动态代理，因此必须要定义接口，而现在使用cglib，则没必要了。

简单来说，继承体系仅对框架有意义，而对于业务代码，则会成为开发与维护的累赘。

## 问题解决及注意事项

此处记录本人在整合过程中遇到的一些问题及后续引用该框架的开发者需要注意的事项。

### 动态`DataSource`的问题，此处的实现为`DynamicRoutingDataSource`
`Service`方法(包括`Service`方法再调用其它方法)将会共用同一个数据库连接，即在`Service`方法，无法进行数据源的切换，进入`Service`方法时，会从`DataSource`中请求数据库连接，这也使事务得到发挥作用。如果要切换`DataSource`，则可以使用切换线程来实现

### `@Async`在debug状态下，会使整个请求挂起,此时不要误认为异步无效。

### Spring Cache使用限制
`@Cacheable`可以注解在`Repository`上，但必须具体`Service`调用具体`Repository`时才有效，否则无效。

```java
    @Repository
    @CacheConfig(cacheNames = "companies")
    public interface CompanyRepository extends JpaRepository<Company,Long>{

        @Override
        @Cacheable
        Company findOne(Long id);
    }

    @Service
    public class CompanyService extends BaseService{

        @Autowired
        private CompanyRepository repository;

        //如果这里不写repository.findOne(id)，而是使用的是BaseService的findOne，则会无效
        @Override
        public Company findOne(Long id) {
            return repository.findOne(id);
        }
    }

```
可以使用以上的方式来实现，但当你需要有一个基础`BaseService`时，就无法使用此方式，此时只能重新实现`Repository`

```java
    public interface CompanyOtherRepository {
        Company findOne(Long id);
    }

    @Repository
    public interface CompanyRepository extends JpaRepository<Company,Long>,CompanyOtherRepository {
    }

    @Repository
    @CacheConfig(cacheNames = "companies")
    public class CompanyRepositoryImpl extends SimpleJpaRepository<Company, Long> implements CompanyOtherRepository {

        @Autowired
        public CompanyRepositoryImpl(EntityManager entityManager) {
            super(Company.class, entityManager);
        }

        @Cacheable
        @Override
        public Company findOne(Long id) {
            return super.findOne(id);
        }
    }

```
注：此处`CompanyOtherRepository`不是必需的

### 解决关联实体JSON序列化死循环问题
1. 使用`@JsonIgnore`来使用得一方可以不被序列化，常用于`1 vs n`中 `1`端的`n`属性上
2. 使用`@JsonManagedReference`与`@JsonBackReference`对，被`@JsonBackReference`注解的属性将不会被序列化出来，目前看不出跟@JsonIgnore有啥区别
3. 使用`@JsonIgnoreProperties`,如在注解在`User`的`roles`属性上

```java
    public class User{
        @JsonIgnoreProperties("users")
        private List<Role> roles;

        //.... another code
    }
```

综上的方式，1跟2没有区别，可以使用1来得简单，1与3的效果不同，所以在使用上可以很容易区分。


## 参考文献
[Spring Framework Reference](http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/)

[Spring Data JPA Reference](http://docs.spring.io/spring-data/jpa/docs/1.8.2.RELEASE/reference/html/)

[Spring Data Redis Reference](http://docs.spring.io/spring-data/redis/docs/1.6.2.RELEASE/reference/html/)

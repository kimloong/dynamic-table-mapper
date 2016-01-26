# 基于Spring框架快速开发
本框架并不是自己实现以下提及功能，而是基于Spring框架强大的集成能力，整合或配置

## 分库支持

## 分库支持

## 多租户支持

## 缓存支持





## 问题解决及注意事项

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

### 解决关联实体JSON序列化死循环问题
1. 使用`@JsonIgnore`来使用得一方可以不被序列化，常用于`1 vs n`中 `1`端的`n`属性上
2. 使用`@JsonManagedReference`与`@JsonBackReference`对，被`@JsonBackReference`注解的属性将不会被序列化出来，目前看不出跟@JsonIgnore有啥区别
3. 使用`@JsonIgnoreProperties`,如在注解在`User`的`roles`属性上

``` java
    public class User{
        @JsonIgnoreProperties("users")
        private List<Role> roles;

        //.... another code
    }
```

综上的方式，1跟2没有区别，可以使用1来得简单，1与3的效果不同，所以在使用上可以很容易区分。
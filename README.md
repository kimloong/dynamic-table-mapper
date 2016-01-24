### 解决关联实体JSON序列化死循环问题
1. 使用`@JsonIgnore`来使用得一方可以不被序列化，常用于`1 vs n`中 `1`端的`n`属性上
2. 使用`JsonManagedReference`与`@JsonBackReference`对，被`@JsonBackReference`注解的属性将不会被序列化出来，目前看不出跟@JsonIgnore有啥区别
3. 使用`@JsonIgnoreProperties`,如在注解在`User`的`roles`属性上

``` java
    public class User{
        @JsonIgnoreProperties("users")
        private List<Role> roles;

        //.... another code
    }
```

综上的方式，1跟2没有区别，可以使用1来得简单，1与3的效果不同，所以在使用上可以很容易区分。
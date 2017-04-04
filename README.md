# 第七节  SpringBoot 通过JPA连接数据库
## 1 添加pom依赖
```xml
<!-- jpa依赖 -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>

		<!-- MySQL -->
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
		</dependency>

		<!-- druid数据源 -->
		<dependency>
			<groupId>com.alibaba</groupId>
			<artifactId>druid</artifactId>
			<version>1.0.0</version>
		</dependency>

		<!-- web -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<!-- test -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>

		<!--热部署依赖-->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<optional>true</optional>
		</dependency>
```
# 2 数据源
```xml
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/springboot_db?useUnicode = true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull
spring.datasource.username=root
spring.datasource.password=root
``` 
# 3 数据库脚本初始化
```sql
CREATE DATABASE /*!32312 IF NOT EXISTS*/`springboot_db` /*!40100 DEFAULT CHARACTER SET utf8 */;
 
USE `springboot_db`;
 
DROP TABLE IF EXISTS `t_author`;
 
CREATE TABLE `t_author` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `real_name` varchar(32) NOT NULL COMMENT '用户名称',
  `nick_name` varchar(32) NOT NULL COMMENT '用户匿名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```
# 4 方法二 通过调用EntityManager类方法
## 4.1 实体对象
```java
@Entity
@Table(name="t_author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "real_Name")
    private String realName;

    @Column(name = "nick_Name")
    private String nickName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
```
## 4.2 DAO相关
```java
public interface AuthorDao {
    List<Author> findAll();
    Author findAuthor(Long id);
}

@Repository
public class AuthorDaoImpl implements AuthorDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Author> findAll() {
        return this.entityManager.createQuery("select t from Author t ",Author.class).getResultList();
    }

    @Override
    public Author findAuthor(Long id) {
        return this.entityManager.createQuery("select t from Author t where id = ?", Author.class).setParameter(1,id).getSingleResult();
    }
}
```
注意 ``表名为为对象名，不是具体的表名``
## 4.3 Service相关
```java
@Service
public class AuthorService {

    @Autowired
    private AuthorDao authorDao;

    public List<Author> findAll(){
        return this.authorDao.findAll();
    }

    public Author findAuthor(Long id){
        return this.authorDao.findAuthor(id);
    }
}
```
## 4.4 Controller相关
```java
@RestController
@RequestMapping(value = "/data/jpa/author2")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    /**
     * 查询用户列表
     */
    @RequestMapping(method = RequestMethod.GET)
    public Map<String,Object> getAuthorList(HttpServletRequest request){
        List<Author> authorList = this.authorService.findAll();
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("total",authorList.size());
        param.put("rows",authorList);
        return param;

    }

    /**
     * 查询用户信息
     */
    @RequestMapping(value = "/{userId:\\d+}",method = RequestMethod.GET)
    public Author getAuthor(@PathVariable Long userId, HttpServletRequest request){
        Author author = this.authorService.findAuthor(userId);
        if (author == null){
            throw new RuntimeException("查询错误");
        }
        return author;
    }
}
```
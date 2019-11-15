"# config-server-demo" 

[官方文档：](https://cloud.spring.io/spring-cloud-config/reference/html/#_quick_start)

运行此服务在浏览器中或者如下用curl请求：
```bash
$ curl localhost:8888/foo/development
{"name":"foo","label":"master","propertySources":[
  {"name":"https://github.com/scratches/config-repo/foo-development.properties","source":{"bar":"spam"}},
  {"name":"https://github.com/scratches/config-repo/foo.properties","source":{"foo":"bar"}}
]}
```
http请求路径如下
```bash
/{application}/{profile}[/{label}]
/{application}-{profile}.yml
/{label}/{application}-{profile}.yml
/{application}-{profile}.properties
/{label}/{application}-{profile}.properties
```
> 注意： application通常是SpringApplication应用的配置文件中spring.config.name的值，profile是有效的环境(或者","分隔的属性列表)，label
是可选的git分支(默认是master)

# 二、安全
我们可以用任何方式来保护Config Server（从物理网络安全性到OAuth2承载令牌），因为Spring Security和Spring Boot提供了对许多安全性安排的支持。

要使用默认的Spring Boot配置的HTTP Basic安全性，请在依赖Spring Security（例如，通过spring-boot-starter-security）。默认值为用户名user和随机生成的密码。随机密码在实践中没有用，因此我们建议您（通过设置spring.security.user.password）配置密码并对其进行加密（有关如何操作的说明，请参见下文）。
```bash
spring:
  security:
    user:
      name: user
      password: dddd
```
# 三、配置文件加密
配置
```bash
encrypt:
  # key随便填写一个安全的即可
  key: 123@89#@!99df
```
如果远程属性源包含加密的内容（以开头的值{cipher}），则在通过HTTP发送给客户端之前，将对它们进行解密。此设置的主要优点是，当属性值处于“静止”状态时（例如，在git存储库中），不需要使用纯文本格式。如果无法解密某个值，则将其从属性源中删除，并使用相同的键但以前缀invalid和表示“不适用”（通常为<n/a>）的值添加其他属性。这在很大程度上是为了防止密文用作密码并意外泄漏。

如果为配置客户端应用程序设置远程配置存储库，则它可能包含application.yml与以下内容类似的内容：

**application.yml**
```bash
spring:
  datasource:
    username: dbuser
    password: '{cipher}FKSAJDFGYOS8F7GLHAKERGFHLSAJ'
```
.properties文件中的加密值不能用引号引起来。否则，该值不会解密。以下示例显示了有效的值：

**application.properties**
```bash
spring.datasource.username: dbuser
spring.datasource.password: {cipher}FKSAJDFGYOS8F7GLHAKERGFHLSAJ
```
config server会公开/encrypt and /decrypt endpoints(假设它们是安全的，并且只能由授权代理访问).如果您编辑远程配置文件，则可以使用Config Server通过POST到/encrypt端点来加密值，如以下示例所示：
```bash
$ curl localhost:8888/encrypt -d mysecret
682bc583f4641835fa2db009355293665d2647dade3375c0ee201de2a49f7bda
```
也可以用postman请求
> 如果您加密的值中包含需要URL编码的字符，则应使用--data-urlencode选项进行curl请求以确保其编码正确。

也可以通过/decrypt（如果服务器配置了对称密钥或完整密钥对）来执行解密操作，如以下示例所示：
```bash
$ curl localhost:8888/decrypt -d 682bc583f4641835fa2db009355293665d2647dade3375c0ee201de2a49f7bda
mysecret
```

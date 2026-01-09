# Water Info Backend (Spring Boot + MySQL)

基于 Spring Boot 3 的后端服务，实现 `/api/v1` 下的接口，包含实体、仓储、控制器、示例数据以及基础鉴权与权限管理。

## 环境要求
- JDK 17+
- Maven 3.8+
- MySQL 8.x（建议本地建库 `water_info`）

## 快速开始
1) 创建数据库并更新账号密码：
```sql
CREATE DATABASE water_info DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```
编辑 `server/src/main/resources/application.yml` 中的 `spring.datasource.url/username/password`。

2) 启动：
```bash
cd server
mvn spring-boot:run
```
默认端口 `8080`，上下文路径 `/api/v1`。

3) 查看接口文档：
- Swagger UI: `http://localhost:8080/api/v1/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api/v1/v3/api-docs`

4) 初始化数据：
```bash
mysql -u <user> -p < server/db/init.sql
```
包含建库/建表和示例 admin 用户与示例站点（UUID 存储为 BINARY(16)）。

## 项目结构（简述）
- `pom.xml`：Spring Boot 3.2 + Web + Data JPA + Security + Validation + MySQL + SpringDoc。
- `application.yml`：MySQL 连接、JPA 配置、JWT 占位配置。
- `domain/entity`：用户、站点、水位/流量/水质记录、报警等实体。
- `repository`：对应的 JPA 仓储。
- `service`：业务占位与示例数据（TODO：接入真实逻辑）。
- `controller`：按照 `/docs/API-Documentation.md` 描述的路由提供接口。
- `common`：统一返回体、异常处理。

## 认证与安全
- 已接入基于 Token 的登录鉴权，并通过角色（admin/operator/viewer）控制接口访问权限。
- 使用 Spring Security 过滤器从 `Authorization: Bearer <token>` 解析会话并注入认证用户。

## 下一步建议
- 接入真实的 MySQL 表结构与数据流，完善 Service 层。
- 用实体/DTO 映射替换当前的示例数据。
- 为关键接口补充集成测试与数据校验。

# Water Info Platform

Spring Boot + MySQL 后端（`server/`）和 Vue 3 前端（`backend/`），用于水文监测平台。接口定义见 `docs/API-Documentation.md`，后端统一前缀 `/api/v1`。

## 项目结构
- `server/`：Spring Boot 3 后端，MyBatis-Plus + Security，MySQL 持久化，Swagger。
- `backend/`：Vue 3 + TypeScript + Element Plus 前端，包含登录与权限管理页面。
- `docs/`：接口文档。

## 运行要求
- JDK 17+，Maven 3.8+
- Node.js 20+，npm
- MySQL 8.x（本地库 `water_info`）

## 后端启动（server）
1) 初始化数据库（可选示例数据）：
```bash
mysql -u <user> -p < server/db/init.sql
```
2) 配置 `server/src/main/resources/application.yml` 中的数据库账号/密码。
3) 启动：
```bash
cd server
mvn spring-boot:run
```
默认端口 `8080`，上下文 `/api/v1`；Swagger: `http://localhost:8080/api/v1/swagger-ui.html`。

## 前端启动（backend）
1) 复制并修改环境变量：
```bash
cd backend
cp .env.example .env    # 如需修改后端地址，编辑 VITE_API_BASE_URL
```
2) 安装依赖并运行：
```bash
npm install
npm run dev
# 生产构建
npm run build
```
开发默认端口 `5173`。

## 认证与跨域
- 后端使用基于 Token 的鉴权与角色权限控制（管理员/运维/访客）。
- 前端登录请求 `/auth/login`，成功后本地存储 Token 并在请求头附带 `Authorization: Bearer <token>`。

## 其它
- 数据库脚本：`server/db/init.sql`
- 后端配置说明：`server/README.md`
- 前端配置说明：`backend/README.md`

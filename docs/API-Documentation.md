🌊 水质监测平台 API 文档
目录
文档信息
认证方式
通用响应格式
认证模块 (Auth)
用户管理模块 (Users)
监测站点模块 (Stations)
水位监测模块 (Water Levels)
流量监测模块 (Flow Monitoring)
水质监测模块 (Water Quality)
警报管理模块 (Alarms)
数据导出模块 (Export)
角色权限矩阵
附录
文档信息
项目名称: 水质监测信息管理系统
API 版本: v1
文档日期: 2024年1月
Base URL: http://localhost:8080/api/v1
API文档地址: http://localhost:8080/swagger-ui.html
认证方式
所有 API 请求需要在 Header 中携带 Token：

Authorization: Bearer <your-jwt-token>
通用响应格式
成功响应
{
  "code": 200,
  "message": "success",
  "data": {
    // 业务数据
  },
  "timestamp": "2024-01-01T12:00:00"
}
分页响应
{
  "code": 200,
  "message": "success",
  "data": {
    "content": [],
    "totalElements": 100,
    "totalPages": 10,
    "currentPage": 1,
    "pageSize": 10
  },
  "timestamp": "2024-01-01T12:00:00"
}
错误响应
{
  "code": 401,
  "message": "未登录或Token已过期",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
状态码说明
状态码	说明
200	成功
400	请求参数错误
401	未认证或Token过期
403	无权限访问
404	资源不存在
500	服务器内部错误
认证模块 (Auth)
1. 用户登录
接口路径: POST /auth/login

请求参数:

{
  "username": "admin",
  "password": "admin123"
}
参数名	类型	必填	说明
username	String	是	用户名
password	String	是	密码
响应示例:

{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400,
    "user": {
      "id": 1,
      "username": "admin",
      "realName": "系统管理员",
      "role": "admin",
      "email": "admin@waterinfo.com"
    }
  },
  "timestamp": "2024-01-01T12:00:00"
}
2. 用户登出
接口路径: POST /auth/logout

请求头: Authorization: Bearer <token>

响应示例:

{
  "code": 200,
  "message": "登出成功",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
3. 获取当前用户信息
接口路径: GET /auth/me

请求头: Authorization: Bearer <token>

响应示例:

{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "admin",
    "realName": "系统管理员",
    "email": "admin@waterinfo.com",
    "phone": "13800138000",
    "role": "admin",
    "status": 1,
    "lastLoginAt": "2024-01-01T10:30:00",
    "createdAt": "2024-01-01T00:00:00"
  },
  "timestamp": "2024-01-01T12:00:00"
}
4. 刷新Token
接口路径: POST /auth/refresh

请求头: Authorization: Bearer <token>

响应示例:

{
  "code": 200,
  "message": "Token刷新成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400
  },
  "timestamp": "2024-01-01T12:00:00"
}
用户管理模块 (Users)
5. 获取用户列表
接口路径: GET /users

请求头: Authorization: Bearer <token>

请求参数:

参数名	类型	必填	说明
page	Integer	否	页码，从1开始，默认1
size	Integer	否	每页数量，默认10
username	String	否	用户名模糊搜索
role	String	否	角色过滤 (admin/operator/viewer)
status	Integer	否	状态过滤 (0-禁用, 1-启用)
响应示例:

{
  "code": 200,
  "message": "success",
  "data": {
    "content": [
      {
        "id": 1,
        "username": "admin",
        "realName": "系统管理员",
        "email": "admin@waterinfo.com",
        "phone": "13800138000",
        "role": "admin",
        "status": 1,
        "createdAt": "2024-01-01T00:00:00",
        "lastLoginAt": "2024-01-01T10:30:00"
      },
      {
        "id": 2,
        "username": "operator1",
        "realName": "操作员1",
        "email": "operator1@waterinfo.com",
        "phone": "13800138001",
        "role": "operator",
        "status": 1,
        "createdAt": "2024-01-02T00:00:00",
        "lastLoginAt": "2024-01-03T08:00:00"
      }
    ],
    "totalElements": 10,
    "totalPages": 1,
    "currentPage": 1,
    "pageSize": 10
  },
  "timestamp": "2024-01-01T12:00:00"
}
权限说明: 需要 admin 角色

6. 获取单个用户
接口路径: GET /users/{id}

路径参数:

参数名	类型	必填	说明
id	Long	是	用户ID
响应示例:

{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "admin",
    "realName": "系统管理员",
    "email": "admin@waterinfo.com",
    "phone": "13800138000",
    "role": "admin",
    "status": 1,
    "createdAt": "2024-01-01T00:00:00",
    "updatedAt": "2024-01-01T00:00:00",
    "lastLoginAt": "2024-01-01T10:30:00"
  },
  "timestamp": "2024-01-01T12:00:00"
}
7. 创建用户
接口路径: POST /users

请求头:

Authorization: Bearer <token>
Content-Type: application/json
请求体:

{
  "username": "newuser",
  "password": "password123",
  "realName": "新用户",
  "email": "newuser@waterinfo.com",
  "phone": "13800138002",
  "role": "operator"
}
参数名	类型	必填	说明
username	String	是	用户名，唯一
password	String	是	密码，最少6位
realName	String	否	真实姓名
email	String	否	邮箱，唯一
phone	String	否	电话
role	String	否	角色，默认viewer
响应示例:

{
  "code": 200,
  "message": "用户创建成功",
  "data": {
    "id": 3,
    "username": "newuser",
    "realName": "新用户",
    "email": "newuser@waterinfo.com",
    "role": "operator",
    "status": 1,
    "createdAt": "2024-01-01T12:00:00"
  },
  "timestamp": "2024-01-01T12:00:00"
}
权限说明: 需要 admin 角色

8. 更新用户
接口路径: PUT /users/{id}

路径参数:

参数名	类型	必填	说明
id	Long	是	用户ID
请求体:

{
  "realName": "更新的名称",
  "email": "newemail@waterinfo.com",
  "phone": "13900139000",
  "role": "admin",
  "status": 1
}
响应示例:

{
  "code": 200,
  "message": "用户更新成功",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
权限说明: 需要 admin 角色

9. 删除用户
接口路径: DELETE /users/{id}

路径参数:

参数名	类型	必填	说明
id	Long	是	用户ID
响应示例:

{
  "code": 200,
  "message": "用户删除成功",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
权限说明: 需要 admin 角色

10. 修改密码
接口路径: PUT /users/{id}/password

路径参数:

参数名	类型	必填	说明
id	Long	是	用户ID
请求体:

{
  "oldPassword": "oldpassword",
  "newPassword": "newpassword123"
}
响应示例:

{
  "code": 200,
  "message": "密码修改成功",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
错误示例:

{
  "code": 400,
  "message": "原密码错误",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
监测站点模块 (Stations)
11. 获取站点列表
接口路径: GET /stations

请求头: Authorization: Bearer <token>

请求参数:

参数名	类型	必填	说明
page	Integer	否	页码，从1开始，默认1
size	Integer	否	每页数量，默认10
status	String	否	状态过滤 (active/inactive/maintenance)
keyword	String	否	关键词搜索(匹配名称和位置)
响应示例:

{
  "code": 200,
  "message": "success",
  "data": {
    "content": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440001",
        "name": "主站监测点",
        "location": "北京市朝阳区",
        "latitude": 39.9042,
        "longitude": 116.4074,
        "description": "主要监测站点，位于朝阳区",
        "status": "active",
        "createdAt": "2024-01-01T00:00:00",
        "updatedAt": "2024-01-01T00:00:00"
      },
      {
        "id": "550e8400-e29b-41d4-a716-446655440002",
        "name": "副站监测点",
        "location": "北京市海淀区",
        "latitude": 39.9562,
        "longitude": 116.3105,
        "description": "辅助监测站点，位于海淀区",
        "status": "active",
        "createdAt": "2024-01-02T00:00:00",
        "updatedAt": "2024-01-02T00:00:00"
      }
    ],
    "totalElements": 5,
    "totalPages": 1,
    "currentPage": 1,
    "pageSize": 10
  },
  "timestamp": "2024-01-01T12:00:00"
}
12. 获取单个站点
接口路径: GET /stations/{id}

路径参数:

参数名	类型	必填	说明
id	String	是	站点ID (UUID)
响应示例:

{
  "code": 200,
  "message": "success",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "name": "主站监测点",
    "location": "北京市朝阳区",
    "latitude": 39.9042,
    "longitude": 116.4074,
    "description": "主要监测站点，位于朝阳区",
    "status": "active",
    "createdAt": "2024-01-01T00:00:00",
    "updatedAt": "2024-01-01T00:00:00"
  },
  "timestamp": "2024-01-01T12:00:00"
}
13. 创建站点
接口路径: POST /stations

请求头:

Authorization: Bearer <token>
Content-Type: application/json
请求体:

{
  "name": "新监测站",
  "location": "北京市海淀区",
  "latitude": 39.9562,
  "longitude": 116.3105,
  "description": "新建设的监测站点"
}
参数名	类型	必填	说明
name	String	是	站点名称
location	String	否	位置描述
latitude	BigDecimal	否	纬度
longitude	BigDecimal	否	经度
description	String	否	描述信息
响应示例:

{
  "code": 200,
  "message": "站点创建成功",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440006",
    "name": "新监测站",
    "location": "北京市海淀区",
    "latitude": 39.9562,
    "longitude": 116.3105,
    "description": "新建设的监测站点",
    "status": "active",
    "createdAt": "2024-01-01T12:00:00"
  },
  "timestamp": "2024-01-01T12:00:00"
}
权限说明: 需要 admin 或 operator 角色

14. 更新站点
接口路径: PUT /stations/{id}

路径参数:

参数名	类型	必填	说明
id	String	是	站点ID (UUID)
请求体:

{
  "name": "更新的站点名称",
  "location": "新地址",
  "latitude": 40.0000,
  "longitude": 116.5000,
  "description": "更新的描述信息",
  "status": "active"
}
响应示例:

{
  "code": 200,
  "message": "站点更新成功",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
权限说明: 需要 admin 或 operator 角色

15. 删除站点
接口路径: DELETE /stations/{id}

路径参数:

参数名	类型	必填	说明
id	String	是	站点ID (UUID)
响应示例:

{
  "code": 200,
  "message": "站点删除成功",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
权限说明: 需要 admin 角色

16. 获取站点统计
接口路径: GET /stations/statistics

请求头: Authorization: Bearer <token>

响应示例:

{
  "code": 200,
  "message": "success",
  "data": {
    "totalStations": 5,
    "activeStations": 4,
    "inactiveStations": 0,
    "maintenanceStations": 1
  },
  "timestamp": "2024-01-01T12:00:00"
}
水位监测模块 (Water Levels)
17. 获取最新水位数据
接口路径: GET /water-levels/latest

请求头: Authorization: Bearer <token>

请求参数:

参数名	类型	必填	说明
stationId	String	否	站点ID (不过滤则返回所有站点最新数据)
响应示例:

{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": "660e8400-e29b-41d4-a716-446655440001",
      "stationId": "550e8400-e29b-41d4-a716-446655440001",
      "stationName": "主站监测点",
      "currentLevel": 12.50,
      "warningLevel": 15.00,
      "dangerLevel": 18.00,
      "status": "normal",
      "recordedAt": "2024-01-01T12:00:00",
      "createdAt": "2024-01-01T12:00:00"
    },
    {
      "id": "660e8400-e29b-41d4-a716-446655440002",
      "stationId": "550e8400-e29b-41d4-a716-446655440002",
      "stationName": "副站监测点",
      "currentLevel": 10.20,
      "warningLevel": 14.00,
      "dangerLevel": 17.00,
      "status": "normal",
      "recordedAt": "2024-01-01T12:00:00",
      "createdAt": "2024-01-01T12:00:00"
    }
  ],
  "timestamp": "2024-01-01T12:00:00"
}
状态说明:

normal: 正常
warning: 警戒
danger: 危险
18. 获取站点历史水位数据
接口路径: GET /water-levels/station/{stationId}

路径参数:

参数名	类型	必填	说明
stationId	String	是	站点ID (UUID)
请求参数:

参数名	类型	必填	说明
page	Integer	否	页码，默认1
size	Integer	否	每页数量，默认100
startDate	DateTime	否	开始时间 (ISO8601格式: 2024-01-01T00:00:00)
endDate	DateTime	否	结束时间 (ISO8601格式)
sort	String	否	排序字段，默认recordedAt
order	String	否	排序方向 (asc/desc)，默认desc
响应示例:

{
  "code": 200,
  "message": "success",
  "data": {
    "content": [
      {
        "id": "660e8400-e29b-41d4-a716-446655440001",
        "stationId": "550e8400-e29b-41d4-a716-446655440001",
        "currentLevel": 12.50,
        "warningLevel": 15.00,
        "dangerLevel": 18.00,
        "status": "normal",
        "recordedAt": "2024-01-01T12:00:00",
        "createdAt": "2024-01-01T12:00:00"
      },
      {
        "id": "660e8400-e29b-41d4-a716-446655440002",
        "stationId": "550e8400-e29b-41d4-a716-446655440001",
        "currentLevel": 12.45,
        "warningLevel": 15.00,
        "dangerLevel": 18.00,
        "status": "normal",
        "recordedAt": "2024-01-01T11:00:00",
        "createdAt": "2024-01-01T11:00:00"
      }
    ],
    "totalElements": 100,
    "totalPages": 1,
    "currentPage": 1,
    "pageSize": 100
  },
  "timestamp": "2024-01-01T12:00:00"
}
19. 获取最新一条水位记录
接口路径: GET /water-levels/latest/one

请求头: Authorization: Bearer <token>

响应示例:

{
  "code": 200,
  "message": "success",
  "data": {
    "id": "660e8400-e29b-41d4-a716-446655440001",
    "stationId": "550e8400-e29b-41d4-a716-446655440001",
    "currentLevel": 12.50,
    "warningLevel": 15.00,
    "dangerLevel": 18.00,
    "status": "normal",
    "recordedAt": "2024-01-01T12:00:00",
    "createdAt": "2024-01-01T12:00:00"
  },
  "timestamp": "2024-01-01T12:00:00"
}
20. 添加水位记录
接口路径: POST /water-levels

请求头:

Authorization: Bearer <token>
Content-Type: application/json
请求体:

{
  "stationId": "550e8400-e29b-41d4-a716-446655440001",
  "currentLevel": 12.5,
  "warningLevel": 15.0,
  "dangerLevel": 18.0,
  "recordedAt": "2024-01-01T12:00:00"
}
参数名	类型	必填	说明
stationId	String	是	站点ID (UUID)
currentLevel	BigDecimal	是	当前水位 (米)
warningLevel	BigDecimal	否	警戒水位 (米)
dangerLevel	BigDecimal	否	危险水位 (米)
recordedAt	DateTime	否	记录时间，默认当前时间
响应示例:

{
  "code": 200,
  "message": "水位记录添加成功",
  "data": {
    "id": "660e8400-e29b-41d4-a716-446655440001",
    "stationId": "550e8400-e29b-41d4-a716-446655440001",
    "currentLevel": 12.5,
    "warningLevel": 15.0,
    "dangerLevel": 18.0,
    "status": "normal",
    "recordedAt": "2024-01-01T12:00:00",
    "createdAt": "2024-01-01T12:00:00"
  },
  "timestamp": "2024-01-01T12:00:00"
}
权限说明: 需要 admin 或 operator 角色

21. 获取水位统计
接口路径: GET /water-levels/statistics

请求头: Authorization: Bearer <token>

请求参数:

参数名	类型	必填	说明
stationId	String	否	站点ID
startDate	DateTime	否	开始时间
endDate	DateTime	否	结束时间
响应示例:

{
  "code": 200,
  "message": "success",
  "data": {
    "avgLevel": 10.5,
    "maxLevel": 18.2,
    "minLevel": 5.3,
    "normalCount": 100,
    "warningCount": 5,
    "dangerCount": 2,
    "totalRecords": 107
  },
  "timestamp": "2024-01-01T12:00:00"
}
流量监测模块 (Flow Monitoring)
22. 获取最新流量数据
接口路径: GET /flow-monitoring/latest

请求头: Authorization: Bearer <token>

请求参数:

参数名	类型	必填	说明
stationId	String	否	站点ID
响应示例:

{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": "770e8400-e29b-41d4-a716-446655440001",
      "stationId": "550e8400-e29b-41d4-a716-446655440001",
      "stationName": "主站监测点",
      "flowRate": 150.25,
      "velocity": 2.5,
      "status": "normal",
      "recordedAt": "2024-01-01T12:00:00",
      "createdAt": "2024-01-01T12:00:00"
    }
  ],
  "timestamp": "2024-01-01T12:00:00"
}
字段说明:

字段名	说明
flowRate	流量 (立方米/秒)
velocity	流速 (米/秒)
23. 获取站点历史流量数据
接口路径: GET /flow-monitoring/station/{stationId}

路径参数:

参数名	类型	必填	说明
stationId	String	是	站点ID (UUID)
请求参数: 同水位监测接口 (18)

响应示例:

{
  "code": 200,
  "message": "success",
  "data": {
    "content": [
      {
        "id": "770e8400-e29b-41d4-a716-446655440001",
        "stationId": "550e8400-e29b-41d4-a716-446655440001",
        "flowRate": 150.25,
        "velocity": 2.5,
        "status": "normal",
        "recordedAt": "2024-01-01T12:00:00",
        "createdAt": "2024-01-01T12:00:00"
      }
    ],
    "totalElements": 50,
    "totalPages": 1,
    "currentPage": 1,
    "pageSize": 50
  },
  "timestamp": "2024-01-01T12:00:00"
}
24. 获取最新一条流量记录
接口路径: GET /flow-monitoring/latest/one

请求头: Authorization: Bearer <token>

响应示例: 同最新流量数据接口 (22)，返回单条记录

25. 添加流量记录
接口路径: POST /flow-monitoring

请求头:

Authorization: Bearer <token>
Content-Type: application/json
请求体:

{
  "stationId": "550e8400-e29b-41d4-a716-446655440001",
  "flowRate": 150.25,
  "velocity": 2.5,
  "recordedAt": "2024-01-01T12:00:00"
}
参数名	类型	必填	说明
stationId	String	是	站点ID (UUID)
flowRate	BigDecimal	是	流量 (立方米/秒)
velocity	BigDecimal	否	流速 (米/秒)
recordedAt	DateTime	否	记录时间
响应示例:

{
  "code": 200,
  "message": "流量记录添加成功",
  "data": {
    "id": "770e8400-e29b-41d4-a716-446655440001",
    "stationId": "550e8400-e29b-41d4-a716-446655440001",
    "flowRate": 150.25,
    "velocity": 2.5,
    "status": "normal",
    "recordedAt": "2024-01-01T12:00:00",
    "createdAt": "2024-01-01T12:00:00"
  },
  "timestamp": "2024-01-01T12:00:00"
}
权限说明: 需要 admin 或 operator 角色

水质监测模块 (Water Quality)
26. 获取最新水质数据
接口路径: GET /water-quality/latest

请求头: Authorization: Bearer <token>

请求参数:

参数名	类型	必填	说明
stationId	String	否	站点ID
响应示例:

{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": "880e8400-e29b-41d4-a716-446655440001",
      "stationId": "550e8400-e29b-41d4-a716-446655440001",
      "stationName": "主站监测点",
      "ph": 7.2,
      "dissolvedOxygen": 8.5,
      "turbidity": 3.2,
      "temperature": 22.5,
      "conductivity": 450.0,
      "status": "normal",
      "recordedAt": "2024-01-01T12:00:00",
      "createdAt": "2024-01-01T12:00:00"
    }
  ],
  "timestamp": "2024-01-01T12:00:00"
}
字段说明:

字段名	单位	说明
ph	-	pH值 (0-14)
dissolvedOxygen	mg/L	溶解氧
turbidity	NTU	浊度
temperature	℃	温度
conductivity	μS/cm	电导率
27. 获取站点历史水质数据
接口路径: GET /water-quality/station/{stationId}

路径参数:

参数名	类型	必填	说明
stationId	String	是	站点ID (UUID)
请求参数: 同水位监测接口 (18)

响应示例:

{
  "code": 200,
  "message": "success",
  "data": {
    "content": [
      {
        "id": "880e8400-e29b-41d4-a716-446655440001",
        "stationId": "550e8400-e29b-41d4-a716-446655440001",
        "ph": 7.2,
        "dissolvedOxygen": 8.5,
        "turbidity": 3.2,
        "temperature": 22.5,
        "conductivity": 450.0,
        "status": "normal",
        "recordedAt": "2024-01-01T12:00:00",
        "createdAt": "2024-01-01T12:00:00"
      }
    ],
    "totalElements": 30,
    "totalPages": 1,
    "currentPage": 1,
    "pageSize": 30
  },
  "timestamp": "2024-01-01T12:00:00"
}
28. 添加水质记录
接口路径: POST /water-quality

请求头:

Authorization: Bearer <token>
Content-Type: application/json
请求体:

{
  "stationId": "550e8400-e29b-41d4-a716-446655440001",
  "ph": 7.2,
  "dissolvedOxygen": 8.5,
  "turbidity": 3.2,
  "temperature": 22.5,
  "conductivity": 450.0,
  "recordedAt": "2024-01-01T12:00:00"
}
参数名	类型	必填	说明
stationId	String	是	站点ID (UUID)
ph	BigDecimal	否	pH值
dissolvedOxygen	BigDecimal	否	溶解氧 (mg/L)
turbidity	BigDecimal	否	浊度 (NTU)
temperature	BigDecimal	否	温度 (℃)
conductivity	BigDecimal	否	电导率 (μS/cm)
recordedAt	DateTime	否	记录时间
响应示例:

{
  "code": 200,
  "message": "水质记录添加成功",
  "data": {
    "id": "880e8400-e29b-41d4-a716-446655440001",
    "stationId": "550e8400-e29b-41d4-a716-446655440001",
    "ph": 7.2,
    "dissolvedOxygen": 8.5,
    "turbidity": 3.2,
    "temperature": 22.5,
    "conductivity": 450.0,
    "status": "normal",
    "recordedAt": "2024-01-01T12:00:00",
    "createdAt": "2024-01-01T12:00:00"
  },
  "timestamp": "2024-01-01T12:00:00"
}
权限说明: 需要 admin 或 operator 角色

警报管理模块 (Alarms)
29. 获取警报列表
接口路径: GET /alarms

请求头: Authorization: Bearer <token>

请求参数:

参数名	类型	必填	说明
page	Integer	否	页码，默认1
size	Integer	否	每页数量，默认10
status	String	否	状态 (active/resolved)
severity	String	否	严重程度 (low/medium/high/critical)
stationId	String	否	站点ID
startDate	DateTime	否	开始时间
endDate	DateTime	否	结束时间
响应示例:

{
  "code": 200,
  "message": "success",
  "data": {
    "content": [
      {
        "id": "990e8400-e29b-41d4-a716-446655440001",
        "stationId": "550e8400-e29b-41d4-a716-446655440001",
        "stationName": "主站监测点",
        "alarmType": "water_level_warning",
        "severity": "high",
        "message": "水位超过警戒线，当前水位：15.5m，警戒水位：15.0m",
        "status": "active",
        "createdAt": "2024-01-01T12:00:00",
        "resolvedAt": null,
        "resolvedBy": null
      },
      {
        "id": "990e8400-e29b-41d4-a716-446655440002",
        "stationId": "550e8400-e29b-41d4-a716-446655440002",
        "stationName": "副站监测点",
        "alarmType": "water_quality_abnormal",
        "severity": "medium",
        "message": "水质异常，pH值：6.2，超出正常范围",
        "status": "resolved",
        "createdAt": "2024-01-01T10:00:00",
        "resolvedAt": "2024-01-01T11:00:00",
        "resolvedBy": {
          "id": 2,
          "username": "operator1",
          "realName": "操作员1"
        }
      }
    ],
    "totalElements": 20,
    "totalPages": 2,
    "currentPage": 1,
    "pageSize": 10
  },
  "timestamp": "2024-01-01T12:00:00"
}
警报类型说明:

类型	说明
water_level_warning	水位警戒
water_level_danger	水位危险
water_quality_abnormal	水质异常
flow_abnormal	流量异常
equipment_failure	设备故障
严重程度说明:

程度	说明
low	低
medium	中
high	高
critical	严重
30. 获取活跃警报数量
接口路径: GET /alarms/active/count

请求头: Authorization: Bearer <token>

响应示例:

{
  "code": 200,
  "message": "success",
  "data": 5,
  "timestamp": "2024-01-01T12:00:00"
}
31. 获取单个警报
接口路径: GET /alarms/{id}

路径参数:

参数名	类型	必填	说明
id	String	是	警报ID (UUID)
响应示例:

{
  "code": 200,
  "message": "success",
  "data": {
    "id": "990e8400-e29b-41d4-a716-446655440001",
    "stationId": "550e8400-e29b-41d4-a716-446655440001",
    "stationName": "主站监测点",
    "alarmType": "water_level_warning",
    "severity": "high",
    "message": "水位超过警戒线，当前水位：15.5m，警戒水位：15.0m",
    "status": "active",
    "createdAt": "2024-01-01T12:00:00",
    "resolvedAt": null,
    "resolvedBy": null
  },
  "timestamp": "2024-01-01T12:00:00"
}
32. 解决警报
接口路径: PUT /alarms/{id}/resolve

路径参数:

参数名	类型	必填	说明
id	String	是	警报ID (UUID)
请求头: Authorization: Bearer <token>

响应示例:

{
  "code": 200,
  "message": "警报已解决",
  "data": {
    "id": "990e8400-e29b-41d4-a716-446655440001",
    "status": "resolved",
    "resolvedAt": "2024-01-01T12:30:00"
  },
  "timestamp": "2024-01-01T12:30:00"
}
权限说明: 需要 admin 或 operator 角色

33. 获取警报统计
接口路径: GET /alarms/statistics

请求头: Authorization: Bearer <token>

请求参数: 同4.5

响应示例:

{
  "code": 200,
  "message": "success",
  "data": {
    "totalAlarms": 50,
    "activeAlarms": 5,
    "resolvedAlarms": 45,
    "criticalAlarms": 2,
    "highAlarms": 10,
    "mediumAlarms": 25,
    "lowAlarms": 13
  },
  "timestamp": "2024-01-01T12:00:00"
}
数据导出模块 (Export)
34. 导出水位数据
接口路径: POST /export/water-levels

请求头:

Authorization: Bearer <token>
Content-Type: application/json
请求体:

{
  "stationId": "550e8400-e29b-41d4-a716-446655440001",
  "startDate": "2024-01-01T00:00:00",
  "endDate": "2024-01-31T23:59:59",
  "format": "excel",
  "includeChart": false
}
参数名	类型	必填	说明
stationId	String	否	站点ID (不传则导出所有站点)
startDate	DateTime	是	开始时间
endDate	DateTime	是	结束时间
format	String	否	导出格式 (excel/csv)，默认excel
includeChart	Boolean	否	是否包含图表数据，默认false
响应: 文件下载

Excel格式: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
CSV格式: text/csv
响应头:

Content-Disposition: attachment; filename=water-levels-2024-01-01-to-2024-01-31.xlsx
Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
Excel文件结构:

站点名称	站点位置	当前水位(m)	警戒水位(m)	危险水位(m)	状态	记录时间
主站监测点	北京市朝阳区	12.50	15.00	18.00	normal	2024-01-01 12:00:00
权限说明: 需要 admin 或 operator 角色

35. 导出流量数据
接口路径: POST /export/flow-monitoring

请求头:

Authorization: Bearer <token>
Content-Type: application/json
请求体:

{
  "stationId": "550e8400-e29b-41d4-a716-446655440001",
  "startDate": "2024-01-01T00:00:00",
  "endDate": "2024-01-31T23:59:59",
  "format": "excel"
}
Excel文件结构:

站点名称	站点位置	流量(m³/s)	流速(m/s)	状态	记录时间
主站监测点	北京市朝阳区	150.25	2.5	normal	2024-01-01 12:00:00
权限说明: 需要 admin 或 operator 角色

36. 导出水质数据
接口路径: POST /export/water-quality

请求头:

Authorization: Bearer <token>
Content-Type: application/json
请求体:

{
  "stationId": "550e8400-e29b-41d4-a716-446655440001",
  "startDate": "2024-01-01T00:00:00",
  "endDate": "2024-01-31T23:59:59",
  "format": "excel"
}
Excel文件结构:

站点名称	站点位置	pH值	溶解氧(mg/L)	浊度(NTU)	温度(℃)	电导率(μS/cm)	状态	记录时间
主站监测点	北京市朝阳区	7.2	8.5	3.2	22.5	450.0	normal	2024-01-01 12:00:00
权限说明: 需要 admin 或 operator 角色

37. 导出警报数据
接口路径: POST /export/alarms

请求头:

Authorization: Bearer <token>
Content-Type: application/json
请求体:

{
  "status": "all",
  "severity": "all",
  "stationId": "550e8400-e29b-41d4-a716-446655440001",
  "startDate": "2024-01-01T00:00:00",
  "endDate": "2024-01-31T23:59:59",
  "format": "excel"
}
参数名	类型	必填	说明
status	String	否	状态过滤 (all/active/resolved)，默认all
severity	String	否	严重程度过滤 (all/critical/high/medium/low)，默认all
stationId	String	否	站点ID
startDate	DateTime	是	开始时间
endDate	DateTime	是	结束时间
format	String	否	格式 (excel/csv)，默认excel
Excel文件结构:

站点名称	警报类型	严重程度	警报信息	状态	创建时间	解决时间	解决人
主站监测点	water_level_warning	high	水位超过警戒线	resolved	2024-01-01 10:00:00	2024-01-01 11:00:00	操作员1
权限说明: 需要 admin 或 operator 角色

38. 导出综合报告
接口路径: POST /export/report

请求头:

Authorization: Bearer <token>
Content-Type: application/json
请求体:

{
  "stationId": "550e8400-e29b-41d4-a716-446655440001",
  "startDate": "2024-01-01T00:00:00",
  "endDate": "2024-01-31T23:59:59",
  "includeWaterLevel": true,
  "includeFlowMonitoring": true,
  "includeWaterQuality": true,
  "includeAlarms": true,
  "format": "excel"
}
参数名	类型	必填	说明
stationId	String	否	站点ID
startDate	DateTime	是	开始时间
endDate	DateTime	是	结束时间
includeWaterLevel	Boolean	否	是否包含水位数据，默认true
includeFlowMonitoring	Boolean	否	是否包含流量数据，默认true
includeWaterQuality	Boolean	否	是否包含水质数据，默认true
includeAlarms	Boolean	否	是否包含警报数据，默认true
format	String	否	格式 (excel/pdf)，默认excel
Excel文件结构 (多个Sheet):

Sheet 1: 汇总信息
Sheet 2: 水位数据
Sheet 3: 流量数据
Sheet 4: 水质数据
Sheet 5: 警报记录
权限说明: 需要 admin 或 operator 角色

角色权限矩阵
接口	admin	operator	viewer
登录/登出	✅	✅	✅
获取用户列表	✅	❌	❌
用户CRUD	✅	❌	❌
站点列表/详情	✅	✅	✅
站点创建/更新/删除	✅	✅	❌
水位数据操作	✅	✅	✅
流量数据操作	✅	✅	✅
水质数据操作	✅	✅	✅
警报操作	✅	✅	❌
数据导出	✅	✅	❌
附录
A. 错误码详细说明
错误码	错误信息	说明
400	请求参数错误	参数格式不正确或缺少必填参数
401	未登录或Token已过期	Token无效或已过期
403	无权限访问	没有操作权限
404	资源不存在	指定的资源ID不存在
500	服务器内部错误	服务器异常
B. 日期时间格式
所有日期时间使用 ISO 8601 格式：

2024-01-01T12:00:00
或带时区：

2024-01-01T12:00:00+08:00
C. 数据类型定义
站点状态:

active: 活跃
inactive: 停用
maintenance: 维护中
警报状态:

active: 未解决
resolved: 已解决
用户角色:

admin: 管理员
operator: 操作员
viewer: 访客
技术支持
如有问题，请联系系统管理员或查看在线文档。

在线API文档: http://localhost:8080/swagger-ui.html

接口统计
模块	接口数量
认证模块	4
用户管理模块	6
监测站点模块	6
水位监测模块	5
流量监测模块	4
水质监测模块	3
警报管理模块	5
数据导出模块	5
总计	38
文档版本: 1.0 最后更新: 2024年1月
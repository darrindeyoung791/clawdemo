## Why

实验室设备管理目前依赖人工登记，借还记录混乱、审批无迹可查。需要一个轻量系统让用户在线查询设备、提交借还申请，管理员统一审批和管理设备。

## What Changes

- 新增用户注册与登录功能，区分普通用户和管理员角色
- 新增设备目录管理，管理员可增删设备，支持分类和搜索
- 新增设备借还流程：用户提交申请 → 管理员审批 → 用户确认归还
- 新增完整的前端页面：登录页、设备列表、借还操作、审批管理、设备管理后台
- 所有列表和搜索页支持关键字搜索

## Capabilities

### New Capabilities
- `user-auth`: 用户注册、登录、角色权限控制（普通用户/管理员）
- `equipment-catalog`: 设备信息管理、分类枚举、搜索、状态跟踪
- `borrow-return`: 借用申请、审批流转、归还确认、记录查询

### Modified Capabilities
<!-- none -->

## Impact

- 后端新增 Entity / Mapper / Service / Controller 三层结构
- 新增 4 张数据库表：user、equipment、borrow_record、approval_record（可合并）
- 前端新增 Vue 3 项目，含登录、设备、借还、管理 4 个主要页面
- 新增 `src/main/resources/application.properties` 中 MySQL 数据源配置
- 新增建表 SQL 脚本

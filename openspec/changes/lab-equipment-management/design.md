## Context

Spring Boot + Vue 3 全栈项目，目前只有脚手架代码。需要从零搭建实验室设备管理系统，含用户认证、设备管理、借还审批三个模块。

## Goals / Non-Goals

**Goals:**
- 完整的用户注册登录 + 角色权限（普通用户/管理员）
- 设备 CRUD + 分类枚举 + 关键字搜索
- 借还审批流程（提交→审批→借出→归还）
- 响应式 MD3 风格前端

**Non-Goals:**
- 不做消息队列，审批通知用同步响应
- 不做邮件/短信通知
- 不做多语言

## Decisions

### 后端架构
- 标准三层：Controller → Service → Mapper (MyBatis-Plus)
- 使用 MyBatis-Plus 而非 JPA，减少样板代码
- Spring Security 做认证 + 角色鉴权（`ROLE_USER` / `ROLE_ADMIN`）
- JWT Token 做无状态登录

### 数据库设计
- `user`：id, username, password, display_name, role, created_at, updated_at
- `equipment`：id, name, code, category (enum: INSTRUMENT/CONSUMABLE/SOFTWARE/OTHER), location, total_quantity, available_quantity, status (AVAILABLE/MAINTENANCE/RETIRED), created_at, updated_at
- `borrow_record`：id, user_id, equipment_id, quantity, start_time, end_time, status (PENDING/APPROVED/REJECTED/RETURNED), reject_reason, created_at, updated_at

### 前端架构
- Vue 3 + Vite + Vue Router + Pinia
- 页面：登录注册 / 设备列表 / 我的借还 / 审批管理 / 设备管理（admin）
- 直接通过 `<link>` 加载 `css/*.css`，通过 class `.light`/`.dark` 切换主题
- 所有列表页支持搜索

### 审批流程
- 提交借出 → 状态 PENDING → 管理员审批通过 → APPROVED（扣减设备可用数量）→ 用户还回 → RETURNED（恢复设备数量）
- 管理员拒绝 → REJECTED（填写原因）

## Risks / Trade-offs

- 无消息队列：审批无实时通知，用户需手动刷新页面查看状态
- JWT 无 refresh token 机制：简化实现，token 过期需重新登录
- MyBatis-Plus 自动生成基础 CRUD，复杂统计查询需手写 XML

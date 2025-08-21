# 認證客戶端首頁

這是一個基於 Spring Boot 和 OAuth2 的認證客戶端應用程式，具有現代化的響應式首頁設計。

## 功能特色

### 🎨 現代化設計
- **響應式佈局**：支援桌面、平板和手機設備
- **漸變背景**：美觀的紫色漸變背景
- **毛玻璃效果**：使用 backdrop-filter 實現現代化視覺效果
- **動畫效果**：平滑的淡入動畫和懸停效果

### 🔐 認證功能
- **OAuth2 登入**：支援 OpenID Connect 標準
- **用戶資料顯示**：顯示登入用戶的基本信息
- **個人資料頁面**：詳細的用戶信息和令牌顯示
- **安全登出**：安全的登出功能

### 📱 用戶體驗
- **直觀導航**：清晰的導航欄和麵包屑
- **狀態指示**：登入/登出狀態的視覺指示
- **複製功能**：一鍵複製令牌和聲明信息
- **錯誤處理**：友好的錯誤頁面

## 頁面結構

### 首頁 (`/`)
- 歡迎區域和登入按鈕
- 功能特色展示
- 統計數據展示
- 用戶信息卡片（登入後顯示）

### 個人資料頁面 (`/profile`)
- 用戶基本信息
- OpenID Connect 聲明
- JWT 令牌顯示
- 複製功能

### 錯誤頁面
- 友好的錯誤信息顯示
- 返回首頁和上一頁選項

## 技術棧

- **後端**：Spring Boot 3.5.4
- **安全**：Spring Security + OAuth2 Client
- **模板引擎**：Thymeleaf
- **前端框架**：Bootstrap 5.3.0
- **圖標**：Font Awesome 6.4.0
- **動畫**：CSS3 動畫和過渡效果

## 快速開始

### 1. 啟動應用程式
```bash
mvn spring-boot:run
```

### 2. 訪問首頁
打開瀏覽器訪問：`http://localhost:8080`

### 3. 登入
點擊「立即登入」按鈕進行 OAuth2 認證

## 配置說明

### OAuth2 配置
在 `application.yml` 中配置 OAuth2 客戶端：

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          oidc-client:
            client-id: oidc-client
            client-secret: secret
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
            scope: 
              - openid
              - profile
        provider:
          auth-server:
            issuer-uri: http://localhost:9000
```

### 安全配置
- 首頁和登入頁面允許匿名訪問
- 其他頁面需要認證
- 使用 cookie 儲存授權請求

## 自定義樣式

### CSS 變數
在 `src/main/resources/static/css/style.css` 中定義了主要的顏色變數：

```css
:root {
    --primary-color: #4f46e5;
    --secondary-color: #7c3aed;
    --accent-color: #06b6d4;
    --success-color: #10b981;
    --warning-color: #f59e0b;
    --danger-color: #ef4444;
    --dark-color: #1f2937;
    --light-color: #f8fafc;
}
```

### 動畫效果
- 淡入動畫：`fadeInUp`
- 脈衝動畫：`pulse`
- 浮動動畫：`float`

## 響應式設計

### 斷點
- **桌面**：≥992px
- **平板**：768px - 991px
- **手機**：<768px

### 適配特性
- 流體網格系統
- 響應式字體大小
- 觸控友好的按鈕
- 折疊式導航欄

## 無障礙支援

- **鍵盤導航**：支援 Tab 鍵導航
- **焦點指示**：清晰的焦點樣式
- **減少動畫**：支援 `prefers-reduced-motion`
- **深色模式**：支援 `prefers-color-scheme: dark`

## 瀏覽器支援

- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+

## 開發指南

### 添加新頁面
1. 在 `HomeController` 中添加新的映射
2. 在 `templates` 目錄中創建對應的 HTML 模板
3. 更新導航欄（如需要）

### 修改樣式
1. 編輯 `src/main/resources/static/css/style.css`
2. 或修改模板中的內聯樣式
3. 重新啟動應用程式以查看更改

### 添加新功能
1. 在控制器中添加新的端點
2. 創建對應的模板
3. 更新安全配置（如需要）

## 故障排除

### 常見問題

**Q: 登入按鈕不工作？**
A: 檢查 OAuth2 配置和認證服務器是否正在運行

**Q: 樣式沒有載入？**
A: 確保靜態資源路徑正確，檢查瀏覽器開發者工具

**Q: 動畫效果不顯示？**
A: 檢查瀏覽器是否支援 CSS3 動畫，或禁用減少動畫設置

## 貢獻

歡迎提交 Issue 和 Pull Request 來改進這個專案！

## 授權

本專案採用 MIT 授權條款。

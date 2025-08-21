# 自訂登出頁面配置說明

## 功能概述

本專案實現了完整的自訂登出功能，包括：

1. **登出確認頁面** - 用戶在登出前需要確認
2. **登出成功頁面** - 顯示登出成功信息和選項
3. **自訂登出處理器** - 處理登出邏輯和清理工作
4. **安全配置** - 確保登出過程的安全性

## 頁面流程

```
用戶點擊登出 → 登出確認頁面 → 確認登出 → 登出成功頁面
```

## 文件結構

```
src/main/java/com/niqdev/authclient/
├── controller/
│   └── HomeController.java          # 處理登出相關頁面
└── config/
    ├── SecurityConfig.java          # 安全配置
    └── CustomLogoutSuccessHandler.java  # 自訂登出處理器

src/main/resources/templates/
├── logout-confirm.html              # 登出確認頁面
└── logout-success.html              # 登出成功頁面
```

## 配置詳情

### 1. 安全配置 (SecurityConfig.java)

```java
.logout(logout -> logout
    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
    .logoutSuccessHandler(customLogoutSuccessHandler)
    .invalidateHttpSession(true)
    .deleteCookies("JSESSIONID")
    .clearAuthentication(true)
)
```

**配置說明：**
- `logoutRequestMatcher`: 設定登出請求的 URL 模式
- `logoutSuccessHandler`: 使用自訂的登出成功處理器
- `invalidateHttpSession`: 使會話失效
- `deleteCookies`: 刪除相關的 cookie
- `clearAuthentication`: 清除認證信息

### 2. 自訂登出處理器 (CustomLogoutSuccessHandler.java)

```java
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, 
                               HttpServletResponse response, 
                               Authentication authentication) {
        // 記錄登出事件
        // 清除 cookie
        // 重定向到登出成功頁面
    }
}
```

**功能：**
- 記錄用戶登出事件
- 清除所有相關的 cookie
- 重定向到登出成功頁面

### 3. 控制器方法 (HomeController.java)

```java
@GetMapping("/logout-success")
public String logoutSuccess(Model model) {
    model.addAttribute("logoutMessage", "您已成功登出");
    model.addAttribute("logoutTime", java.time.LocalDateTime.now());
    return "logout-success";
}

@GetMapping("/logout-confirm")
public String logoutConfirm(@AuthenticationPrincipal OidcUser oidcUser, Model model) {
    if (oidcUser != null) {
        model.addAttribute("user", oidcUser);
        return "logout-confirm";
    }
    return "redirect:/";
}
```

## 頁面特色

### 登出確認頁面 (logout-confirm.html)

- **警告提示**：清楚說明登出的後果
- **確認按鈕**：使用危險色調的確認按鈕
- **取消選項**：允許用戶取消登出操作
- **響應式設計**：支援各種設備

### 登出成功頁面 (logout-success.html)

- **成功圖標**：使用動畫效果的成功圖標
- **時間顯示**：顯示登出時間
- **安全提醒**：提醒用戶會話已安全終止
- **自動重定向**：10秒後自動返回首頁
- **操作選項**：返回首頁或重新登入

## 安全特性

### 1. 會話清理
- 使當前會話失效
- 清除認證信息
- 刪除相關 cookie

### 2. Cookie 清理
```java
String[] cookiesToDelete = {"JSESSIONID", "remember-me", "oauth2_auth_request"};
```

### 3. 訪問控制
- 登出確認頁面需要認證
- 登出成功頁面允許匿名訪問

## 自定義選項

### 1. 修改自動重定向時間
在 `logout-success.html` 中修改：
```javascript
let countdown = 10; // 改為您想要的秒數
```

### 2. 添加更多清理邏輯
在 `CustomLogoutSuccessHandler` 中添加：
```java
// 清除其他 cookie
// 記錄到日誌系統
// 發送通知
```

### 3. 修改頁面樣式
- 編輯 CSS 變數來改變顏色主題
- 修改動畫效果
- 調整響應式佈局

## 測試方法

1. **啟動應用程式**
   ```bash
   mvn spring-boot:run
   ```

2. **登入測試**
   - 訪問首頁
   - 點擊登入按鈕
   - 完成 OAuth2 認證

3. **登出測試**
   - 點擊導航欄的登出按鈕
   - 在確認頁面點擊「確認登出」
   - 查看登出成功頁面
   - 測試自動重定向功能

## 故障排除

### 常見問題

**Q: 登出後仍然可以訪問受保護的頁面？**
A: 檢查 `invalidateHttpSession(true)` 和 `clearAuthentication(true)` 是否正確配置

**Q: Cookie 沒有被清除？**
A: 確保在 `CustomLogoutSuccessHandler` 中正確設置 cookie 的 path 和 maxAge

**Q: 登出確認頁面無法訪問？**
A: 檢查安全配置中的 `requestMatchers("/logout-confirm").authenticated()`

## 擴展功能

### 1. 添加登出原因選擇
```html
<select name="logoutReason">
    <option value="user">用戶主動登出</option>
    <option value="timeout">會話超時</option>
    <option value="security">安全原因</option>
</select>
```

### 2. 添加登出統計
```java
// 記錄登出統計信息
logoutStatisticsService.recordLogout(userId, logoutReason, logoutTime);
```

### 3. 添加郵件通知
```java
// 發送登出通知郵件
emailService.sendLogoutNotification(userEmail, logoutTime, deviceInfo);
```

## 最佳實踐

1. **安全性**：確保所有認證信息都被正確清除
2. **用戶體驗**：提供清晰的確認和反饋
3. **可訪問性**：支援鍵盤導航和螢幕閱讀器
4. **響應式設計**：在所有設備上都有良好的體驗
5. **性能**：避免不必要的重定向和請求

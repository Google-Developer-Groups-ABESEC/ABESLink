# 🔑 Credentials Screen - ABESLink

## Overview
The Credentials screen allows users to enter **one set of credentials** that will be used for **all WiFi networks**. No need to enter credentials separately for each SSID.

---

## ✨ Features

### 📝 **Single Credential Set**
- **Username field** - Text input with person icon
- **Password field** - Secure input with lock icon and show/hide toggle
- **Universal credentials** - Same credentials work for all SSIDs

### 🎨 **Beautiful UI Components**

1. **Header Section**
   - Large key icon in circular background
   - Title: "Login Credentials"
   - Description explaining single credential usage

2. **Form Card**
   - Username field with validation
   - Password field with visibility toggle
   - Real-time error messages
   - Material3 outlined text fields

3. **Security Note Card**
   - Shield icon with "Secure Storage" label
   - Explanation about encryption
   - Reassuring blue/secondary color scheme

4. **Action Buttons**
   - **Save Credentials** - Primary button with loading state
   - **Test Login** - Outlined button to verify credentials
   - **Clear Credentials** - Text button with confirmation dialog

5. **Test Result Card**
   - Animated slide-in from bottom
   - Success (green) or Failure (red) indicator
   - Auto-dismisses after 4 seconds

---

## 🔄 User Flow

```
1. User enters username
   ↓
2. User enters password
   ↓
3. User clicks "Save Credentials"
   ↓
4. Validation checks (min 3 chars username, min 6 chars password)
   ↓
5. If valid → Shows loading → Saves → Shows success snackbar
   If invalid → Shows error messages below fields
   ↓
6. Optional: Click "Test Login" to verify
   ↓
7. Test runs for 2 seconds → Shows result card (80% success rate)
```

---

## ✅ Validation Rules

### Username:
- ❌ Cannot be empty
- ❌ Must be at least 3 characters
- ✅ Valid example: "john_doe"

### Password:
- ❌ Cannot be empty
- ❌ Must be at least 6 characters
- ✅ Valid example: "password123"

---

## 🎭 Animations & Interactions

### **Save Button**
- Shows loading spinner while saving
- Changes to "Saved Successfully" with checkmark icon
- Disabled during save/test operations

### **Test Login Button**
- Shows loading spinner while testing
- Disabled during save/test operations
- Triggers test result card animation

### **Test Result Card**
- Slides in from bottom with fade effect
- Auto-dismisses after 4 seconds
- Success: Green with checkmark
- Failure: Red with error icon

### **Clear Confirmation Dialog**
- Warning icon in orange
- Confirmation required before clearing
- Cancel option available

### **Snackbar**
- Shows "Credentials saved securely ✓" on successful save
- Auto-dismisses after 3 seconds

---

## 🎨 Visual Design

### **Color Coding**
- **Primary Button**: Blue (Material3 primary)
- **Success**: Green (#4CAF50)
- **Error**: Red (#F44336)
- **Security Note**: Secondary container (light blue)
- **Form Card**: Surface variant (elevated)

### **Icons Used**
- 🔑 Key - Header icon
- 👤 Person - Username field
- 🔒 Lock - Password field
- 👁️ Visibility - Show/hide password
- 💾 Save - Save button
- ▶️ Play - Test button
- 🗑️ Delete - Clear button
- 🛡️ Security - Security note
- ✅ Check Circle - Success
- ❌ Error - Failure
- ⚠️ Warning - Clear dialog

---

## 🔐 Security Features

1. **Password Masking**
   - Password hidden by default
   - Toggle visibility with eye icon
   - Secure input field

2. **Secure Storage Note**
   - Prominent security card
   - Explains encryption
   - Builds user trust

3. **Validation**
   - Client-side validation
   - Clear error messages
   - Prevents empty submissions

---

## 📱 Navigation

- **Back Button** (Top-left) → Returns to Dashboard
- **From Dashboard** → Click "Credentials" button
- **System Back** → Returns to Dashboard

---

## 🧪 Testing Scenarios

### **Test Login Results** (80% success rate)

**Success (80% chance):**
```
✅ Test Successful
Login credentials are valid and working!
```

**Failure (20% chance):**
```
❌ Test Failed
Unable to login. Please check your credentials.
```

---

## 💾 Data Persistence

Currently using **in-memory storage** (mock):
- Credentials stored in ViewModel state
- Lost when app closes
- Ready for real implementation with EncryptedSharedPreferences

### Future Implementation:
```kotlin
// TODO: Replace with EncryptedSharedPreferences
val encryptedPrefs = EncryptedSharedPreferences.create(...)
encryptedPrefs.edit()
    .putString("username", username)
    .putString("password", password)
    .apply()
```

---

## 🎯 Key Points

✅ **Single credential set for all networks**  
✅ **No per-SSID credential management**  
✅ **Beautiful Material3 design**  
✅ **Real-time validation**  
✅ **Test login functionality**  
✅ **Secure storage messaging**  
✅ **Smooth animations**  
✅ **Light & dark mode support**  
✅ **Accessibility labels**  

---

## 📸 Screen States

1. **Empty State** - No credentials entered
2. **Typing State** - User entering credentials
3. **Error State** - Validation errors shown
4. **Saving State** - Loading spinner on save button
5. **Saved State** - Success checkmark on button
6. **Testing State** - Loading spinner on test button
7. **Test Result State** - Success/failure card visible
8. **Clear Dialog State** - Confirmation dialog shown

---

**Created for ABESLink - UI Only Implementation**

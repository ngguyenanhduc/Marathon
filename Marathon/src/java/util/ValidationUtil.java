package util;

/**
 * Author: PhucNTHE173021
 *
 * Version: 1
 *
 * Last Update Date: 17/7/2026
 *
 * Purpose: validate du lieu tai khoan va ho so Runner
 */
public class ValidationUtil {

    private static final String USERNAME_PATTERN = "[A-Za-z0-9_]{4,50}";
    private static final String EMAIL_PATTERN
            = "[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+";
    private static final String PHONE_PATTERN = "[0-9+\\- ]{8,20}";

    private ValidationUtil() {
    }

    //validate toan bo du lieu tren form dang ky tai khoan Runner
    public static String validateRegistration(
            String username,
            String password,
            String confirmPassword,
            String fullName,
            String email,
            String phone) {

        if (username == null || !username.matches(USERNAME_PATTERN)) {
            return "Username phai co 4-50 ky tu, chi gom chu, so va dau _.";
        }

        if (password == null || password.length() < 6
                || password.length() > 255) {
            return "Mat khau phai co tu 6 den 255 ky tu.";
        }

        if (!password.equals(confirmPassword)) {
            return "Xac nhan mat khau khong trung khop.";
        }

        return validateProfile(fullName, email, phone);
    }

    //validate cac thong tin ca nhan Runner duoc phep cap nhat
    public static String validateProfile(
            String fullName,
            String email,
            String phone) {

        if (fullName == null || fullName.isBlank()
                || fullName.length() > 100) {
            return "Ho va ten la bat buoc va khong qua 100 ky tu.";
        }

        if (email == null || email.length() > 100
                || !email.matches(EMAIL_PATTERN)) {
            return "Email khong dung dinh dang.";
        }

        if (phone != null && !phone.isBlank()
                && !phone.matches(PHONE_PATTERN)) {
            return "So dien thoai phai co 8-20 ky tu so, +, - hoac khoang trang.";
        }

        return null;
    }
}

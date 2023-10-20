package edu.carroll.ifa.web.form;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdatePasswordForm {

    @NotNull
    @Size(min = 6, message = "Username must be at least 6 characters long")
    private String username;

    @NotNull
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String currentPassword;

    @NotNull
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String newPassword;

    @NotNull
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String confirmNewPassword;

    /**
     * Returns the currentPassword from the UpdatePasswordForm.
     * @return currentPassword
     */
    public String getCurrentPassword() {
        return currentPassword;
    }

    /**
     * Sets the currenPassword from the UpdatePasswordForm
     * @param currentPassword - currentPassword from the UpdatePasswordForm
     */
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    /**
     * Returns the newPassword from the UpdatePasswordForm.
     * @return newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Sets the newPassword from the UpdatePasswordForm
     * @param newPassword - newPassword from the UpdatePasswordForm
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * Returns the confirmNewPassword from the UpdatePasswordForm.
     * @return confirmNewPassword
     */
    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    /**
     * Sets the confirmNewPassword from the UpdatePasswordForm
     * @param confirmNewPassword - confirmNewPassword from the UpdatePasswordForm
     */
    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    /**
     * Returns the username from the UpdatePasswordForm.
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username from the UpdatePasswordForm
     * @param username - Username from the UpdatePasswordForm
     */
    public void setUsername(String username) {
        this.username = username;
    }


}



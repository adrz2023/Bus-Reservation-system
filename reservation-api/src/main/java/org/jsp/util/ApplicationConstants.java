package org.jsp.util;



	public interface ApplicationConstants {
		String ADMIN_VERIFY_LINK = "api/admin/activate?token=";
		String USER_VERIFY_LINK="api/user/verify-link?token";
		String ADMIN_RESET_PASSWORD_LINK="api/admin/verify-link?token";
		String USER_RESET_PASSWORD_LINK="api/user/verify-link?token";
}

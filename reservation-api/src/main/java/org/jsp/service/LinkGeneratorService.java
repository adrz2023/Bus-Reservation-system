package org.jsp.service;
import static org.jsp.util.ApplicationConstants.ADMIN_VERIFY_LINK;
import static org.jsp.util.ApplicationConstants.ADMIN_RESET_PASSWORD_LINK;

import org.jsp.dao.VendorDao;
import org.jsp.model.Vendor;
import org.jsp.util.AccountStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import net.bytebuddy.utility.RandomString;
@Service
public class LinkGeneratorService {

	@Autowired
	private VendorDao vendorDao;

	public String getActivationLink(Vendor admin, HttpServletRequest request) {
		admin.setToken(RandomString.make(4));
		admin.setStatus(AccountStatus.IN_ACTIVE.toString());
		vendorDao.saveAdmin(admin);
		String siteUrl = request.getRequestURL().toString();
		return siteUrl.replace(request.getServletPath(), ADMIN_VERIFY_LINK + admin.getToken());
	}
	
	
	
	public String getResetPasswordLink(Vendor admin, HttpServletRequest request) {
		admin.setToken(RandomString.make(5));
		vendorDao.saveAdmin(admin);
		String siteUrl=request.getRequestURL().toString();
		return siteUrl.replace(request.getServletPath(),ADMIN_RESET_PASSWORD_LINK+admin.getToken());
	}
}
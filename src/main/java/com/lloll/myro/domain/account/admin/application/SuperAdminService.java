package com.lloll.myro.domain.account.admin.application;

import com.lloll.myro.domain.account.admin.application.request.AddAdminRequest;
import com.lloll.myro.domain.account.admin.application.request.DeleteAdminRequest;
import com.lloll.myro.domain.account.admin.application.request.UpdateAdminRequest;
import com.lloll.myro.domain.account.admin.domain.Admin;
import java.util.List;

public interface SuperAdminService {

    List<Admin> findAllAdmins();

    Admin saveAdmin(AddAdminRequest addAdminRequest);

    Admin updateAdmin(String token, UpdateAdminRequest updateAdminRequest);

    void deleteAdmin(String token, DeleteAdminRequest deleteAdminRequest);

}

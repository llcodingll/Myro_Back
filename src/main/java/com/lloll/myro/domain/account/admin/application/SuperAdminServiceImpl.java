package com.lloll.myro.domain.account.admin.application;

import com.lloll.myro.domain.account.admin.api.request.AddAdminRequest;
import com.lloll.myro.domain.account.admin.api.request.DeleteAdminRequest;
import com.lloll.myro.domain.account.admin.api.request.UpdateAdminRequest;
import com.lloll.myro.domain.account.admin.dao.AdminRepository;
import com.lloll.myro.domain.account.admin.domain.Admin;
import com.lloll.myro.domain.account.jwt.TokenProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SuperAdminServiceImpl implements SuperAdminService {

    private final AdminRepository adminRepository;
    private final TokenProvider tokenProvider;

    @Override
    public List<Admin> findAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public Admin saveAdmin(AddAdminRequest addAdminRequest) {
        return adminRepository.save(new Admin(addAdminRequest));
    }

    @Override
    public Admin updateAdmin(String token, UpdateAdminRequest updateAdminRequest) {
        Admin admin = adminRepository.findById(updateAdminRequest.getAdminId()).orElseThrow();
        admin.updateUserDetails(updateAdminRequest);
        return adminRepository.save(admin);
    }

    @Override
    public void deleteAdmin(String token, DeleteAdminRequest deleteAdminRequest) {
        tokenProvider.validateToken(token);
        adminRepository.deleteById(deleteAdminRequest.getAdminId());
    }
}

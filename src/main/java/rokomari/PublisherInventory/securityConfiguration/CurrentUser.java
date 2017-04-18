package rokomari.PublisherInventory.securityConfiguration;


import org.springframework.security.core.authority.AuthorityUtils;
import rokomari.PublisherInventory.model.admin.EndUser;
import rokomari.PublisherInventory.model.admin.UserRole;

import java.util.List;

public class CurrentUser extends org.springframework.security.core.userdetails.User{

    private EndUser endUser;

    public CurrentUser(EndUser endUser) {
        super(endUser.getName(), endUser.getPassword(), endUser.getIsEnabled(),true,true,true,AuthorityUtils.createAuthorityList(endUser.getRoles().toString()));
        this.endUser = endUser;
    }

    public EndUser getUser() {
        return endUser;
    }

    public int getId() {
        return endUser.getId();
    }

    public List<UserRole> getRole() {
        return endUser.getRoles();
    }
}

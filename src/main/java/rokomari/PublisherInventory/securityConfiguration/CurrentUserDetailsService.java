package rokomari.PublisherInventory.securityConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rokomari.PublisherInventory.model.admin.EndUser;
import rokomari.PublisherInventory.model.admin.UserRole;
import rokomari.PublisherInventory.service.admin.services.EndUserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Transactional(readOnly=true)
@Service("userDetailsService")
public class CurrentUserDetailsService implements UserDetailsService {

    @Autowired
    EndUserService endUserService;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        EndUser account= endUserService.getUserByUserName(username);

        if(account==null) {throw new UsernameNotFoundException("No such user: " + username);
        } else if (account.getRoles().isEmpty()) {
            throw new UsernameNotFoundException("User " + username + " has no authorities");
        }

        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        /*boolean isEnabled = account.isEnabled();*/

        /*return new User(
                account.getName(),
                account.getPassword()*//*.toLowerCase(),*//*
                account.isEnabled(),
                *//*accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,*//*
                getAuthorities(account.getRoles()));*/

        return new User(
                account.getName(),
                account.getPassword(),
                account.getIsEnabled(),
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                getAuthorities(account.getRoles()));
    }

    public List<String> getRolesAsList(List<UserRole> roles) {
        List <String> rolesAsList = new ArrayList<String>();
        for(UserRole role : roles){
            rolesAsList.add(role.getRole());
        }
        return rolesAsList;
    }

    public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
            System.out.println("Role in current: "+role);
        }
        return authorities;
    }

    public Collection<? extends GrantedAuthority> getAuthorities(List<UserRole> roles) {
        List<GrantedAuthority> authList = getGrantedAuthorities(getRolesAsList(roles));
        System.out.println("Role in collection: "+roles.get(0).getRole());
        return authList;
    }

}

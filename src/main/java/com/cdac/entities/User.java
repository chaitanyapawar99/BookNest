package com.cdac.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"booksForSale","orders","reviews","cart","image"})
public class User extends BaseEntity implements UserDetails {

    @Column(length = 20, name = "first_name")
    private String firstName;

    @Column(length = 30, name = "last_name")
    private String lastName;

    @Column(length = 30, unique = true, nullable = false)
    private String email;

    @Column(length = 300, nullable = false)
    private String password;

    @Column(name = "dob")
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, name = "user_role")
    private UserRole userRole;

    // store image as LOB; load lazily to avoid fetching with every user
    @Lob
    @Column(name = "image", nullable = true)
    private byte[] image;

    // Relationships: explicit fetch types
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Book> booksForSale = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Cart cart;

    // convenience constructor for signup
    public User(String firstName, String lastName, String email, String password, LocalDate dob, UserRole userRole) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dob = dob;
        this.userRole = userRole;
    }

    // minimal constructor for projections etc.
    public User(String firstName, String lastName, LocalDate dob) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
    }

    // --- UserDetails methods ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // prefix with ROLE_ so hasRole("ADMIN") works
        String roleName = (this.userRole == null) ? "ROLE_USER" : "ROLE_" + this.userRole.name();
        return List.of(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    // Lombok provides getPassword(), but it's clearer to explicitly override:
    @Override
    public String getPassword() {
        return this.password;
    }

    // For now return true (account active). Adjust based on your entity fields.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

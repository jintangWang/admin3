package tech.wetech.admin3.sys.model;

import static jakarta.persistence.FetchType.LAZY;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.*;

/**
 * 用户
 *
 * @author cjbi
 */
@Entity
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String username;

    private String avatar;

    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private State state;

    @Column
    private LocalDateTime createdTime;

    @Column
    private String type;

    @Column(columnDefinition = "int(11) default 0")
    private Long imageCount = 0L;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.DETACH)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Role roles;

    @ManyToMany(fetch = LAZY, cascade = CascadeType.DETACH)
    @JoinTable(name = "label_user", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "label_id", referencedColumnName = "id"))
    private Set<Label> labels = new LinkedHashSet<>();

    @OneToMany(mappedBy = "id")
    private Set<UserCredential> credentials = new LinkedHashSet<>();

    @OneToOne(fetch = LAZY)
    private Organization organization;

    public String getOrgFullName() {
        return concatOrgName(getOrganization());
    }

    private String concatOrgName(Organization org) {
        if (org.getParent() != null) {
            return concatOrgName(org.getParent()).concat("-").concat(org.getName());
        }
        return org.getName();
    }

    @PrePersist
    protected void onCreate() {
        this.createdTime = LocalDateTime.now();
    }

    /**
     * 获取用户权限列表
     *
     * @return
     */
    public Set<String> findPermissions() {
        return roles.getResources().stream().map(Resource::getPermission).collect(Collectors.toSet());
    }

    public enum Gender {
        MALE, FEMALE
    }

    public enum State {
        NORMAL, LOCKED
    }

    public boolean isLocked() {
        return this.state == State.LOCKED;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public Role getRoles() {
        return roles;
    }

    public void setRoles(Role roles) {
        this.roles = roles;
    }

    public Set<UserCredential> getCredentials() {
        return credentials;
    }

    public void setCredentials(Set<UserCredential> credentials) {
        this.credentials = credentials;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Label> getLabels() {
        return labels;
    }

    public void setLabels(Set<Label> labels) {
        this.labels = labels;
    }

    public Long getImageCount() {
        return imageCount;
    }

    public void setImageCount(Long imageCount) {
        this.imageCount = imageCount;
    }
}

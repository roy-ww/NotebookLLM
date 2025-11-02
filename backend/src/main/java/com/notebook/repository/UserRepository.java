package com.notebook.repository;

import com.notebook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户数据访问接口
 * 提供对用户数据的基本操作方法
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户对象
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 根据微信OpenId查找用户
     * @param wechatOpenid 微信OpenId
     * @return 用户对象
     */
    Optional<User> findByWechatOpenid(String wechatOpenid);
    
    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 检查微信OpenId是否存在
     * @param wechatOpenid 微信OpenId
     * @return 是否存在
     */
    boolean existsByWechatOpenid(String wechatOpenid);
}
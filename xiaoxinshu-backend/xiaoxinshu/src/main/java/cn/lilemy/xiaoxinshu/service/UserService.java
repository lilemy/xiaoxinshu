package cn.lilemy.xiaoxinshu.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.lilemy.xiaoxinshu.model.dto.user.UserEditRequest;
import cn.lilemy.xiaoxinshu.model.dto.user.UserQueryRequest;
import cn.lilemy.xiaoxinshu.model.entity.User;
import cn.lilemy.xiaoxinshu.model.vo.LoginUserVO;
import cn.lilemy.xiaoxinshu.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @author qq233
 * @description 针对表【user(用户)】的数据库操作Service
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @return 用户信息（脱敏）
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户编辑
     *
     * @param userEditRequest 用户编辑请求体
     * @return 是否操作成功
     */
    boolean userEdit(UserEditRequest userEditRequest);

    /**
     * 用户注销
     *
     * @return 注销状态
     */
    boolean userLogout();

    /**
     * 获取当前用户
     *
     * @return 获取当前登录用户
     */
    User getLoginUser();

    /**
     * 获取脱敏用户信息
     *
     * @param user 用户信息
     * @return 脱敏用户
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取脱敏用户信息
     *
     * @param user 用户
     * @return 脱敏用户信息
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏用户信息
     *
     * @param userId 用户id
     * @return 脱敏用户信息
     */
    UserVO getUserVO(Long userId);

    /**
     * 获取脱敏的用户信息
     *
     * @param userList 用户列表
     * @return 脱敏用户列表
     */
    List<UserVO> getUserVO(List<User> userList);


    /**
     * 是否为管理员
     *
     * @return true -> 管理员
     */
    boolean isAdmin();

    /**
     * 是否为管理员
     *
     * @param user 用户信息
     * @return true -> 管理员
     */
    boolean isAdmin(User user);

    /**
     * 添加用户签到记录
     *
     * @param userId 用户 id
     * @return 当前是否已签到成功
     */
    boolean addUserSignIn(long userId);

    /**
     * 获取用户指定年份的签到记录
     *
     * @param userId 用户 id
     * @param year   年份（为空表示当前年份）
     * @return 签到记录映射
     */
    List<Integer> getUserSignInRecord(long userId, Integer year);


    /**
     * 用户分页查询
     *
     * @param userQueryRequest 分页查询请求体
     * @return User
     */
    Wrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

}

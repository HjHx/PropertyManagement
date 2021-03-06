package manage;

import entity.User;
import exception.UserException;
import java.util.List;

/**
 * @description: 用户管理层
 */
public interface UserManage {

    /**
     * 用户登录验证
     * @param loginname
     * @param password
     * @return
     * @throws UserException
     */
    User login(String loginname, String password) throws UserException;

    /**
     * 获取某用户
     * @param uid
     * @return
     * @throws UserException
     */
    User getUser(int uid) throws UserException;

    /**
     * 列出所有用户
     * @return
     * @throws UserException
     */
    List<User> listAllUser () throws UserException;

    /**
     * 创建新用户
     * @param loginname
     * @param password
     * @param usertype
     * @return
     * @throws UserException
     */
    boolean saveUser(String loginname, String password, int usertype, String operator) throws UserException;

    /**
     * 删除某用户
     * @param uid
     * @return
     * @throws UserException
     */
    boolean deleteUser(int uid, String operator) throws UserException;

    /**
     * 修改用户密码
     * @param uid
     * @param password
     * @param operator
     * @return
     * @throws UserException
     */
    boolean changePassword(int uid,String password, String operator) throws UserException;
}

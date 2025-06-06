declare namespace API {
  type BaseResponseBoolean = {
    /** 响应状态码 */
    code?: number;
    /** 响应数据 */
    data?: boolean;
    /** 响应信息 */
    message?: string;
  };

  type BaseResponseLoginUserVO = {
    /** 响应状态码 */
    code?: number;
    data?: LoginUserVO;
    /** 响应信息 */
    message?: string;
  };

  type BaseResponseLong = {
    /** 响应状态码 */
    code?: number;
    /** 响应数据 */
    data?: number;
    /** 响应信息 */
    message?: string;
  };

  type BaseResponsePageUser = {
    /** 响应状态码 */
    code?: number;
    data?: PageUser;
    /** 响应信息 */
    message?: string;
  };

  type BaseResponseUser = {
    /** 响应状态码 */
    code?: number;
    data?: User;
    /** 响应信息 */
    message?: string;
  };

  type BaseResponseUserVO = {
    /** 响应状态码 */
    code?: number;
    data?: UserVO;
    /** 响应信息 */
    message?: string;
  };

  type deleteUserParams = {
    id: number;
  };

  type getUserParams = {
    id: number;
  };

  type getUserVOParams = {
    id: number;
  };

  type listUserByPageParams = {
    userQueryRequest: UserQueryRequest;
  };

  type LoginUserVO = {
    /** id */
    id?: number;
    /** 账号 */
    userAccount?: string;
    /** 用户昵称 */
    userName?: string;
    /** 用户头像 */
    userAvatar?: string;
    /** 用户简介 */
    userProfile?: string;
    /** 用户手机号 */
    userPhone?: string;
    /** 用户邮箱 */
    userEmail?: string;
    /** 用户性别(0男 1女) */
    userGender?: number;
    /** 用户生日 */
    userBirthday?: string;
    /** 用户角色(user admin) */
    userRole?: string;
    /** 备注 */
    remark?: string;
    /** 编辑时间 */
    editTime?: string;
    /** 创建时间 */
    createTime?: string;
    /** 更新时间 */
    updateTime?: string;
  };

  type OrderItem = {
    column?: string;
    asc?: boolean;
  };

  type PageUser = {
    records?: User[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PageUser;
    searchCount?: PageUser;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type User = {
    /** id */
    id?: number;
    /** 账号 */
    userAccount?: string;
    /** 密码 */
    userPassword?: string;
    /** 用户昵称 */
    userName?: string;
    /** 用户头像 */
    userAvatar?: string;
    /** 用户简介 */
    userProfile?: string;
    /** 用户手机号 */
    userPhone?: string;
    /** 用户邮箱 */
    userEmail?: string;
    /** 用户性别(0男 1女) */
    userGender?: number;
    /** 用户生日 */
    userBirthday?: string;
    /** 用户角色(user admin) */
    userRole?: string;
    /** 备注 */
    remark?: string;
    /** 编辑时间 */
    editTime?: string;
    /** 创建时间 */
    createTime?: string;
    /** 更新时间 */
    updateTime?: string;
    /** 是否删除 */
    isDelete?: number;
  };

  type UserCreateRequest = {
    /** 账号 */
    userAccount: string;
    /** 密码 */
    userPassword?: string;
    /** 用户昵称 */
    userName?: string;
    /** 用户头像 */
    userAvatar?: string;
    /** 用户简介 */
    userProfile?: string;
    /** 用户手机号 */
    userPhone?: string;
    /** 用户邮箱 */
    userEmail?: string;
    /** 用户性别(0男 1女) */
    userGender?: number;
    /** 用户生日 */
    userBirthday?: string;
    /** 用户角色(user admin) */
    userRole?: string;
    /** 备注 */
    remark?: string;
  };

  type UserEditRequest = {
    /** id */
    id: number;
    /** 用户昵称 */
    userName?: string;
    /** 用户头像 */
    userAvatar?: string;
    /** 用户简介 */
    userProfile?: string;
    /** 用户手机号 */
    userPhone?: string;
    /** 用户邮箱 */
    userEmail?: string;
    /** 用户性别(0男 1女) */
    userGender?: number;
    /** 用户生日 */
    userBirthday?: string;
    /** 备注 */
    remark?: string;
  };

  type UserLoginRequest = {
    /** 用户账号 */
    userAccount: string;
    /** 用户密码 */
    userPassword: string;
  };

  type UserQueryRequest = {
    /** 当前页号 */
    current?: number;
    /** 页面大小 */
    pageSize?: number;
    /** 排序字段 */
    sortField?: string;
    /** 排序顺序（默认降序） */
    sortOrder?: string;
    /** 账号 */
    userAccount?: string;
    /** 用户昵称 */
    userName?: string;
    /** 用户手机号 */
    userPhone?: string;
    /** 用户邮箱 */
    userEmail?: string;
    /** 用户性别(0男 1女) */
    userGender?: number;
    /** 用户生日 */
    userBirthday?: string;
    /** 用户角色(user admin) */
    userRole?: string;
  };

  type UserRegisterRequest = {
    /** 用户账号 */
    userAccount: string;
    /** 用户密码 */
    userPassword: string;
    /** 确认密码 */
    checkPassword: string;
  };

  type UserUpdateRequest = {
    /** 用户id */
    id: number;
    /** 用户密码 */
    userPassword?: string;
    /** 用户昵称 */
    userName?: string;
    /** 用户头像 */
    userAvatar?: string;
    /** 用户简介 */
    userProfile?: string;
    /** 用户手机号 */
    userPhone?: string;
    /** 用户邮箱 */
    userEmail?: string;
    /** 用户性别(0男 1女) */
    userGender?: number;
    /** 用户生日 */
    userBirthday?: string;
    /** 用户角色(user admin) */
    userRole?: string;
    /** 备注 */
    remark?: string;
  };

  type UserVO = {
    /** id */
    id?: number;
    /** 账号 */
    userAccount?: string;
    /** 用户昵称 */
    userName?: string;
    /** 用户头像 */
    userAvatar?: string;
    /** 用户简介 */
    userProfile?: string;
    /** 用户手机号 */
    userPhone?: string;
    /** 用户邮箱 */
    userEmail?: string;
    /** 用户性别(0男 1女) */
    userGender?: number;
    /** 用户生日 */
    userBirthday?: string;
    /** 用户角色(user admin) */
    userRole?: string;
    /** 备注 */
    remark?: string;
    /** 创建时间 */
    createTime?: string;
  };
}
